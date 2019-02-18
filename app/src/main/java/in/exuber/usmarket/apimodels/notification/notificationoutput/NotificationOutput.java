
package in.exuber.usmarket.apimodels.notification.notificationoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("typeId")
    @Expose
    private Object typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("notifyTo")
    @Expose
    private NotifyTo notifyTo;
    @SerializedName("createdOn")
    @Expose
    private Long createdOn;
    @SerializedName("notifySentOn")
    @Expose
    private Object notifySentOn;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("mobileId")
    @Expose
    private String mobileId;
    @SerializedName("seen")
    @Expose
    private String seen;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("totalLead")
    @Expose
    private Integer totalLead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getTypeId() {
        return typeId;
    }

    public void setTypeId(Object typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NotifyTo getNotifyTo() {
        return notifyTo;
    }

    public void setNotifyTo(NotifyTo notifyTo) {
        this.notifyTo = notifyTo;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Object getNotifySentOn() {
        return notifySentOn;
    }

    public void setNotifySentOn(Object notifySentOn) {
        this.notifySentOn = notifySentOn;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getTotalLead() {
        return totalLead;
    }

    public void setTotalLead(Integer totalLead) {
        this.totalLead = totalLead;
    }

}
