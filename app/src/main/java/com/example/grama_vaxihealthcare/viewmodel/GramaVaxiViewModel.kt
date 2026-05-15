package com.example.grama_vaxihealthcare.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.grama_vaxihealthcare.data.entity.*
import com.example.grama_vaxihealthcare.data.repository.GramaVaxiRepository
import com.example.grama_vaxihealthcare.worker.VaccinationReminderWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class GramaVaxiViewModel(
    private val repository: GramaVaxiRepository,
    private val context: Context
) : ViewModel() {

    private val _currentFarmerId = MutableStateFlow<Long?>(null)
    
    val allFarmers: StateFlow<List<Farmer>> = repository.allFarmers.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val farmer: StateFlow<Farmer?> = _currentFarmerId.flatMapLatest { id ->
        if (id == null) {
            flowOf(null)
        } else {
            repository.allFarmers.map { farmers -> farmers.find { it.id == id } }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allAnimals: StateFlow<List<Animal>> = repository.allAnimals.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val allCampAlerts: StateFlow<List<CampAlert>> = repository.allCampAlerts.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val allInboxReports: StateFlow<List<InboxReport>> = repository.allInboxReports.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    init {
        seedSampleCampsIfEmpty()
    }

    private fun seedSampleCampsIfEmpty() {
        viewModelScope.launch {
            val currentCamps = repository.allCampAlerts.first()
            if (currentCamps.isEmpty()) {
                val samples = listOf(
                    CampAlert(
                        title = "doctor_arriving_temple",
                        message = "doctor_arriving_temple",
                        date = "15 May 2026",
                        location = "temple_square_loc",
                        doctorName = "dr_ramesh"
                    ),
                    CampAlert(
                        title = "free_vaccination_camp",
                        message = "free_vaccination_camp",
                        date = "18 May 2026",
                        location = "village_panchayat_hall",
                        doctorName = "dr_kavya"
                    ),
                    CampAlert(
                        title = "emergency_livestock_camp",
                        message = "emergency_livestock_camp",
                        date = "21 May 2026",
                        location = "market_road",
                        doctorName = "dr_suresh"
                    ),
                    CampAlert(
                        title = "goat_sheep_drive",
                        message = "goat_sheep_drive",
                        date = "25 May 2026",
                        location = "community_center",
                        doctorName = "dr_meena"
                    ),
                    CampAlert(
                        title = "mobile_veterinary_unit",
                        message = "mobile_veterinary_unit",
                        date = "28 May 2026",
                        location = "bus_stand",
                        doctorName = "dr_ravi"
                    )
                )
                samples.forEach { repository.insertCampAlert(it) }
            }
        }
    }

    // Search and Filtering for Calendar
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredAnimals: StateFlow<List<Animal>> = combine(allAnimals, _searchQuery) { animals, query ->
        if (query.isBlank()) animals
        else animals.filter { 
            it.name.contains(query, ignoreCase = true) || it.type.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSchedules: StateFlow<List<VaccinationSchedule>> = repository.allSchedules.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val upcomingShotsCount: StateFlow<Int> = repository.getUpcomingShotsCount(getStartOfToday())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private fun getStartOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _selectedAnimalId = MutableStateFlow<Long?>(null)
    
    val selectedAnimal: StateFlow<Animal?> = combine(_selectedAnimalId, repository.allAnimals) { id, animals ->
        animals.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val selectedAnimalVaccinations: StateFlow<List<Vaccination>> = _selectedAnimalId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getVaccinationsForAnimal(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedAnimalReports: StateFlow<List<DiseaseReport>> = _selectedAnimalId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getReportsForAnimal(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedAnimal(id: Long?) {
        _selectedAnimalId.value = id
    }

    fun loginFarmer(id: Long) {
        _currentFarmerId.value = id
    }

    fun logout() {
        _currentFarmerId.value = null
    }

    fun registerFarmer(name: String, phone: String, village: String) {
        viewModelScope.launch {
            val id = repository.insertFarmer(Farmer(name = name, phoneNumber = phone, villageName = village))
            _currentFarmerId.value = id
        }
    }

    fun deleteFarmer(farmer: Farmer) {
        viewModelScope.launch {
            repository.deleteFarmer(farmer)
        }
    }

    // Camera and Photo state
    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri: StateFlow<Uri?> = _capturedImageUri.asStateFlow()

    fun setCapturedImageUri(uri: Uri?) {
        _capturedImageUri.value = uri
    }

    fun addAnimal(animal: Animal) {
        viewModelScope.launch {
            val id = repository.insertAnimal(animal)
            
            // Add default schedules for the new animal as requested
            val defaultVaccines = listOf("Nobivac", "Recombitek", "Canigen", "Vanguard", "HS")
            val baseTime = System.currentTimeMillis()
            defaultVaccines.forEachIndexed { index, name ->
                val scheduledDate = baseTime + (index + 1) * 30L * 24 * 60 * 60 * 1000L
                val daysRemaining = calculateDaysRemaining(scheduledDate)
                repository.insertSchedule(
                    VaccinationSchedule(
                        animalId = id,
                        vaccineName = name,
                        date = scheduledDate,
                        daysRemaining = daysRemaining
                    )
                )
            }

            // Record initial history entry
            repository.insertVaccination(
                Vaccination(
                    animalId = id,
                    vaccineName = "Registration & Health Checkup",
                    dateAdministered = System.currentTimeMillis(),
                    nextDueDate = animal.nextVaccinationDate ?: (System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000)
                )
            )
            // Schedule long term notification
            animal.nextVaccinationDate?.let { nextDate ->
                scheduleLongTermNotification(id, animal.name, nextDate)
            }
        }
    }

    fun updateVaccineDate(schedule: VaccinationSchedule, newDate: Long) {
        viewModelScope.launch {
            val daysRemaining = calculateDaysRemaining(newDate)
            repository.updateSchedule(schedule.copy(date = newDate, daysRemaining = daysRemaining))
        }
    }

    private fun calculateDaysRemaining(targetDate: Long): Int {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val diff = targetDate - today
        return if (diff < 0) 0 else (diff / (24 * 60 * 60 * 1000L)).toInt()
    }

    fun deleteAnimal(animal: Animal) {
        viewModelScope.launch {
            repository.deleteAnimal(animal)
            WorkManager.getInstance(context).cancelAllWorkByTag("animal_${animal.id}")
        }
    }

    fun recordVaccination(animalId: Long, vaccineName: String, date: Long, nextDate: Long) {
        viewModelScope.launch {
            repository.insertVaccination(
                Vaccination(animalId = animalId, vaccineName = vaccineName, dateAdministered = date, nextDueDate = nextDate)
            )
            // Update the animal's main record
            val currentAnimal = allAnimals.value.find { it.id == animalId }
            currentAnimal?.let {
                repository.updateAnimal(it.copy(lastVaccinationDate = date, nextVaccinationDate = nextDate))
                scheduleLongTermNotification(animalId, it.name, nextDate)
            }
        }
    }

    private fun scheduleLongTermNotification(animalId: Long, animalName: String, vaccinationTime: Long) {
        val delay = vaccinationTime - System.currentTimeMillis()
        if (delay > 0) {
            val data = Data.Builder()
                .putLong("animalId", animalId)
                .putString("animalName", animalName)
                .build()

            val notificationWork = OneTimeWorkRequestBuilder<VaccinationReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("animal_$animalId")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "vaccination_$animalId",
                ExistingWorkPolicy.REPLACE,
                notificationWork
            )
        }
    }

    fun submitDiseaseReport(animalId: Long, symptoms: String, imageUrl: String?) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val animal = allAnimals.value.find { it.id == animalId }
            repository.insertReport(DiseaseReport(animalId = animalId, symptoms = symptoms, imageUrl = imageUrl, aiSuggestions = null, reportDate = now))
            animal?.let {
                repository.insertInboxReport(
                    InboxReport(
                        animalId = it.id,
                        animalName = it.name,
                        animalType = it.type,
                        symptoms = symptoms,
                        reportDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(now)),
                        reportTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(now)),
                        createdAt = now
                    )
                )
            }
        }
    }
}

class GramaVaxiViewModelFactory(
    private val repository: GramaVaxiRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GramaVaxiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GramaVaxiViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
