package cn.fayne.logindemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.fayne.logindemo.R;
import cn.fayne.logindemo.bean.Comment;

/**
 * Created by fan on 18-4-10.
 */

public class AdapterComment extends BaseAdapter{

    Context mContext;
    List<Comment> mData;
    public AdapterComment(Context context, List<Comment> data) {
        mContext = context;
        mData = data;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Comment> getmData() {
        return mData;
    }

    public void setmData(List<Comment> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
            holder.comment_name = view.findViewById(R.id.comment_name);
            holder.comment_content = view.findViewById(R.id.comment_content);
            holder.comment_time = view.findViewById(R.id.comment_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.comment_name.setText(mData.get(i).getmName());
        holder.comment_content.setText(mData.get(i).getmContent());
        holder.comment_time.setText(mData.get(i).getmTime());
        return view;
    }

    public void addComment(Comment comment) {
        mData.add(comment);
        notifyDataSetChanged();
    }
    private static class ViewHolder {
        public TextView comment_name;
        public TextView comment_content;
        public TextView comment_time;
    }
}
