
package in.exuber.usmarket.apimodels.paidcommision;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaidCommissionOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("paymentDate")
    @Expose
    private Double paymentDate;
    @SerializedName("productId")
    @Expose
    private ProductId productId;
    @SerializedName("salesId")
    @Expose
    private SalesId salesId;
    @SerializedName("associateId")
    @Expose
    private AssociateId associateId;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("pending")
    @Expose
    private String pending;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;
    @SerializedName("dataStatus")
    @Expose
    private Object dataStatus;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("createdOn")
    @Expose
    private Double createdOn;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedOn")
    @Expose
    private Object updatedOn;
    @SerializedName("totalCommission")
    @Expose
    private String totalCommission;
    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Double paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public SalesId getSalesId() {
        return salesId;
    }

    public void setSalesId(SalesId salesId) {
        this.salesId = salesId;
    }

    public AssociateId getAssociateId() {
        return associateId;
    }

    public void setAssociateId(AssociateId associateId) {
        this.associateId = associateId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Object getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Object dataStatus) {
        this.dataStatus = dataStatus;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
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

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
