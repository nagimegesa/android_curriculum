package com.xxzz.curriculum;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static void makeToast(Context context, String message, int time) {
        Toast.makeText(context, message, time).show();
    }

    static public String readAllFile(Path path) throws IOException {
        BufferedInputStream inputStream =
                new BufferedInputStream(Files.newInputStream(path.toAbsolutePath()));
        InputStreamReader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
