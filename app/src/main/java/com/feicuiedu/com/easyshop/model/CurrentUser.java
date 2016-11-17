package com.feicuiedu.com.easyshop.model;

import com.feicuiedu.com.easyshop.network.EasyShopApi;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 将User转换为EaseUser
 */
public class CurrentUser {

    private CurrentUser() {
    }

    private static List<String> list;

    public static void setList(List<String> list) {
        CurrentUser.list = list;
    }

    public static List<String> getList() {
        return list;
    }

    public static List<EaseUser> convertAll(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        ArrayList<EaseUser> easeUsers = new ArrayList<>();
        for (User user : users) {
            easeUsers.add(convert(user));
        }
        return easeUsers;
    }

    public static EaseUser convert(User user) {
        EaseUser easeUser = new EaseUser(user.getHx_Id());
        easeUser.setNick(user.getNick_Name());
        easeUser.setAvatar(EasyShopApi.IMAGE_URL + user.getHead_Image());
        EaseCommonUtils.setUserInitialLetter(easeUser);
        return easeUser;
    }
}
