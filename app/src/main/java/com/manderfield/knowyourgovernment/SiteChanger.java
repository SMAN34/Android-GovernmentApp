package com.manderfield.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.Serializable;

public class SiteChanger implements Serializable {
    private String facebookId;
    private String twitterId;
    private String youtubeId;

    public String getFacebookId() {
        return facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }
}