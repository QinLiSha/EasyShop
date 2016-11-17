package com.feicuiedu.com.easyshop.user.register;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.CurrentUser;
import com.feicuiedu.com.easyshop.model.UserResult;
import com.feicuiedu.com.easyshop.model.User;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.hyphenate.easeui.domain.EaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.Call;

/**
 * 注册页面的逻辑实现
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private Call call = null;
    private String password;

    @Override
    public void attachView(RegisterView view) {
        super.attachView(view);
        EventBus.getDefault()
                .register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
         /*页面解绑时,取消网络请求*/
        if (call != null) call.cancel();
        EventBus.getDefault()
                .unregister(this);
    }

    public void register(final String userName, String pwd) {
        this.password = pwd;
        getView().showProgress();
        call = EasyShopClient.getInstance().register(userName, pwd);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                password = null;
                getView().hideProgress();
                getView().showMessage(e.getMessage());
                CachePreferences.clearAllData();
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body, UserResult.class);
                if (userResult.getCode() == 1) {
                    //*还需要登录环信*//*
                    getView().showMessage("注册成功！");
                    User user = userResult.getData();
                    CachePreferences.setUser(user);

                    EaseUser easeUser = CurrentUser.convert(user);
                    HxUserManager.getInstance().asyncLogin(easeUser, password);
                } else if (userResult.getCode() == 2) {
                    CachePreferences.clearAllData();
                    getView().hideProgress();
                    getView().showMessage(userResult.getMessage());
                    getView().registerFailed();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        // 判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;

        this.password = null;
        getView().registerSuccess();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        // 判断是否是登录失败事件
        if (event.type != HxEventType.LOGIN) return;

        this.password = null;
        getView().hideProgress();
        getView().showMessage(event.toString());
    }
}
