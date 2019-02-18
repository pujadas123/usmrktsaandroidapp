
package in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lead")
    @Expose
    private Lead lead;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy_ createdBy;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CreatedBy_ getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy_ createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Object createdOn) {
        this.createdOn = createdOn;
    }

}
