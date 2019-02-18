
package in.exuber.usmarket.apimodels.campaigntraining.campaigntrainingoutput;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Campaign {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("compaignId")
    @Expose
    private String compaignId;
    @SerializedName("compaignName")
    @Expose
    private String compaignName;
    @SerializedName("compaignIntro")
    @Expose
    private Object compaignIntro;
    @SerializedName("compaignDesc")
    @Expose
    private String compaignDesc;
    @SerializedName("compaignClosure")
    @Expose
    private String compaignClosure;
    @SerializedName("compaignContactDetails")
    @Expose
    private String compaignContactDetails;
    @SerializedName("compaignStartDate")
    @Expose
    private Object compaignStartDate;
    @SerializedName("compaignEndDate")
    @Expose
    private Object compaignEndDate;
    @SerializedName("company")
    @Expose
    private Object company;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("compaignImage")
    @Expose
    private String compaignImage;
    @SerializedName("compaignVideo")
    @Expose
    private String compaignVideo;
    @SerializedName("priorityListing")
    @Expose
    private Object priorityListing;
    @SerializedName("youtubeUrl")
    @Expose
    private String youtubeUrl;
    @SerializedName("dataStatus")
    @Expose
    private String dataStatus;
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("language")
    @Expose
    private Object language;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("campaignStatus")
    @Expose
    private Object campaignStatus;
    @SerializedName("leads")
    @Expose
    private List<Object> leads = null;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("createdOn")
    @Expose
    private Double createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Double updatedOn;
    @SerializedName("notStarted")
    @Expose
    private Integer notStarted;
    @SerializedName("notInterested")
    @Expose
    private Integer notInterested;
    @SerializedName("inProgress")
    @Expose
    private Integer inProgress;
    @SerializedName("completed")
    @Expose
    private Integer completed;
    @SerializedName("totalCampaign")
    @Expose
    private Integer totalCampaign;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Object getCompaignIntro() {
        return compaignIntro;
    }

    public void setCompaignIntro(Object compaignIntro) {
        this.compaignIntro = compaignIntro;
    }

    public String getCompaignDesc() {
        return compaignDesc;
    }

    public void setCompaignDesc(String compaignDesc) {
        this.compaignDesc = compaignDesc;
    }

    public String getCompaignClosure() {
        return compaignClosure;
    }

    public void setCompaignClosure(String compaignClosure) {
        this.compaignClosure = compaignClosure;
    }

    public String getCompaignContactDetails() {
        return compaignContactDetails;
    }

    public void setCompaignContactDetails(String compaignContactDetails) {
        this.compaignContactDetails = compaignContactDetails;
    }

    public Object getCompaignStartDate() {
        return compaignStartDate;
    }

    public void setCompaignStartDate(Object compaignStartDate) {
        this.compaignStartDate = compaignStartDate;
    }

    public Object getCompaignEndDate() {
        return compaignEndDate;
    }

    public void setCompaignEndDate(Object compaignEndDate) {
        this.compaignEndDate = compaignEndDate;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCompaignImage() {
        return compaignImage;
    }

    public void setCompaignImage(String compaignImage) {
        this.compaignImage = compaignImage;
    }

    public String getCompaignVideo() {
        return compaignVideo;
    }

    public void setCompaignVideo(String compaignVideo) {
        this.compaignVideo = compaignVideo;
    }

    public Object getPriorityListing() {
        return priorityListing;
    }

    public void setPriorityListing(Object priorityListing) {
        this.priorityListing = priorityListing;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(Object campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public List<Object> getLeads() {
        return leads;
    }

    public void setLeads(List<Object> leads) {
        this.leads = leads;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Double getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Double createdOn) {
        this.createdOn = createdOn;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Double getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Double updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getNotStarted() {
        return notStarted;
    }

    public void setNotStarted(Integer notStarted) {
        this.notStarted = notStarted;
    }

    public Integer getNotInterested() {
        return notInterested;
    }

    public void setNotInterested(Integer notInterested) {
        this.notInterested = notInterested;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getTotalCampaign() {
        return totalCampaign;
    }

    public void setTotalCampaign(Integer totalCampaign) {
        this.totalCampaign = totalCampaign;
    }

}
