package com.feicuiedu.com.easyshop.main.me.personinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.commons.ActivityUtils;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.components.AvatarLoadOptions;
import com.feicuiedu.com.easyshop.components.PicWindow;
import com.feicuiedu.com.easyshop.components.ProgressDialogFragment;
import com.feicuiedu.com.easyshop.main.MainActivity;
import com.feicuiedu.com.easyshop.model.ItemShow;
import com.feicuiedu.com.easyshop.model.User;
import com.feicuiedu.com.easyshop.network.EasyShopApi;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoActivity extends MvpActivity<PersonInfoView, PersonInfoPresenter> implements PersonInfoView, AdapterView.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.iv_user_head)
    ImageView iv_user_head;
    @BindString(R.string.username_update)
    String str_username_update;
    @BindString(R.string.id_update)
    String str_id_update;
    @BindString(R.string.username)
    String str_username;
    @BindString(R.string.hx_id)
    String str_hx_id;
    @BindString(R.string.nickname)
    String str_nickname;

    private ActivityUtils activityUtils;
    private PicWindow picWindow;
    private ProgressDialogFragment progressDialogFragment;
    private PersonInfoAdapter adapter;
    private ArrayList<ItemShow> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_person_info);
        /*获取头像*/
        updateAvatar(CachePreferences.getUser().getHead_Image());
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions,ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PersonInfoAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @NonNull
    @Override
    public PersonInfoPresenter createPresenter() {
        return new PersonInfoPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*从修改昵称页面返回后刷新数据*/
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }

    /*从CachePreferences获取用户数据*/
    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow(str_username, user.getName()));
        list.add(new ItemShow(str_nickname, user.getNick_Name()));
        list.add(new ItemShow(str_hx_id, user.getHx_Id()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    /*用户信息的单行点击事件*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                activityUtils.showToast(str_username_update);
                break;
            case 1:
                activityUtils.startActivity(NickNameActivity.class);
                break;
            case 2:
                activityUtils.showToast(str_id_update);
                break;
        }
    }

    /*头像的点击事件,对头像弹窗的控制*/
    @OnClick({R.id.iv_user_head, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                if (picWindow == null) {
                    picWindow = new PicWindow(this, listener);
                }
                if (picWindow.isShowing()) {
                    picWindow.dismiss();
                    return;
                }
                picWindow.show();
                break;
            case R.id.btn_login_out:
                HxUserManager.getInstance().asyncLogout();
                 /*登出关掉通知栏中的通知*/
                EaseUI.getInstance()
                        .getNotifier()
                        .reset();
                CachePreferences.clearAllData();
                Intent intent = new Intent(PersonInfoActivity.this, MainActivity.class);
                /*清除所有旧的Activity*/
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 帮助我们去处理结果(剪切完的图像)
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    /*图片裁剪*/
    private final CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            presenter.uploadAvatar(file);
        }

        @Override
        public void onCropCancel() {
        }

        @Override
        public void onCropFailed(String message) {
        }

        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return PersonInfoActivity.this;
        }
    };

    /*头像弹窗内的监听事件*/
    private PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            /*从相册选择*/
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            /*从相机选择*/
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    @Override
    public void updateAvatar(String url) {
        /*设置头像*/
        ImageLoader.getInstance()
                .displayImage(EasyShopApi.IMAGE_URL + url, iv_user_head, AvatarLoadOptions.build());
    }

    @Override
    public void showProgress() {
        if (progressDialogFragment == null) {
            progressDialogFragment = new ProgressDialogFragment();
        }
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
