package net.chris.demo.guardian.database

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = GuardianDatabase.NAME, version = GuardianDatabase.VERSION)
object GuardianDatabase {

    const val NAME = "Database"

    const val VERSION = 1

}
