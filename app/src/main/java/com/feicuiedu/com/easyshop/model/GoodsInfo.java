package com.feicuiedu.com.easyshop.model;

import java.io.Serializable;

/**
 * 商品的详情类
 */
@SuppressWarnings("unused")
public class GoodsInfo implements Serializable {

    private String id;
    /*商品价格*/
    private String price;
    /*商品图片URL*/
    private String page;
    /*商品名称*/
    private String name;
    /*商品在商品表中的主键*/
    private String uuid;
    /*商品类型*/
    private String type;
    /*商品的发布者*/
    private String master;

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getPage() {
        return page;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getMaster() {
        return master;
    }
}

