package in.exuber.usmarket.models.paymentinfogetmodel;

public class PaymentInfoGetOutputList {

    String firstName,lastName,address1,address2,apt,city,state,postalCode,country,email,routingNumber,accountNo,swiftCode,paypalName,paypalEmail;

    public PaymentInfoGetOutputList(String firstName, String lastName, String address1, String address2, String apt, String city, String state, String postalCode, String country, String email, String routingNumber, String accountNo, String swiftCode, String paypalName, String paypalEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.address2 = address2;
        this.apt = apt;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.email = email;
        this.routingNumber = routingNumber;
        this.accountNo = accountNo;
        this.swiftCode = swiftCode;
        this.paypalName = paypalName;
        this.paypalEmail = paypalEmail;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getPaypalName() {
        return paypalName;
    }

    public void setPaypalName(String paypalName) {
        this.paypalName = paypalName;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }
}
