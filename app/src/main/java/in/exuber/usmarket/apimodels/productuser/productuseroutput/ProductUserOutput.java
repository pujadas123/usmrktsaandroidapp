
package in.exuber.usmarket.apimodels.productuser.productuseroutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductUserOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userProductId")
    @Expose
    private String userProductId;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("productId")
    @Expose
    private ProductId productId;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(String userProductId) {
        this.userProductId = userProductId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
