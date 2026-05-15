package com.example.grama_vaxihealthcare.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import com.example.grama_vaxihealthcare.data.AppDatabase
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class VaccinationReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val animalDao = database.animalDao()
        val notificationHelper = NotificationHelper(applicationContext)

        val animals = animalDao.getAllAnimals().firstOrNull() ?: return Result.success()

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val threeDaysLater = today + (3 * 24 * 60 * 60 * 1000L)

        for (animal in animals) {
            animal.nextVaccinationDate?.let { nextDate ->
                if (nextDate >= today && nextDate < today + (24 * 60 * 60 * 1000L)) {
                    notificationHelper.showNotification(
                        "Vaccination Due Today",
                        "Vaccination due for ${animal.name} (${animal.type}) today!"
                    )
                } else if (nextDate >= threeDaysLater && nextDate < threeDaysLater + (24 * 60 * 60 * 1000L)) {
                    notificationHelper.showNotification(
                        "Upcoming Vaccination",
                        "Vaccination due for ${animal.name} in 3 days."
                    )
                }
            }
        }

        return Result.success()
    }
}
