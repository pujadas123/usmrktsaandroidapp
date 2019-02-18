
package in.exuber.usmarket.apimodels.addproduct.addproductinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductInput {

    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("productId")
    @Expose
    private ProductId productId;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;

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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

}
