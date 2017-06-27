package com.fanglin.fenhong.microbuyer.buyer;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.fanglin.fenhong.microbuyer.base.event.FavoritesEditEvent;
import com.fanglin.fenhong.microbuyer.buyer.adapter.DrFavAdapter;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsFavAdapter;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 收藏达人
 * Created by lizhixin on 2016/6/7.
 */
public class FavoritesDrFragment extends FavoritesCommonFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initAdapter() {
        cType = "talent";
        adapter = new DrFavAdapter(getActivity());
        adapter.setType(5);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleEditEvent(FavoritesEditEvent event) {
        if (TextUtils.equals(event.type, "dr")) {
            adapter.isShowChk = !adapter.isShowChk;
            LDel.setVisibility(adapter.isShowChk ? View.VISIBLE : View.GONE);
            recycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
        }
    }

}
