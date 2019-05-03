package in.exuber.usmarket.utils;

import java.util.List;

import in.exuber.usmarket.apimodels.addlead.addleadinput.AddLeadInput;
import in.exuber.usmarket.apimodels.addproduct.addproductinput.AddProductInput;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.apimodels.campaigntraining.campaigntrainingoutput.CampaignTrainingOutput;
import in.exuber.usmarket.apimodels.categorylist.CategoryListOutput;
import in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput.ConvertedLeadsOutput;
import in.exuber.usmarket.apimodels.editlead.editleadinput.EditLeadInput;
import in.exuber.usmarket.apimodels.editpaymentinfo.editpaymentinfoinput.EditPaymentInfoInput;
import in.exuber.usmarket.apimodels.editproduct.editproductinput.EditProductInput;
import in.exuber.usmarket.apimodels.faq.faqoutput.FAQOutput;
import in.exuber.usmarket.apimodels.glossary.glossaryoutput.GlossaryOutput;
import in.exuber.usmarket.apimodels.login.logininput.LoginInput;
import in.exuber.usmarket.apimodels.login.loginoutput.LoginOutput;
import in.exuber.usmarket.apimodels.notification.notificationoutput.NotificationOutput;
import in.exuber.usmarket.apimodels.paymentinfo.paymentinfooutput.PaymentInfoOutput;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.apimodels.producttraining.producttrainingoutput.ProductTrainingOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.apimodels.readyleads.readyleadsoutput.ReadyLeadsOutput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.ShareCampaignInput;
import in.exuber.usmarket.apimodels.sharecampaignlog.sharecampaignlogoutput.ShareCampaignLogOutput;
import in.exuber.usmarket.apimodels.sharedcampaign.sharedcampaignoutput.SharedCampaignOutput;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.ShareProductInput;
import in.exuber.usmarket.apimodels.signup.signupoutput.SignupInput;
import in.exuber.usmarket.apimodels.upadateappintro.updateappintroinput.UpdateAppIntroInput;
import in.exuber.usmarket.apimodels.updatemobileid.updatemobileidinput.UpdateMobileIdInput;
import in.exuber.usmarket.apimodels.updatenotificationseen.updatenotificationseeninput.UpdateNotificationSeenInput;
import in.exuber.usmarket.models.ProfilePicModel.ProfileImageModel;
import in.exuber.usmarket.apimodels.paidcommision.PaidCommissionOutput;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {


    //POST - Login
    @POST("MobileLogin/")
    Call<LoginOutput> validateUser(@Body LoginInput loginInput);

    //POST - Register
    @POST("SuperAdmin/SalesAssociate/")
    Call<ResponseBody> registerUser(@Body SignupInput signupInput);

    //PUT - Update App Intro
    @PUT("SuperAdmin/AppIntro/")
    Call<ResponseBody> updateAppIntro(@Body UpdateAppIntroInput updateAppIntroInput);

    //GET - Email Check
    @GET("SuperAdmin/Email/{emailId}/")
    Call<ResponseBody> emailCheck(@Path("emailId") String emailId);

    //GET - Forgot Password
    @GET("SuperAdmin/UserByEmail/{emailId}/")
    Call<ResponseBody> forgotPassword(@Path("emailId") String emailId);

    //PUT - Update Mobile Id
    @PUT("SuperAdmin/UpdateUserMobileId/")
    Call<ResponseBody> updateMobileId(@Body UpdateMobileIdInput updateMobileIdInput);

    //GET - Get Products
    @GET("POAdmin/ProductList/")
    Call<List<ProductOutput>> getProduct(@Header("auth-token") String authToken,
                                         @Header("user-id") String userId,
                                         @Header("role-id") String roleId,
                                         @Header("service") String service);

    //GET - Get Products By UserId
    @GET("SuperAdmin/UserProductById/{userId}/")
    Call<List<ProductUserOutput>> getProductByUserId(@Header("auth-token") String authToken,
                                                     @Header("user-id") String userId,
                                                     @Header("role-id") String roleId,
                                                     @Header("service") String service,
                                                     @Path("userId") String userIdPath);

    //POST - Add Products
    @POST("SuperAdmin/UserProduct/")
    Call<ResponseBody> addProduct(@Header("auth-token") String authToken,
                                  @Header("user-id") String userId,
                                  @Header("role-id") String roleId,
                                  @Header("service") String service,
                                  @Body List<AddProductInput> addProductInputList);

    //PUT - Edit Products
    @PUT("SuperAdmin/UpdateUserProduct/")
    Call<ResponseBody> editProduct(@Header("auth-token") String authToken,
                                   @Header("user-id") String userId,
                                   @Header("role-id") String roleId,
                                   @Header("service") String service,
                                   @Body List<EditProductInput> editProductInputList);




    //POST - Share Products
    @POST("SuperAdmin/SharedCampaign/")
    Call<ResponseBody> shareProducts(@Header("auth-token") String authToken,
                                     @Header("user-id") String userId,
                                     @Header("role-id") String roleId,
                                     @Header("service") String service,
                                     @Body ShareProductInput shareProductInput);

    //GET - Get Product Training
    @GET("POAdmin/TrainingByProductId/{productId}/")
    Call<List<ProductTrainingOutput>> getProductTraining(@Header("auth-token") String authToken,
                                                              @Header("user-id") String userId,
                                                              @Header("role-id") String roleId,
                                                              @Header("service") String service,
                                                              @Path("productId") String productId);


    //GET - Get All Leads
    @GET("SuperAdmin/GetAllLeadsBySA/")
    Call<List<AllLeadsOutput>> getActiveLeads(@Header("auth-token") String authToken,
                                              @Header("user-id") String userId,
                                              @Header("role-id") String roleId,
                                              @Header("service") String service);

    //GET - Get Ready Leads
    @GET("SuperAdmin/ReadyLeads/")
    Call<List<ReadyLeadsOutput>> getReadyLeads(@Header("auth-token") String authToken,
                                               @Header("user-id") String userId,
                                               @Header("role-id") String roleId,
                                               @Header("service") String service);

    //GET - Get Ready Leads
    @GET("SuperAdmin/GetAllLeadsByCustomer/")
    Call<List<ConvertedLeadsOutput>> getConvertedLeads(@Header("auth-token") String authToken,
                                                       @Header("user-id") String userId,
                                                       @Header("role-id") String roleId,
                                                       @Header("service") String service);


    //POST - ADD Leads
    @POST("SuperAdmin/LeadsByCustomer/")
    Call<ResponseBody> addLeads(@Header("auth-token") String authToken,
                                @Header("user-id") String userId,
                                @Header("role-id") String roleId,
                                @Header("service") String service,
                                @Body AddLeadInput addLeadInput);

    //PUT - Edit Leads
    @PUT("SuperAdmin/CustomerUpdate/")
    Call<ResponseBody> editLeads(@Header("auth-token") String authToken,
                                 @Header("user-id") String userId,
                                 @Header("role-id") String roleId,
                                 @Header("service") String service,
                                 @Body EditLeadInput editLeadInput);


    //GET - Get New Campaigns
    @GET("POAdmin/NewCampaign/")
    Call<List<CampaignOutput>> getNewCampaigns(@Header("auth-token") String authToken,
                                               @Header("user-id") String userId,
                                               @Header("role-id") String roleId,
                                               @Header("service") String service);

    //GET - Get Existing Campaigns
    @GET("POAdmin/ExistingCampaign/")
    Call<List<CampaignOutput>> getExistingCampaigns(@Header("auth-token") String authToken,
                                                    @Header("user-id") String userId,
                                                    @Header("role-id") String roleId,
                                                    @Header("service") String service);

    //GET - Get Campaigns By product Id
    @GET("POAdmin/CampaignByProductId/{productId}/")
    Call<List<CampaignOutput>> getCampaignsByProductId(@Header("auth-token") String authToken,
                                                       @Header("user-id") String userId,
                                                       @Header("role-id") String roleId,
                                                       @Header("service") String service,
                                                       @Path("productId") String productId);


    //POST - Share Campaigns
    @POST("SuperAdmin/SharedCampaign/")
    Call<ResponseBody> shareCampaigns(@Header("auth-token") String authToken,
                                      @Header("user-id") String userId,
                                      @Header("role-id") String roleId,
                                      @Header("service") String service,
                                      @Body ShareCampaignInput shareCampaignInput);

    //GET - Share Campaign Log
    @GET("SuperAdmin/GetData/")
    Call<List<ShareCampaignLogOutput>> getShareCampaignLog(@Header("auth-token") String authToken,
                                                           @Header("user-id") String userId,
                                                           @Header("role-id") String roleId,
                                                           @Header("service") String service,
                                                           @Query("userId") String userIdQuery,
                                                           @Query("campaignId") String campaignIdQuery);

    //GET - Campaign Training
    @GET("POAdmin/TrainingByCampaignId/{campaignId}/")
    Call<List<CampaignTrainingOutput>> getCampaignTraining(@Header("auth-token") String authToken,
                                                           @Header("user-id") String userId,
                                                           @Header("role-id") String roleId,
                                                           @Header("service") String service,
                                                           @Path("campaignId") String campaignId);

    //GET - Shared Campaigns
    @GET("SuperAdmin/SharedCampaignById/{userId}/")
    Call<List<SharedCampaignOutput>> getSharedCampaigns(@Header("auth-token") String authToken,
                                                        @Header("user-id") String userId,
                                                        @Header("role-id") String roleId,
                                                        @Header("service") String service,
                                                        @Path("userId") String userIdPath);

    //GET - Faq
    @GET("SuperAdmin/Faq/")
    Call<List<FAQOutput>> getFAQ(@Header("auth-token") String authToken,
                                 @Header("user-id") String userId,
                                 @Header("role-id") String roleId,
                                 @Header("service") String service);

    //GET - Glossary
    @GET("SuperAdmin/Glossary/")
    Call<List<GlossaryOutput>> getGlossary(@Header("auth-token") String authToken,
                                           @Header("user-id") String userId,
                                           @Header("role-id") String roleId,
                                           @Header("service") String service);

    //GET - Payment Info
    @GET("SuperAdmin/GetPaymentInfo/{userId}/")
    Call<PaymentInfoOutput> getPaymentInfo(@Header("auth-token") String authToken,
                                           @Header("user-id") String userId,
                                           @Header("role-id") String roleId,
                                           @Header("service") String service,
                                           @Path("userId") String userIdPath);

    //POST - Edit Payment Info
    @POST("SuperAdmin/PaymentInfo/")
    Call<ResponseBody> editPaymentInfo(@Header("auth-token") String authToken,
                                       @Header("user-id") String userId,
                                       @Header("role-id") String roleId,
                                       @Header("service") String service,
                                       @Body EditPaymentInfoInput editPaymentInfoInput);



    //GET - Notifications
    @GET("POAdmin/GetNotifyData/")
    Call<List<NotificationOutput>> getNotification(@Header("auth-token") String authToken,
                                                   @Header("user-id") String userId,
                                                   @Header("role-id") String roleId,
                                                   @Header("service") String service);

    //PUT - Notifications
    @PUT("POAdmin/Notifications/")
    Call<ResponseBody> updateNotificationSeen(@Header("auth-token") String authToken,
                                              @Header("user-id") String userId,
                                              @Header("role-id") String roleId,
                                              @Header("service") String service,
                                              @Body UpdateNotificationSeenInput updateNotificationSeenInput);

    //GET - Paid Commissions
    @GET("SuperAdmin/SalesPayment/{userId}/")
    Call<List<PaidCommissionOutput>> getPaidCommissions(@Header("auth-token") String authToken,
                                                        @Header("user-id") String userId,
                                                        @Header("role-id") String roleId,
                                                        @Header("service") String service,
                                                        @Path("userId") String userIdPath);


    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);



    //Multipart - Upload Image
    @Multipart
    @POST("/SuperAdmin/UserProfile/")
    Call<ProfileImageModel> uploadImage(
            @Part MultipartBody.Part file,
            @Part("userId") RequestBody userId);

    //GET - Get Category
    @GET("POAdmin/Category/")
    Call<List<CategoryListOutput>> getCategoryList(@Header("auth-token") String authToken,
                                                      @Header("user-id") String userId,
                                                      @Header("role-id") String roleId,
                                                      @Header("service") String service);


}
