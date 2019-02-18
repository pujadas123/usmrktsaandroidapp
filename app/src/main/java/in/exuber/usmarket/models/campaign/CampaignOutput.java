package in.exuber.usmarket.models.campaign;

import java.util.ArrayList;

public class CampaignOutput {

    private String compaignId,compaignName,compaignDesc,productName,ProductCategory,ProductPrice,ProductCommission,ProductDesc,
            ProductImg1,ProductImg2,ProductImg3,ProductImg4,compaignImage,categoryId,categoryName,id,languageName,createdOn;

    public CampaignOutput(String compaignId, String compaignName, String compaignDesc, String productName, String productCategory, String productPrice, String productCommission, String productDesc, String productImg1, String productImg2, String productImg3, String productImg4, String compaignImage, String categoryId, String categoryName, String id, String languageName, String createdOn) {
        this.compaignId = compaignId;
        this.compaignName = compaignName;
        this.compaignDesc = compaignDesc;
        this.productName = productName;
        ProductCategory = productCategory;
        ProductPrice = productPrice;
        ProductCommission = productCommission;
        ProductDesc = productDesc;
        ProductImg1 = productImg1;
        ProductImg2 = productImg2;
        ProductImg3 = productImg3;
        ProductImg4 = productImg4;
        this.compaignImage = compaignImage;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.id = id;
        this.languageName = languageName;
        this.createdOn = createdOn;
    }


    public String getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(String compaignId) {
        this.compaignId = compaignId;
    }

    public String getCompaignName() {
        return compaignName;
    }

    public void setCompaignName(String compaignName) {
        this.compaignName = compaignName;
    }

    public String getCompaignDesc() {
        return compaignDesc;
    }

    public void setCompaignDesc(String compaignDesc) {
        this.compaignDesc = compaignDesc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductCommission() {
        return ProductCommission;
    }

    public void setProductCommission(String productCommission) {
        ProductCommission = productCommission;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getProductImg1() {
        return ProductImg1;
    }

    public void setProductImg1(String productImg1) {
        ProductImg1 = productImg1;
    }

    public String getProductImg2() {
        return ProductImg2;
    }

    public void setProductImg2(String productImg2) {
        ProductImg2 = productImg2;
    }

    public String getProductImg3() {
        return ProductImg3;
    }

    public void setProductImg3(String productImg3) {
        ProductImg3 = productImg3;
    }

    public String getProductImg4() {
        return ProductImg4;
    }

    public void setProductImg4(String productImg4) {
        ProductImg4 = productImg4;
    }

    public String getCompaignImage() {
        return compaignImage;
    }

    public void setCompaignImage(String compaignImage) {
        this.compaignImage = compaignImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
