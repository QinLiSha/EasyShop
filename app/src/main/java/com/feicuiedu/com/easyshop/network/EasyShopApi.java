package com.feicuiedu.com.easyshop.network;

/**
 * 此接口中包含了易淘的所有网络连接路径
 */
public interface EasyShopApi {

    String BASE_URL = "http://wx.feicuiedu.com:9094/yitao/";
    /*图片的基路径*/
    String IMAGE_URL = "http://wx.feicuiedu.com:9094/";

    String LOGIN = "UserWeb?method=login";
    String REGISTER = "UserWeb?method=register";
    String UPDATE = "UserWeb?method=update";
    String GET_NAMES = "UserWeb?method=getNames";
    String GET_USER = "UserWeb?method=getUsers";


    String ALL_GOODS = "GoodsServlet?method=getAll";
    String ADD = "GoodsServlet?method=add";
    String DETAIL = "GoodsServlet?method=view";
    String DELETE = "GoodsServlet?method=delete";

}

