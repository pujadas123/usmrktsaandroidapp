
package in.exuber.usmarket.models.ProfilePicModel;

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

    /**
     * No args constructor for use in serialization
     * 
     */
    public Role() {
    }

    /**
     * 
     * @param id
     * @param lookUpKey
     * @param description
     * @param name
     * @param dataStatus
     */
    public Role(String id, String name, String description, Object lookUpKey, Object dataStatus) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.lookUpKey = lookUpKey;
        this.dataStatus = dataStatus;
    }

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
