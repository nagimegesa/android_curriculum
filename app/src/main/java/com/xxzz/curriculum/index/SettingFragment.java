package com.xxzz.curriculum.index;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.Config;
import com.xxzz.curriculum.R;

public class SettingFragment extends Fragment {
    static private SettingFragment fragment;
    int flag = 3;   // spinner -- 默认字号为20 flag=3
    private View view;
    private Spinner readFontSize;
    private SwitchCompat nightStatus, musicStatus;
    private TextView bookMark;
    private Context parent;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

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
        Config.getInstance().readSettingConfig(parent);
        dbHelper = new DBHelper(parent);
        db = dbHelper.getWritableDatabase();
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
        musicStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.getInstance().setMusicStatus(isChecked, getActivity());
            }
        });

        //
        nightStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.getInstance().setNightStatus(isChecked, getActivity());
                if(((AppCompatActivity)getActivity()).getDelegate().getLocalNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //?????
                }
            }
        });
        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookMarkActivity.class);
                startActivity(intent);
            }
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

    public void initSettingFrag(){
        readFontSize = view.findViewById(R.id.sp_setFontSize);
        nightStatus = view.findViewById(R.id.sc_night_switch);
        musicStatus = view.findViewById(R.id.sc_music_switch);
        bookMark = view.findViewById(R.id.tv_click_bookmark);
        flag = Config.getInstance().getSpinnerPlace(readFontSize,getActivity());
        readFontSize.setSelection(flag);
        nightStatus.setChecked(Config.config.isNightStatus());
        musicStatus.setChecked(Config.config.isMusicStatus());
    }
}



