package net.chris.demo.guardian.ui.recycler;

public class SectionListItem {

    private String id;

    private String sectionName;

    private boolean isSelected;

    private SectionListItem(String id, String sectionName, boolean isSelected) {
        this.id = id;
        this.sectionName = sectionName;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class Builder {
        private String id;
        private String sectionName;
        private boolean isSelected;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSectionName(String sectionName) {
            this.sectionName = sectionName;
            return this;
        }

        public Builder setSelected(boolean selected) {
            this.isSelected = selected;
            return this;
        }

        public SectionListItem build() {
            return new SectionListItem(id, sectionName, isSelected);
        }
    }
}
