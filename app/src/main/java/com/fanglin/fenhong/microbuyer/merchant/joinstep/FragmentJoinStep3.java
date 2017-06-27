package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.model.EditTipsEntity;
import com.fanglin.fenhong.microbuyer.base.model.StoreCategory;
import com.fanglin.fenhong.microbuyer.base.model.StoreCls;
import com.fanglin.fenhong.microbuyer.base.model.StoreGrade;
import com.fanglin.fenhong.microbuyer.base.model.StoreInfo;
import com.fanglin.fenhong.microbuyer.merchant.adapter.JoinStep3Adapter;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-30.
 * 供应商审核第一步--检查当前到达的步骤
 */
public class FragmentJoinStep3 extends BaseFragment implements StoreInfo.StoreInfoModeCallBack, JoinStep3Adapter.JoinStep3CallBack {
    View view;
    @ViewInject(R.id.Laccount)
    LinearLayout Laccount;
    @ViewInject(R.id.tv_account)
    TextView tv_account;
    @ViewInject(R.id.Lshopname)
    LinearLayout Lshopname;
    @ViewInject(R.id.tv_shopname)
    TextView tv_shopname;
    @ViewInject(R.id.Lshoplv)
    LinearLayout Lshoplv;
    @ViewInject(R.id.tv_shoplv)
    TextView tv_shoplv;
    @ViewInject(R.id.Lshopcls)
    LinearLayout Lshopcls;
    @ViewInject(R.id.tv_shopcls)
    TextView tv_shopcls;
    @ViewInject(R.id.Lscope)
    LinearLayout Lscope;
    @ViewInject(R.id.rcv)
    RecyclerView rcv;
    @ViewInject(R.id.btn_submit)
    TextView btn_submit;
    @ViewInject(R.id.LHold)
    LinearLayout LHold;

    StoreInfo storeInfo;
    JoinStep3Adapter adapter;
    boolean isSubimt = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_joinstep3, null);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        storeInfo = new StoreInfo();
        storeInfo.setModelCallBack(this);

        rcv.setLayoutManager(new AutoGridLayoutManager(act, 1));
        adapter = new JoinStep3Adapter(act);
        adapter.setCallback(this);
        rcv.setAdapter(adapter);

        FHLib.EnableViewGroup(LHold, false);
        btn_submit.setVisibility(View.GONE);
    }

    public void getData() {
        isSubimt = false;
        if (storeInfo != null) storeInfo.getData(member);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onSuccess(StoreInfo info) {
        if (info != null) {
            tv_account.setText(info.seller_name);
            tv_shopname.setText(info.store_name);
            tv_shoplv.setText(info.sg_name);
            tv_shopcls.setText(info.sc_name);
            adapter.setList(info.store_classes);
            rcv.getAdapter().notifyDataSetChanged();
            storeInfo.grade_list = info.grade_list;
            storeInfo.store_class = info.store_class;
            if (TextUtils.equals("2", info.step)) {
                FHLib.EnableViewGroup(LHold, true);
                btn_submit.setVisibility(View.VISIBLE);
            } else {
                FHLib.EnableViewGroup(LHold, false);
                btn_submit.setVisibility(View.GONE);
            }
        } else {
            if (rmpos != -1) {
                adapter.removeItem(rmpos);
                rcv.getAdapter().notifyDataSetChanged();
                rmpos = -1;
            }
            if (item != null) {
                if (!adapter.containsItem(item)) {
                    adapter.addItem(item);
                    rcv.getAdapter().notifyDataSetChanged();
                } else {
                    BaseFunc.showMsg(act, getString(R.string.tips_of_sameCategory));
                }
                item = null;
            }

            if (storeInfo.sg_name != null) {
                tv_shoplv.setText(storeInfo.sg_name);
            }
            if (storeInfo.sc_name != null) {
                tv_shopcls.setText(storeInfo.sc_name);
            }
            if (isSubimt) {
                ((JoinStepActivity) act).changePage(0);
            }
            BaseFunc.showMsg(act, getString(R.string.op_success));
        }
    }

    @Override
    public void onError(String errcode) {

    }

    private int rmpos = -1;
    StoreCategory item;

    @Override
    public void onDelete(int position) {
        rmpos = position;
        String[] res = adapter.getIds_Names(position);
        storeInfo.gc_ids = res[0];
        storeInfo.gc_names = res[1];

        final FHHintDialog fhd = new FHHintDialog(act);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                isSubimt = false;
                storeInfo.delete_cls(member);
            }
        });
        fhd.setTvContent(getString(R.string.hint_delete));
        fhd.show();
    }

    @OnClick(value = {R.id.Laccount, R.id.Lshopname, R.id.Lshoplv, R.id.Lshopcls, R.id.Lscope, R.id.btn_submit})
    public void onViewClick(View v) {
        EditTipsEntity entity = new EditTipsEntity();
        entity.lastActivity = this.getClass().getName();//记录从哪里来
        entity.isCompanyInfo = false;
        switch (v.getId()) {
            case R.id.Laccount:
                entity.type = 7;
                entity.field = ((TextView) Laccount.getChildAt(0)).getText().toString();//要编辑的内容
                entity.content = tv_account.getText().toString();//已经输入的内容
                entity.tips = getString(R.string.tips_of_sellername);//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Lshopname:
                entity.type = 8;
                entity.field = ((TextView) Lshopname.getChildAt(0)).getText().toString();//要编辑的内容
                entity.content = tv_shopname.getText().toString();//已经输入的内容
                entity.tips = getString(R.string.tips_of_storename);//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Lshoplv:
                String val = new Gson().toJson(storeInfo.grade_list);
                BaseFunc.gotoActivity4Result(this, SelectStoreGradeActivity.class, val, 0x101);
                break;
            case R.id.Lshopcls:
                String val1 = new Gson().toJson(storeInfo.store_class);
                BaseFunc.gotoActivity4Result(this, SelectStoreClsActivity.class, val1, 0x102);
                break;
            case R.id.Lscope:
                BaseFunc.gotoActivity4Result(this, SelectStoreCategoryActivity.class, entity.lastActivity, 0x100);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 7:
                tv_account.setText(data.getStringExtra("VAL"));
                break;
            case 8:
                tv_shopname.setText(data.getStringExtra("VAL"));
                break;

            case 0x100:
                try {
                    String[] res = new Gson().fromJson(data.getStringExtra("VAL"), new TypeToken<String[]>() {
                    }.getType());
                    item = new StoreCategory();
                    item.id1 = res[0];
                    item.name1 = res[3];
                    item.id2 = res[1];
                    item.name2 = res[4];
                    item.id3 = res[2];
                    item.name3 = res[5];

                    if (!adapter.containsItem(item)) {
                        isSubimt = false;
                        storeInfo.gc_ids = item.id1 + "," + item.id2 + "," + item.id3;
                        storeInfo.gc_names = item.name1 + "," + item.name2 + "," + item.name3;
                        /*添加经营类目*/
                        storeInfo.update(member);
                    } else {
                        BaseFunc.showMsg(act, getString(R.string.tips_of_sameCategory));
                    }
                } catch (Exception e) {
                    //
                }
                break;
            case 0x101:
                try {
                    isSubimt = false;
                    StoreGrade grade = new Gson().fromJson(data.getStringExtra("VAL"), StoreGrade.class);
                    storeInfo.sg_id = grade.sg_id;
                    storeInfo.sg_name = grade.sg_desc;
                    storeInfo.update(member);
                } catch (Exception e) {
                    //
                }
                break;
            case 0x102:
                try {
                    isSubimt = false;
                    StoreCls cls = new Gson().fromJson(data.getStringExtra("VAL"), StoreCls.class);
                    storeInfo.sc_id = cls.sc_id;
                    storeInfo.sc_name = cls.sc_desc;
                    storeInfo.update(member);
                } catch (Exception e) {
                    //
                }
                break;
        }
    }

    private void submit() {
        if (tv_account.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_account) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Laccount);
            return;
        }

        if (tv_shopname.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_shopname) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lshopname);
            return;
        }

        if (tv_shoplv.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.tips_select) + getString(R.string.merchantin_shoplv)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lshoplv);
            return;
        }
        if (tv_shopcls.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.tips_select) + getString(R.string.merchantin_shopcls)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lshopcls);
            return;
        }

        if (adapter.getItemCount() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.tips_select) + getString(R.string.merchantin_scope_select)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lscope);
            return;
        }

        final FHHintDialog fhd = new FHHintDialog(act);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                isSubimt = true;
                storeInfo.submit(member);
            }
        });
        fhd.setTvContent(getString(R.string.tips_of_submit));
        fhd.show();
    }

}
