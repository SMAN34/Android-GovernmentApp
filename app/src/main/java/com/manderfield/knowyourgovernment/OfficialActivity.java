package com.manderfield.knowyourgovernment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private TextView location;
    private TextView office;
    private TextView name;
    private TextView party;
    private TextView address;
    private TextView phone;
    private TextView email;
    private TextView web;

    private ImageButton photo;
    private ImageButton facebook;
    private ImageButton twitter;
    private ImageButton youtube;
    private ImageView DemRep;

    private Intent intent;
    private Officials official;

        @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_officials);

            location = (TextView) findViewById(R.id.officials_location);
            office = (TextView) findViewById(R.id.officials_office);
            name = (TextView) findViewById(R.id.officials_name);
            party = (TextView) findViewById(R.id.officials_party);
            address = (TextView) findViewById(R.id.officials_address);
            phone = (TextView) findViewById(R.id.officials_phone);
            email = (TextView) findViewById(R.id.officials_email);
            web = (TextView) findViewById(R.id.officials_website);
            photo = (ImageButton) findViewById(R.id.officials_photo);
            DemRep = (ImageView) findViewById(R.id.DemRepIm);
            facebook = (ImageButton) findViewById(R.id.facebook);
            twitter = (ImageButton) findViewById(R.id.twitter);
            youtube = (ImageButton) findViewById(R.id.youtube);

            intent = getIntent();
            location.setText(intent.getCharSequenceExtra("location"));
            official = (Officials) intent.getSerializableExtra("official");
            office.setText(official.getOffice());
            name.setText(official.getName());
            if (official.getParty() != null) {

                if (official.getParty().equals("Republican Party")) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    party.setText('(' + official.getParty() + ')');
                    DemRep.setImageResource(R.drawable.rep_logo);

                }
                else if (official.getParty().equals("Democratic Party")) {
                    party.setText('(' + official.getParty() + ')');
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    DemRep.setImageResource(R.drawable.dem_logo);
                }
                else
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                    DemRep.setImageResource(R.drawable.missing);
                }
            //else getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            if (official.getAddress() != null)
                address.setText(official.getAddress());
            if (official.getPhone() != null)
                phone.setText(official.getPhone());
            if (official.getEmail() != null)
                email.setText(official.getEmail());
            if (official.getUrl() != null)
                web.setText(official.getUrl());

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

            if (official.getChannel() == null) {
                facebook.setVisibility(View.INVISIBLE);
                youtube.setVisibility(View.INVISIBLE);
                twitter.setVisibility(View.INVISIBLE);

            } else {
                if (official.getChannel().getFacebookId() == null)
                    facebook.setVisibility(View.INVISIBLE);
                if (official.getChannel().getYoutubeId() == null)
                    youtube.setVisibility(View.INVISIBLE);
                if (official.getChannel().getTwitterId() == null)
                    twitter.setVisibility(View.INVISIBLE);
            }
        }
        public void photoClick (View v){
            if (official.getPhotoUrl() == null) {
                return;
            }
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("official", official);
            intent.putExtra("location", location.getText());
            Log.d("before go to pa", location.getText().toString());
            startActivityForResult(intent, 1);
        }
///////////////DONE
        public void youtubeClick (View v){
            String name = official.getChannel().getYoutubeId();
            Intent intent = null;
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse("https://www.youtube.com/" + name));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
            }
        }
///////////DONE
        public void facebookClick (View v){
            String FACEBOOK_URL = "https://www.facebook.com/" + official.getChannel().getFacebookId();
            String urlToUse;

            PackageManager packageManager = getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) { //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    urlToUse = "fb://page/" + official.getChannel().getFacebookId();
                }
            } catch (PackageManager.NameNotFoundException e) {
                urlToUse = FACEBOOK_URL; //normal web url
            }
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(urlToUse));
            startActivity(facebookIntent);
        }
    /////////////DONE
        public void twitterClick (View v){
            Intent intent = null;
            String name = official.getChannel().getTwitterId();
            try {
                // get the Twitter app if possible
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
            }
            startActivity(intent);
        }
    }
