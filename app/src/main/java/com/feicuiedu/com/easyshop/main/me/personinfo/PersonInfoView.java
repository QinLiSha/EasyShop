package com.feicuiedu.com.easyshop.main.me.personinfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface PersonInfoView extends MvpView {

    /**
     * 更新头像
     */
    void updateAvatar(String url);

    void showProgress();

    void hideProgress();

    void showMessage(String msg);
}
