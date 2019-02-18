
package in.exuber.usmarket.apimodels.signup.signupoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupInput {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("prefLanguage")
    @Expose
    private String prefLanguage;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("saAg")
    @Expose
    private String saAg;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("usResident")
    @Expose
    private String usResident;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPrefLanguage() {
        return prefLanguage;
    }

    public void setPrefLanguage(String prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSaAg() {
        return saAg;
    }

    public void setSaAg(String saAg) {
        this.saAg = saAg;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUsResident() {
        return usResident;
    }

    public void setUsResident(String usResident) {
        this.usResident = usResident;
    }

}
