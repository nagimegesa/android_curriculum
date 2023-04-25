package com.xxzz.curriculum.read;

import android.widget.SeekBar;

public class SeekBarOnChange implements SeekBar.OnSeekBarChangeListener {
    private final SeekBarOnChangeCallBack callBack;

    public SeekBarOnChange(SeekBarOnChangeCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        callBack.onSeekBarChange(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public interface SeekBarOnChangeCallBack {
        default void onSeekBarChange(int process) {
        }
    }
}
