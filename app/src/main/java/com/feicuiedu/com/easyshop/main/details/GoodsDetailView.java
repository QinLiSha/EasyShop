package com.feicuiedu.com.easyshop.main.details;


import com.feicuiedu.com.easyshop.model.GoodsDetail;
import com.feicuiedu.com.easyshop.model.User;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

public interface GoodsDetailView extends MvpView {

    void showProgress();

    void hideProgress();

    /*设置图片路径*/
    void setImageData(ArrayList<String> viewList);

    /*设置商品信息*/
    void setData(GoodsDetail data, User goods_user);

    /*商品不存在了*/
    void showError();

    void showMessage(String msg);

    /*删除商品*/
    void deleteEnd();
}
