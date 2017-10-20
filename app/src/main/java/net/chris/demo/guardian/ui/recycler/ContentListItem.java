package net.chris.demo.guardian.ui.recycler;

public class ContentListItem {

    private String id;

    private String sectionName;

    private String webPublicationDate;

    private String webTitle;

    private String webUrl;

    private boolean isSelected;

    private ContentListItem(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, boolean isSelected) {
        this.id = id;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
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

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class Builder {
        private String id;
        private String sectionName;
        private String webPublicationDate;
        private String webTitle;
        private String webUrl;
        private boolean isSelected;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSectionName(String sectionName) {
            this.sectionName = sectionName;
            return this;
        }

        public Builder setWebPublicationDate(String webPublicationDate) {
            this.webPublicationDate = webPublicationDate;
            return this;
        }

        public Builder setWebTitle(String webTitle) {
            this.webTitle = webTitle;
            return this;
        }

        public Builder setWebUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public Builder setSelected(boolean selected) {
            this.isSelected = selected;
            return this;
        }

        public ContentListItem build() {
            return new ContentListItem(id, sectionName, webPublicationDate, webTitle, webUrl, isSelected);
        }
    }
}
