package ru.palushin86.inventory.db

import android.content.Context
import androidx.room.*
import ru.palushin86.inventory.db.entities.*

@Database(
    entities = [
        InventoryEntityDb::class,
        ParameterEntityDb::class,
        TagEntityDb::class,
        FilterSetEntityDb::class,
        FilterEntityDb::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}