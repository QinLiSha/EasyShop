package com.feicuiedu.com.easyshop;

import com.feicuiedu.com.easyshop.network.EasyShopClient;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @BeforeClass
    public static void initTimber(){
        Timber.plant(new Timber.Tree() {
            @Override protected void log(int priority, String tag, String message, Throwable t) {
                System.out.println( message);
            }
        });
    }

    @Test
    public void getUsers() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("yt1cd3ce4132a746db8a1a8f1c0b34f907");

        Timber.d(list.toString());

        Call call = EasyShopClient.getInstance().getUsers(list);
        Response response = call.execute();
        Timber.d(response.body().string());
    }

    @Test
    public void getSearchUser() throws IOException {
        Call call = EasyShopClient.getInstance().getSearchUser("任祥");
        Response response = call.execute();
        Timber.d(response.body().string());
    }
}