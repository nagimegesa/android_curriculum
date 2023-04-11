package com.xxzz.curriculum.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.R;

public class SettingFragment extends Fragment {

    static private SettingFragment fragment;

    public SettingFragment() {
    }

    /**
     * 单例模式, 写的时候只能用这个得到 SettingFragment 的实例, 不要直接调用 new
     *
     * @return SettingFragment实例
     */
    public static SettingFragment getInstance() {
        if (fragment == null)
            fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}