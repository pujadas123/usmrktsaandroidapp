
package in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignId {

    @SerializedName("compaignId")
    @Expose
    private String compaignId;

    public String getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(String compaignId) {
        this.compaignId = compaignId;
    }

}
