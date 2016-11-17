package com.feicuiedu.com.easyshop.main.me.goodsload;

import com.feicuiedu.com.easyshop.commons.LogUtils;
import com.feicuiedu.com.easyshop.commons.MyFileUtils;
import com.feicuiedu.com.easyshop.model.GoodsLoad;
import com.feicuiedu.com.easyshop.model.ImageItem;
import com.feicuiedu.com.easyshop.model.GoodsLoadResult;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class GoodsLoadPresenter extends MvpNullObjectBasePresenter<GoodsLoadView> {

    private Call call;

    /*商品上传*/
    public void upload(GoodsLoad goodsLoad, List<ImageItem> list) {
        getView().showProgress();
        call = EasyShopClient.getInstance().upload(goodsLoad, getFiles(list));
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                LogUtils.i("错误结果：" + e.getMessage());
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseInUi(okhttp3.Call call, String body) {
                getView().hideProgress();
                LogUtils.i("上传结果：" + body);
                GoodsLoadResult result = new Gson().fromJson(body, GoodsLoadResult.class);
                getView().showMessage(result.getMessage());
                if (result.getCode() == 1)
                    getView().upLoadSuccess();
            }
        });
    }

    /**
     * 根据ImageItem获取图片文件
     *
     * @param list {@link ImageItem} 的列表
     */
    private ArrayList<File> getFiles(List<ImageItem> list) {
        ArrayList<File> files = new ArrayList<>();
        for (ImageItem imageItem : list) {
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
}
