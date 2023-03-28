package com.xxzz.curriculum.index;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzz.curriculum.R;

public class IndexFragment extends Fragment {
    private static IndexFragment fragment;
    public IndexFragment() {
    }

    /**
     *  单例模式, 写的时候只能用这个得到 IndexFragment 的实例, 不要直接调用 new
     * @return IndexFragment实例
     */
    public static IndexFragment getInstance() {
        if(fragment == null)
            fragment = new IndexFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }
}