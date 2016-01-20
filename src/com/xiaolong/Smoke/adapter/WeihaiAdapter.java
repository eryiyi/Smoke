package com.xiaolong.Smoke.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import com.xiaolong.Smoke.module.Weihai;
import com.xiaolong.Smoke.util.StringUtil;

import java.util.List;

/**
 */
public class WeihaiAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Weihai> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public WeihaiAdapter(List<Weihai> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_weihai_adapter, null);
            holder.img_one = (ImageView) convertView.findViewById(R.id.img_one);
            holder.content_one = (TextView) convertView.findViewById(R.id.content_one);
            holder.item_doc = (TextView) convertView.findViewById(R.id.item_doc);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.click_one = (TextView) convertView.findViewById(R.id.click_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Weihai cell = lists.get(position);
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getsImage(), holder.img_one, SmokeApplication.txOptions, animateFirstListener);
        holder.item_title.setText(cell.getsTitle()==null?"":cell.getsTitle());
        String cont = cell.getsContent()==null?"":cell.getsContent();
        cont = StringUtil.delHTMLTag(cont);
        if(cont.length() > 30){
            cont = cont.substring(0, 29);
        }
        holder.content_one.setText(Html.fromHtml(cont)+"...");
        holder.click_one.setText(cell.getnHit()==null?"0":cell.getnHit());
        holder.item_doc.setText(String.valueOf(position+1));
        return convertView;
    }

    class ViewHolder {
        ImageView img_one;
        TextView item_doc;
        TextView item_title;
        TextView content_one;
        TextView click_one;
    }

}