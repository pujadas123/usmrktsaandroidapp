
package in.exuber.usmarket.apimodels.login.loginoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessId {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("accessTokenId")
    @Expose
    private String accessTokenId;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;
    @SerializedName("email")
    @Expose
    private Object email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
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

    public Object getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Object createdOn) {
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

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

}
