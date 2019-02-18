package in.exuber.usmarket.models.faq;

public class FaqOutput {
    private String faqId,question,answer;

    public FaqOutput(String faqId, String question, String answer) {
        this.faqId = faqId;
        this.question = question;
        this.answer = answer;
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
}
