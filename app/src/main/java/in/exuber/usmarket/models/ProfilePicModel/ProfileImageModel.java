
package in.exuber.usmarket.models.ProfilePicModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userKey")
    @Expose
    private Object userKey;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("otp")
    @Expose
    private Object otp;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("role")
    @Expose
    private Role role;
    @SerializedName("picId")
    @Expose
    private String picId;
    @SerializedName("deviceId")
    @Expose
    private Object deviceId;
    @SerializedName("gLatitude")
    @Expose
    private Object gLatitude;
    @SerializedName("gLongitude")
    @Expose
    private Object gLongitude;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("prefLanguage")
    @Expose
    private Object prefLanguage;
    @SerializedName("devicePlatform")
    @Expose
    private Object devicePlatform;
    @SerializedName("socialType")
    @Expose
    private Object socialType;
    @SerializedName("socialId")
    @Expose
    private Object socialId;
    @SerializedName("socialImage")
    @Expose
    private Object socialImage;
    @SerializedName("company")
    @Expose
    private Object company;
    @SerializedName("dataStatus")
    @Expose
    private String dataStatus;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdOn")
    @Expose
    private Double createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Double updatedOn;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("physicalAddress")
    @Expose
    private Object physicalAddress;
    @SerializedName("education")
    @Expose
    private Object education;
    @SerializedName("workExperience")
    @Expose
    private Object workExperience;
    @SerializedName("linkedin")
    @Expose
    private Object linkedin;
    @SerializedName("facebook")
    @Expose
    private Object facebook;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
    @SerializedName("instagram")
    @Expose
    private Object instagram;
    @SerializedName("accessId")
    @Expose
    private Object accessId;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("userStatus")
    @Expose
    private Object userStatus;
    @SerializedName("totalSalesAssociate")
    @Expose
    private Integer totalSalesAssociate;
    @SerializedName("totalLeads")
    @Expose
    private Integer totalLeads;
    @SerializedName("totalCustomer")
    @Expose
    private Integer totalCustomer;
    @SerializedName("totalCompany")
    @Expose
    private Integer totalCompany;
    @SerializedName("totalProduct")
    @Expose
    private Integer totalProduct;
    @SerializedName("totalCampaign")
    @Expose
    private Integer totalCampaign;
    @SerializedName("totalSales")
    @Expose
    private Integer totalSales;
    @SerializedName("bank_details")
    @Expose
    private Object bankDetails;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProfileImageModel() {
    }

    /**
     * 
     * @param address1
     * @param picId
     * @param address2
     * @param type
     * @param password
     * @param city
     * @param bankDetails
     * @param socialImage
     * @param twitter
     * @param postalCode
     * @param userKey
     * @param userId
     * @param facebook
     * @param role
     * @param physicalAddress
     * @param otp
     * @param deviceId
     * @param gLongitude
     * @param lastName
     * @param totalSales
     * @param socialType
     * @param prefLanguage
     * @param totalCustomer
     * @param country
     * @param createdOn
     * @param phoneNo
     * @param createdBy
     * @param email
     * @param company
     * @param dataStatus
     * @param userStatus
     * @param totalLeads
     * @param instagram
     * @param state
     * @param devicePlatform
     * @param gLatitude
     * @param education
     * @param accessId
     * @param updatedBy
     * @param id
     * @param totalProduct
     * @param totalCompany
     * @param name
     * @param updatedOn
     * @param linkedin
     * @param totalSalesAssociate
     * @param totalCampaign
     * @param unit
     * @param workExperience
     * @param dob
     * @param socialId
     */
    public ProfileImageModel(Integer id, String userId, String name, Object userKey, String email, String password, Object otp, String phoneNo, Role role, String picId, Object deviceId, Object gLatitude, Object gLongitude, Object country, Object prefLanguage, Object devicePlatform, Object socialType, Object socialId, Object socialImage, Object company, String dataStatus, Object createdBy, Double createdOn, Object updatedBy, Double updatedOn, Object dob, Object physicalAddress, Object education, Object workExperience, Object linkedin, Object facebook, Object twitter, Object instagram, Object accessId, Object type, String lastName, String address1, String address2, String city, String unit, String state, String postalCode, Object userStatus, Integer totalSalesAssociate, Integer totalLeads, Integer totalCustomer, Integer totalCompany, Integer totalProduct, Integer totalCampaign, Integer totalSales, Object bankDetails) {
        super();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.userKey = userKey;
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.phoneNo = phoneNo;
        this.role = role;
        this.picId = picId;
        this.deviceId = deviceId;
        this.gLatitude = gLatitude;
        this.gLongitude = gLongitude;
        this.country = country;
        this.prefLanguage = prefLanguage;
        this.devicePlatform = devicePlatform;
        this.socialType = socialType;
        this.socialId = socialId;
        this.socialImage = socialImage;
        this.company = company;
        this.dataStatus = dataStatus;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
        this.dob = dob;
        this.physicalAddress = physicalAddress;
        this.education = education;
        this.workExperience = workExperience;
        this.linkedin = linkedin;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.accessId = accessId;
        this.type = type;
        this.lastName = lastName;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.unit = unit;
        this.state = state;
        this.postalCode = postalCode;
        this.userStatus = userStatus;
        this.totalSalesAssociate = totalSalesAssociate;
        this.totalLeads = totalLeads;
        this.totalCustomer = totalCustomer;
        this.totalCompany = totalCompany;
        this.totalProduct = totalProduct;
        this.totalCampaign = totalCampaign;
        this.totalSales = totalSales;
        this.bankDetails = bankDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getUserKey() {
        return userKey;
    }

    public void setUserKey(Object userKey) {
        this.userKey = userKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
        this.otp = otp;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public Object getGLatitude() {
        return gLatitude;
    }

    public void setGLatitude(Object gLatitude) {
        this.gLatitude = gLatitude;
    }

    public Object getGLongitude() {
        return gLongitude;
    }

    public void setGLongitude(Object gLongitude) {
        this.gLongitude = gLongitude;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getPrefLanguage() {
        return prefLanguage;
    }

    public void setPrefLanguage(Object prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    public Object getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(Object devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    public Object getSocialType() {
        return socialType;
    }

    public void setSocialType(Object socialType) {
        this.socialType = socialType;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Object getSocialImage() {
        return socialImage;
    }

    public void setSocialImage(Object socialImage) {
        this.socialImage = socialImage;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Double getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Double createdOn) {
        this.createdOn = createdOn;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Double getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Double updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(Object physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Object getEducation() {
        return education;
    }

    public void setEducation(Object education) {
        this.education = education;
    }

    public Object getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Object workExperience) {
        this.workExperience = workExperience;
    }

    public Object getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(Object linkedin) {
        this.linkedin = linkedin;
    }

    public Object getFacebook() {
        return facebook;
    }

    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    public Object getTwitter() {
        return twitter;
    }

    public void setTwitter(Object twitter) {
        this.twitter = twitter;
    }

    public Object getInstagram() {
        return instagram;
    }

    public void setInstagram(Object instagram) {
        this.instagram = instagram;
    }

    public Object getAccessId() {
        return accessId;
    }

    public void setAccessId(Object accessId) {
        this.accessId = accessId;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Object getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Object userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getTotalSalesAssociate() {
        return totalSalesAssociate;
    }

    public void setTotalSalesAssociate(Integer totalSalesAssociate) {
        this.totalSalesAssociate = totalSalesAssociate;
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

    public Integer getTotalCompany() {
        return totalCompany;
    }

    public void setTotalCompany(Integer totalCompany) {
        this.totalCompany = totalCompany;
    }

    public Integer getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Integer totalProduct) {
        this.totalProduct = totalProduct;
    }

    public Integer getTotalCampaign() {
        return totalCampaign;
    }

    public void setTotalCampaign(Integer totalCampaign) {
        this.totalCampaign = totalCampaign;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Object getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(Object bankDetails) {
        this.bankDetails = bankDetails;
    }

}
