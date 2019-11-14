package com.police.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ys.etv.R;
import com.police.entity.CallPhoneEntity;

import java.util.List;

/**
 * Created by jsjm on 2018/11/27.
 */

public class CallPhoneAdapter extends BaseAdapter {
    List<CallPhoneEntity> appInfos;
    Context context;
    LayoutInflater inflater;

    public CallPhoneAdapter(Context paramContext, List<CallPhoneEntity> paramList) {
        this.context = paramContext;
        this.appInfos = paramList;
        inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return this.context;
    }

    public int getCount() {
        return appInfos.size();
    }

    public Object getItem(int paramInt) {
        return this.appInfos.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(final int position, View convertView, ViewGroup paramViewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_bean_call, null);
            viewHolder.iv_bean_item = ((TextView) convertView.findViewById(R.id.iv_bean_item));
            viewHolder.tv_bean_item = ((TextView) convertView.findViewById(R.id.tv_bean_item));
            viewHolder.btn_item = ((Button) convertView.findViewById(R.id.btn_item));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CallPhoneEntity entty = appInfos.get(position);
        viewHolder.iv_bean_item.setText(entty.getDesc());
        viewHolder.tv_bean_item.setText(entty.getTitle());
        viewHolder.btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.clicktem(position, entty);
                }
            }
        });
        return convertView;
    }

    public void setOnAdapterItemClick(ItemClickAdapterListener listener) {
        this.listener = listener;
    }

    private class ViewHolder {
        TextView iv_bean_item;
        TextView tv_bean_item;
        Button btn_item;
    }

    ItemClickAdapterListener listener;


    public interface ItemClickAdapterListener {
        void clicktem(int position, CallPhoneEntity entity);
    }

}
