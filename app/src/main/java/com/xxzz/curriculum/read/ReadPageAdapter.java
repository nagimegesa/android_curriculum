package com.xxzz.curriculum.read;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class ReadPageAdapter extends PagerAdapter {
    private final List<View> views;
    ReadPageAdapter(List<View> views) {
       this.views = views;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    //滑动切换的时候销毁当前的组件
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,
                            @NonNull Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    //将当前视图添加到container中并返回当前View视图
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
