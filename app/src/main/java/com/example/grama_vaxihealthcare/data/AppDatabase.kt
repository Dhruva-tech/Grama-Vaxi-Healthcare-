package com.example.grama_vaxihealthcare.data

import android.content.Context
import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.grama_vaxihealthcare.data.dao.*
import com.example.grama_vaxihealthcare.data.entity.*

@Database(
    entities = [
        Farmer::class,
        Animal::class,
        Vaccination::class,
        DiseaseReport::class,
        InboxReport::class,
        ChatHistory::class,
        CampAlert::class,
        VaccinationSchedule::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun animalDao(): AnimalDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun diseaseReportDao(): DiseaseReportDao
    abstract fun inboxReportDao(): InboxReportDao
    abstract fun chatHistoryDao(): ChatHistoryDao
    abstract fun campAlertDao(): CampAlertDao
    abstract fun vaccinationScheduleDao(): VaccinationScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grama_vaxi_db"
                )
                    .addMigrations(MIGRATION_6_7)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS disease_reports_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        animalId INTEGER NOT NULL,
                        symptoms TEXT NOT NULL,
                        imageUrl TEXT,
                        aiSuggestions TEXT,
                        reportDate INTEGER NOT NULL,
                        FOREIGN KEY(animalId) REFERENCES animals(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO disease_reports_new (id, animalId, symptoms, imageUrl, aiSuggestions, reportDate)
                    SELECT id, animalId, symptoms, imageUrl, aiSuggestions, reportDate
                    FROM disease_reports
                    WHERE animalId IN (SELECT id FROM animals)
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE disease_reports")
                db.execSQL("ALTER TABLE disease_reports_new RENAME TO disease_reports")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_disease_reports_animalId ON disease_reports(animalId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS vaccinations_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        animalId INTEGER NOT NULL,
                        vaccineName TEXT NOT NULL,
                        dateAdministered INTEGER NOT NULL,
                        nextDueDate INTEGER NOT NULL,
                        FOREIGN KEY(animalId) REFERENCES animals(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO vaccinations_new (id, animalId, vaccineName, dateAdministered, nextDueDate)
                    SELECT id, animalId, vaccineName, dateAdministered, nextDueDate
                    FROM vaccinations
                    WHERE animalId IN (SELECT id FROM animals)
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE vaccinations")
                db.execSQL("ALTER TABLE vaccinations_new RENAME TO vaccinations")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_vaccinations_animalId ON vaccinations(animalId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS inbox_reports_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        animalId INTEGER NOT NULL,
                        animalName TEXT NOT NULL,
                        animalType TEXT NOT NULL,
                        symptoms TEXT NOT NULL,
                        reportDate TEXT NOT NULL,
                        reportTime TEXT NOT NULL,
                        status TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(animalId) REFERENCES animals(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO inbox_reports_new (id, animalId, animalName, animalType, symptoms, reportDate, reportTime, status, createdAt)
                    SELECT inbox_reports.id, animals.id, inbox_reports.animalName, inbox_reports.animalType, inbox_reports.symptoms,
                           inbox_reports.reportDate, inbox_reports.reportTime, inbox_reports.status, inbox_reports.createdAt
                    FROM inbox_reports
                    INNER JOIN animals
                        ON inbox_reports.animalName = animals.name
                        AND inbox_reports.animalType = animals.type
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE inbox_reports")
                db.execSQL("ALTER TABLE inbox_reports_new RENAME TO inbox_reports")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_inbox_reports_animalId ON inbox_reports(animalId)")
            }
        }
    }
}
