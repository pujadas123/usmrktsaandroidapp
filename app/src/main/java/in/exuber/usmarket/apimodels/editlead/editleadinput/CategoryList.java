
package in.exuber.usmarket.apimodels.editlead.editleadinput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList {

    @SerializedName("category")
    @Expose
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
