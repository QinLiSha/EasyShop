package com.feicuiedu.com.easyshop.model;

/**
 * 个人信息页面展示的实体类
 */
public class ItemShow {

    /*单行布局的名称*/
    private String item_name;
    /*单行布局的内容*/
    private String user_name;

    public ItemShow(String item_name, String user_name) {
        this.item_name = item_name;
        this.user_name = user_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getUser_name() {
        return user_name;
    }
}
