
package in.exuber.usmarket.apimodels.readyleads.readyleadsoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lead {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("leadId")
    @Expose
    private String leadId;
    @SerializedName("userId")
    @Expose
    private Object userId;
    @SerializedName("companyId")
    @Expose
    private Object companyId;
    @SerializedName("product")
    @Expose
    private Product__ product;
    @SerializedName("campaign")
    @Expose
    private Campaign_ campaign;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("phoneNo")
    @Expose
    private Object phoneNo;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("userName")
    @Expose
    private Object userName;
    @SerializedName("leadStatus")
    @Expose
    private Object leadStatus;
    @SerializedName("source")
    @Expose
    private Object source;
    @SerializedName("leadSource")
    @Expose
    private Object leadSource;
    @SerializedName("contactInfo")
    @Expose
    private Object contactInfo;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("platform")
    @Expose
    private Object platform;
    @SerializedName("assignedTo")
    @Expose
    private Object assignedTo;
    @SerializedName("leadOwner")
    @Expose
    private Object leadOwner;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("categoryInterests")
    @Expose
    private Object categoryInterests;
    @SerializedName("productInterests")
    @Expose
    private Object productInterests;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;
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
    private Object categoryList;
    @SerializedName("productList")
    @Expose
    private Object productList;
    @SerializedName("totalLeads")
    @Expose
    private Integer totalLeads;
    @SerializedName("totalCustomer")
    @Expose
    private Integer totalCustomer;
    @SerializedName("facebook")
    @Expose
    private Object facebook;
    @SerializedName("instagram")
    @Expose
    private Object instagram;
    @SerializedName("website")
    @Expose
    private Object website;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
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

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Product__ getProduct() {
        return product;
    }

    public void setProduct(Product__ product) {
        this.product = product;
    }

    public Campaign_ getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign_ campaign) {
        this.campaign = campaign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Object phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public Object getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Object leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(Object leadSource) {
        this.leadSource = leadSource;
    }

    public Object getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Object contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getPlatform() {
        return platform;
    }

    public void setPlatform(Object platform) {
        this.platform = platform;
    }

    public Object getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Object assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Object getLeadOwner() {
        return leadOwner;
    }

    public void setLeadOwner(Object leadOwner) {
        this.leadOwner = leadOwner;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
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

    public Object getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Object categoryList) {
        this.categoryList = categoryList;
    }

    public Object getProductList() {
        return productList;
    }

    public void setProductList(Object productList) {
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

    public Object getFacebook() {
        return facebook;
    }

    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    public Object getInstagram() {
        return instagram;
    }

    public void setInstagram(Object instagram) {
        this.instagram = instagram;
    }

    public Object getWebsite() {
        return website;
    }

    public void setWebsite(Object website) {
        this.website = website;
    }

    public Object getTwitter() {
        return twitter;
    }

    public void setTwitter(Object twitter) {
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

}
