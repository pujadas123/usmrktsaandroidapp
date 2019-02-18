package in.exuber.usmarket.models.notification;

public class NotificationOutput {

    private int id;
    private String notificationDescription;
    private String notificationDate;
    private String notificationType;
    private String notificationSeenType;

    public NotificationOutput(int id, String notificationDescription, String notificationDate, String notificationType, String notificationSeenType) {
        this.id = id;
        this.notificationDescription = notificationDescription;
        this.notificationDate = notificationDate;
        this.notificationType = notificationType;
        this.notificationSeenType = notificationSeenType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationSeenType() {
        return notificationSeenType;
    }

    public void setNotificationSeenType(String notificationSeenType) {
        this.notificationSeenType = notificationSeenType;
    }
}
