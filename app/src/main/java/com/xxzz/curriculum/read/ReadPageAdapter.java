package com.xxzz.curriculum.read;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xxzz.curriculum.R;

import java.io.IOException;

public class ReadPageAdapter extends RecyclerView.Adapter<ReadPageAdapter.Holder> {
    private final BookReader reader;

    ReadPageAdapter(BookReader reader) {
        this.reader = reader;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_read_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.read_main_img);
        TextView textView = holder.itemView.findViewById(R.id.read_main_text);
        Pages pages = null;
        try {
            pages = reader.getIndexPage(position + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageView.setImageBitmap(pages.getMap());
        textView.setText(pages.getText());
    }

    @Override
    public int getItemCount() {
        return reader.getBook().getPages();
    }

    class Holder extends RecyclerView.ViewHolder {
        @SuppressLint("CutPasteId")
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}