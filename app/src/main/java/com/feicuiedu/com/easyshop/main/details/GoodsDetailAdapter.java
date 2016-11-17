package com.feicuiedu.com.easyshop.main.details;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class GoodsDetailAdapter extends PagerAdapter {

    private ArrayList<ImageView> list;
    private OnItemClickListener listener;

    public GoodsDetailAdapter(ArrayList<ImageView> list) {
        this.list = list;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = list.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick();
            }
        });
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * ViewPager中Item的单击事件
     */
    public interface OnItemClickListener {
        void onItemClick();
    }
}
