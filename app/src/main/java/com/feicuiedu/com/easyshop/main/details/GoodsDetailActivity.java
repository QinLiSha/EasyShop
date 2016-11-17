package com.feicuiedu.com.easyshop.main.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.apphx.model.repository.DefaultLocalUsersRepo;
import com.feicuiedu.apphx.presentation.call.voice.HxVoiceCallActivity;
import com.feicuiedu.apphx.presentation.chat.HxChatActivity;
import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.commons.ActivityUtils;
import com.feicuiedu.com.easyshop.components.AvatarLoadOptions;
import com.feicuiedu.com.easyshop.components.ProgressDialogFragment;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.CurrentUser;
import com.feicuiedu.com.easyshop.model.GoodsDetail;
import com.feicuiedu.com.easyshop.model.User;
import com.feicuiedu.com.easyshop.network.EasyShopApi;
import com.feicuiedu.com.easyshop.user.login.LoginActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends MvpActivity<GoodsDetailView, GoodsDetailPresenter>
        implements GoodsDetailView {

    private static final String UUID = "uuid";
    private static final String STATE = "state";

    public static Intent getStartIntent(Context context, String uuid, int state) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(UUID, uuid);
        intent.putExtra(STATE, state);
        return intent;
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    /*使用开源库CircleIndicator来实现ViewPager的圆点指示器。*/
    @Bind(R.id.indicator)
    CircleIndicator indicator;
    @Bind(R.id.tv_detail_name)
    TextView tv_detail_name;
    @Bind(R.id.tv_detail_price)
    TextView tv_detail_price;
    @Bind(R.id.tv_detail_master)
    TextView tv_detail_master;
    @Bind(R.id.tv_detail_describe)
    TextView tv_detail_describe;
    @Bind(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @Bind(R.id.tv_goods_error)
    TextView tv_goods_error;
    @Bind(R.id.btn_detail_message)
    Button btn_detail_message;
    @Bind(R.id.btn_detail_call)
    Button btn_detail_call;

    private String str_uuid;
    private ArrayList<ImageView> list;
    /*用来存放图片uri的list*/
    private ArrayList<String> list_uri;
    private GoodsDetailAdapter adapter;
    private ProgressDialogFragment dialogFragment;
    private ActivityUtils activityUtils;
    private User goods_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        activityUtils = new ActivityUtils(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adapter = new GoodsDetailAdapter(list);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        viewPager.setAdapter(adapter);
        /*ViewPager的Item单击事件*/
        adapter.setListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent intent = GoodsDetailInfoActivity.getStartIntent(GoodsDetailActivity.this, list_uri);
                startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }

    /*对页面数据进行初始化*/
    private void init() {
        /*商品在商品表中的主键*/
        str_uuid = getIntent().getStringExtra(UUID);
        /*从不同页面进入详情页面的状态值,0为默认值,1是我的商品页面进入*/
        int btn_show = getIntent().getIntExtra(STATE, 0);
        if (btn_show == 1) {
            /*我的商品页面进入显示删除按钮,隐藏下方联系按钮*/
            tv_goods_delete.setVisibility(View.VISIBLE);
            btn_detail_message.setVisibility(View.GONE);
            btn_detail_call.setVisibility(View.GONE);
        }
        presenter.getData(str_uuid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    /*讲图片路径转化为一个ImageView*/
    private void getImage(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance()
                    .displayImage(EasyShopApi.IMAGE_URL + list.get(i),
                            imageView, AvatarLoadOptions.build_item());
            this.list.add(imageView);
        }
    }

    @OnClick({R.id.btn_detail_call, R.id.btn_detail_message, R.id.tv_goods_delete})
    public void onClick(View view) {
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.btn_detail_call:
                if (goods_user.getHx_Id().equals(CachePreferences.getUser().getHx_Id())) {
                    activityUtils.showToast("这个商品是自己发布的哦！");
                    return;
                }
                Intent intent = HxVoiceCallActivity.getStartIntent(this, goods_user.getHx_Id(), false);
                startActivity(intent);
                break;
            case R.id.btn_detail_message:
                if (goods_user.getHx_Id().equals(CachePreferences.getUser().getHx_Id())) {
                    activityUtils.showToast("这个商品是自己发布的哦！");
                    return;
                }
                DefaultLocalUsersRepo.getInstance(this).save(CurrentUser.convert(goods_user));
                startActivity(HxChatActivity.getStartIntent(GoodsDetailActivity.this, goods_user.getHx_Id()));
                break;
            case R.id.tv_goods_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.goods_title_delete);
                builder.setMessage(R.string.goods_info_delete);
                builder.setPositiveButton(R.string.goods_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.delete(str_uuid);
                    }
                });
                builder.setNegativeButton(R.string.popu_cancle, null);
                builder.create().show();
                break;
        }
    }

    @Override
    public void showProgress() {
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
        }
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        dialogFragment.dismiss();
    }

    @Override
    public void setImageData(ArrayList<String> viewList) {
        list_uri = viewList;
        getImage(viewList);
        adapter.notifyDataSetChanged();
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setData(GoodsDetail data, User goods_user) {
        this.goods_user = goods_user;
        tv_detail_name.setText(data.getName());
        tv_detail_price.setText(getString(R.string.goods_money, data.getPrice()));
        tv_detail_master.setText(getString(R.string.goods_detail_master, goods_user.getNick_Name()));
        tv_detail_describe.setText(data.getDescription());
    }

    @Override
    public void showError() {
        tv_goods_error.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.goods_overdue);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void deleteEnd() {
        finish();
    }
}
