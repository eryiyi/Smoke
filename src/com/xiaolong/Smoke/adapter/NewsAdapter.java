package com.xiaolong.Smoke.adapter;

import android.content.Context;
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
import com.xiaolong.Smoke.module.News;
import com.xiaolong.Smoke.util.StringUtil;

import java.util.List;

/**
 */
public class NewsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<News> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public NewsAdapter(List<News> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_adapter, null);
            holder.img_one = (ImageView) convertView.findViewById(R.id.img_one);
            holder.content_one = (TextView) convertView.findViewById(R.id.content_one);
            holder.click_one = (TextView) convertView.findViewById(R.id.click_one);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        News cell = lists.get(position);
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getsImage(), holder.img_one, SmokeApplication.newsOptions, animateFirstListener);
        String cont = cell.getsContent()==null?"":cell.getsContent();
        cont = StringUtil.delHTMLTag(cont);
        if(cont.length() > 30){
            cont = cont.substring(0, 29);
        }
        holder.title.setText(Html.fromHtml(cell.getsTitle()==null?"":cell.getsTitle()));
        holder.content_one.setText(Html.fromHtml(cont ) + "...");
        holder.click_one.setText(cell.getnHit()==null?"0":cell.getnHit());
        return convertView;
    }

    class ViewHolder {
        ImageView img_one;
        TextView title;
        TextView content_one;
        TextView click_one;
    }

}