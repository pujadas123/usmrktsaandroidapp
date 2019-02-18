
package in.exuber.usmarket.apimodels.login.loginoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lookUpKey")
    @Expose
    private Object lookUpKey;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Object getLookUpKey() {
        return lookUpKey;
    }

    public void setLookUpKey(Object lookUpKey) {
        this.lookUpKey = lookUpKey;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

}
