package com.xiaolong.Smoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.module.TalkObj;
import com.xiaolong.Smoke.util.RelativeDateFormat;

import java.util.List;

/**
 */
public class TalkAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<TalkObj> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public TalkAdapter(List<TalkObj> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_talk_adapter, null);
            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.content_one = (TextView) convertView.findViewById(R.id.content_one);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.click_one = (TextView) convertView.findViewById(R.id.click_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TalkObj cell = lists.get(position);
        holder.item_title.setText(cell.getsTitle()==null?"":cell.getsTitle());
        if(cell.getsContent()!= null){
            String cont = cell.getsContent();
            if(cont.length() >30){
                cont = cont.substring(0,29);
            }
            holder.content_one.setText(cont + "...");
        }

        holder.click_one.setText(cell.getnHit()==null?"0":cell.getnHit());
        holder.item_name.setText(cell.getsNickName() ==null?"":cell.getsNickName());
        holder.item_dateline.setText(RelativeDateFormat.format(Long.parseLong((cell.getnRegisterDate() == null ? "" : cell.getnRegisterDate())+ "000" )));
        return convertView;
    }

    class ViewHolder {
        TextView item_title;
        TextView item_name;
        TextView content_one;
        TextView click_one;
        TextView item_dateline;
    }

}