
package in.exuber.usmarket.apimodels.sharecampaignlog.sharecampaignlogoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareCampaignLogOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sharedId")
    @Expose
    private String sharedId;
    @SerializedName("campaignId")
    @Expose
    private CampaignId campaignId;
    @SerializedName("productId")
    @Expose
    private ProductId productId;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdOn")
    @Expose
    private Long createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSharedId() {
        return sharedId;
    }

    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    public CampaignId getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(CampaignId campaignId) {
        this.campaignId = campaignId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
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

}
