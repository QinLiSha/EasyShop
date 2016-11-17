package com.feicuiedu.com.easyshop.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.feicuiedu.apphx.presentation.contact.list.HxContactListFragment;
import com.feicuiedu.apphx.presentation.conversation.HxConversationListFragment;
import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.commons.ActivityUtils;
import com.feicuiedu.com.easyshop.main.me.MeFragment;
import com.feicuiedu.com.easyshop.main.shop.ShopFragment;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.feicuiedu.com.easyshop.model.UserResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 易淘首页面
 */
public class MainActivity extends AppCompatActivity {

    @Bind({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] textViews;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    /*点击2次退出程序*/
    private boolean isExit = false;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        init();
    }

    /*进入页面数据初始化,默认显示为是市场页面*/
    private void init() {
        textViews[0].setSelected(true);
        if (CachePreferences.getUser().getName() == null) {
            viewPager.setAdapter(fragmentUnLoginAdapter);
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setAdapter(fragmentLoginAdapter);
            viewPager.setCurrentItem(0);
        }
        /*ViewPager的滑动事件*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (TextView textView : textViews) {
                    textView.setSelected(false);
                }
                tv_title.setText(textViews[position].getText());
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserResult result) {
        if (CachePreferences.getUser().getName() != null) {
            viewPager.setCurrentItem(0);
            viewPager.setAdapter(fragmentLoginAdapter);
        }
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    public void onClick(TextView view) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        view.setSelected(true);
        // 参数false代表瞬间切换，而不是平滑过渡
        viewPager.setCurrentItem((Integer) view.getTag(), false);
        tv_title.setText(textViews[(Integer) view.getTag()].getText());
    }


    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            activityUtils.showToast("再按一次退出程序");
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private FragmentPagerAdapter fragmentLoginAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new HxConversationListFragment();
                case 2:
                    return new HxContactListFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private FragmentStatePagerAdapter fragmentUnLoginAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new NoLoginFragment();
                case 2:
                    return new NoLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
}
