
package in.exuber.usmarket.apimodels.product.productoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategory {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private Object desc;
    @SerializedName("url")
    @Expose
    private Object url;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
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

}
