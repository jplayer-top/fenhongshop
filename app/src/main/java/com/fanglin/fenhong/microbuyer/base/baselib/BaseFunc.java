package com.fanglin.fenhong.microbuyer.base.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.MainActivity;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.FHwifiHintPopWindow;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.listener.TalentImageClickListener;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.base.model.CartAction;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckEntity;
import com.fanglin.fenhong.microbuyer.base.model.EntityOfAddTalentImage;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.NativeUrlEntity;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageTag;
import com.fanglin.fenhong.microbuyer.base.model.TalentTagImage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.buyer.CategoryActivity;
import com.fanglin.fenhong.microbuyer.buyer.FavoritesActivity;
import com.fanglin.fenhong.microbuyer.buyer.GoodsDetailsActivity;
import com.fanglin.fenhong.microbuyer.buyer.GoodsListActivity;
import com.fanglin.fenhong.microbuyer.buyer.GoodsPromBundleListActivity;
import com.fanglin.fenhong.microbuyer.buyer.LoginActivity;
import com.fanglin.fenhong.microbuyer.buyer.RetrievePwdActivity;
import com.fanglin.fenhong.microbuyer.common.AOGRemindListActivity;
import com.fanglin.fenhong.microbuyer.common.ActListDtlActivity;
import com.fanglin.fenhong.microbuyer.common.CartCheckActivity;
import com.fanglin.fenhong.microbuyer.common.Express100Activity;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.FHExpressActivity;
import com.fanglin.fenhong.microbuyer.common.GroupActivity;
import com.fanglin.fenhong.microbuyer.common.HotBrandsActivity;
import com.fanglin.fenhong.microbuyer.common.MsgCenterActivity;
import com.fanglin.fenhong.microbuyer.common.NationalPavilionActivity;
import com.fanglin.fenhong.microbuyer.common.OrderActivity;
import com.fanglin.fenhong.microbuyer.common.OrderDtlActivity;
import com.fanglin.fenhong.microbuyer.common.PaySuccessActivity;
import com.fanglin.fenhong.microbuyer.common.PointsActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsListActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsProcessActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fenhong.microbuyer.common.StoreGoodsClassActivity;
import com.fanglin.fenhong.microbuyer.common.StoreGoodsListActivity;
import com.fanglin.fenhong.microbuyer.common.StoreNewGoodsActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DFGoodsDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyCategoryDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyFreeMainActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyNewAddrsActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeBrandDtlActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeCartActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeCartCheckActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreePersonalActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreePersonalInviteActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeSearchActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderListActivity;
import com.fanglin.fenhong.microbuyer.merchant.joinstep.JoinStepActivity;
import com.fanglin.fenhong.microbuyer.microshop.BalanceActivity;
import com.fanglin.fenhong.microbuyer.microshop.BonusActivity;
import com.fanglin.fenhong.microbuyer.microshop.CommissionActivity;
import com.fanglin.fenhong.microbuyer.microshop.DepositeActivity;
import com.fanglin.fenhong.microbuyer.microshop.EditImageTagActivity;
import com.fanglin.fenhong.microbuyer.microshop.FancyShopActivity;
import com.fanglin.fenhong.microbuyer.microshop.FansListActivity;
import com.fanglin.fenhong.microbuyer.microshop.MicroshopListActivity;
import com.fanglin.fenhong.microbuyer.microshop.ShareListActivity;
import com.fanglin.fenhong.microbuyer.microshop.TalentImagesDetailActivity;
import com.fanglin.fenhong.microbuyer.microshop.TeamActivity;
import com.fanglin.fenhong.microbuyer.wxapi.WXPayEntryActivity;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.photocropper.CropperActivity;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.custom.Custom3LinesLayout;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bingoogolapple.bgabanner.BGABanner;
import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * 作者：Created by Plucky on 2015/7/6.
 * 功能描述：基础方法类
 */
public class BaseFunc {

    /**
     * Activity 页面跳转
     * json为页面之间的传值
     */
    public static void gotoActivity(Context c, Class<?> cls, String json) {
        if (c == null || cls == null) return;
        Intent intent = new Intent(c, cls);
        if (json != null) {
            intent.putExtra("VAL", json);
        }
        c.startActivity(intent);
    }

    /**
     * 页面跳转
     *
     * @param c      上下文
     * @param cls    跳转至页面
     * @param bundle bundle 数据
     */
    public static void gotoActivityBundle(Context c, Class<?> cls, Bundle bundle) {
        if (c == null || cls == null) return;
        Intent intent = new Intent(c, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        c.startActivity(intent);
    }


    public static void gotoActivity4Result(Activity act, Class<?> cls, String json, int requestcode) {
        Intent intent = new Intent(act, cls);
        if (json != null) {
            intent.putExtra("VAL", json);
        }
        act.startActivityForResult(intent, requestcode);
    }

    public static void gotoActivity4Result(Fragment fragment, Class<?> cls, String json, int requestcode) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if (json != null) {
            intent.putExtra("VAL", json);
        }
        fragment.startActivityForResult(intent, requestcode);
    }

    public static void showMsg(Context context, String txt) {
        if (context == null) return;
        if (FHLib.isAppOnForeground(context)) {
            Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showSelfToast(Context context, String str) {
        if (context == null || str == null) {
            return;
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_simple_transparent_bg, null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(str);
        setFont(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 当网络不通时显示 无网络提示
     *
     * @param act    Activity
     * @param parent a parent view to get the {@link android.view.View#getWindowToken()} token from
     */
    public static FHwifiHintPopWindow showWifiUnconnectHint(Activity act, View parent, FHwifiHintPopWindow hintPopWindow) {
        if (parent != null) {
            if (hintPopWindow == null) {
                hintPopWindow = new FHwifiHintPopWindow(act);
            }
            hintPopWindow.showAtLocation(parent, Gravity.TOP, 0, (int) (act.getResources().getDimension(R.dimen.dp_of_46) + BaseFunc.getStatusBarHeight(act)));
            return hintPopWindow;
        }
        return null;
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static SpotsDialog getLoadingDlg(Context c, String txt) {
        return new SpotsDialog(c, txt);
    }


    /**
     * 设置字体
     */
    public static void setFont(ViewGroup group, Typeface font) {
        if (group == null) return;
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v != null) {
                if (v instanceof TextView) {
                    ((TextView) v).setTypeface(font);
                } else if (v instanceof ViewGroup) setFont((ViewGroup) v, font);
            }
        }
    }

    /**
     * 设置字体
     */
    public static void setFont(View v) {
        if (v == null) return;
        Typeface font = geticonFontType(v.getContext());
        if (v instanceof TextView) {
            ((TextView) v).setTypeface(font);
        }
    }

    public static void setFont(ViewGroup group) {
        if (group == null) return;
        Typeface font = geticonFontType(group.getContext());
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup) setFont((ViewGroup) v, font);
        }
    }

    public static Typeface geticonFontType(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(), "iconfont.ttf");
        } catch (Exception e) {
            return null;
        }
    }

    public static Typeface geticonFontTypeExtra(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(), "iconfont_extra.ttf");
        } catch (Exception e) {
            return null;
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context c) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getDisplayHeight(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        return dm.heightPixels;
    }

    public static int getStatusBarHeight(Activity act) {
        Rect frame = new Rect();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 搜索动作记录
     */
    public static void add_search(Context c, String keys, Member member) {

        FHCache.addSearch(c, keys);

        if (keys == null || member == null) return;
        new BaseBO().add_search_history(member.token, member.member_id, keys);
    }

    /**
     * 温馨提示 + 拨打电话
     */
    public static void Call(final Context mContext, final String phoneno) {
        if (phoneno == null) return;
        if (mContext == null) return;
        final FHHintDialog fhd = new FHHintDialog(mContext);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));
                mContext.startActivity(i);
            }
        });
        fhd.setTvContent(mContext.getString(R.string.confirm_dial) + phoneno);
        fhd.show();
    }

    public static void gotoLogin(Context c) {
        if (c == null) return;
        Activity act = (Activity) c;
        showMsg(c, c.getString(R.string.please_loginin_first));
        /** 清理用戶相關緩存*/
        FHCache.clearUserCache(c);
        FHApp fhApp = (FHApp) act.getApplication();
        fhApp.registerXinge();/*退出登录之后重新注册信鸽*/
        fhApp.setCartAction(null);//清空购物车数量
        gotoActivity(act, LoginActivity.class, null);
    }

    /**
     * 带回调的登录请求
     */
    public static void gotoLogin(Context c, int reqcode) {
        if (c == null) return;
        Activity act = (Activity) c;
        showMsg(c, c.getString(R.string.please_loginin_first));
        /** 清理用戶相關緩存*/
        FHCache.clearUserCache(c);
        ((FHApp) act.getApplication()).registerXinge();/*退出登录之后重新注册信鸽*/
        gotoActivity4Result(act, LoginActivity.class, null, reqcode);
    }

    public static String formatHtml(String html) {
        String res;
        try {
            res = Html.fromHtml(html).toString();
        } catch (Exception e) {
            res = html;
        }
        return res;
    }

    public static Spanned fromHtml(String html, Html.TagHandler handler) {
        Spanned res;
        try {
            res = Html.fromHtml(html, null, handler);
        } catch (Exception e) {
            res = null;
        }
        return res;
    }

    public static Spanned fromHtml(String html) {
        Spanned res;
        try {
            res = Html.fromHtml(html);
        } catch (Exception e) {
            res = null;
        }
        return res;
    }

    public static boolean isValidUrl(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isValidImgUrl(String imgurl) {
        if (isValidUrl(imgurl)) {
            imgurl = imgurl.toLowerCase();
            return imgurl.contains(".jpg") || imgurl.contains(".jpeg") || imgurl.contains(".gif") || imgurl.contains(".png") || imgurl.contains(".tif");
        } else {
            return false;
        }
    }


    /**
     * 时间戳转换为时分秒
     */
    public static String getCNTimeByTimeStamp(long offset) {
        if (offset > 0) {
            long[] t = getD_H_M_S(offset);
            StringBuilder sb = new StringBuilder();
            if (t[0] > 0) {
                sb.append(t[0]);
                sb.append("天");
            }
            sb.append(t[1]);
            sb.append("时");
            sb.append(t[2]);
            sb.append("分");
            sb.append(t[3]);
            sb.append("秒");
            return sb.toString();
        } else {
            return "0秒";
        }
    }

    public static long[] getD_H_M_S(long offset) {
        long[] res = new long[]{0, 0, 0, 0};
        long day = offset / 86400;
        double day1 = Math.floor(day);
        long hour = (long) Math.floor((offset - day1 * 86400) / 3600);
        long minute = (long) Math.floor((offset - day1 * 86400 - hour * 3600) / 60);
        long second = (long) Math.floor(offset - day1 * 24 * 60 * 60 - hour * 3600 - minute * 60);
        res[0] = day;
        res[1] = hour;
        res[2] = minute;
        res[3] = second;
        return res;
    }

    /**
     * 将List转换为以逗号隔开的字符串
     */
    public static String list2QuoteStr(List<String> list) {
        if (list == null || list.size() == 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (sb.length() == 0) {
                sb.append(list.get(i));
            } else {
                sb.append(",");
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    /**
     * 将以逗号隔开的字符串转换为List
     */
    public static List<String> quoteStrToArrayList(String str) {
        if (str == null) return null;
        List<String> result = new ArrayList<>();
        String[] tmp = str.split(",");
        if (tmp.length > 0) {
            Collections.addAll(result, tmp);
        }
        return result;
    }

    public static void startQQChat(Context c, String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            showMsg(c, "请先安装QQ");
        }
    }

    public static void toggleSoftInput(Context c) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInput(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //强制隐藏输入法
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    public static String[] getAvaliableBanks() {
        return new String[]{"支付宝", "邮政银行", "交通银行", "农业银行", "中国人民银行", "兴业银行", "建设银行", "招商银行", "中国银行", "华夏银行", "广发银行", "民生银行", "工商银行"};
    }

    public static int getBandIndex(String bankName) {
        String res[] = getAvaliableBanks();
        if (res.length > 0) {
            int index = 0;
            for (int i = 0; i < res.length; i++) {
                if (TextUtils.equals(bankName, res[i])) {
                    index = i;
                }
            }
            return index;
        } else {
            return 0;
        }
    }

    public static String getBankIconByName(Context c, String bankname) {
        String res = c.getString(R.string.if_zfb);
        if (TextUtils.equals("支付宝", bankname)) {
            res = c.getString(R.string.if_zfb);
        }
        if (TextUtils.equals("邮政银行", bankname)) {
            res = c.getString(R.string.if_yzyh);
        }
        if (TextUtils.equals("交通银行", bankname)) {
            res = c.getString(R.string.if_jtyh);
        }
        if (TextUtils.equals("农业银行", bankname)) {
            res = c.getString(R.string.if_nyyh);
        }
        if (TextUtils.equals("中国人民银行", bankname)) {
            res = c.getString(R.string.if_rmyh);
        }
        if (TextUtils.equals("兴业银行", bankname)) {
            res = c.getString(R.string.if_xyyh);
        }
        if (TextUtils.equals("建设银行", bankname)) {
            res = c.getString(R.string.if_jsyh);
        }
        if (TextUtils.equals("招商银行", bankname)) {
            res = c.getString(R.string.if_zsyh);
        }
        if (TextUtils.equals("中国银行", bankname)) {
            res = c.getString(R.string.if_zgyh);
        }
        if (TextUtils.equals("华夏银行", bankname)) {
            res = c.getString(R.string.if_hxyh);
        }
        if (TextUtils.equals("广发银行", bankname)) {
            res = c.getString(R.string.if_gfyh);
        }
        if (TextUtils.equals("民生银行", bankname)) {
            res = c.getString(R.string.if_msyh);
        }
        if (TextUtils.equals("工商银行", bankname)) {
            res = c.getString(R.string.if_gsyh);
        }
        return res;
    }

    public static String getMaskMobile(String mobile) {
        if (FHLib.isMobileNO(mobile)) {
            String pre = mobile.substring(0, 3);
            String aff = mobile.substring(mobile.length() - 4);
            return pre + "****" + aff;
        } else {
            return mobile;
        }
    }

    /**
     * Url 编码
     */
    public static String UrlEncode(String url) {
        String res;
        try {
            res = URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            res = url;
        }
        return res;
    }


    /**
     * 防止頭像異常 iOS
     */
    public static Uri getMember_avatar(String member_avatar) {
        if (BaseFunc.isValidUrl(member_avatar)) {
            if (member_avatar.contains("!")) {
                return Uri.parse(member_avatar);
            } else {
                if (member_avatar.contains("http://img.fenhongshop.com")) {
                    return Uri.parse(member_avatar + "!medium");
                }

                if (member_avatar.contains("http://pic.fenhongshop.com")) {
                    return Uri.parse(member_avatar + "!pic600");
                }

                return Uri.parse(member_avatar);
            }
        }
        return null;
    }

    public static int[] StrArr2Int(String[] str) {
        int array[] = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            array[i] = Integer.parseInt(str[i]);
        }
        return array;
    }

    /**
     * 将浮点数d以四舍五入法截取num位小数后返回
     */
    public static String truncDouble(double d, int num) {
        String str = "#0";
        if (num > 0) {
            str += ".";
            for (int i = 0; i < num; i++) {
                str += String.valueOf(0);
            }
        }
        DecimalFormat df = new DecimalFormat(str);
        return df.format(d);
    }

    /**
     * 判断一个字符串是否是整数
     *
     * @param str str
     */
    public static boolean isInteger(String str) {
        if (TextUtils.isEmpty(str)) return false;//不要让程序抛错

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void getCartNum(Member member) {
        if (member == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                /**
                 * 接口数据结构在3.1.0发生改变 以前仅返回数量  Added  By Plucky
                 */
                CartAction cartAction;
                if (isSuccess) {
                    try {
                        cartAction = new Gson().fromJson(data, CartAction.class);
                    } catch (Exception e) {
                        cartAction = null;
                    }
                } else {
                    cartAction = null;
                }
                FHApp.getInstance().setCartAction(cartAction);
                EventBus.getDefault().post(new CartActionEvent(cartAction));
            }
        }).get_cart_num(member.member_id, member.token);
    }

    public static void setCartNumofTv(TextView tv, int cartNum) {
        if (tv == null) return;
        if (cartNum > 0) {
            tv.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomIn).duration(300).playOn(tv);
            if (cartNum >= 100) {
                tv.setText(BaseVar.OVER100);
            } else {
                tv.setText(String.valueOf(cartNum));
            }
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 计算数组的行数
     * Added By Plucky
     *
     * @param count   数组元素个数
     * @param rowspan 列数（每行占的个数）
     * @return 行数
     */
    public static int getRowCountOfList(int count, int rowspan) {
        if (count > 0) {
            if (count % rowspan == 0) {
                return count / rowspan;
            } else {
                return (count / rowspan) + 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取当前元素位于数组的索引
     * Added By Plucky
     *
     * @param row     行标
     * @param col     列标
     * @param rowspan 列数
     * @return index
     */
    public static int getIndexOfList(int row, int col, int rowspan) {
        return (row == 0 ? 0 : row * rowspan) + col;
    }

    public static void urlClick(Context c, String url) {
        NativeUrlEntity entity = isNative(url);
        if (entity != null) {
            urlRedirect(c, entity);
        } else {
            if (isValidUrl(url)) {
                if (url.contains("!login")) {
                    /** 检查登录 */
                    Member member = null;
                    if (c instanceof Activity) {
                        member = FHCache.getMember((Activity) c);
                    }
                    if (member != null) {
                        gotoActivity(c, FHBrowserActivity.class, url);
                    } else {
                        BaseFunc.gotoLogin(c);
                    }
                } else {
                    gotoActivity(c, FHBrowserActivity.class, url);
                }
            }
        }
    }

    public static void urlRedirect(Context c, NativeUrlEntity entity) {
        if (entity != null) {
            if (entity.type == NativeUrlEntity.TYPE_CALL) {
                Call(c, entity.val);
            } else {
                if (entity.activityClass != null) {
                    if (entity.bundle != null) {
                        gotoActivityBundle(c, entity.activityClass, entity.bundle);
                    } else if (entity.activityClass == DutyFreeMainActivity.class) {
                        int mType = FHCache.getMemberType(c);
                        if (mType > 0) {
                            //如果是买手
                            gotoActivity(c, DutyFreeMainActivity.class, null);
                        } else {
                            //否则进入URL
                            gotoActivity(c, FHBrowserActivity.class, entity.val);
                        }
                    } else if (entity.activityClass == WXPayEntryActivity.class) {
                        topUp((BaseFragmentActivity) c, FHBrowserActivity.REQ_PAY);
                    } else {
                        gotoActivity(c, entity.activityClass, entity.val);
                    }
                }
            }
        }
    }

    /**
     * 去商品详情页面
     *
     * @param context       context
     * @param goodsId       goodsId
     * @param resource_tags 统计用，可传可不传
     */
    public static void gotoGoodsDetail(Context context, String goodsId, String resource_tags, String talent_deductid) {
        if (context != null && goodsId != null) {
            Intent intent = new Intent(context, GoodsDetailsActivity.class);
            intent.putExtra("goodsId", goodsId);
            intent.putExtra("resourceTags", resource_tags);
            intent.putExtra("talentDeductid", talent_deductid);
            context.startActivity(intent);
        }
    }

    /**
     * 判断URL是否为原生扩展
     * Added By Plucky
     *
     * @param url originUrl
     * @return 若为原生映射 则返回Activity类
     */
    public static NativeUrlEntity isNative(String url) {
        if (BaseFunc.isValidUrl(url)) {
            if (url.contains("!goodsdetail")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String goods_id = uri.getQueryParameter("goods_id");
                String resource_tags = uri.getQueryParameter("resource_tags");
                String talentid = uri.getQueryParameter("talentid");
                if (!TextUtils.isEmpty(goods_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = GoodsDetailsActivity.class;

                    Bundle bundle = new Bundle();
                    bundle.putString("goodsId", goods_id);
                    bundle.putString("resourceTags", resource_tags);
                    bundle.putString("talentDeductid", talentid);

                    nativeUrlEntity.bundle = bundle;
                    return nativeUrlEntity;
                }
            }
            if (url.contains("!store")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String store_id = uri.getQueryParameter("store_id");
                if (!TextUtils.isEmpty(store_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = StoreActivity.class;
                    nativeUrlEntity.val = store_id;
                    return nativeUrlEntity;
                }
            }

            if (url.contains("!microshop")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String shop_id = uri.getQueryParameter("shop_id");
                if (!TextUtils.isEmpty(shop_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = FancyShopActivity.class;
                    nativeUrlEntity.val = shop_id;
                    return nativeUrlEntity;
                }
            }

            /** 普通订单详情*/
            if (url.contains("!order")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String order_id = uri.getQueryParameter("order_id");
                if (!TextUtils.isEmpty(order_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = OrderDtlActivity.class;
                    nativeUrlEntity.val = order_id;
                    return nativeUrlEntity;
                }
            }

            /** 退货订单*/
            if (url.contains("!refund")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String refund_id = uri.getQueryParameter("refund_id");
                if (!TextUtils.isEmpty(refund_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = ReturnGoodsProcessActivity.class;
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", refund_id);
                    nativeUrlEntity.bundle = bundle;
                    return nativeUrlEntity;
                }
            }

            /** 快递详细 */
            if (url.contains("!express")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String e_code = uri.getQueryParameter("e_code");
                String e_name = uri.getQueryParameter("e_name");
                String shipping_code = uri.getQueryParameter("shipping_code");
                if (!TextUtils.isEmpty(e_code) && !TextUtils.isEmpty(shipping_code)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("e_code", e_code);
                    bundle.putString("e_name", e_name);
                    bundle.putString("shipping_code", shipping_code);

                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = Express100Activity.class;
                    nativeUrlEntity.bundle = bundle;

                    return nativeUrlEntity;
                }
            }

            /** 分红快递详细 */
            if (url.contains("!fhexpress")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String order_id = uri.getQueryParameter("order_id");
                if (!TextUtils.isEmpty(order_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = FHExpressActivity.class;
                    nativeUrlEntity.val = order_id;
                    return nativeUrlEntity;
                }
            }

            /** 我的奖金*/
            if (url.contains("!commission")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = CommissionActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 我的团队*/
            if (url.contains("!team")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = TeamActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 我的微店列表*/
            if (url.contains("!microshops")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = MicroshopListActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 主题馆*/
            if (url.contains("!activity")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String activity_id = uri.getQueryParameter("activity_id");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = ActListDtlActivity.class;
                nativeUrlEntity.val = activity_id;
                return nativeUrlEntity;
            }

            /** 我的钱包*/
            if (url.contains("!balance")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = BalanceActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 优惠券*/
            if (url.contains("!coupons")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = BonusActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 供应商入驻*/
            if (url.contains("!merchantin")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = JoinStepActivity.class;
                return nativeUrlEntity;
            }

            /** 我的积分*/
            if (url.contains("!points")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = PointsActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 订单列表 */
            if (url.contains("!orders")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = OrderActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 我的推广 */
            if (url.contains("!shares")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = ShareListActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 我的收藏 */
            if (url.contains("!favs")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = FavoritesActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 消息中心 */
            if (url.contains("!msgs")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = MsgCenterActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 品牌馆列表 */
            if (url.contains("!brands")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = HotBrandsActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 国家馆 */
            if (url.contains("!nation")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = NationalPavilionActivity.class;
                Bundle bundle = new Bundle();
                bundle.putString("activity_id", val);
                nativeUrlEntity.bundle = bundle;
                return nativeUrlEntity;
            }

            /** 退款退货列表 */
            if (url.contains("!returns")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = ReturnGoodsListActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 去分类页面 */
            if (url.contains("!category")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = CategoryActivity.class;
                nativeUrlEntity.val = null;
                return nativeUrlEntity;
            }

            /** 去消息中心 -- 到货提醒页面 */
            if (url.contains("!arrival")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = AOGRemindListActivity.class;
                nativeUrlEntity.val = null;
                return nativeUrlEntity;
            }

            /** 提现页面 */
            if (url.contains("!withdraw")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = DepositeActivity.class;
                nativeUrlEntity.val = null;
                return nativeUrlEntity;
            }

            /** 时光详情评论 */
            if (url.contains("!time")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String timeid = uri.getQueryParameter("timeid");
                String commentid = uri.getQueryParameter("commentid");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = TalentImagesDetailActivity.class;

                Bundle bundle = new Bundle();
                bundle.putString("TIMEID", timeid);
                bundle.putString("COMMENTID", commentid);
                bundle.putBoolean("ISEVALUATE", false);
                nativeUrlEntity.bundle = bundle;

                return nativeUrlEntity;
            }

            /** 达人粉丝列表 */
            if (url.contains("!fans")) {
                /** 解决最后一个参数存在的异常*/
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String talentid = uri.getQueryParameter("talentid");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = FansListActivity.class;
                nativeUrlEntity.val = talentid;
                return nativeUrlEntity;
            }

            /** 品牌聚合页 */
            if (url.contains("!group")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String val = uri.getQueryParameter("val");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = GroupActivity.class;
                nativeUrlEntity.val = val;
                return nativeUrlEntity;
            }

            /** 商品列表页 */
            if (url.contains("!goodslist")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String key = uri.getQueryParameter("key");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = GoodsListActivity.class;

                Bundle bundle = new Bundle();
                bundle.putString("key", key);
                bundle.putString("gc_id", null);
                nativeUrlEntity.bundle = bundle;

                return nativeUrlEntity;
            }

            /** 极速免税商品详情页 */
            if (url.contains("!dutyproduct")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String product_id = uri.getQueryParameter("product_id");
                if (!TextUtils.isEmpty(product_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = DFGoodsDetailActivity.class;
                    nativeUrlEntity.val = product_id;
                    return nativeUrlEntity;
                }
            }

            /** 急速免税店首页 */
            if (url.contains("!dutyfree")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = DutyFreeMainActivity.class;
                nativeUrlEntity.val = url.replaceAll("!dutyfree", "");
                return nativeUrlEntity;
            }

            /** 极速免税品牌详情页*/
            if (url.contains("!dutybrand")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String brand_id = uri.getQueryParameter("brand_id");
                if (!TextUtils.isEmpty(brand_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = DutyfreeBrandDtlActivity.class;
                    nativeUrlEntity.val = brand_id;
                    return nativeUrlEntity;
                }
            }

            /** 极速免税二级分类详情页*/
            if (url.contains("!dutycategory")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);

                String category_id = uri.getQueryParameter("category_id");
                if (!TextUtils.isEmpty(category_id)) {
                    NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                    nativeUrlEntity.activityClass = DutyCategoryDetailActivity.class;
                    nativeUrlEntity.val = category_id;
                    return nativeUrlEntity;
                }
            }

            /** 极速免税搜索页面 */
            if (url.contains("!dutysearch")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = DutyfreeSearchActivity.class;
                return nativeUrlEntity;
            }

            /** 极速免税个人中心 */
            if (url.contains("!dutyucenter")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = DutyfreePersonalActivity.class;
                return nativeUrlEntity;
            }

            /** 极速免税 订单列表*/
            if (url.contains("!dutyorders")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);
                String index = uri.getQueryParameter("index");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = OrderListActivity.class;
                nativeUrlEntity.val = index;
                return nativeUrlEntity;
            }

            /** 极速免税 订单详情 */
            if (url.contains("!dutyorder")) {
                String[] arr = url.split("!");
                Uri uri = Uri.parse(arr[0]);
                String order_id = uri.getQueryParameter("order_id");

                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = OrderDetailActivity.class;
                nativeUrlEntity.val = order_id;
                return nativeUrlEntity;
            }

            /** 套装列表 */
            if (url.contains("!goodsbundle")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = GoodsPromBundleListActivity.class;
                return nativeUrlEntity;
            }

            /** 开通VIP买手 */
            if (url.contains("!dutytopup")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = WXPayEntryActivity.class;
                return nativeUrlEntity;
            }
            /**
             * 邀请奖励列表
             */
            if (url.contains("!dutyreward")) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.activityClass = DutyfreePersonalInviteActivity.class;
                return nativeUrlEntity;
            }

        }

        if (!TextUtils.isEmpty(url) && url.contains("tel:")) {
            String[] arr = url.split(":");
            if (arr.length > 1 && !TextUtils.isEmpty(arr[1])) {
                NativeUrlEntity nativeUrlEntity = new NativeUrlEntity();
                nativeUrlEntity.type = NativeUrlEntity.TYPE_CALL;
                nativeUrlEntity.activityClass = null;
                nativeUrlEntity.val = arr[1];
                return nativeUrlEntity;
            }
        }

        return null;
    }

    public static void gotoGoodsBundle(Context mContext, List<GoodsBundling> bundling, int goods_source) {
        Intent intent = new Intent(mContext, GoodsPromBundleListActivity.class);
        intent.putExtra("VAL", new Gson().toJson(bundling));
        intent.putExtra("goods_source", goods_source);
        mContext.startActivity(intent);
    }

    public static void recordMemberinfo(FHApp app, Activity act, String data) {
        try {
            Member m = new Gson().fromJson(data, Member.class);
            if (m != null && !TextUtils.isEmpty(m.member_id)) {
                /**
                 * 更新用户信息
                 */
                FHCache.setMember(act, m);
                app.member = m;
                app.fhdb.saveOrUpdate(m);

                /**
                 * 重新注册信鸽
                 */
                app.registerXinge();

                /**
                 * 解决多次打开登录界面
                 * 如果真正登录成功了 则恢复初始状态
                 */
                VarInstance.getInstance().setHasExecLogin(false);

                /**
                 * 更新购物车数量
                 */
                getCartNum(m);
            }
        } catch (Exception e) {
            FHLog.d("Plucky", "record2Sqlite error");
        }
    }

    public static String getCaptchaUrl(String mobile) {
        return String.format(BaseVar.URL_GET_CAPTCHA, mobile);
    }

    public static String getSecondStrOfMillis(long millisUntilFinished) {
        return millisUntilFinished / 1000 + "秒";
    }

    public static View getTalentBanner(Context mContext, List<TalentTagImage> images, TalentImageClickListener imgClick, final String talentId) {
        List<View> tagViews = BaseFunc.getTalentViews(mContext, images, imgClick, talentId);
        if (tagViews != null && tagViews.size() > 0) {
            View view = View.inflate(mContext, R.layout.item_talentbanner_hold, null);
            BGABanner banner = (BGABanner) view.findViewById(R.id.banner);
            banner.setViews(tagViews);
            return view;
        } else {
            return null;
        }
    }

    public static void setVisibleOfViewGroup(ViewGroup viewGroup, boolean visible) {
        if (viewGroup != null) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (!(childView instanceof ImageView)) {
                    childView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 成为VIP买手
     */
    public static void topUp(BaseFragmentActivity activity, int req) {
        PayEntity entity = new PayEntity();
        entity.vip_pay_type = 1;//购买VIP买手
        entity.lastClassName = activity.getClass().getName();
        BaseFunc.gotoPayActivity(activity, entity, req);
    }

    public static List<View> getTalentViews(final Context mContext, List<TalentTagImage> images, final TalentImageClickListener imgClick, final String talentMemberId) {
        if (mContext != null && images != null && images.size() > 0) {
            List<View> views = new ArrayList<>();
            for (TalentTagImage tagImage : images) {
                View view = View.inflate(mContext, R.layout.item_tagimage_of_banner, null);
                final FrameLayout FContainer = (FrameLayout) view.findViewById(R.id.FContainer);
                ImageView ivTimes = (ImageView) view.findViewById(R.id.ivTimes);
                new FHImageViewUtil(ivTimes).setImageURI(tagImage.getTime_image(), FHImageViewUtil.SHOWTYPE.TALENTTIME);
                ivTimes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imgClick != null) {
                            imgClick.onImageClick(FContainer);
                        }
                    }
                });
                List<TalentImageTag> tags = tagImage.getGoods();
                if (tags != null && tags.size() > 0) {
                    for (int i = 0; i < tags.size(); i++) {
                        final TalentImageTag tag = tags.get(i);
                        View theTagView = View.inflate(mContext, R.layout.item_imagetag, null);
                        Custom3LinesLayout imgTag = (Custom3LinesLayout) theTagView.findViewById(R.id.custom3LinesView);
                        imgTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseFunc.gotoGoodsDetail(mContext, tag.getGoods_id(), null, talentMemberId);
                            }
                        });
                        boolean isLeft = false;
                        if (tag.getLine1() != null) {
                            isLeft = tag.getLine1().isLeft();
                            imgTag.setTextTop(tag.getLine1().getValue());
                        }
                        if (tag.getLine2() != null) {
                            imgTag.setTextMiddle(tag.getLine2().getValue());
                        }
                        if (tag.getLine3() != null) {
                            imgTag.setTextBottom(tag.getLine3().getValue());
                        }
                        imgTag.setDirection(isLeft);
                        imgTag.setPositionByLocation(tag.getLocation());
                        FContainer.addView(theTagView);
                    }
                }
                views.add(view);
            }
            return views;
        } else {
            return null;
        }
    }

    public static String getJuBaoUrl(String reportName, String type, String reportId) {
        String rep;
        try {
            rep = URLEncoder.encode(reportName, "utf-8");
        } catch (Exception e) {
            rep = reportName;
        }
        return String.format(BaseVar.URL_JUBAO, rep, type, reportId);
    }

    public static void juBao(BaseFragmentActivity activity, String reportName, String type, String reportId) {
        if (activity == null || activity.member == null) {
            gotoLogin(activity);
            return;
        }
        String url = getJuBaoUrl(reportName, type, reportId);
        gotoActivity(activity, FHBrowserActivity.class, url);
    }

    public static void gotoTalentImageDetail(Context context, String timeId, boolean isEvaluate) {
        Intent intent = new Intent(context, TalentImagesDetailActivity.class);
        intent.putExtra("TIMEID", timeId);
        intent.putExtra("COMMENTID", "暂时不需要");
        intent.putExtra("ISEVALUATE", isEvaluate);
        context.startActivity(intent);
    }

    /**
     * 进入图片标签编辑界面
     *
     * @param activity    Activity
     * @param imageUrl    imageUrl 可选
     * @param talentImage 如果不为null则表示修改 可选
     */
    public static void gotoEditImageTagActivity(Activity activity, String imageUrl, EntityOfAddTalentImage talentImage) {
        Intent intent = new Intent(activity, EditImageTagActivity.class);
        if (talentImage != null) {
            intent.putExtra("ENTITY", new Gson().toJson(talentImage));
        } else {
            intent.putExtra("IMGURL", imageUrl);
        }
        activity.startActivity(intent);
    }

    public static void selectPictures(BaseFragmentActivity activity, int reqCode, boolean crop, int outputX, int outputY, int aspectX, int aspectY) {
        Intent intent = new Intent(activity, CropperActivity.class);
        if (crop) {
            intent.putExtra("CROP", "true");
        } else {
            intent.putExtra("CROP", "false");
        }

        if (outputX > 0 && outputY > 0) {
            intent.putExtra("OUTPUTX", outputX);
            intent.putExtra("OUTPUTY", outputY);
        }

        if (aspectX == aspectY) {
            String model = android.os.Build.MODEL;
            model = model.toUpperCase();

            if (model.contains("AL00")) {
                //华为特殊处理 不然会显示圆
                intent.putExtra("ASPECTX", 9999);
                intent.putExtra("ASPECTY", 9998);
            } else {
                intent.putExtra("ASPECTX", 1);
                intent.putExtra("ASPECTY", 1);
            }
        } else {
            intent.putExtra("ASPECTX", aspectX);
            intent.putExtra("ASPECTY", aspectY);
        }

        activity.startActivityForResult(intent, reqCode);
    }

    public static void selectPictures(Fragment fragment, int reqCode, boolean crop, int outputX, int outputY, int aspectX, int aspectY) {
        Intent intent = new Intent(fragment.getActivity(), CropperActivity.class);
        if (crop) {
            intent.putExtra("CROP", "true");
        } else {
            intent.putExtra("CROP", "false");
        }

        if (outputX > 0 && outputY > 0) {
            intent.putExtra("OUTPUTX", outputX);
            intent.putExtra("OUTPUTY", outputY);
        }

        if (aspectX == aspectY) {
            String model = android.os.Build.MODEL;
            model = model.toUpperCase();
            if (model.contains("AL00")) {
                //华为特殊处理 不然会显示圆
                intent.putExtra("ASPECTX", 9999);
                intent.putExtra("ASPECTY", 9998);
            } else {
                intent.putExtra("ASPECTX", 1);
                intent.putExtra("ASPECTY", 1);
            }
        } else {
            intent.putExtra("ASPECTX", aspectX);
            intent.putExtra("ASPECTY", aspectY);
        }

        fragment.startActivityForResult(intent, reqCode);
    }

    public static void gotoStoreGoodsListActivity(Activity activity, String storeId, String classId,
                                                  String contact, String share, String search) {
        Intent intent = new Intent(activity, StoreGoodsListActivity.class);
        intent.putExtra("STOREID", storeId);
        intent.putExtra("CLASSID", classId);
        intent.putExtra("SEARCH", search);
        intent.putExtra("SHARE", share);
        intent.putExtra("CONTACT", contact);
        activity.startActivity(intent);
    }

    public static void gotoStoreClassActivity(Activity activity, String storeId, String share, String contact) {
        Intent intent = new Intent(activity, StoreGoodsClassActivity.class);
        intent.putExtra("STOREID", storeId);
        intent.putExtra("SHARE", share);
        intent.putExtra("CONTACT", contact);
        activity.startActivity(intent);
    }

    public static void gotoStoreNewGoodsActivity(Activity activity, String storeId, String share, String contact) {
        Intent intent = new Intent(activity, StoreNewGoodsActivity.class);
        intent.putExtra("STOREID", storeId);
        intent.putExtra("SHARE", share);
        intent.putExtra("CONTACT", contact);
        activity.startActivity(intent);
    }

    public static long dateStr2TimeInMillis(String dateStr, String format) {
        try {
            dateStr = dateStr.trim();
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
            Date date = dateFormat.parse(dateStr);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void gotoCouponGoodsPage(Context context, String store_id, String coupon_type) {
        String url = String.format(BaseVar.URL_COUPONS_GOODS, store_id, coupon_type);
        gotoActivity(context, FHBrowserActivity.class, url);
    }

    public static void setAddressDefault(Member member, String addressId) {
        if (TextUtils.isEmpty(addressId) || member == null) return;
        final Address addr = new Address();
        addr.address_id = addressId;
        addr.is_default = "1";
        new BaseBO().set_address(member.member_id, member.token, addr);
    }

    /**
     * 回到首页
     *
     * @param c     Context
     * @param index int 0 第一页  4我的
     */
    public static void gotoHome(Context c, int index) {
        Intent i = new Intent(c, MainActivity.class);
        i.putExtra("TOHOME", index);
        c.startActivity(i);
        ((Activity) c).finish();
    }

    /**
     * 获取文件保存路径
     *
     * @param dirName  String
     * @param fileName String
     * @return String
     */
    public static String getFileAbsPath(String dirName, String fileName) {
        return FHLog.getAppPath(dirName) + File.separator + fileName;
    }

    /**
     * 获取编码URL
     *
     * @param url String
     * @return String
     */
    public static String getEncodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * 按时间戳生成文件路径
     *
     * @param fileName String
     * @return String
     */
    public static String getImagePathByTime(String fileName) {

        String aff;
        if (TextUtils.isEmpty(fileName)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String time = dateFormat.format(new Date());
            aff = time + ".png";
        } else {
            aff = fileName;
        }

        return FHLog.getAppPath("FHApp/image") + File.separator + aff;
    }

    /**
     * 删除图片文件
     *
     * @param context  Context
     * @param filepath String
     */
    public static void deleteImageFile(Context context, String filepath) {
        if (TextUtils.isEmpty(filepath) || context == null) return;
        File file = new File(filepath);
        boolean isOK = file.delete();
        if (isOK) {
            FHLib.refreshMedia(context, file);
        }
    }

    /**
     * 进入极速免税订单确认
     *
     * @param activity Context
     * @param cartInfo String
     * @param from     String  1来自购物车 ，2来自立即购买
     */
    public static void dutyCartCheck(Activity activity, String cartInfo, String from) {
        Intent intent = new Intent(activity, DutyfreeCartCheckActivity.class);
        intent.putExtra("FROM", from);
        intent.putExtra("CART_INFO", cartInfo);
        activity.startActivityForResult(intent, DutyfreeCartActivity.REQCHECK);
    }

    public static void gotoDutyNewAddress(Context c, String cartID, DutyAddress address) {
        Intent intent = new Intent(c, DutyNewAddrsActivity.class);
        intent.putExtra("CARTID", cartID);
        if (address != null) {
            intent.putExtra("ADDRESS", new Gson().toJson(address));
        }
        c.startActivity(intent);
    }

    public static Uri getURIByString(String url) {
        Uri aURI = null;
        try {

            if (!TextUtils.isEmpty(url)) {
                aURI = Uri.parse(url);
            }
        } catch (Exception e) {
            aURI = null;
        }
        return aURI;
    }

    /**
     * 给各个界面传Key-Value
     *
     * @param jsonstr String
     * @return Intent
     */
    public static Bundle getBundleByJson(String jsonstr) {
        Bundle bundle = new Bundle();
        try {
            JSONObject json = new JSONObject(jsonstr);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = json.getString(key);
                bundle.putString(key, val);
            }
        } catch (Exception e) {
            FHLog.d("Plucky", "getBundleByJson error:" + e.getMessage());
        }
        return bundle;
    }

    /**
     * 进入订单确认页
     *
     * @param mContext    BaseFragmentActivity
     * @param entity      CartCheckEntity
     * @param requestCode int
     */
    public static void gotoCartCheckActivity(BaseFragmentActivity mContext, CartCheckEntity entity, int requestCode) {
        if (entity != null) {
            Intent intent = new Intent(mContext, CartCheckActivity.class);
            intent.putExtra("goodsId_num", entity.getGoodsIdNum());
            intent.putExtra("goods_source", entity.getGoodsSource());
            intent.putExtra("if_cart", entity.getIfCart());
            intent.putExtra("resource_tags", entity.getResourceTags());
            intent.putExtra("pintuan_id", entity.getPintuanId());
            intent.putExtra("pintuan_group_id", entity.getPintuanGroupId());
            intent.putExtra("pintuan_parent_id", entity.getPintuanParentId());
            if (requestCode > 0) {
                mContext.startActivityForResult(intent, requestCode);
            } else {
                mContext.startActivity(intent);
            }
        }
    }

    /**
     * 进入商品列表页
     *
     * @param mContext BaseFragmentActivity
     * @param key      String
     * @param is_own   int
     * @param gc_id    String
     */
    public static void gotoGoodsListActivity(BaseFragmentActivity mContext, String key, int is_own, String gc_id) {
        Intent intent = new Intent(mContext, GoodsListActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("is_own", is_own);
        intent.putExtra("gc_id", gc_id);
        mContext.startActivity(intent);
    }

    /**
     * 进入支付页面
     *
     * @param activity    BaseFragmentActivity
     * @param entity      PayEntity
     * @param requestCode int
     */
    public static void gotoPayActivity(BaseFragmentActivity activity, PayEntity entity, int requestCode) {
        if (entity != null) {
            String val = entity.getString();
            if (requestCode > 0) {
                gotoActivity4Result(activity, WXPayEntryActivity.class, val, requestCode);
            } else {
                gotoActivity(activity, WXPayEntryActivity.class, val);
            }
        }
    }

    /**
     * 进入支付成功的页面
     *
     * @param activity    BaseFragmentActivity
     * @param entity      PayEntity
     * @param requestCode int
     */
    public static void gotoPaySuccessActivity(BaseFragmentActivity activity, PayEntity entity, int requestCode) {
        if (entity != null) {
            String val = entity.getString();
            if (requestCode > 0) {
                gotoActivity4Result(activity, PaySuccessActivity.class, val, requestCode);
            } else {
                gotoActivity(activity, PaySuccessActivity.class, val);
            }
        }
    }

    /**
     * 找回密码
     *
     * @param activity    BaseFragmentActivity
     * @param account     String
     * @param requestCode int
     */
    public static void gotoRetrievePwdActivity(BaseFragmentActivity activity, String account, int requestCode) {
        Intent intent = new Intent(activity, RetrievePwdActivity.class);
        intent.putExtra("LASTACTIVITY", account.getClass().getName());
        intent.putExtra("ACCOUNT", account);
        activity.startActivityForResult(intent, requestCode);
    }
}
