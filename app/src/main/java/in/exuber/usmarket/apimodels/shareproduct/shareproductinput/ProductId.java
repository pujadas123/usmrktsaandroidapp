
package in.exuber.usmarket.apimodels.shareproduct.shareproductinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductId {

    @SerializedName("productId")
    @Expose
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
