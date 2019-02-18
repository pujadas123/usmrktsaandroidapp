
package in.exuber.usmarket.apimodels.campaigntraining.campaigntrainingoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignTrainingOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("trainingId")
    @Expose
    private String trainingId;
    @SerializedName("campaign")
    @Expose
    private Campaign campaign;
    @SerializedName("product")
    @Expose
    private Product_ product;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("company")
    @Expose
    private Company company;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy_ createdBy;
    @SerializedName("createdOn")
    @Expose
    private Double createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("article")
    @Expose
    private String article;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Product_ getProduct() {
        return product;
    }

    public void setProduct(Product_ product) {
        this.product = product;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public CreatedBy_ getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy_ createdBy) {
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

    public Object getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Object updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

}
