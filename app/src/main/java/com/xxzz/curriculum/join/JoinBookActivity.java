package com.xxzz.curriculum.join;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;

public class JoinBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_book);
        getLayoutInflater();
    }
}