package in.exuber.usmarket.models;

public class LeadsActivieCategoryOutput {
    String Categoryid,categoryname;

    public LeadsActivieCategoryOutput(String categoryid, String categoryname) {
        Categoryid = categoryid;
        this.categoryname = categoryname;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String categoryid) {
        Categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
