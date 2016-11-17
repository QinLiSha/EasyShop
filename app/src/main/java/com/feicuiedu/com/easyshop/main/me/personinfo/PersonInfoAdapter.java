package com.feicuiedu.com.easyshop.main.me.personinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.model.ItemShow;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonInfoAdapter extends BaseAdapter {

    private ArrayList<ItemShow> list = new ArrayList<>();

    public PersonInfoAdapter(ArrayList<ItemShow> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_info, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_name.setText(list.get(position).getItem_name());
        viewHolder.tv_person.setText(list.get(position).getUser_name());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_item_name)
        TextView tv_item_name;
        @Bind(R.id.tv_person)
        TextView tv_person;

        public ViewHolder(View viewItem) {
            ButterKnife.bind(this, viewItem);
        }
    }

}
