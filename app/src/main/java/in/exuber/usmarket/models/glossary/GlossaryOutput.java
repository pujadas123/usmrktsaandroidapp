package in.exuber.usmarket.models.glossary;

public class GlossaryOutput {

    String glossaryId;
    String term;
    String defination;

    public GlossaryOutput(String glossaryId, String term, String defination) {
        this.glossaryId = glossaryId;
        this.term = term;
        this.defination = defination;
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
}
