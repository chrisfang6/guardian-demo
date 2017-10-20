package net.chris.demo.guardian.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = GuardianDatabase.NAME, version = GuardianDatabase.VERSION)
public class GuardianDatabase {

    public static final String NAME = "Database";

    public static final int VERSION = 1;

}
