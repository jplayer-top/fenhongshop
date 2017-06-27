package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
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

/**
 * 作者： Created by Plucky on 15-10-31.
 * 购物车新开页 与首页的购物车一致
 * modify by lizhixin on 2016/03/18
 */
public class CartActivity extends BaseFragmentActivity implements CartSectionAdapter.ItemCheckedListener, FHHintDialog.FHHintListener {
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

    @ViewInject(R.id.LCartHold)
    LinearLayout LCartHold;
    @ViewInject(R.id.LBotHold)
    LinearLayout LBotHold;
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
    @ViewInject(R.id.ivBack)
    ImageView ivBack;
    @ViewInject(R.id.LProgress)
    LinearLayout LProgress;
    boolean isActive = true;
    private LinearLayout v;
    private FHHintDialog deleteDialog;//弹框提示是否删除商品
    private String deleteids = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = (LinearLayout) View.inflate(mContext, R.layout.fragment_cart, null);
        setContentView(v);
        ViewUtils.inject(this);

        initView();

    }

    private void initView() {
        v.setPadding(0, 0, 0, 0);
        ivBack.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.INVISIBLE);
        cartsLayout = new CartsLayout(mContext);

        calculateLayout = new CalculateLayout(mContext);
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
                    BaseFunc.gotoCartCheckActivity(CartActivity.this, entity, 0);
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.sale_invalid_tips));
                }
            }
        });
        LBotHold.removeAllViews();
        LBotHold.addView(calculateLayout.getView());
        LBotHold.setVisibility(View.GONE);

        //删除提示对话框
        deleteDialog = new FHHintDialog(this);
        deleteDialog.setHintListener(this);
        deleteDialog.setTvTitle(getString(R.string.dlg_title));
        deleteDialog.setTvContent(getString(R.string.sure_to_delete_cart));
        deleteDialog.setTvLeft(getString(R.string.cancel));
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;

        gc_area = fhApp.getIsGlobal();
        changeStatus(gc_area == 0 ? 1 : 0);
        getData();
    }

    @OnClick(value = {R.id.LSea, R.id.LChina, R.id.tv_login, R.id.tv_edit, R.id.ivBack, R.id.tv_cancel, R.id.tv_delete})
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
                BaseFunc.gotoLogin(this);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_edit:
                setFourDelBtnState(false, true, false, true);
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
            case R.id.tv_cancel:
                setFourDelBtnState(true, false, true, false);
                if (cartsLayout.cartSectionAdapter != null) {
                    cartsLayout.cartSectionAdapter.clearEditSelected();
                    cartsLayout.cartSectionAdapter.notifyDataSetChanged();
                }
                break;
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
            CartSectionAdapter adapter = new CartSectionAdapter(mContext);
            adapter.setItemCheckedListener(this);
            cartsLayout.setAdapter(adapter, (gc_area == 0));
        } else {
            LSea.setSelected(false);
            LChina.setSelected(true);
            ivSea.setVisibility(View.INVISIBLE);
            ivChina.setVisibility(View.VISIBLE);
            CartSectionAdapter adapter = new CartSectionAdapter(mContext);
            adapter.setItemCheckedListener(this);
            cartsLayout.setAdapter(adapter, (gc_area == 0));
        }

        setFourDelBtnState(true, false, true, false);
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
        member = FHCache.getMember(this);
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

        final SpotsDialog sad = BaseFunc.getLoadingDlg(mContext, getString(R.string.deleting));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                if (isActive) {
                    sad.show();
                }
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isActive) {
                    sad.dismiss();
                }
                if (isSuccess) {
                    /**
                     * 这里只需要去内存数据进行刷新即可
                     */
                    gc_area = fhApp.getIsGlobal();
                    changeStatus(gc_area == 0 ? 1 : 0);
                    getData();

                    //通知首页购物车数据变化
                    BaseFunc.getCartNum(member);
                }
            }
        }).delete_cart(cartids, member.member_id, member.token, gc_area);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    /**
     * 选中或取消选中商品时触发此方法
     * 用于控制删除按钮的显示与隐藏
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
     * 设置顶部删除相关四个按钮的显示隐藏
     *
     * @param backBtn   返回键
     * @param cancelBtn 取消键
     * @param editBtn   编辑键
     * @param delBtn    删除键
     */
    private void setFourDelBtnState(boolean backBtn, boolean cancelBtn, boolean editBtn, boolean delBtn) {
        if (backBtn)
            ivBack.setVisibility(View.VISIBLE);
        else
            ivBack.setVisibility(View.INVISIBLE);
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
        setFourDelBtnState(true, false, true, false);
        if (cartsLayout.cartSectionAdapter != null) {
            cartsLayout.cartSectionAdapter.clearEditSelected();
            cartsLayout.cartSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRightClick() {
        deleteCarts(deleteids);
    }

}
