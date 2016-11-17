package com.feicuiedu.com.easyshop.commons;

public class Preconditions {

    public static void checkNonNull(Object object, String info){
        if (object == null){
            throw new RuntimeException("CheckNonNull fail: "+ info);
        }
    }
}
