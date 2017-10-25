package net.chris.demo.guardian.ui.recycler

class ContentListItem private constructor(
        var id: String?,
        var sectionName: String?,
        var webPublicationDate: String?,
        var webTitle: String?,
        var webUrl: String?,
        var isSelected: Boolean
) {

    class Builder {
        private var id: String? = null
        private var sectionName: String? = null
        private var webPublicationDate: String? = null
        private var webTitle: String? = null
        private var webUrl: String? = null
        private var isSelected: Boolean = false

        fun setId(id: String): Builder {
            this.id = id
            return this
        }

        fun setSectionName(sectionName: String): Builder {
            this.sectionName = sectionName
            return this
        }

        fun setWebPublicationDate(webPublicationDate: String): Builder {
            this.webPublicationDate = webPublicationDate
            return this
        }

        fun setWebTitle(webTitle: String): Builder {
            this.webTitle = webTitle
            return this
        }

        fun setWebUrl(webUrl: String): Builder {
            this.webUrl = webUrl
            return this
        }

        fun setSelected(selected: Boolean): Builder {
            this.isSelected = selected
            return this
        }

        fun build(): ContentListItem {
            return ContentListItem(id, sectionName, webPublicationDate, webTitle, webUrl, isSelected)
        }
    }
}
