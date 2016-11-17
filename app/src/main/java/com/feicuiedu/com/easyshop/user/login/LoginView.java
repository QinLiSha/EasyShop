package com.feicuiedu.com.easyshop.user.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginView extends MvpView {

    void showProgress();

    void hideProgress();

    /*登录失败*/
    void loginFailed();

    /*登录成功*/
    void loginSussed();

    void showMessage(String msg);

    /*用户名和密码不符合要求*/
    void showUserPasswordError(String msg);
}
