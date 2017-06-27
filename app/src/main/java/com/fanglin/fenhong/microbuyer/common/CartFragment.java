package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckEntity;
import com.fanglin.fenhong.microbuyer.base.model.SerializationCarts;
import com.fanglin.fenhong.microbuyer.buyer.CalculateLayout;
import com.fanglin.fenhong.microbuyer.buyer.CartsLayout;
import com.fanglin.fenhong.microbuyer.buyer.adapter.CartSectionAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import org.json.JSONObject;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： Created by Plucky on 2015/8/18.
 * modify by lizhixin on 2016/03/17
 */
public class CartFragment extends BaseFragment implements CartSectionAdapter.ItemCheckedListener, FHHintDialog.FHHintListener {

    @ViewInject(R.id.LGrp)
    LinearLayout LGrp;

    @ViewInject(R.id.LSea)
    LinearLayout LSea;
    @ViewInject(R.id.LChina)
    LinearLayout LChina;

    @ViewInject(R.id.ivSea)
    ImageView ivSea;
    @ViewInject(R.id.ivChina)
    ImageView ivChina;

    @ViewInject(R.id.llRrefresh)
    LinearLayout llRefresh;
    @ViewInject(R.id.LCartHold)
    LinearLayout LCartHold;
    @ViewInject(R.id.LBotHold)
    LinearLayout LBotHold;
    @ViewInject(R.id.LProgress)
    LinearLayout LProgress;
    CalculateLayout calculateLayout;
    CartsLayout cartsLayout;
    int gc_area = 0;//  0 国内  1 国外
    @ViewInject(R.id.LTips)
    LinearLayout LTips;
    @ViewInject(R.id.tv_msg)
    TextView tv_msg;
    @ViewInject(R.id.tv_login)
    TextView tv_login;
    @ViewInject(R.id.tv_edit)
    TextView tv_edit;
    @ViewInject(R.id.tv_delete)
    TextView tv_delete;
    @ViewInject(R.id.tv_cancel)
    TextView tv_cancel;
    boolean isActive = true;

    private String deleteids = null;

    private LinearLayout v;
    private FHHintDialog deleteDialog;//弹框提示是否删除商品

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = (LinearLayout) View.inflate(act, R.layout.fragment_cart, null);
        ViewUtils.inject(this, v);

        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {
        cartsLayout = new CartsLayout(act);

        calculateLayout = new CalculateLayout(act);
        calculateLayout.Calculate(null);
        cartsLayout.calculateLayout = calculateLayout;
        calculateLayout.setCallBack(new CalculateLayout.CalculateCallBack() {
            @Override
            public void onCheckBoxClick(boolean b) {
                cartsLayout.cartSectionAdapter.selectAll(b);
                cartsLayout.cartSectionAdapter.notifyDataSetChanged();

                onItemChecked();
            }

            @Override
            public void onCalculateClick() {
                CalculateData cd = cartsLayout.cartSectionAdapter.getCalculate(-1);
                if (cd == null || cd.selected_id_num == null || cd.selected_id_num.length() == 0)
                    return;
                if (cartsLayout.cartSectionAdapter.isValid()) {
                    CartCheckEntity entity = new CartCheckEntity();
                    entity.setGoodsIdNum(cd.selected_id_num);
                    entity.setGoodsSource(0);
                    entity.setIfCart(1);
                    BaseFunc.gotoCartCheckActivity(act, entity, 0);
                } else {
                    BaseFunc.showMsg(act, act.getString(R.string.sale_invalid_tips));
                }

            }
        });
        LBotHold.removeAllViews();
        LBotHold.addView(calculateLayout.getView());
        LBotHold.setVisibility(View.GONE);

        /**
         * 只在第一次加载的时候去判断是否断网，来显示这两个控件
         */
        if (FHLib.isNetworkConnected(getActivity()) == 0) {
            llRefresh.setVisibility(View.VISIBLE);
        } else {
            llRefresh.setVisibility(View.GONE);
        }

        //删除提示对话框
        deleteDialog = new FHHintDialog(act);
        deleteDialog.setHintListener(this);
        deleteDialog.setTvTitle(act.getString(R.string.dlg_title));
        deleteDialog.setTvContent(act.getString(R.string.sure_to_delete_cart));
        deleteDialog.setTvLeft(act.getString(R.string.cancel));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        /**
         * MainActivity onResume()事件中 getCartNum会触发刷新事件
         * 所以不需要再onCreate和onResume中添加数据请求的操作
         * Added By Plucky
         */
    }

    @OnClick(value = {R.id.LSea, R.id.LChina, R.id.tv_login, R.id.tv_edit, R.id.tv_delete, R.id.tv_cancel})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.LSea:
                gc_area = 1;
                changeStatus(0);
                getData();
                break;
            case R.id.LChina:
                gc_area = 0;
                changeStatus(1);
                getData();
                break;
            case R.id.tv_login:
                BaseFunc.gotoLogin(act);
                break;
            case R.id.tv_edit:
                setFourDelBtnState(true, false, true);
                tv_delete.setSelected(false);
                break;
            case R.id.tv_delete:
                if (cartsLayout.cartSectionAdapter == null) return;
                CalculateData cd = cartsLayout.cartSectionAdapter.getCalculate(-1);
                deleteids = cd.selected_id;
                if (deleteids == null || deleteids.length() == 0) {
                    return;
                }
                deleteDialog.show();
                break;
            case R.id.tv_cancel: {
                setFourDelBtnState(false, true, false);
                if (cartsLayout.cartSectionAdapter != null) {
                    cartsLayout.cartSectionAdapter.clearEditSelected();
                    cartsLayout.cartSectionAdapter.notifyDataSetChanged();
                }
                break;
            }
            default:
                break;
        }
    }

    private void changeStatus(int index) {
        RefreshTipsView(index);
        if (index == 0) {
            LSea.setSelected(true);
            LChina.setSelected(false);
            ivSea.setVisibility(View.VISIBLE);
            ivChina.setVisibility(View.INVISIBLE);

            LBotHold.setVisibility(View.GONE);
            CartSectionAdapter adapter = new CartSectionAdapter(act);
            adapter.setItemCheckedListener(this);
            cartsLayout.setAdapter(adapter, (gc_area == 0));
        } else {
            LSea.setSelected(false);
            LChina.setSelected(true);
            ivSea.setVisibility(View.INVISIBLE);
            ivChina.setVisibility(View.VISIBLE);

            CartSectionAdapter adapter = new CartSectionAdapter(act);
            adapter.setItemCheckedListener(this);
            cartsLayout.setAdapter(adapter, (gc_area == 0));
        }
        //初始化3个按钮的状态
        setFourDelBtnState(false, true, false);
    }

    private void RefreshTipsView(int index) {
        if (member == null) {
            LTips.setVisibility(View.VISIBLE);
            tv_login.setVisibility(View.VISIBLE);
            tv_msg.setText(getString(R.string.hint_cartlogin));
            tv_msg.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            tv_login.setVisibility(View.GONE);
            tv_msg.setGravity(Gravity.CENTER);
            if (index == 0) {
                tv_msg.setText(getString(R.string.hint_cartsea));
                LTips.setVisibility(View.VISIBLE);
            } else {
                LTips.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        member = FHCache.getMember(act);
        if (member == null) {
            LCartHold.removeAllViews();
            LCartHold.addView(cartsLayout.getView(LBotHold, null));
            return;
        }
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                FHLib.EnableViewGroup(LGrp, false);//不可点
                LProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                llRefresh.setVisibility(View.GONE);
                FHLib.EnableViewGroup(LGrp, true);//恢复可点
                LProgress.setVisibility(View.INVISIBLE);
                DrawList(data);
            }
        }).get_cart_list(member.member_id, member.token, gc_area);
    }

    private void DrawList(String data) {
        SerializationCarts serializationCarts = SerializationCarts.parseData(data);
        JSONObject carts = serializationCarts != null ? serializationCarts.store_cart_list : null;
        cartsLayout.setCartData(serializationCarts);
        LCartHold.removeAllViews();
        LCartHold.addView(cartsLayout.getView(LBotHold, carts));
    }

    private void deleteCarts(String cartids) {
        if (cartids == null) return;
        if (member == null) return;

        final SpotsDialog sad = BaseFunc.getLoadingDlg(act, act.getString(R.string.deleting));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                if (isActive) sad.show();
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isActive) sad.dismiss();
                if (isSuccess) {
                    BaseFunc.getCartNum(member);//删除成功更新数量
                    setFourDelBtnState(false, true, false);
                }
            }
        }).delete_cart(cartids, member.member_id, member.token, gc_area);
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    /**
     * 选中或取消选中商品时触发此方法
     */
    @Override
    public void onItemChecked() {
        if (cartsLayout.cartSectionAdapter == null) return;
        CalculateData cd = cartsLayout.cartSectionAdapter.getCalculate(-1);
        String cartids = cd.selected_id;
        if (cartids == null || cartids.length() == 0) {
            tv_delete.setSelected(false);
        } else {
            tv_delete.setSelected(true);
        }
    }

    /**
     * 设置顶部删除相关3个按钮的显示隐藏
     *
     * @param cancelBtn c
     * @param editBtn   e
     * @param delBtn    d
     */
    private void setFourDelBtnState(boolean cancelBtn, boolean editBtn, boolean delBtn) {
        if (cancelBtn)
            tv_cancel.setVisibility(View.VISIBLE);
        else
            tv_cancel.setVisibility(View.INVISIBLE);
        if (editBtn)
            tv_edit.setVisibility(View.VISIBLE);
        else
            tv_edit.setVisibility(View.GONE);
        if (delBtn)
            tv_delete.setVisibility(View.VISIBLE);
        else
            tv_delete.setVisibility(View.GONE);

        //如果删除按钮出来了 则处于编辑模式
        if (cartsLayout.cartSectionAdapter != null) {
            cartsLayout.cartSectionAdapter.setEditMode(delBtn);
            cartsLayout.cartSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLeftClick() {
        setFourDelBtnState(false, true, false);
        if (cartsLayout.cartSectionAdapter != null) {
            cartsLayout.cartSectionAdapter.clearEditSelected();
            cartsLayout.cartSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRightClick() {
        deleteCarts(deleteids);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleCartAction(CartActionEvent event) {
        if (event != null) {
            if (event.getCartAction() != null) {
                if (!event.getCartAction().isJustChangeNum()) {
                    gc_area = event.getCartAction().getIs_global();
                    changeStatus(gc_area == 0 ? 1 : 0);
                    getData();
                }
            } else {
                LCartHold.removeAllViews();
                LCartHold.addView(cartsLayout.getView(LBotHold, null));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleNoWfi(WifiUnconnectHintAfter wifiUnconnectHintEntity) {
        /**
         * 从无网络到有网络环境，要隐藏无网络提示控件
         */
        llRefresh.setVisibility(View.GONE);
        if (cartsLayout == null || cartsLayout.cartSectionAdapter == null || cartsLayout.cartSectionAdapter.list == null) {
            BaseFunc.getCartNum(member);
        }
    }

}
