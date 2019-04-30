package in.exuber.usmarket.utils;


public class Constants {

    //App Constants
    public static final String PREFERENCE_NAME = "us_market_preference_15_02_2019";

    public static final String IS_LOGGED_IN = "is_logged_in";

    public static final String IS_APPINTRO_OVER = "is_appintro_over";
    public static final String IS_PRODUCT_ADDED = "is_product_added";
    public static final String IS_SHOWCASE_SHOWN = "is_showcase_shown";


    public static final String IS_PRODUCT_DATA_CHANGED = "is_product_data_changed";
    public static final String IS_ACTIVE_LEAD_DATA_CHANGED = "is_active_lead_data_changed";
    public static final String IS_READY_LEAD_DATA_CHANGED = "is_ready_lead_data_changed";
    public static final String IS_CONVERTED_LEAD_DATA_CHANGED = "is_converted_lead_data_changed";

    public static final String IS_NOTIFICATION_CLICK = "is_notification_click";

    public static final String IS_REMEMBER_ME_SELECTED = "is_remember_me_selected";
    public static final String REMEMBER_ME_EMAIL = "remember_me_email";
    public static final String REMEMBER_ME_PASSWORD = "remember_me_password";

    public static final String LOGIN_DATA_STATUS = "ACTIVE";
    public static final String LOGIN_RESPONSE = "login_response";
    public static final String LOGIN_USER_ID = "login_user_id";
    public static final String LOGIN_ROLE_ID = "login_role_id";
    public static final String LOGIN_ACCESSTOKEN_ID = "login_access_token_id";

    public static final String SALES_ASSOCIATE_ROLE_ID = "38";

    public static  final String INTENT_KEY_SELECTED_PRODUCT = "selected_product";
    public static  final String INTENT_KEY_IS_USER_PRODUCT = "is_user_product";

    public static  final String INTENT_KEY_SELECTED_LEAD = "selected_lead";

    public static  final String INTENT_KEY_SELECTED_CAMPAIGN = "selected_campaign";
    public static  final String INTENT_KEY_SELECTED_VIDEO_URL = "selected_video_url";

    public static  final String INTENT_KEY_SELECTED_FILE_URL = "selected_file_url";

    public static  final String LEADSOURCE_SOCIAL_NETWORK_ID = "54";
    public static  final String LEADSOURCE_WEBSITE_ID = "25";
    public static  final String LEADSOURCE_EMAIL_ID = "26";
    public static  final String LEADSOURCE_PHONE_ID = "27";
    public static  final String LEADSOURCE_REFEREL_ID = "48";
    public static  final String LEADSOURCE_OTHER_ID = "49";

    public static  final String PRODUCT_CATEGORY_REALESTATE_ID = "1";
    public static  final String PRODUCT_CATEGORY_INVESTMENT_ID = "2";

    public static  final String SHARE_FACEBOOK_ID = "51";
    public static  final String SHARE_INSTAGRAM_ID = "29";
    public static  final String SHARE_TWITTER_ID = "30";
    public static  final String SHARE_WHATSAPP_ID = "50";

    public static  final String CAMPAIGN_LANGUAGE_ENGLISH_ID = "33";
    public static  final String CAMPAIGN_LANGUAGE_SPANISH_ID = "34";
    public static  final String CAMPAIGN_LANGUAGE_FRENCH_ID = "35";

    public static  final String TRAINING_PDF_ID = "20";
    public static  final String TRAINING_IMAGE_ID = "21";
    public static  final String TRAINING_VIDEO_ID = "22";
    public static  final String TRAINING_LINK_ID = "46";
    public static  final String TRAINING_ARTICLE_ID = "47";

    public static  final String NOTIFICATION_SEEN_STATUS = "SEEN";

    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";


    //Service Names
    public static final String SERVICE_GET_PRODUCT_LIST = "getProductList";
    public static final String SERVICE_GET_PRODUCT_LIST_BY_USERID = "getUserProductByUserId";

    public static final String SERVICE_ADD_PRODUCT = "insertUserProduct";
    public static final String SERVICE_EDIT_PRODUCT = "updateUserProduct";

    public static final String SERVICE_GET_PRODUCT_TRAINING = "getTrainingByProductId";

    public static final String SERVICE_GET_ACTIVE_LEADS = "getLeadsBySA";
    public static final String SERVICE_GET_READY_LEADS = "getReadyLeads";
    public static final String SERVICE_GET_CONVERTED_LEADS = "getLeadsByCustomers";

    public static final String SERVICE_ADD_LEADS = "insertLeadsByCustomer";
    public static final String SERVICE_EDIT_LEADS = "updateCustomerLeads";

    public static final String SERVICE_GET_NEW_CAMPAIGNS = "getNewCompaign";
    public static final String SERVICE_GET_EXISTING_CAMPAIGNS = "getExistingCompaign";
    public static final String SERVICE_GET_CAMPAIGNS_BY_PRODUCT_ID = "getCampaignByProductId";
    public static final String SERVICE_GET_SHARED_CAMPAIGNS = "getSharedCampaignByUser";


    public static final String SERVICE_SHARE_CAMPAIGNS = "insertSharedCampaign";
    public static final String SERVICE_SHARE_CAMPAIGNS_LOGS = "getDataByCampAndUser";

    public static final String SERVICE_GET_CAMPAIGN_TRAINING = "getTrainingByCampaignId";

    public static final String SERVICE_GET_FAQ = "getFaq";
    public static final String SERVICE_GET_GLOSSARY = "getGlossary";

    public static final String SERVICE_GET_PAYMENT_INFO = "getPaymentInfoById";
    public static final String SERVICE_EDIT_PAYMENT_INFO = "insertPaymentInfo";

    public static final String SERVICE_GET_NOTIFICATIONS = "getNotify";
    public static final String SERVICE_UPDATE_NOTIFICATION_SEEN = "getNotify";

    public static final String SERVICE_GET_PAID_COMMISSIONS = "getSalesPaymentBySa";


    ///Services..............
    public static String USER_ID = "user_id";

    public static final String DOMAIN = "http://usmrkt.us-east-2.elasticbeanstalk.com";


    /////Update Profile Service............
    public static final String APP_UPDATE_PROFILE_SERVICENAME ="updateUser";
    public static final String APP_UPDATE_PROFILE_LIST = DOMAIN + "/SuperAdmin/User/";


    ////Profile Get Data List Service.............
    public static final String APP_PROFILE_INFO_DISPLAY_SERVICE_NAME = "getReportData";
    public static final String APP_PROFILE_INFO_DISPLAY_API = DOMAIN + "/SuperAdmin/ReportData/";

    ////Paid Comission List Service.............
    public static final String APP_PAID_COMISSION_SERVICE_NAME = "getSalesPaymentBySa";
    public static final String APP_PAID_COMISSION_LIST_API = DOMAIN + "/SuperAdmin/SalesPayment/";

    //DONE
    public static final String APP_REGISTER_API = DOMAIN + "/SuperAdmin/SalesAssociate/";

    public static final String APP_LOGIN_API = DOMAIN + "/Login/";

    ////Product List Service.............
    public static final String APP_PRODUCT_LIST_SERVICE_NAME_API = "getProducts";
    public static final String APP_PRODUCT_LIST_API = DOMAIN + "/POAdmin/ProductList/";

    ////Add_Product List Service.............
    public static final String APP_SELECT_PRODUCT_LIST_SERVICE_NAME_API = "insertUserProduct";
    public static final String APP_SELECT_PRODUCT_LIST_API = DOMAIN + "/SuperAdmin/UserProduct/";

    ////Edit_Product List Service.............
    public static final String APP_EDIT_PRODUCT_LIST_SERVICE_NAME_API = "updateUserProduct";
    public static final String APP_EDIT_PRODUCT_LIST_API = DOMAIN + "/SuperAdmin/UpdateUserProduct/";


    ////Selected_Product List Service.............
    public static final String APP_SELECTED_PRODUCT_LIST_SERVICE_NAME_API = "getUserProductByUserId";
    public static final String APP_SELECTED_PRODUCT_LIST_API = DOMAIN + "/SuperAdmin/UserProductById/";

    ////Campaigns Training List Service.............
    public static final String APP_PRODUCT_TRAINING_LIST_SERVICE_NAME_API = "getTrainingByProductId";
    public static final String APP_PRODUCT_TRAINING_LIST_API = DOMAIN + "/POAdmin/TrainingByProductId/";


    ////Get Converted Leads List Service.............
    public static final String APP_CONVERTED_LEADS_LIST_SERVICE_NAME = "getLeadsByCustomers";
    public static final String APP_CONVERTED_LEADS_LIST_API = DOMAIN + "/SuperAdmin/GetAllLeadsByCustomer/";


    ////Get Converted Leads List Service.............
    public static final String APP_READY_LEADS_LIST_SERVICE_NAME = "getReadyLeads";
    public static final String APP_READY_LEADS_LIST_API = DOMAIN + "/SuperAdmin/ReadyLeads/";

    ////Get Active Leads List Service.............
    public static final String APP_GET_LEAD_LIST_SERVICE_NAME = "getLeadsBySA";
    public static final String APP_GET_LEAD_LIST_API = DOMAIN + "/SuperAdmin/GetAllLeadsBySA/";


    ////Add Leads List Service.............
    public static final String APP_LEAD_LIST_SERVICE_NAME = "insertLeadsByCustomer";
    public static final String APP_LEAD_LIST_API = DOMAIN + "/SuperAdmin/LeadsByCustomer/";

    ////Edit Leads List Service.............
    public static final String APP_EDIT_LEAD_LIST_SERVICE_NAME = "updateCustomerLeads";
    public static final String APP_EDIT_LEAD_LIST_API = DOMAIN + "/SuperAdmin/CustomerUpdate/";

    ////New Campaigns List Service.............
    public static final String APP_CAMPAIGNS_LIST_SERVICE_NAME_API = "getNewCompaign";
    public static final String APP_CAMPAIGNS_LIST_API = DOMAIN + "/POAdmin/NewCampaign/";

    ////Existing Campaigns List Service.............
    public static final String APP_EXISTING_CAMPAIGNS_LIST_SERVICE_NAME_API = "getExistingCompaign";
    public static final String APP_EXISTING_CAMPAIGNS_LIST_API = DOMAIN + "/POAdmin/ExistingCampaign/";

    ////Shared Campaigns List Service.............
    public static final String APP_SHARED_CAMPAIGNS_LIST_SERVICE_NAME_API = "getSharedCampaignByUser";
    public static final String APP_SHARED_CAMPAIGNS_LIST_API = DOMAIN + "/SuperAdmin/SharedCampaignById/";

    ////Campaigns For Share List Service.............
    public static final String APP_CAMPAIGNS_SHARE_LIST_SERVICE_NAME_API = "insertSharedCampaign";
    public static final String APP_CAMPAIGNS_SHARE_LIST_API = DOMAIN + "/SuperAdmin/SharedCampaign/";

    ////Campaigns Training List Service.............
    public static final String APP_CAMPAIGNS_TRAINING_LIST_SERVICE_NAME_API = "getTrainingByCampaignId";
    public static final String APP_CAMPAIGNS_TRAINING_LIST_API = DOMAIN + "/POAdmin/TrainingByCampaignId/";

    ////Campaigns Training List Service.............
    public static final String APP_PRODUCT_CAMPAIGN_LIST_SERVICE_NAME_API = "getCampaignByProductId";
    public static final String APP_PRODUCT_CAMPAIGN_LIST_API = DOMAIN + "/POAdmin/CampaignByProductId/";

    public static final String FILE_DIRECTORY = "/CorporateStop/Files/";

    ////FAQ List Service.............
    public static final String APP_FAQ_LIST_SERVICE_NAME = "getFaq";
    public static final String APP_FAQ_LIST_API = DOMAIN + "/SuperAdmin/Faq/";

    ////Forgot Password Is Email Exists List Service.............
    public static final String APP_FORGOT_PASSWORD_EMAIL_EXISTS_SERVICE_NAME_API = "isEmailExist";
    public static final String APP_FORGOT_PASSWORD_EMAIL_EXISTS_LIST_API = DOMAIN + "/SuperAdmin/Email/";

    ////Forgot Password List Service.............
    public static final String APP_FORGOT_PASSWORD_SERVICE_NAME_API = "getUserByEmailID";
    public static final String APP_FORGOT_PASSWORD_LIST_API = DOMAIN + "/SuperAdmin/UserByEmail/";

    ////Glossery List Service.............
    public static final String APP_GLOSSARY_LIST_SERVICE_NAME = "getGlossary";
    public static final String APP_GLOSSARY_LIST_API = DOMAIN + "/SuperAdmin/Glossary/";

    ////Payment Info Get List Service.............
    public static final String APP_PAYMENT_INFO_DISPLAY_SERVICE_NAME = "getPaymentInfoById";
    public static final String APP_PAYMENT_INFO_DISPLAY_API = DOMAIN + "/SuperAdmin/GetPaymentInfo/";

    ////Payment Info List Service.............
    public static final String APP_PAYMENT_INFO_SERVICE_NAME = "insertPaymentInfo";
    public static final String APP_PAYMENT_INFO_API = DOMAIN + "/SuperAdmin/PaymentInfo/";

    public static final String APP_NOTIFICATION_SERVICE_NAME_API = "getNotify";
    public static final String APP_NOTIFICATION__LIST_API = DOMAIN + "/POAdmin/GetNotifyData/";

    ////Notification PUT Service..................
    public static final String APP_NOTIFICATION_SEEN_SERVICE = "getNotify";
    public static final String APP_NOTIFICATION_SEEN = DOMAIN + "/POAdmin/Notifications/";

    ////AppIntro PUT Service..................
    public static final String APP_APPINTRO_SEEN = DOMAIN + "/SuperAdmin/AppIntro/";





}

