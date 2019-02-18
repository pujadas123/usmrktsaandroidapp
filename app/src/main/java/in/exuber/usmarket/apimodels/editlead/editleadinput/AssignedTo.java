
package in.exuber.usmarket.apimodels.editlead.editleadinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignedTo {

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
