
package in.exuber.usmarket.apimodels.editlead.editleadinput;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.exuber.usmarket.apimodels.addlead.addleadinput.ProductList;

public class EditLeadInput {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("leadId")
    @Expose
    private String leadId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("flagCode")
    @Expose
    private String flagCode;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("leadSource")
    @Expose
    private LeadSource leadSource;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("categoryList")
    @Expose
    private List<CategoryList> categoryList = null;
    @SerializedName("productList")
    @Expose
    private List<ProductList> productList = null;
    @SerializedName("assignedTo")
    @Expose
    private AssignedTo assignedTo;
    @SerializedName("leadOwner")
    @Expose
    private LeadOwner leadOwner;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("updatedBy")
    @Expose
    private UpdatedBy updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFlagCode() {
        return flagCode;
    }

    public void setFlagCode(String flagCode) {
        this.flagCode = flagCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LeadSource getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(LeadSource leadSource) {
        this.leadSource = leadSource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public List<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public AssignedTo getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LeadOwner getLeadOwner() {
        return leadOwner;
    }

    public void setLeadOwner(LeadOwner leadOwner) {
        this.leadOwner = leadOwner;
    }

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

    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }
}
