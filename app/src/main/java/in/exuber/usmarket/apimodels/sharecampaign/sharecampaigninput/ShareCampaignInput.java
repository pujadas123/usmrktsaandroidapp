
package in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareCampaignInput {

    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("campaignId")
    @Expose
    private CampaignId campaignId;

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public CampaignId getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(CampaignId campaignId) {
        this.campaignId = campaignId;
    }

}
