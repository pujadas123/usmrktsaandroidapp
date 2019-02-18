
package in.exuber.usmarket.apimodels.updatemobileid.updatemobileidinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateMobileIdInput {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("mobileId")
    @Expose
    private String mobileId;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UpdateMobileIdInput() {
    }

    /**
     * 
     * @param mobileId
     * @param userId
     */
    public UpdateMobileIdInput(String userId, String mobileId) {
        super();
        this.userId = userId;
        this.mobileId = mobileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

}
