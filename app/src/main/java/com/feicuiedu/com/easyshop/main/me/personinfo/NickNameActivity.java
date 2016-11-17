package com.feicuiedu.com.easyshop.main.me.personinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.feicuiedu.apphx.model.repository.DefaultLocalUsersRepo;
import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.commons.ActivityUtils;
import com.feicuiedu.com.easyshop.commons.RegexUtils;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.CurrentUser;
import com.feicuiedu.com.easyshop.model.User;
import com.feicuiedu.com.easyshop.model.UserResult;
import com.feicuiedu.com.easyshop.network.EasyShopClient;
import com.feicuiedu.com.easyshop.network.UICallback;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 个人信息昵称修改的页面
 */
public class NickNameActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_nickname)
    EditText et_nickname;

    private ActivityUtils activityUtils;
    private String str_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_nickname);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions,ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        str_nickname = et_nickname.getText().toString();
        if (RegexUtils.verifyNickname(str_nickname) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.nickname_rules);
            activityUtils.showToast(msg);
            return;
        }
        init();
    }

    /*昵称修改的*/
    private void init() {
        final User user = CachePreferences.getUser();
        user.setNick_Name(str_nickname);
        Call call = EasyShopClient.getInstance().uploadUser(user);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                activityUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body, UserResult.class);

                if (userResult == null) {
                    activityUtils.showToast("未知错误");
                    return;
                } else if (userResult.getCode() != 1) {
                    activityUtils.showToast(userResult.getMessage());
                    return;
                }

                DefaultLocalUsersRepo.getInstance(NickNameActivity.this)
                        .save(CurrentUser.convert(userResult.getData()));
                CachePreferences.setUser(userResult.getData());
                activityUtils.showToast("修改成功");
                onBackPressed();

            }
        });
    }

}
