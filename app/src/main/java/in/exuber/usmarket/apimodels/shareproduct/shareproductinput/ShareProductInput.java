
package in.exuber.usmarket.apimodels.shareproduct.shareproductinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareProductInput {

    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("productId")
    @Expose
    private ProductId productId;

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

}
