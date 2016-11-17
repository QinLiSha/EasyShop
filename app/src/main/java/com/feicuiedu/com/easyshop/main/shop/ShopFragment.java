package com.feicuiedu.com.easyshop.main.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.commons.ActivityUtils;
import com.feicuiedu.com.easyshop.main.details.GoodsDetailActivity;
import com.feicuiedu.com.easyshop.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商城展示的Fragment
 */
public class ShopFragment extends MvpFragment<ShopView, ShopPresenter>
        implements ShopView{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_load_error)
    TextView tv_load_error;
    @Bind(R.id.refreshLayout)
    PtrClassicFrameLayout ptrLayout;
    @BindString(R.string.load_more_end)
    String load_more_end;
    @BindString(R.string.refresh_more_end)
    String refresh_more_end;

    private ActivityUtils activityUtils;
    private GoodsAdapter goodsAdapter;
    /**
     * 获取商品时,商品的类型,获取全部商品时为空
     */
    private String pageType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        goodsAdapter = new GoodsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        recyclerView.setAdapter(goodsAdapter);
    }

    /*RecyclerView和PtrClassicFrameLayout的初始化*/
    private void initView() {
        /*设置recyclerView的manager*/
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        /*设置refreshLayout*/
        /*使用本对象作为key，来记录上一次刷新时间，如果两次下拉间隔太近，不会触发刷新方法*/
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setBackgroundResource(R.color.recycler_bg);
        /*关闭Header所耗时长*/
        ptrLayout.setDurationToCloseHeader(1500);
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
        /*商城页,商品的单击事件*/
        goodsAdapter.setListener(new GoodsAdapter.OnItemClickedListener() {
            @Override
            public void onPhotoClicked(GoodsInfo goodsInfo) {
                Intent intent = GoodsDetailActivity.getStartIntent(getContext(), goodsInfo.getUuid(), 0);
                startActivity(intent);
            }
        });
    }

    /*点击错误视图时刷新数据*/
    @OnClick(R.id.tv_load_error)
    public void onClick() {
        ptrLayout.autoRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*当前页面没有数据时,刷新*/
        if (goodsAdapter.getItemCount() == 0) {
            ptrLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrLayout.autoRefresh();
                }
            }, 200);
        }
    }

    @Override
    public void showRefresh() {
        tv_load_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRefreshError(String msg) {
        ptrLayout.refreshComplete();
        if (goodsAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(refresh_more_end);
        ptrLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        ptrLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tv_load_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        ptrLayout.refreshComplete();
        if (goodsAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(load_more_end);
        ptrLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        ptrLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        goodsAdapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        goodsAdapter.clear();
        if (data != null) {
            goodsAdapter.addData(data);
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

}