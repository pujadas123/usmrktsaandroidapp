
package in.exuber.usmarket.apimodels.updatenotificationseen.updatenotificationseeninput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateNotificationSeenInput {

    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UpdateNotificationSeenInput() {
    }

    /**
     * 
     * @param id
     */
    public UpdateNotificationSeenInput(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
