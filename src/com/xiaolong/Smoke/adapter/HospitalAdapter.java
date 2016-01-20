package com.xiaolong.Smoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.SmokeApplication;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.module.DocObj;

import java.util.List;

/**
 */
public class HospitalAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<DocObj> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public HospitalAdapter(List<DocObj> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hospital_adapter, null);
            holder.img_one = (ImageView) convertView.findViewById(R.id.img_one);
            holder.content_one = (TextView) convertView.findViewById(R.id.content_one);
            holder.item_doc = (TextView) convertView.findViewById(R.id.item_doc);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.one = (TextView) convertView.findViewById(R.id.one);
            holder.two = (TextView) convertView.findViewById(R.id.two);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DocObj cell = lists.get(position);
        imageLoader.displayImage(InternetURL.INTERNAL_PIC +cell.getsImage(), holder.img_one, SmokeApplication.txOptions, animateFirstListener);
        holder.item_title.setText(cell.getsName() == null ? "" : cell.getsName());
        holder.content_one.setText(cell.getsAddress()==null?"":cell.getsAddress());
        holder.item_doc.setText(String.valueOf(position+1));
        holder.one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, "000");
            }
        });
        holder.two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 2, "000");
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_one;
        TextView item_doc;
        TextView item_title;
        TextView content_one;
        TextView one;
        TextView two;
    }

}