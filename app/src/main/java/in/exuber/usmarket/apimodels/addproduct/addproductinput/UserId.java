
package in.exuber.usmarket.apimodels.addproduct.addproductinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserId {

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
