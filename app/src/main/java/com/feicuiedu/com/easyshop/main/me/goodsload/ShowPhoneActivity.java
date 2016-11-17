package com.feicuiedu.com.easyshop.main.me.goodsload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.feicuiedu.com.easyshop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 展示商品上传图片的详情页
 */
public class ShowPhoneActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_goods_phone)
    ImageView iv_goods_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_phone);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bitmap bitmap = getIntent().getParcelableExtra("images");
        int mWidth = getIntent().getIntExtra("width", 0);
        int mHeight = getIntent().getIntExtra("height", 0);

        iv_goods_phone.setMaxWidth(mWidth);
        iv_goods_phone.setMaxHeight(mHeight);
        iv_goods_phone.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
