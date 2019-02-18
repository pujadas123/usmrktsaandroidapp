package in.exuber.usmarket.models.leads;

import java.util.ArrayList;

public class LeadsOutput {

    private String LeadId,LeadUserName,firstName,lastName, LeadSourceID, LeadSourceName,LeadphoneNo,Leademail,Leadfacebook,Leadinstagram,Leadtwitter,LeadWebsite, CategoryInterests;
    private  int LeadsLength;

    public LeadsOutput(String leadId, String leadUserName, String firstName, String lastName, String leadSourceID, String leadSourceName, String leadphoneNo, String leademail, String leadfacebook, String leadinstagram, String leadtwitter, String leadWebsite, String categoryInterests, int leadsLength) {
        LeadId = leadId;
        LeadUserName = leadUserName;
        this.firstName = firstName;
        this.lastName = lastName;
        LeadSourceID = leadSourceID;
        LeadSourceName = leadSourceName;
        LeadphoneNo = leadphoneNo;
        Leademail = leademail;
        Leadfacebook = leadfacebook;
        Leadinstagram = leadinstagram;
        Leadtwitter = leadtwitter;
        LeadWebsite = leadWebsite;
        CategoryInterests = categoryInterests;
        LeadsLength = leadsLength;
    }

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
    }

    public String getLeadUserName() {
        return LeadUserName;
    }

    public void setLeadUserName(String leadUserName) {
        LeadUserName = leadUserName;
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

    public String getLeadSourceID() {
        return LeadSourceID;
    }

    public void setLeadSourceID(String leadSourceID) {
        LeadSourceID = leadSourceID;
    }

    public String getLeadSourceName() {
        return LeadSourceName;
    }

    public void setLeadSourceName(String leadSourceName) {
        LeadSourceName = leadSourceName;
    }

    public String getLeadphoneNo() {
        return LeadphoneNo;
    }

    public void setLeadphoneNo(String leadphoneNo) {
        LeadphoneNo = leadphoneNo;
    }

    public String getLeademail() {
        return Leademail;
    }

    public void setLeademail(String leademail) {
        Leademail = leademail;
    }

    public String getLeadfacebook() {
        return Leadfacebook;
    }

    public void setLeadfacebook(String leadfacebook) {
        Leadfacebook = leadfacebook;
    }

    public String getLeadinstagram() {
        return Leadinstagram;
    }

    public void setLeadinstagram(String leadinstagram) {
        Leadinstagram = leadinstagram;
    }

    public String getLeadtwitter() {
        return Leadtwitter;
    }

    public void setLeadtwitter(String leadtwitter) {
        Leadtwitter = leadtwitter;
    }

    public String getLeadWebsite() {
        return LeadWebsite;
    }

    public void setLeadWebsite(String leadWebsite) {
        LeadWebsite = leadWebsite;
    }

    public String getCategoryInterests() {
        return CategoryInterests;
    }

    public void setCategoryInterests(String categoryInterests) {
        CategoryInterests = categoryInterests;
    }

    public int getLeadsLength() {
        return LeadsLength;
    }

    public void setLeadsLength(int leadsLength) {
        LeadsLength = leadsLength;
    }
}
