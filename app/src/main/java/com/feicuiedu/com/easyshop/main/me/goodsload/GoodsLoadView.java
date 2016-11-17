package com.feicuiedu.com.easyshop.main.me.goodsload;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface GoodsLoadView extends MvpView {

    /*上传成功*/
    void upLoadSuccess();

    void showProgress();

    void hideProgress();

    void showMessage(String msg);
}
