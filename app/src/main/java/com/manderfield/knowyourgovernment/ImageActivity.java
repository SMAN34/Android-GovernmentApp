package com.manderfield.knowyourgovernment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private TextView location;
    private TextView office;
    private TextView name;
    private ImageView photo;
    private Intent intent;
    private Officials official;
    private ImageView DemRep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        location = (TextView) findViewById(R.id.image_location);
        office = (TextView) findViewById(R.id.image_office);
        name = (TextView) findViewById(R.id.image_name);
        photo = (ImageView) findViewById(R.id.image_image);
        DemRep = (ImageView) findViewById(R.id.DemRepIm);

        intent = getIntent();
        official = (Officials) intent.getSerializableExtra("official");
        Log.d("after go to pa", location.getText().toString());
        CharSequence ch = intent.getCharSequenceExtra("location");

        location.setText(ch);
        office.setText(official.getOffice());
        name.setText(official.getName());

        if (official.getParty() != null) {
            if (official.getParty().equals("Republican Party")) {
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                DemRep.setImageResource(R.drawable.rep_logo);

            } else if (official.getParty().equals("Democratic Party")) {
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                DemRep.setImageResource(R.drawable.dem_logo);

            } else {
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);

            }
        }
            if (official.getPhotoUrl() != null) {
                Picasso artist = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                        final String changedUrl = official.getPhotoUrl().replace("http:", "https:");
                        picasso.load(changedUrl).error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder).into(photo);
                    }
                }).build();

                artist.load(official.getPhotoUrl()).error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder).into(photo);
            } else {
                Picasso.with(this).load(official.getPhotoUrl()).error(R.drawable.brokenimage).placeholder(R.drawable.missing).into(photo);
            }
        }
    }

