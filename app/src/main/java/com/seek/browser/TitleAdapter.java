package com.seek.browser;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class TitleAdapter extends BaseAdapter {

    private List<String> titles;
    private Context context;
    public TextView[] tv_titles;
    int position = 0;

    public TitleAdapter(List<String> titles, Context context,int position) {
        this.titles = titles;
        this.context = context;
        tv_titles = new TextView[titles.size()];
        this.position = position;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**曾用来在选中标题后改变字体颜色，但发现标题第一项
     * 不能响应，暂时未找到解决办法，所以用了其他方式，
     * 这里就注释掉了
     public void setFocus(int position) {
     for (int i = 0; i < getCount(); i++) {
     if(i == position){
     tv_titles[i].setTextColor(Color.WHITE);
     }else{
     tv_titles[i].setTextColor(Color.GRAY);
     }
     }
     }
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * 动态添加标题textView控件，并设置布局属性
         */
        tv_titles[position] = new TextView(context);

        /**
         * 设置textView中的字居中
         */
        tv_titles[position].setGravity(Gravity.CENTER);

        tv_titles[position].setText(titles.get(position));

        tv_titles[position].setTextSize(20);

        /**
         * 设置TextView的大小
         */
        tv_titles[position].setLayoutParams(new GridView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_titles[position].setPadding(0, 20, 0, 10);

        /**
         * 在选中某一标题后，重新声明adapter对象，通过构造函数给的position
         * 确定把哪个标题的字体颜色直接初始化
         */
        if(position == this.position){
            tv_titles[position].setTextColor(Color.WHITE);
        }else{
            tv_titles[position].setTextColor(Color.GRAY);
        }
        return tv_titles[position];
    }

}
