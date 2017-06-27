package com.fanglin.fenhong.microbuyer.base.baselib;

import com.fanglin.fenhong.microbuyer.BuildConfig;

/**
 * 作者： Created by Plucky on 2015/7/6.
 * 基础参数
 * 其实用Global代替Sea似乎更准确
 */
public class BaseVar {
    public static final String MSGNUM = "messagenum";
    public static final String UCENTER = "ucenter";
    public static final String VLOGIN = "login";
    public static final String DEFAREA = "default_area";//记录默认定位地址
    public static final String FIRST = "isFirstTimeStartThisVersion";
    public static final String SETTINGS = "app_settings";
    public static final String DEFQQKF = "2049615914";
    public static final String FAILEDWEB = "file:///android_asset/error.html";
    public static final String FAILEDWEBNULL = "file:///android_asset/error_null.html";

    public static final String OVER100 = "99+";
    public static final String ZEROBYTE = "0B";

    public static final String SEARCH_CACHE = "the_cache_of_search_when_not_login";
    public static final String MEMBER_TYPE_CACHE = "the_cache_of_member_type";

    /**
     * 图片
     */
    public static final String DEFAULT_BANNER = "http://www.fenhongshop.com/data/upload/microshop/default_banner.jpg";


    /**
     * API
     */
    public static final String EXPRESS100 = "http://www.kuaidi100.com/query?type=%1$s&postid=%2$s";
//    public static final String HOST = "https://test.fenhongshop.com";
    public static final String HOST = "https://www.fenhongshop.com";

    public static final String FLAG = "&flag=android&vercode=" + BuildConfig.VERSION_CODE + "&vername=" + BuildConfig.VERSION_NAME;

    public static final String APPDECLARE = HOST + "/www/tmpl/andriod/declare.html?flag=android";

    public static final String API_SHARE_URL_FMT = HOST + "/api/index.php?act=deduct&op=index&flag=android&rurl=%1$s";

    public static final String API_GET_INDEX_SLIDERS = HOST + "/api/index.php?act=common_index&op=get_slides" + FLAG;
    public static final String API_GET_AREA_LIST = HOST + "/api/index.php?act=common_index&op=get_area_list" + FLAG;

    public static final String APP_LOGO = HOST + "/wap/images/wap/logo.png";
    public static final String API_GET_LAST_VERSION = HOST + "/api/index.php?act=common_app&op=get_newest_version" + FLAG;
    public static final String API_GET_HOME_NAVIGATION = HOST + "/api/index.php?act=common_index&op=get_navigation" + FLAG;//首页导航按钮列表

    //物流数据请求路径
    public static final String API_FH_EXPRESS = HOST + "/api/index.php?act=buyer_delivery&op=query_express" + FLAG;

    /**
     * 会员相关API
     */
    public static final String API_LOGIN = HOST + "/api/index.php?act=common_member&op=login" + FLAG;
    public static final String API_REGISTER = HOST + "/api/index.php?act=common_member&op=register" + FLAG;
    public static final String API_GET_CART_NUM = HOST + "/api/index.php?act=buyer_cart&op=goods_count" + FLAG;//获取购物车数量

    public static final String API_GET_SMS_CODE = HOST + "/api/index.php?act=common_member&op=get_sms_code" + FLAG;

    public static final String API_RESET_PWD = HOST + "/api/index.php?act=common_member&op=reset_pwd" + FLAG;

    public static final String API_GET_ADDRESS = HOST + "/api/index.php?act=buyer_delivery&op=get_address" + FLAG;
    public static final String API_SET_ADDRESS = HOST + "/api/index.php?act=buyer_delivery&op=set_address" + FLAG;
    public static final String API_DEL_ADDRESS = HOST + "/api/index.php?act=buyer_delivery&op=del_address" + FLAG;

    public static final String API_GET_MEMBERINFO = HOST + "/api/index.php?act=common_member&op=get_member_info" + FLAG;
    public static final String API_EDIT_MEMBERINFO = HOST + "/api/index.php?act=common_member&op=edit_member_info" + FLAG;
    public static final String API_UCENTER = HOST + "/api/index.php?act=common_member&op=ucenter" + FLAG;
    public static final String API_GET_POINTS = HOST + "/api/index.php?act=buyer_account&op=points" + FLAG;
    public static final String API_GET_TRACES = HOST + "/api/index.php?act=shoper_commission&op=gettrace" + FLAG;
    public static final String API_SHARE_RECORD = HOST + "/api/index.php?act=shoper_commission&op=share" + FLAG;
    public static final String API_TALENT_SHARE_RECORD = HOST + "/talent/api.php?act=share&op=share" + FLAG;
    public static final String API_DEPOSITDTL = HOST + "/api/index.php?act=buyer_account&op=withdraw_list" + FLAG;
    public static final String API_GETBANKCARD = HOST + "/api/index.php?act=buyer_account&op=bankcard_list" + FLAG;
    public static final String API_DELBANKCARD = HOST + "/api/index.php?act=buyer_account&op=del_bankcard" + FLAG;
    public static final String API_SETBANKCARD = HOST + "/api/index.php?act=buyer_account&op=set_bankcard" + FLAG;

    public static final String API_GET_TEAM = HOST + "/api/index.php?act=shoper_commission&op=getteam" + FLAG;//我的团队
    public static final String API_GET_COMMISSION = HOST + "/api/index.php?act=shoper_commission&op=getmoney" + FLAG;//我的奖金

    public static final String API_SETPAYPWD = HOST + "/api/index.php?act=buyer_account&op=set_paypwd" + FLAG;
    public static final String API_WITH_DRAW_CASH = HOST + "/api/index.php?act=buyer_account&op=withdraw_cash" + FLAG;
    public static final String API_GET_DEDUCTMONEY = HOST + "/api/index.php?act=shoper_commission&op=getdeduct" + FLAG;
    public static final String API_GET_USER_ACCOUNT = HOST + "/api/index.php?act=buyer_account&op=get_user_account" + FLAG;

    /**
     * 商品相关API
     */
    public static final String API_GET_GOODS_CLS = HOST + "/api/index.php?act=common_goods&op=get_goods_class" + FLAG;
    public static final String API_GET_GOODS = HOST + "/api/index.php?act=common_goods&op=get_goods_list" + FLAG;
    public static final String API_GET_GOODS_DTL = HOST + "/api/index.php?act=common_goods&op=get_goods_detail" + FLAG;
    public static final String API_GET_GOODS_COMMENTS = HOST + "/api/index.php?act=common_goods&op=get_goods_comments" + FLAG;
    public static final String API_ADD_FAVORITES = HOST + "/api/index.php?act=buyer_favorite&op=add_favorites" + FLAG;
    public static final String API_DELETE_FAVORITES = HOST + "/api/index.php?act=buyer_favorite&op=delete_favorites" + FLAG;
    public static final String API_GET_FAVORITES = HOST + "/api/index.php?act=buyer_favorite&op=get_favorites_list" + FLAG;
    public static final String API_GET_BROWSELIST = HOST + "/api/index.php?act=buyer_browse&op=get_browse_list" + FLAG;
    public static final String API_ADD_BROWSE = HOST + "/api/index.php?act=buyer_browse&op=add_browse" + FLAG;
    public static final String API_DELETE_BROWSE = HOST + "/api/index.php?act=buyer_browse&op=delete_browse" + FLAG;
    public static final String API_GET_HOT_SEARCH = HOST + "/api/index.php?act=common_goods&op=get_hot_search" + FLAG;
    public static final String API_GET_SEARCH_HISTORY = HOST + "/api/index.php?act=common_goods&op=get_search_history_list" + FLAG;
    public static final String API_ADD_SEARCH_HISTORY = HOST + "/api/index.php?act=common_goods&op=add_search_history" + FLAG;
    public static final String API_DELETE_SEARCH_HISTORY = HOST + "/api/index.php?act=common_goods&op=delete_search_history" + FLAG;
    public static final String API_GET_AREA_FREIGHT = HOST + "/api/index.php?act=common_goods&op=get_area_freight" + FLAG;

    public static final String API_GET_ARTICLE_LIST = HOST + "/api/index.php?act=common_article&op=get_article_list" + FLAG;
    public static final String API_GET_GOODS_SCHEME = HOST + "/api/index.php?act=buyer_shopping&op=get_goods_scheme" + FLAG;

    public static final String API_GET_GRP_BUY_CLASS = HOST + "/api/index.php?act=buyer_groupbuy&op=get_groupbuy_class" + FLAG;
    public static final String API_GET_GRP_BUY_BANNER = HOST + "/api/index.php?act=buyer_groupbuy&op=get_groupbuy_banner" + FLAG;
    public static final String API_GET_GRP_BUY_GOODS_LIST = HOST + "/api/index.php?act=buyer_groupbuy&op=get_groupbuy_goods_list" + FLAG;

    public static final String API_GET_ACTIVITY_LIST = HOST + "/api/index.php?act=buyer_activity&op=get_activity_list" + FLAG;
    public static final String API_GET_ACTIVITY_DTL = HOST + "/api/index.php?act=buyer_activity&op=get_activity_detail" + FLAG;
    public static final String API_GET_ACTIVITY_GOODS = HOST + "/api/index.php?act=buyer_activity&op=get_activity_goods" + FLAG;

    public static final String API_GET_ADV_LIST = HOST + "/api/index.php?act=common_index&op=get_adv_list" + FLAG;
    public static final String API_GET_HOT_BRANDS = HOST + "/api/index.php?act=buyer_shopping&op=get_brandhouse_brands" + FLAG;
    public static final String API_GET_HOT_BRANDS_GOODS = HOST + "/api/index.php?act=buyer_shopping&op=get_brandhouse_goods" + FLAG;

    public static final String API_GET_SHOP_CLASS = HOST + "/api/index.php?act=buyer_discovery&op=get_shop_class" + FLAG;
    public static final String API_GET_MSHOP_LIST = HOST + "/api/index.php?act=buyer_discovery&op=get_shop_list" + FLAG;
    public static final String API_GET_MSHOP_GOODS = HOST + "/api/index.php?act=buyer_discovery&op=get_shop_goods" + FLAG;
    public static final String API_GET_MSHOP_INFO = HOST + "/api/index.php?act=buyer_discovery&op=get_shop_info" + FLAG;
    public static final String API_GET_MICROSHOP_LIST = HOST + "/api/index.php?act=shoper_manage&op=get_shop_list" + FLAG;

    public static final String API_GET_STORE_HOME_INFO = HOST + "/api/index.php?act=buyer_shopping&op=get_store_home_info" + FLAG;
    public static final String API_GET_STORE_HOME_CLS = HOST + "/api/index.php?act=buyer_shopping&op=get_store_class" + FLAG;
    public static final String API_GET_STORE_HOME_NUM = HOST + "/api/index.php?act=buyer_shopping&op=get_store_number" + FLAG;
    public static final String API_GET_STORE_NEW_GOODS = HOST + "/api/index.php?act=buyer_shopping&op=get_store_new_goods" + FLAG;

    public static final String API_GET_GOODS_PROM_BUNDLING = HOST + "/api/index.php?act=common_goods&op=get_bundling_combo" + FLAG;//促销 优惠套装与推荐组合

    /**
     * 购物车相关
     */
    public static final String API_GET_CART_LIST = HOST + "/api/index.php?act=buyer_cart&op=list" + FLAG;
    public static final String API_DEL_CART = HOST + "/api/index.php?act=buyer_cart&op=del" + FLAG;
    public static final String API_ADD_CART = HOST + "/api/index.php?act=buyer_cart&op=add" + FLAG;
    public static final String API_UPDATE_CART = HOST + "/api/index.php?act=buyer_cart&op=update" + FLAG;
    public static final String API_CHECK_CART = HOST + "/api/index.php?act=buyer_cart&op=check_cart" + FLAG;

    /**
     * 订单相关
     */
    public static final String API_SUBMIT = HOST + "/api/index.php?act=buyer_order&op=submit" + FLAG;
    public static final String API_GET_ORDER_LIST = HOST + "/api/index.php?act=buyer_order&op=get_order_list" + FLAG;
    public static final String API_CANCEL_ORDER = HOST + "/api/index.php?act=buyer_order&op=cancle_order" + FLAG;
    public static final String API_DELETE_ORDER = HOST + "/api/index.php?act=buyer_order&op=delete_order" + FLAG;
    public static final String API_ORDER_DTL = HOST + "/api/index.php?act=buyer_order&op=get_order_detail" + FLAG;

    public static final String API_RECEIVE_ORDER = HOST + "/api/index.php?act=buyer_order&op=receive_order" + FLAG;
    public static final String API_GET_APP_MSGLST = HOST + "/api/index.php?act=buyer_message&op=get_app_message_list" + FLAG;
    public static final String API_GET_ORDER_EVALUATING_GOODS = HOST + "/api/index.php?act=buyer_order&op=get_order_evaluate_goods" + FLAG;//获取订单评价商品(新版)
    public static final String API_EVALUATE_GOODS_BATCH = HOST + "/api/index.php?act=buyer_order&op=batch_evaluate" + FLAG;

    /**
     * 退货相关
     */
    public static final String API_RETURN_GOODS_INIT = HOST + "/api/index.php?act=buyer_order&op=get_refund_info" + FLAG;//申请退货 初始化页面
    public static final String API_RETURN_GOODS_SUBMIT = HOST + "/api/index.php?act=buyer_order&op=add_refund" + FLAG;//申请退货 提交 —— 单商品
    public static final String API_RETURN_GOODS_SUBMIT_ALL = HOST + "/api/index.php?act=buyer_order&op=add_refund_all" + FLAG;//申请退货 提交 —— 全部商品
    public static final String API_RETURN_GOODS_GET_STATE = HOST + "/api/index.php?act=buyer_order&op=get_refund_state" + FLAG;//申请退货 获取退款退货进度详情
    public static final String API_RETURN_GOODS_GET_REFUND_DETAIL = HOST + "/api/index.php?act=buyer_order&op=get_refund_detail" + FLAG;//申请退货 获取退款退货详情
    public static final String API_RETURN_GOODS_GET_REFUND_ADDR = HOST + "/api/index.php?act=buyer_order&op=get_refund_address" + FLAG;//申请退货 获取退款退货地址
    public static final String API_RETURN_GOODS_REFUND_SHIP = HOST + "/api/index.php?act=buyer_order&op=refund_ship" + FLAG;//申请退货 提交发货

    public static final String API_GET_REFUND_LIST = HOST + "/api/index.php?act=buyer_order&op=get_refund_list" + FLAG;//申请退款/退货列表
    public static final String API_GET_REFUND_PROGRESS = HOST + "/api/index.php?act=buyer_order&op=get_refund_progress" + FLAG;//获取退款退货进度
    public static final String API_CHECK_PAYPWD = HOST + "/api/index.php?act=buyer_account&op=check_paypwd" + FLAG;//获取退款退货进度

    /**
     * 获取快递列表
     */
    public static final String API_GET_EXPRESS = HOST + "/api/index.php?act=common_index&op=get_express" + FLAG;//申请退款/退货列表
    public static final String API_GET_COUPON_LIST = HOST + "/api/index.php?act=buyer_coupon&op=coupon_list" + FLAG;//我的优惠券列表
    public static final String API_PAID_COUPON_BATCH = HOST + "/api/index.php?act=buyer_coupon&op=paid_coupon_batch" + FLAG;//支付成功之后的红包

    /**
     * 意见反馈*
     */
    public static final String API_FEED_BACK = HOST + "/api/index.php?act=member_feedback&op=add_buyer_feedback" + FLAG;
    public static final String API_CRASH_REPORT = "http://123.56.126.220/fh_app/App/index.php?m=API&c=Fhbug&a=add";

    /**
     * H5页面相关
     */
    public static final String GOODSDTL = HOST + "/wap/tmpl/andriod/product_info.html?goods_commonid=%1$s" + FLAG;

    public static final String ABOUTUS = HOST + "/wap/tmpl/andriod/about_us.html?flag=android";
    public static final String MICROSHOP_GUIDE = HOST + "/wap/tmpl/weidian/small_store.html?flag=android";
    public static final String INVITE_FRIEND = HOST + "/wap/tmpl/member/friend.html?flag=android";
    public static final String GOODS_COMMISSION = HOST + "/wap/tmpl/andriod/commission.html?goods_price=%1$s&goods_commission=%2$s" + FLAG;

    /**
     * 供应商入驻相关
     */
    public static final String MERCHANTPOLICY = HOST + "/shop/index.php?act=index_news_wap&id=178&f=android" + FLAG;
    public static final String API_STORE_JOIN = HOST + "/api/index.php?act=common_store_joinin" + FLAG;
    public static final String API_COMPANY_INFO = HOST + "/api/index.php?act=common_store_joinin&op=company_info" + FLAG;
    public static final String API_COMPANY_INFO_SAVE = HOST + "/api/index.php?act=common_store_joinin&op=company_info_save" + FLAG;
    public static final String API_COMPANY_INFO_SUBMIT = HOST + "/api/index.php?act=common_store_joinin&op=company_info_submit" + FLAG;
    public static final String API_STORE_INFO = HOST + "/api/index.php?act=common_store_joinin&op=store_info" + FLAG;
    public static final String API_STORE_INFO_SAVE = HOST + "/api/index.php?act=common_store_joinin&op=store_info_save" + FLAG;
    public static final String API_STORE_CLS_DELETE = HOST + "/api/index.php?act=common_store_joinin&op=store_class_del" + FLAG;
    public static final String API_STORE_INFO_SUBMIT = HOST + "/api/index.php?act=common_store_joinin&op=store_info_submit" + FLAG;
    public static final String API_STORE_PAY_OFFLINE = HOST + "/api/index.php?act=common_store_joinin&op=pay_offline" + FLAG;

    /**
     * 数据库相关
     */
    public static final int DBVER = 23;
    public static final int REQUESTNUM = 20;
    public static final int REQUESTNUM_10 = 10;
    public static final String DBNAME = "FHDB";

    public static final String LOCALAPPNAMEFMT = "%1$s/FHGlobal_%2$s.apk";

    /**
     * 分享相关Url
     */
    public static final String SHARE_ACTIVITY_DTL = HOST + "/wap/tmpl/activity/theme_details.html?title=%1$s&activity_id=%2$s&" + FLAG;
    public static final String SHARE_ACTIVITY_NAT_CLASS = HOST + "/wap/tmpl/activity/pav_goods_list.html?class_id=%1$s&activity_title=%2$s&activity_id=%3$s" + FLAG;//国家馆
    public static final String SHARE_ACTIVITY_NAT = HOST + "/wap/tmpl/activity/pavilion.html?activity_id=%1$s&" + FLAG;//国家馆分类
    public static final String SHARE_GOODS_DTL = HOST + "/wap/tmpl/shopping/goods_details.html?goods_id=%1$s" + FLAG;
    public static final String SHARE_HOTBRAND_DTL = HOST + "/wap/tmpl/activity/brand_details.html?brand_id=%1$s&brand_banner=%2$s&brand_name=%3$s&share_desc=%4$s&share_img=%5$s&share_title=%6$s" + FLAG;
    public static final String SHARE_FANCYSHOP = HOST + "/wap/tmpl/activity/discover_details.html?shop_id=%1$s" + FLAG;
    public static final String API_SWITCH = HOST + "/api/index.php?act=common_index&op=function_switch" + FLAG;
    public static final String APP_SHARE_HOME_LOGO = HOST + "/wap/images/index_share.jpg";//首页分享图片
    public static final String APP_SHARE_HOME_URL = HOST + "/wap/tmpl/index/index.html?a=1";//首页购分享链接

    public static final String API_GPS = HOST + "/api/index.php?act=common_index&op=record_gps" + FLAG;
    public static final String API_DEFAULT_SEARCH_HINT = HOST + "/api/index.php?act=common_goods&op=get_default_search" + FLAG;//首页头部搜索提示文字

    public static final String WAP_EVALUATE_POINT_RULES = HOST + "/api/index.php?act=common_article&op=get_evaluate_explain" + FLAG;// 评价送积分规则

    public static final String WXO0MCH = "1242874002";
    public static final String WXO0API = "ee6da44e8118145fb01040bcf2ef2213";

    public static final String API_GET_TN = HOST + "/api/index.php?act=buyer_order&op=get_tn" + FLAG;

    public static final String PERSONAL_AGREEMENT = HOST + "/wap/tmpl/andriod/personal_agreement.html";//个人申报协议
    public static final String MERCHANT_POLICY = HOST + "/wap/tmpl/andriod/faq.html";//商家服务政策


    public static final String API_GET_SEQUENCE_LIST = HOST + "/api/index.php?act=buyer_seckilling&op=get_sequence_list" + FLAG;
    public static final String API_GET_SEQUENCE_GOODS = HOST + "/api/index.php?act=buyer_seckilling&op=get_sequence_goods" + FLAG;
    public static final String API_GET_SEQUENCE_DATA = HOST + "/api/index.php?act=buyer_seckilling&op=get_seckilling_data" + FLAG;

    public static final String API_GET_INDEX_DATA = HOST + "/api/index.php?act=common_index&op=get_index_data" + FLAG;
    public static final String API_GET_CHANNEL_DATA = HOST + "/api/index.php?act=buyer_shopping&op=get_channel_data" + FLAG;

    /**
     * 商品售罄 提醒相关
     */
    public static final String API_ADD_ARRIVAL_NOTICE = HOST + "/api/index.php?act=common_goods&op=add_arrival_notice" + FLAG;
    public static final String API_CANCEL_ARRIVAL_NOTICE = HOST + "/api/index.php?act=common_goods&op=cancel_arrival_notice" + FLAG;
    public static final String API_GET_ARRIVAL_NOTICE_GOODS = HOST + "/api/index.php?act=common_goods&op=get_arrival_notice" + FLAG;

    public static final String API_IF_ACCOUNT_EXISTS = HOST + "/api/index.php?act=common_member&op=if_account_exists" + FLAG;

    public static final String API_USER_AGREEMENT = HOST + "/wap/tmpl/andriod/agreement.html?a=1" + FLAG;

    public static final String API_SMS_CODE_INIT = HOST + "/api/index.php?act=common_member&op=sms_code_init&mobile=%1$s" + FLAG;
    public static final String URL_GET_CAPTCHA = HOST + "/api/index.php?act=common_index&op=get_captcha&mobile=%1$s" + FLAG;

    public static final String URL_CHECK_CAPTCHA = HOST + "/api/index.php?act=common_index&op=check_captcha" + FLAG;

    public static final String URL_GET_TALENT_HOME = HOST + "/talent/api.php?act=talent&op=get_talent_home" + FLAG;
    public static final String API_GET_SHARED_GOODS = HOST + "/api/index.php?act=shoper_base&op=get_shared_goods" + FLAG;
    public static final String API_GET_SHOP_INFO = HOST + "/api/index.php?act=shoper_base&op=get_shop_info" + FLAG;
    public static final String API_SET_SHOP_INFO = HOST + "/api/index.php?act=shoper_base&op=set_shop_info" + FLAG;

    public static final String API_GET_TIME_DETAIL = HOST + "/talent/api.php?act=time&op=get_time_detail" + FLAG;
    public static final String API_FOLLOW_TALENT = HOST + "/talent/api.php?act=talent&op=follow_talent" + FLAG;
    public static final String API_UP_COMMENT = HOST + "/talent/api.php?act=time_comment&op=up_comment" + FLAG;
    public static final String API_CANCEL_UP = HOST + "/talent/api.php?act=time_comment&op=cancel_up" + FLAG;
    public static final String API_ADD_COMMENT = HOST + "/talent/api.php?act=time_comment&op=add_comment" + FLAG;

    public static final String API_GET_TAGS = HOST + "/talent/api.php?act=time_tags&op=get_tags" + FLAG;
    public static final String API_DELETE_RECENT_TAGS = HOST + "/talent/api.php?act=time_tags&op=delete_history" + FLAG;

    public static final String API_SEARCH_TAGS = HOST + "/talent/api.php?act=time_tags&op=search_tags" + FLAG;
    public static final String API_ADD_TAG = HOST + "/talent/api.php?act=time_tags&op=add_tag" + FLAG;

    public static final String API_GET_LINK_GOODS = HOST + "/talent/api.php?act=time_goods&op=get_goods_list" + FLAG;
    public static final String API_ADD_TIME = HOST + "/talent/api.php?act=time&op=add_time" + FLAG;
    public static final String API_DELETE_TIME = HOST + "/talent/api.php?act=time&op=delete_time" + FLAG;

    public static final String URL_JUBAO = HOST + "/wap/tmpl/specialty/report.html?report_name=%1$s&type=%2$s&report_id=%3$s" + FLAG;
    public static final String API_GET_FANS_LIST = HOST + "/talent/api.php?act=talent&op=get_fans_list" + FLAG;

    public static final String API_GET_RECOMMEND_GOODS = HOST + "/talent/api.php?act=talent&op=get_recommend_goods" + FLAG;
    public static final String API_GET_TALENT_TIME_LIST = HOST + "/talent/api.php?act=time&op=get_talent_time_list" + FLAG;

    public static final String API_GET_FOUND_HOME = HOST + "/talent/api.php?act=time&op=get_found_home" + FLAG;
    public static final String API_CHANGE_TALENT_INFO = HOST + "/talent/api.php?act=talent&op=change_talent_info" + FLAG;
    public static final String API_GET_SHOP_SETTINGS = HOST + "/api/index.php?act=buyer_discovery&op=get_shop_settings" + FLAG;

    public static final String API_GET_BRANDMESSGE = HOST + "/api/index.php?act=store_brand&op=get_brand_message" + FLAG;
    public static final String URL_GET_GROUPSHAREURL = HOST + "/wap/tmpl/activity/brand.html?brand_id=%1$s" + FLAG;

    public static final String API_GET_BRAND_GOODS = HOST + "/api/index.php?act=store_brand&op=get_brand_goods" + FLAG;
    public static final String API_GET_STORE_FLOW = HOST + "/api/index.php?act=buyer_shopping&op=get_store_home_append" + FLAG;

    public static final String URL_COUPONS_GOODS = HOST + "/wap/tmpl/packet/baggoods.html?store_id=%1$s&coupon_type=%2$s" + FLAG;
    public static final String URL_FRIEND_BAG = HOST + "/wap/tmpl/packet/friend_bag.html?batch_id=2" + FLAG;

    public static final String API_JDPAY = HOST + "/api/index.php?act=buyer_order&op=pay" + FLAG;
    public static final String URL_RENXUAN = HOST + "/wap/tmpl/shopping/good_aggregate.html?goods_id=%1$s&store_id=%2$s" + FLAG;

    public static final String API_GET_THEME2_LIST = "http://42.96.171.11/fh_app/php/theme2.php?random=1" + FLAG;
    public static final String API_GET_POPUP_INFO = HOST + "/api/index.php?act=common_index&op=get_popup_info" + FLAG;
    public static final String API_APPLY_SHOPER = HOST + "/api/index.php?act=common_index&op=apply_shoper" + FLAG;
    public static final String URL_HOW_TO_SHARE = HOST + "/wap/tmpl/weidian/generalize.html?a=1" + FLAG;
    public static final String URL_TO_BE_SHARER = HOST + "/wap/tmpl/weidian/spokeman.html?a=1" + FLAG;

    public static final String URL_TO_GET_VDIAN = HOST + "/wap/tmpl/mircoshop/register.html?a=1" + FLAG;
    public static final String URL_EXPLORER_VDIAN = HOST + "/wap/tmpl/mircoshop/home.html?shop_type=%1$s" + FLAG;

    public static final String URL_QUALITY = HOST + "/wap/tmpl/andriod/quality.html?a=1" + FLAG;
    public static final String API_GETALIPAYINFO = HOST + "/api/index.php?act=buyer_order&op=getAliPayInfo" + FLAG;

    public static final String CALL_URL = HOST + "/wap/tmpl/weidian/sharepage.html?deductid=%1$s" + FLAG;

    //极速免税店 Dutyfree
    public static final String API_DUTYFREE_CATEGORYONE = HOST + "/api/index.php?act=wholesaler&op=goodstype" + FLAG;
    public static final String URL_DUTYFREE_TRUST = HOST + "/wap/tmpl/andriod/trust.html?a=1" + FLAG;
    public static final String API_DUTYFREE_HOMECATEGORY = HOST + "/api/index.php?act=wholesaler&op=goodstype1" + FLAG;
    public static final String API_DUTYFREE_HOMEBRAND = HOST + "/api/index.php?act=wholesaler&op=getbrand" + FLAG;
    public static final String API_DUTYFREE_GOODSDTL = HOST + "/api/index.php?act=wholesaler&op=gooddetails" + FLAG;
    public static final String API_DUTYFREE_ADDCART = HOST + "/api/index.php?act=wholesaler_cart&op=add_cart" + FLAG;
    public static final String API_DUTYFREE_DELCART = HOST + "/api/index.php?act=wholesaler_cart&op=del_cart" + FLAG;
    public static final String API_DUTYFREE_EDITCART = HOST + "/api/index.php?act=wholesaler_cart&op=edit_cart" + FLAG;
    public static final String API_DUTYFREE_CARTLIST = HOST + "/api/index.php?act=wholesaler&op=shopcart" + FLAG;
    public static final String API_DUTYFREE_ORDERCONFIRM = HOST + "/api/index.php?act=wholesaler&op=confirmed_order" + FLAG;
    public static final String API_DUTYFREE_ADDADDR = HOST + "/api/index.php?act=wholesaler_address&op=add_address" + FLAG;
    public static final String API_DUTYFREE_DELADDR = HOST + "/api/index.php?act=wholesaler_address&op=del_address" + FLAG;
    public static final String API_DUTYFREE_EDITADDR = HOST + "/api/index.php?act=wholesaler_address&op=edit_address" + FLAG;
    public static final String API_DUTYFREE_ADDRLIST = HOST + "/api/index.php?act=wholesaler_address&op=get_addressAll" + FLAG;
    public static final String API_DUTYFREE_GENORDER = HOST + "/api/index.php?act=wholesaler_order&op=genOrder" + FLAG;
    public static final String API_DUTYFREE_SELECTCART = HOST + "/api/index.php?act=wholesaler_cart&op=check_all" + FLAG;
    public static final String API_DUTYFREE_ORDERLIST = HOST + "/api/index.php?act=wholesaler&op=orderlist" + FLAG;
    public static final String API_DUTYFREE_ORDERDTL = HOST + "/api/index.php?act=wholesaler&op=order_detail" + FLAG;
    public static final String API_DUTYFREE_DELORDER = HOST + "/api/index.php?act=wholesaler&op=deleteorder" + FLAG;
    public static final String API_DUTYFREE_CANCELORDER = HOST + "/api/index.php?act=wholesaler&op=cancle_order" + FLAG;
    public static final String API_DUTYFREE_ALLRECEIVE = HOST + "/api/index.php?act=wholesaler&op=one_receive_order" + FLAG;
    public static final String API_DUTYFREE_RECEIVEONE = HOST + "/api/index.php?act=wholesaler&op=receive_order" + FLAG;
    public static final String API_DUTYFREE_GOODSLIST = HOST + "/api/index.php?act=wholesaler&op=goodsseek" + FLAG;
    public static final String API_DUTYFREE_BRANDLIST = HOST + "/api/index.php?act=wholesaler&op=brandseek" + FLAG;
    public static final String API_DUTYFREE_CATEGORY_DETAIL = HOST + "/api/index.php?act=wholesaler&op=second_class" + FLAG;
    public static final String API_DUTYFREE_HOMEPAGE = HOST + "/api/index.php?act=wholesaler&op=homepage" + FLAG;
    public static final String API_DUTYFREE_BRANDMESSAGEDETAIL = HOST + "/api/index.php?act=brand_product&op=get_product" + FLAG;
    public static final String API_DUTYFREE_COUNT = HOST + "/api/index.php?act=wholesaler_cart&op=count_number" + FLAG;
    public static final String API_DUTYFREE_UCENTER = HOST + "/api/index.php?act=whosaler_package&op=user_center" + FLAG;
    public static final String API_DUTYFREE_CONSUME_DETAIL = HOST + "/api/index.php?act=whosaler_package&op=consume_detail" + FLAG;
    public static final String API_DUTYFREE_CONSUME_LIST = HOST + "/api/index.php?act=whosaler_package&op=package_balancelist" + FLAG;
    public static final String API_DUTYFREE_TOPUP = HOST + "/api/index.php?act=whosaller_vip_order&op=submit" + FLAG;
    public static final String API_DUTYFREE_MESSAGE_LIST = HOST + "/api/index.php?act=wholesaler_article&op=article_list" + FLAG;
    public static final String API_DUTYFREE_MYINVITE_LIST = HOST + "/api/index.php?act=whosaler_package&op=my_reward" + FLAG;
}
