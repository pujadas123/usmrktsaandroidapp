package in.exuber.usmarket.models.training;

public class TrainingOutput {

    private String CampaignTrainingDescription,compaignId,compaignName,productName,categoryName,languageName,createdOn,CampaignTrainingUrl;

    public TrainingOutput(String campaignTrainingDescription, String compaignId, String compaignName, String productName, String categoryName, String languageName, String createdOn, String campaignTrainingUrl) {
        CampaignTrainingDescription = campaignTrainingDescription;
        this.compaignId = compaignId;
        this.compaignName = compaignName;
        this.productName = productName;
        this.categoryName = categoryName;
        this.languageName = languageName;
        this.createdOn = createdOn;
        CampaignTrainingUrl = campaignTrainingUrl;
    }

    public String getCampaignTrainingDescription() {
        return CampaignTrainingDescription;
    }

    public void setCampaignTrainingDescription(String campaignTrainingDescription) {
        CampaignTrainingDescription = campaignTrainingDescription;
    }

    public String getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(String compaignId) {
        this.compaignId = compaignId;
    }

    public String getCompaignName() {
        return compaignName;
    }

    public void setCompaignName(String compaignName) {
        this.compaignName = compaignName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCampaignTrainingUrl() {
        return CampaignTrainingUrl;
    }

    public void setCampaignTrainingUrl(String campaignTrainingUrl) {
        CampaignTrainingUrl = campaignTrainingUrl;
    }
}
