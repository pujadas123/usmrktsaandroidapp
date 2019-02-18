package in.exuber.usmarket.models.sharedcampaign;

public class SharedCampaignOutput {

    String CampaignName,CampaignDescription,CampaignProductName,categoryId,CampaignCategoryName,CampaignLanguage,CampaignImage,CampaignCreatedOn;

    public SharedCampaignOutput(String campaignName, String campaignDescription, String campaignProductName, String categoryId, String campaignCategoryName, String campaignLanguage, String campaignImage, String campaignCreatedOn) {
        CampaignName = campaignName;
        CampaignDescription = campaignDescription;
        CampaignProductName = campaignProductName;
        this.categoryId = categoryId;
        CampaignCategoryName = campaignCategoryName;
        CampaignLanguage = campaignLanguage;
        CampaignImage = campaignImage;
        CampaignCreatedOn = campaignCreatedOn;
    }


    public String getCampaignName() {
        return CampaignName;
    }

    public void setCampaignName(String campaignName) {
        CampaignName = campaignName;
    }

    public String getCampaignDescription() {
        return CampaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        CampaignDescription = campaignDescription;
    }

    public String getCampaignProductName() {
        return CampaignProductName;
    }

    public void setCampaignProductName(String campaignProductName) {
        CampaignProductName = campaignProductName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCampaignCategoryName() {
        return CampaignCategoryName;
    }

    public void setCampaignCategoryName(String campaignCategoryName) {
        CampaignCategoryName = campaignCategoryName;
    }

    public String getCampaignLanguage() {
        return CampaignLanguage;
    }

    public void setCampaignLanguage(String campaignLanguage) {
        CampaignLanguage = campaignLanguage;
    }

    public String getCampaignImage() {
        return CampaignImage;
    }

    public void setCampaignImage(String campaignImage) {
        CampaignImage = campaignImage;
    }

    public String getCampaignCreatedOn() {
        return CampaignCreatedOn;
    }

    public void setCampaignCreatedOn(String campaignCreatedOn) {
        CampaignCreatedOn = campaignCreatedOn;
    }
}
