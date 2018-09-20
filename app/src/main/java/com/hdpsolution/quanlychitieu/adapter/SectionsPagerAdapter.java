package com.hdpsolution.quanlychitieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdpsolution.quanlychitieu.fragment.FragmentKhoanChi;
import com.hdpsolution.quanlychitieu.fragment.FragmentKhoanthu;
import com.hdpsolution.quanlychitieu.fragment.FragmentThongKe;
import com.hdpsolution.quanlychitieu.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentKhoanthu();
            case 1:
                return new FragmentKhoanChi();
            case 2:
                return new FragmentThongKe();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    @SuppressLint("ResourceAsColor")
    public View getTabView(int position) {
         String tabTitles[] = new String[] { mContext.getString(R.string.tab_profit), mContext.getString(R.string.tab_spend),mContext.getString(R.string.tab_statistical) };
         int[] imageResId = { R.drawable.salary, R.drawable.low_sales_,R.drawable.profit };
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        tv.setTextColor(mContext.getResources().getColor(R.color.colotexWhite));
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        return v;
    }
}
