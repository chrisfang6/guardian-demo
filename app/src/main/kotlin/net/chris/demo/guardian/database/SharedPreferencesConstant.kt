package net.chris.demo.guardian.database

interface SharedPreferencesConstant {
    companion object {

        val KEY_PAGE_SIZE = "page-size"
        val KEY_SECTION = "section"


        /**
         * The number of items returned in one call
         */
        val DEFAULT_PAGE_SIZE = 20
    }

}
