package com.manderfield.knowyourgovernment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView textView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textView = findViewById(R.id.CivicInfo);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}