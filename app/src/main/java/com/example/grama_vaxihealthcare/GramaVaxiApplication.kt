package com.example.grama_vaxihealthcare

import android.app.Application
import androidx.work.*
import com.example.grama_vaxihealthcare.data.AppDatabase
import com.example.grama_vaxihealthcare.data.repository.GramaVaxiRepository
import com.example.grama_vaxihealthcare.worker.CampAlertWorker
import com.example.grama_vaxihealthcare.worker.VaccinationReminderWorker
import java.util.concurrent.TimeUnit

class GramaVaxiApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        GramaVaxiRepository(
            database.farmerDao(),
            database.animalDao(),
            database.vaccinationDao(),
            database.diseaseReportDao(),
            database.inboxReportDao(),
            database.chatHistoryDao(),
            database.campAlertDao(),
            database.vaccinationScheduleDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        setupWorkManager()
    }

    private fun setupWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val vaccinationReminderRequest = PeriodicWorkRequestBuilder<VaccinationReminderWorker>(
            24, TimeUnit.HOURS
        ).setConstraints(constraints).build()

        val campAlertRequest = PeriodicWorkRequestBuilder<CampAlertWorker>(
            12, TimeUnit.HOURS // Check for camps twice a day
        ).setConstraints(constraints).build()

        val workManager = WorkManager.getInstance(this)
        
        workManager.enqueueUniquePeriodicWork(
            "vaccination_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            vaccinationReminderRequest
        )

        workManager.enqueueUniquePeriodicWork(
            "camp_alerts",
            ExistingPeriodicWorkPolicy.KEEP,
            campAlertRequest
        )
    }
}
