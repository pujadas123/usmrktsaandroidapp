
package in.exuber.usmarket.apimodels.login.logininput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginInput {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mobileId")
    @Expose
    private String mobileId;
    @SerializedName("gLatitude")
    @Expose
    private Double locationLatId;
    @SerializedName("gLongitude")
    @Expose
    private Double locationLongId;


    public LoginInput() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public Double getLocationLatId() {
        return locationLatId;
    }

    public void setLocationLatId(Double locationLatId) {
        this.locationLatId = locationLatId;
    }

    public Double getLocationLongId() {
        return locationLongId;
    }

    public void setLocationLongId(Double locationLongId) {
        this.locationLongId = locationLongId;
    }
}
