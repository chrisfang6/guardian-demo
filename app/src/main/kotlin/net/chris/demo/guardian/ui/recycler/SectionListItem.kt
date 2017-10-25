package net.chris.demo.guardian.ui.recycler

class SectionListItem private constructor(
        var id: String?,
        var sectionName: String?,
        var isSelected: Boolean
) {

    class Builder {
        private var id: String? = null
        private var sectionName: String? = null
        private var isSelected: Boolean = false

        fun setId(id: String): Builder {
            this.id = id
            return this
        }

        fun setSectionName(sectionName: String): Builder {
            this.sectionName = sectionName
            return this
        }

        fun setSelected(selected: Boolean): Builder {
            this.isSelected = selected
            return this
        }

        fun build(): SectionListItem {
            return SectionListItem(id, sectionName, isSelected)
        }
    }
}
