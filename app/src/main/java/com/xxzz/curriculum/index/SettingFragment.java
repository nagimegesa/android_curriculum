package com.xxzz.curriculum.index;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.Config;
import com.xxzz.curriculum.R;


public class SettingFragment extends Fragment {
    static private SettingFragment fragment;
    private final Context parent;
    int flag = 3;   // spinner -- 默认字号为20 flag=3
    private View view;
    private Spinner readFontSize;
    private SwitchCompat nightStatus, musicStatus, readDirection;
    private Button bookMark, collection;

    public SettingFragment(Context parent) {
        this.parent = parent;
    }

    /**
     * 单例模式, 写的时候只能用这个得到 SettingFragment 的实例, 不要直接调用 new
     *
     * @return SettingFragment实例
     */
    public static SettingFragment getInstance(Context context) {
        if (fragment == null)
            fragment = new SettingFragment(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Config.getInstance().readSettingConfig(parent);
        setUI();
    }

    private void setUI() {
        flag = Config.getInstance().getSpinnerPlace(readFontSize, getActivity());
        readFontSize.setSelection(flag);
        nightStatus.setChecked(Config.config.isNightStatus());
//        musicStatus.setChecked(Config.config.isMusicStatus());
        readDirection.setChecked(Config.getInstance().isVerticalRead());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initSettingFrag();
        readFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Config.getInstance().setReadFontSize(Float.parseFloat(readFontSize.getSelectedItem().toString()), getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("fontSize", "NothingSelected");
            }
        });
//        musicStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                Config.getInstance().setMusicStatus(isChecked, getActivity());
//            }
//        });

        nightStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.getInstance().switchNightMode(getActivityFromView(), isChecked);
            }
        });
        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookMarkActivity.class);
                startActivity(intent);
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookCollectionActivity.class);
                startActivity(intent);
            }
        });

        readDirection.setOnCheckedChangeListener((b, checked) -> {
            Config.getInstance().setVerticalRead(checked, getActivity());
        });
        return view;
    }

    public Activity getActivityFromView() {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    public void initSettingFrag() {
        readFontSize = view.findViewById(R.id.sp_setFontSize);
        nightStatus = view.findViewById(R.id.sc_night_switch);
//        musicStatus = view.findViewById(R.id.sc_music_switch);
        bookMark = view.findViewById(R.id.bt_bookmark);
        collection = view.findViewById(R.id.bt_collection);
        readDirection = view.findViewById(R.id.sc_swipe_mode);
    }

}



