
package in.exuber.usmarket.apimodels.addlead.addleadinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedBy {

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
