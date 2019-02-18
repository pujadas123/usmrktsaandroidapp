
package in.exuber.usmarket.apimodels.product.productoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("company")
    @Expose
    private Company company;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("intro")
    @Expose
    private String intro;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("closure")
    @Expose
    private String closure;
    @SerializedName("contactDetails")
    @Expose
    private String contactDetails;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("image2")
    @Expose
    private String image2;
    @SerializedName("image3")
    @Expose
    private String image3;
    @SerializedName("image4")
    @Expose
    private String image4;
    @SerializedName("image5")
    @Expose
    private String image5;
    @SerializedName("video1")
    @Expose
    private String video1;
    @SerializedName("video2")
    @Expose
    private String video2;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("available")
    @Expose
    private Object available;
    @SerializedName("productCategory")
    @Expose
    private ProductCategory productCategory;
    @SerializedName("saleAgreement")
    @Expose
    private String saleAgreement;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("dataStatus")
    @Expose
    private String dataStatus;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("createdOn")
    @Expose
    private Long createdOn;
    @SerializedName("updatedBy")
    @Expose
    private UpdatedBy updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Double updatedOn;
    @SerializedName("totalProduct")
    @Expose
    private Integer totalProduct;
    @SerializedName("restrictions")
    @Expose
    private Object restrictions;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }

    public String getVideo2() {
        return video2;
    }

    public void setVideo2(String video2) {
        this.video2 = video2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public Object getAvailable() {
        return available;
    }

    public void setAvailable(Object available) {
        this.available = available;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getSaleAgreement() {
        return saleAgreement;
    }

    public void setSaleAgreement(String saleAgreement) {
        this.saleAgreement = saleAgreement;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Double getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Double updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Integer totalProduct) {
        this.totalProduct = totalProduct;
    }

    public Object getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Object restrictions) {
        this.restrictions = restrictions;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
