package com.feicuiedu.com.easyshop.main.shop;

import com.feicuiedu.com.easyshop.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

public interface ShopView extends MvpView {

    /**
     * 数据刷新 --刷新中
     */
    void showRefresh();

    /**
     * 数据刷新 --刷新出错
     */
    void showRefreshError(String msg);

    /**
     * 数据刷新 --刷新结束
     */
    void showRefreshEnd();

    /**
     * 数据刷新 --隐藏下拉刷新视图
     */
    void hideRefresh();

    /**
     * 加载更多 -- 加载中
     */
    void showLoadMoreLoading();

    /**
     * 加载更多 -- 加载发生错误
     */
    void showLoadMoreError(String msg);

    /**
     * 加载更多 -- 没有更多数据
     */
    void showLoadMoreEnd();

    /**
     * 隐藏加载更多的视图
     */
    void hideLoadMore();

    /**
     * 添加更多数据
     */
    void addMoreData(List<GoodsInfo> data);

    /**
     * 添加刷新更多的数据
     */
    void addRefreshData(List<GoodsInfo> data);

    /**
     * 消息提示
     */
    void showMessage(String msg);
}
