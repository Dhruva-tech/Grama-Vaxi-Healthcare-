package com.example.grama_vaxihealthcare.data.repository

import com.example.grama_vaxihealthcare.data.dao.*
import com.example.grama_vaxihealthcare.data.entity.*
import kotlinx.coroutines.flow.Flow

class GramaVaxiRepository(
    private val farmerDao: FarmerDao,
    private val animalDao: AnimalDao,
    private val vaccinationDao: VaccinationDao,
    private val diseaseReportDao: DiseaseReportDao,
    private val inboxReportDao: InboxReportDao,
    private val chatHistoryDao: ChatHistoryDao,
    private val campAlertDao: CampAlertDao,
    private val vaccinationScheduleDao: VaccinationScheduleDao
) {
    // Farmer
    val allFarmers: Flow<List<Farmer>> = farmerDao.getAllFarmers()
    suspend fun getFarmerById(id: Long) = farmerDao.getFarmerById(id)
    suspend fun insertFarmer(farmer: Farmer) = farmerDao.insertFarmer(farmer)
    suspend fun deleteFarmer(farmer: Farmer) = farmerDao.deleteFarmer(farmer)
    suspend fun deleteAllFarmers() = farmerDao.deleteAllFarmers()

    // Animals
    val allAnimals: Flow<List<Animal>> = animalDao.getAllAnimals()
    suspend fun getAnimalById(id: Long) = animalDao.getAnimalById(id)
    suspend fun insertAnimal(animal: Animal) = animalDao.insertAnimal(animal)
    suspend fun updateAnimal(animal: Animal) = animalDao.updateAnimal(animal)
    suspend fun deleteAnimal(animal: Animal) = animalDao.deleteAnimal(animal)

    // Vaccinations
    val allVaccinations: Flow<List<Vaccination>> = vaccinationDao.getAllVaccinations()
    fun getVaccinationsForAnimal(animalId: Long) = vaccinationDao.getVaccinationsForAnimal(animalId)
    suspend fun insertVaccination(vaccination: Vaccination) = vaccinationDao.insertVaccination(vaccination)

    // Disease Reports
    val allReports: Flow<List<DiseaseReport>> = diseaseReportDao.getAllReports()
    fun getReportsForAnimal(animalId: Long) = diseaseReportDao.getReportsForAnimal(animalId)
    suspend fun insertReport(report: DiseaseReport) = diseaseReportDao.insertReport(report)

    // Inbox Reports
    val allInboxReports: Flow<List<InboxReport>> = inboxReportDao.getAllInboxReports()
    suspend fun insertInboxReport(report: InboxReport) = inboxReportDao.insertInboxReport(report)

    // Chat History
    val allChats: Flow<List<ChatHistory>> = chatHistoryDao.getAllChats()
    suspend fun insertChat(chat: ChatHistory) = chatHistoryDao.insertChat(chat)
    suspend fun clearChatHistory() = chatHistoryDao.clearHistory()

    // Camp Alerts
    val allCampAlerts: Flow<List<CampAlert>> = campAlertDao.getAllCampAlerts()
    suspend fun insertCampAlert(campAlert: CampAlert) = campAlertDao.insertCampAlert(campAlert)
    suspend fun updateCampAlert(campAlert: CampAlert) = campAlertDao.updateCampAlert(campAlert)

    // Vaccination Schedules
    fun getSchedulesForAnimal(animalId: Long) = vaccinationScheduleDao.getSchedulesForAnimal(animalId)
    suspend fun insertSchedule(schedule: VaccinationSchedule) = vaccinationScheduleDao.insertSchedule(schedule)
    suspend fun updateSchedule(schedule: VaccinationSchedule) = vaccinationScheduleDao.updateSchedule(schedule)
    val allSchedules: Flow<List<VaccinationSchedule>> = vaccinationScheduleDao.getAllSchedules()
    fun getUpcomingShotsCount(currentDate: Long) = vaccinationScheduleDao.getUpcomingShotsCount(currentDate)
}
