package com.feicuiedu.com.easyshop.main.me.persongoods;

import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.main.shop.ShopView;
import com.feicuiedu.com.easyshop.model.GoodsResult;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;


public class PersonGoodsPresenter extends MvpNullObjectBasePresenter<ShopView> {

    /**
     * 获取商品时,分页下标
     */
    private int pageInt = 1;

    private Call call;

    public void refreshData(String type) {
        getView().showRefresh();
        call = EasyShopClient.getInstance().getPersonData(1, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getData().size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(goodsResult.getData());
                            getView().hideRefresh();
                        }
                        pageInt = 2;
                        break;
                    default:
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }

    public void loadData(String type) {
        getView().showLoadMoreLoading();
        if (pageInt == 0) pageInt = 1;
        call = EasyShopClient.getInstance().getPersonData(pageInt, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getData().size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
                            getView().addMoreData(goodsResult.getData());
                            getView().hideLoadMore();
                        }
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
}
