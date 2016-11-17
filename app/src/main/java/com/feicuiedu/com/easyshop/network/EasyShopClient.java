package com.feicuiedu.com.easyshop.network;

import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.GoodsLoad;
import com.feicuiedu.com.easyshop.model.User;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class EasyShopClient {

    private OkHttpClient okHttpClient = null;
    private final Gson gson;

    private EasyShopClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)/*添加拦截器，打印请求和响应的日志*/
                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
                .writeTimeout(10, TimeUnit.SECONDS)    // Socket写超时
                .readTimeout(30, TimeUnit.SECONDS)     // Socket读超时
                .build();
        gson = new Gson();
    }

    private static EasyShopClient mInstance;

    public static synchronized EasyShopClient getInstance() {
        if (mInstance == null) {
            mInstance = new EasyShopClient();
        }
        return mInstance;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public Call login(String username, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.LOGIN)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     */
    public Call register(String username, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.REGISTER)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 更新用户头像
     *
     * @param file 要更新的头像文件
     */
    public Call uploadAvatar(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", gson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATE)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 修改用户昵称
     *
     * @param user 用户实体类
     */
    public Call uploadUser(User user) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", gson.toJson(user))
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATE)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 获取商品数据
     *
     * @param pageNo 商品分页
     * @param type   商品类型
     */
    public Call getData(int pageNo, String type) {
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("type", type)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.ALL_GOODS)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 获取个人商品数据
     *
     * @param pageNo 商品分页
     * @param type   商品类型
     * @param master 商品发布者
     */
    public Call getPersonData(int pageNo, String type, String master) {
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("type", type)
                .add("master", master)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.ALL_GOODS)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }


    /**
     * 获取商品详情
     *
     * @param goodsUuid 商品表中的商品主键
     */
    public Call getGoodsData(String goodsUuid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid", goodsUuid)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.DETAIL)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    /**
     * 删除商品
     *
     * @param goodsUuid 商品表中的商品主键
     */
    public Call deleteGoods(String goodsUuid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid", goodsUuid)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.DELETE)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    /**
     * 商品上传
     *
     * @param goodsLoad 商品上传时对应的实体类
     * @param files     商品图片
     */
    public Call upload(GoodsLoad goodsLoad, ArrayList<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("good", gson.toJson(goodsLoad));
        for (File file : files) {
            builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.ADD)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 查找好友
     * 根据昵称获取用户信息
     *
     * @param nickname 用户昵称
     */
    public Call getSearchUser(String nickname) {
        RequestBody requestBody = new FormBody.Builder()
                .add("nickname", nickname)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GET_USER)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 获取好友列表
     * 根据环信ID数组获取用户信息
     *
     * @param ids 环信ID数组
     */
    public Call getUsers(List<String> ids) {
        String names = ids.toString();
        /*清楚List转换后的String中空格*/
        names = names.replace(" ", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("name", names)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GET_NAMES)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }
}
