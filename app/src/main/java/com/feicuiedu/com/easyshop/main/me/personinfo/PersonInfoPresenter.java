package com.feicuiedu.com.easyshop.main.me.personinfo;

import com.feicuiedu.apphx.model.HxMessageManager;
import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.UserResult;
import com.feicuiedu.com.easyshop.model.User;
import com.feicuiedu.com.easyshop.network.EasyShopApi;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class PersonInfoPresenter extends MvpNullObjectBasePresenter<PersonInfoView> {

    private okhttp3.Call call;

    /*上传头像*/
    public void uploadAvatar(File file) {
        getView().showProgress();
        call = EasyShopClient.getInstance().uploadAvatar(file);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hideProgress();
                UserResult userResult = new Gson().fromJson(body, UserResult.class);
                if (userResult == null) {
                    getView().showMessage("未知错误");
                    return;
                } else if (userResult.getCode() != 1) {
                    getView().showMessage(userResult.getMessage());
                    return;
                }
                User user = userResult.getData();
                CachePreferences.setUser(user);
                getView().updateAvatar(userResult.getData().getHead_Image());
                /*环信更新用户头像*/
                HxUserManager.getInstance()
                        .updateAvatar(EasyShopApi.IMAGE_URL + userResult.getData().getHead_Image());
                HxMessageManager.getInstance()
                        .sendAvatarUpdateMessage(EasyShopApi.IMAGE_URL + userResult.getData().getHead_Image());
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
}
