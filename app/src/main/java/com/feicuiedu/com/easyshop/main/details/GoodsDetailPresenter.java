package com.feicuiedu.com.easyshop.main.details;

import com.feicuiedu.com.easyshop.commons.LogUtils;
import com.feicuiedu.com.easyshop.model.GoodsDetail;
import com.feicuiedu.com.easyshop.model.GoodsDetailResult;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

public class GoodsDetailPresenter extends MvpNullObjectBasePresenter<GoodsDetailView> {

    private Call getDetailCall;
    private Call deleteCall;

    /*获取商品的详细数据*/
    public void getData(String uuid) {
        getView().showProgress();
        getDetailCall = EasyShopClient.getInstance().getGoodsData(uuid);
        getDetailCall.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.toString());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hideProgress();
                LogUtils.i("====商品详情====" + body);
                GoodsDetailResult goodsDetailResult = new Gson().
                        fromJson(body, GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1) {
                    GoodsDetail goodsDetail = goodsDetailResult.getDatas();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < goodsDetail.getPages().size(); i++) {
                        list.add(goodsDetail.getPages().get(i).getUri());
                    }
                    getView().setImageData(list);
                    getView().setData(goodsDetail, goodsDetailResult.getUser());
                } else {
                    getView().showError();
                }
            }
        });
    }

    /*删除商品*/
    public void delete(String uuid) {
        deleteCall = EasyShopClient.getInstance().deleteGoods(uuid);
        deleteCall.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.toString());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult goodsDetailResult = new Gson().
                        fromJson(body, GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1) {
                    getView().deleteEnd();
                    getView().showMessage("删除成功");
                }
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (getDetailCall != null) getDetailCall.cancel();
        if (deleteCall != null) deleteCall.cancel();
    }
}
