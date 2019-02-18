
package in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConvertedLeadsOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("leadId")
    @Expose
    private String leadId;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("companyId")
    @Expose
    private Object companyId;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("campaign")
    @Expose
    private Campaign campaign;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userName")
    @Expose
    private Object userName;
    @SerializedName("leadStatus")
    @Expose
    private LeadStatus leadStatus;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("leadSource")
    @Expose
    private LeadSource leadSource;
    @SerializedName("contactInfo")
    @Expose
    private Object contactInfo;
    @SerializedName("dataStatus")
    @Expose
    private String dataStatus;
    @SerializedName("platform")
    @Expose
    private Object platform;
    @SerializedName("assignedTo")
    @Expose
    private AssignedTo assignedTo;
    @SerializedName("leadOwner")
    @Expose
    private LeadOwner leadOwner;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("categoryInterests")
    @Expose
    private Object categoryInterests;
    @SerializedName("productInterests")
    @Expose
    private Object productInterests;
    @SerializedName("createdOn")
    @Expose
    private Double createdOn;
    @SerializedName("updatedBy")
    @Expose
    private UpdatedBy updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Double updatedOn;
    @SerializedName("approved")
    @Expose
    private Object approved;
    @SerializedName("approvedOn")
    @Expose
    private Object approvedOn;
    @SerializedName("approvedBy")
    @Expose
    private Object approvedBy;
    @SerializedName("address1")
    @Expose
    private Object address1;
    @SerializedName("address2")
    @Expose
    private Object address2;
    @SerializedName("unit")
    @Expose
    private Object unit;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("postalCode")
    @Expose
    private Object postalCode;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("convertedLead")
    @Expose
    private Object convertedLead;
    @SerializedName("categoryList")
    @Expose
    private List<CategoryList> categoryList = null;
    @SerializedName("productList")
    @Expose
    private List<ProductList> productList = null;
    @SerializedName("totalLeads")
    @Expose
    private Integer totalLeads;
    @SerializedName("totalCustomer")
    @Expose
    private Integer totalCustomer;
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
    @SerializedName("sn1")
    @Expose
    private Object sn1;
    @SerializedName("sn2")
    @Expose
    private Object sn2;
    @SerializedName("sn3")
    @Expose
    private Object sn3;
    @SerializedName("sn4")
    @Expose
    private Object sn4;
    @SerializedName("un1")
    @Expose
    private Object un1;
    @SerializedName("un2")
    @Expose
    private Object un2;
    @SerializedName("un3")
    @Expose
    private Object un3;
    @SerializedName("un4")
    @Expose
    private Object un4;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public LeadStatus getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public LeadSource getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(LeadSource leadSource) {
        this.leadSource = leadSource;
    }

    public Object getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Object contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getPlatform() {
        return platform;
    }

    public void setPlatform(Object platform) {
        this.platform = platform;
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

    public Object getCategoryInterests() {
        return categoryInterests;
    }

    public void setCategoryInterests(Object categoryInterests) {
        this.categoryInterests = categoryInterests;
    }

    public Object getProductInterests() {
        return productInterests;
    }

    public void setProductInterests(Object productInterests) {
        this.productInterests = productInterests;
    }

    public Double getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Double createdOn) {
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

    public Object getApproved() {
        return approved;
    }

    public void setApproved(Object approved) {
        this.approved = approved;
    }

    public Object getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(Object approvedOn) {
        this.approvedOn = approvedOn;
    }

    public Object getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Object approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Object getAddress1() {
        return address1;
    }

    public void setAddress1(Object address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getConvertedLead() {
        return convertedLead;
    }

    public void setConvertedLead(Object convertedLead) {
        this.convertedLead = convertedLead;
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

    public Integer getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(Integer totalLeads) {
        this.totalLeads = totalLeads;
    }

    public Integer getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(Integer totalCustomer) {
        this.totalCustomer = totalCustomer;
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

    public Object getSn1() {
        return sn1;
    }

    public void setSn1(Object sn1) {
        this.sn1 = sn1;
    }

    public Object getSn2() {
        return sn2;
    }

    public void setSn2(Object sn2) {
        this.sn2 = sn2;
    }

    public Object getSn3() {
        return sn3;
    }

    public void setSn3(Object sn3) {
        this.sn3 = sn3;
    }

    public Object getSn4() {
        return sn4;
    }

    public void setSn4(Object sn4) {
        this.sn4 = sn4;
    }

    public Object getUn1() {
        return un1;
    }

    public void setUn1(Object un1) {
        this.un1 = un1;
    }

    public Object getUn2() {
        return un2;
    }

    public void setUn2(Object un2) {
        this.un2 = un2;
    }

    public Object getUn3() {
        return un3;
    }

    public void setUn3(Object un3) {
        this.un3 = un3;
    }

    public Object getUn4() {
        return un4;
    }

    public void setUn4(Object un4) {
        this.un4 = un4;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
