package com.example.grama_vaxihealthcare.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.data.AppDatabase
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CampAlertWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val campAlertDao = database.campAlertDao()
        val notificationHelper = NotificationHelper(applicationContext)

        val camps = campAlertDao.getAllCampAlerts().first()
        
        // Match the sample data format: "dd MMM yyyy"
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        for (camp in camps) {
            try {
                val campDate = sdf.parse(camp.date)
                if (campDate != null) {
                    val diffInMillis = campDate.time - today.time
                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    // Trigger: 1 day before camp date as requested
                    if (diffInDays == 1L) {
                        // Resolve the localized title from resource if possible
                        val localizedTitle = try {
                            applicationContext.getString(
                                applicationContext.resources.getIdentifier(
                                    camp.title, "string", applicationContext.packageName
                                )
                            )
                        } catch (e: Exception) {
                            camp.title
                        }

                        notificationHelper.showLoudNotification(
                            applicationContext.getString(R.string.government_camp_alert),
                            localizedTitle
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return Result.success()
    }
}
