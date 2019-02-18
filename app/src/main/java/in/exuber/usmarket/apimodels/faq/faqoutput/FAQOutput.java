
package in.exuber.usmarket.apimodels.faq.faqoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAQOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("faqId")
    @Expose
    private String faqId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;
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
    private Object updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFaqId() {
        return faqId;
    }

    public void setFaqId(String faqId) {
        this.faqId = faqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public Object getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Object updatedOn) {
        this.updatedOn = updatedOn;
    }

}
