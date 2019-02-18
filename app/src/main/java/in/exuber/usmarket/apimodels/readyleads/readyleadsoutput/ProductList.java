
package in.exuber.usmarket.apimodels.readyleads.readyleadsoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lead")
    @Expose
    private Lead_ lead;
    @SerializedName("product")
    @Expose
    private Product______ product;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy__ createdBy;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lead_ getLead() {
        return lead;
    }

    public void setLead(Lead_ lead) {
        this.lead = lead;
    }

    public Product______ getProduct() {
        return product;
    }

    public void setProduct(Product______ product) {
        this.product = product;
    }

    public CreatedBy__ getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy__ createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Object createdOn) {
        this.createdOn = createdOn;
    }

}
