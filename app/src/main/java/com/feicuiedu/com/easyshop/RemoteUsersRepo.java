package com.feicuiedu.com.easyshop;

import com.feicuiedu.apphx.model.repository.IRemoteUsersRepo;
import com.feicuiedu.com.easyshop.model.CurrentUser;
import com.feicuiedu.com.easyshop.model.GetUsersResult;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import timber.log.Timber;

/**
 * 查找好友
 * 获取好友列表
 */
public class RemoteUsersRepo implements IRemoteUsersRepo {

    @Override
    public List<EaseUser> queryByName(String username) throws Exception {
        Call call = EasyShopClient.getInstance().getSearchUser(username);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }
        String content = response.body().string();
        Timber.d(content);
        GetUsersResult result = new Gson().fromJson(content, GetUsersResult.class);

        return CurrentUser.convertAll(result.getDatas());
    }

    @Override
    public List<EaseUser> getUsers(List<String> ids) throws Exception {
        CurrentUser.setList(ids);
        /*将好友列表存起来*/
        Call call = EasyShopClient.getInstance().getUsers(ids);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }
        String content = response.body().string();
        Timber.d(content);
        GetUsersResult result = new Gson().fromJson(content, GetUsersResult.class);
        if (result.getCode() == 2) {
            throw new Exception(result.getMessage());
        }

        return CurrentUser.convertAll(result.getDatas());
    }
}
