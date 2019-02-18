
package in.exuber.usmarket.apimodels.glossary.glossaryoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlossaryOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("glossaryId")
    @Expose
    private String glossaryId;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("defination")
    @Expose
    private String defination;
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

    public String getGlossaryId() {
        return glossaryId;
    }

    public void setGlossaryId(String glossaryId) {
        this.glossaryId = glossaryId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefination() {
        return defination;
    }

    public void setDefination(String defination) {
        this.defination = defination;
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
