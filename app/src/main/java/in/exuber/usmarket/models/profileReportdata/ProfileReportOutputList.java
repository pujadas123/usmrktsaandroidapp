package in.exuber.usmarket.models.profileReportdata;

public class ProfileReportOutputList {

    String myLeads,myConLeads,myProduct,mySharedCamp;

    public ProfileReportOutputList(String myLeads, String myConLeads, String myProduct, String mySharedCamp) {
        this.myLeads = myLeads;
        this.myConLeads = myConLeads;
        this.myProduct = myProduct;
        this.mySharedCamp = mySharedCamp;
    }

    public String getMyLeads() {
        return myLeads;
    }

    public void setMyLeads(String myLeads) {
        this.myLeads = myLeads;
    }

    public String getMyConLeads() {
        return myConLeads;
    }

    public void setMyConLeads(String myConLeads) {
        this.myConLeads = myConLeads;
    }

    public String getMyProduct() {
        return myProduct;
    }

    public void setMyProduct(String myProduct) {
        this.myProduct = myProduct;
    }

    public String getMySharedCamp() {
        return mySharedCamp;
    }

    public void setMySharedCamp(String mySharedCamp) {
        this.mySharedCamp = mySharedCamp;
    }
}
