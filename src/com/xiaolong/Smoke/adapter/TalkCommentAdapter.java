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
import com.xiaolong.Smoke.module.Reply_data;
import com.xiaolong.Smoke.util.RelativeDateFormat;

import java.util.List;

/**
 */
public class TalkCommentAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Reply_data> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public TalkCommentAdapter(List<Reply_data> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_adapter, null);
            holder.content_one = (TextView) convertView.findViewById(R.id.content_one);
            holder.floor = (TextView) convertView.findViewById(R.id.floor);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reply_data cell = lists.get(position);
        holder.content_one.setText(cell.getsContent()==null?"":cell.getsContent());
        holder.item_title.setText(cell.getsNickName()==null?"":cell.getsNickName());
//        holder.dateline.setText(cell.getnRegisterDate()==null?"":cell.getnRegisterDate());
        holder.dateline.setText(RelativeDateFormat.format(Long.parseLong((cell.getnRegisterDate() == null ? "" : cell.getnRegisterDate()) + "000")));
        holder.floor.setText(String.valueOf(position+1));
        return convertView;
    }

    class ViewHolder {
        TextView floor;
        TextView item_title;
        TextView content_one;
        TextView dateline;
    }

}