package com.fanglin.fenhong.microbuyer.buyer;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.MainActivity;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.SerializationCarts;
import com.fanglin.fenhong.microbuyer.buyer.adapter.CartSectionAdapter;
import com.fanglin.fenhong.microbuyer.common.CartActivity;
import org.json.JSONObject;

/**
 * 作者： Created by Plucky on 2015/9/17.
 */
public class CartsLayout implements View.OnClickListener {

    private Context mContext;
    private View view;
    private RecyclerView RcvCart;
    LinearLayout LNoresult;
    public CalculateLayout calculateLayout;
    public boolean isChina;
    public TextView tv_around, tv_fav;
    public CartSectionAdapter cartSectionAdapter;

    public CartsLayout(Context c) {
        this.mContext = c;
        view = View.inflate(mContext, R.layout.layout_carts, null);
        RcvCart = (RecyclerView) view.findViewById(R.id.RcvCart);
        LNoresult = (LinearLayout) view.findViewById(R.id.LNoresult);
        tv_around = (TextView) view.findViewById(R.id.tv_around);
        tv_fav = (TextView) view.findViewById(R.id.tv_fav);
        tv_around.setOnClickListener(this);
        tv_fav.setOnClickListener(this);
        int height = BaseFunc.getDisplayMetrics(mContext).heightPixels;
        int dy = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_120);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - dy);
        LNoresult.setLayoutParams(params);
    }

    public void setAdapter(final CartSectionAdapter adapter, final boolean isChina) {
        cartSectionAdapter = adapter;
        this.isChina = isChina;
        RcvCart.setAdapter(cartSectionAdapter);
        LinearLayoutManager cl = new LinearLayoutManager(mContext);
        cl.setSmoothScrollbarEnabled(true);
        RcvCart.setLayoutManager(cl);
        RcvCart.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                CalculateData cd = cartSectionAdapter.getCalculate(-1);
                if (isChina) {
                    if (calculateLayout == null) return;
                    /** 数据集变化的时候去获取数据*/
                    calculateLayout.Calculate(cd);
                }
            }
        });
    }

    public void setCartData(SerializationCarts cartData) {
        if (cartSectionAdapter != null)
            cartSectionAdapter.setCartData(cartData);
    }

    public View getView(LinearLayout LBotHold, JSONObject json) {
        if (cartSectionAdapter != null) {
            cartSectionAdapter.setList(json, isChina);
            RcvCart.getAdapter().notifyDataSetChanged();
            if (cartSectionAdapter.getItemCount() > 0) {
                RcvCart.setVisibility(View.VISIBLE);
                LNoresult.setVisibility(View.GONE);
                if (!isChina) {
                    LBotHold.setVisibility(View.GONE);
                } else {
                    LBotHold.setVisibility(View.VISIBLE);
                }
            } else {
                RcvCart.setVisibility(View.GONE);
                LNoresult.setVisibility(View.VISIBLE);
                LBotHold.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_around:
                lookAround();
                break;
            case R.id.tv_fav:
                lookFav();
                break;
        }
    }

    private void lookAround() {
        //如果是在首页的CartFragment
        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).resetPage(0);
        }

        if (mContext instanceof CartActivity) {
            CartActivity cartActivity = (CartActivity) mContext;
            BaseFunc.gotoActivity(mContext, MainActivity.class, null);
            cartActivity.finish();
        }
    }

    private void lookFav() {
        if (mContext instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
            if (activity.member == null) {
                BaseFunc.gotoLogin(activity);
                return;
            }
            BaseFunc.gotoActivity(activity, FavoritesActivity.class, "0");
        }

    }
}
