package in.exuber.usmarket.models.paidcommision;

public class PaidCommissionOutput {
    String productId,productName;
    int paid,totalAmount;

    public PaidCommissionOutput(String productId, String productName, int paid, int totalAmount) {
        this.productId = productId;
        this.productName = productName;
        this.paid = paid;
        this.totalAmount = totalAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
