package com.fanglin.fenhong.microbuyer.buyer;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import com.fanglin.fenhong.microbuyer.base.event.FavoritesEditEvent;
import com.fanglin.fenhong.microbuyer.buyer.adapter.BrandFavAdapter;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 收藏品牌
 * Created by Plucky on 2016/9/27.
 */
public class FavoritesBrandFragment extends FavoritesCommonFragment implements BrandFavAdapter.FocusClickListener {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initAdapter() {
        cType = "brand";
        adapter = new BrandFavAdapter(getActivity());
        adapter.setType(6);
        ((BrandFavAdapter) adapter).setFocusClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleEditEvent(FavoritesEditEvent event) {
        if (TextUtils.equals(event.type, "brand")) {
            adapter.isShowChk = !adapter.isShowChk;
            LDel.setVisibility(adapter.isShowChk ? View.VISIBLE : View.GONE);
            recycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onFocusClick(String ids) {
        if (fav != null) {
            fav.delete_favorites(getActivity(), member, ids, cType);
        }
    }
}
