package com.manderfield.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader implements Runnable {
    private static final String TAG = "DATALOADER";
    private String data;
    private MainActivity mainActivity;
    //keyremoved
    private String prefix = "https://www.googleapis.com/civicinfo/v2/representatives?;
    public DataLoader(MainActivity mainActivity, String data) {
        this.mainActivity = mainActivity;
        this.data = data;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: STARTING");
        Uri.Builder uriBuilder = Uri.parse(prefix + data).buildUpon();

        String urlToUse = uriBuilder.toString();
        Log.d(TAG, "run: " + urlToUse);

        final StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                parseJSON(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            parseJSON(null);
            return;
        }
        parseJSON(sb.toString());

    }


    private void parseJSON(String s) {

        ArrayList<Officials> offList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject jOfficials = (JSONObject)jObjMain.getJSONObject("normalizedInput");

            String location = jOfficials.getString("city") + ", " + jOfficials.getString("state") + " " + jOfficials.getString("zip");
            mainActivity.setLocationText(location);
            //array for offices
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            //array for officials
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");

            mainActivity.clearList();
    //iterate through offices
            for (int i = 0; i < jArrayOffices.length(); i++) {
                JSONObject jObj = jArrayOffices.getJSONObject(i);
                String officeName = jObj.getString("name");

                JSONArray indicesJSON = jObj.getJSONArray("officialIndices");
                ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j < indicesJSON.length(); j++) {
                    int pos = Integer.parseInt(indicesJSON.getString(j));
                    Officials official = new Officials(officeName);
                    JSONObject jOfficial = jArrayOfficials.getJSONObject(pos);

                    official.setName(jOfficial.getString("name"));

                    JSONArray jAddresses = jOfficial.getJSONArray("address");
                    JSONObject jAddress = jAddresses.getJSONObject(0);

                    String address = "";

                    if (jAddress.has("line1")) address += jAddress.getString("line1") + '\n';
                    if (jAddress.has("line2")) address += jAddress.getString("line2") + '\n';
                    if (jAddress.has("line3")) address += jAddress.getString("line3") + '\n';
                    if (jAddress.has("city")) address += jAddress.getString("city") + ", ";
                    if (jAddress.has("state")) address += jAddress.getString("state") + ' ';
                    if (jAddress.has("zip")) address += jAddress.getString("zip");
                        official.setAddress(address);

                    if (jOfficial.has("party")) official.setParty(jOfficial.getString("party"));
                    if (jOfficial.has("phones"))
                        official.setPhone(jOfficial.getJSONArray("phones").getString(0));

                    if (jOfficial.has("urls"))
                        official.setUrl(jOfficial.getJSONArray("urls").getString(0));

                    if (jOfficial.has("emails"))
                        official.setEmail(jOfficial.getJSONArray("emails").getString(0));

                    if (jOfficial.has("channels")) {
                        SiteChanger site = new SiteChanger();

                        JSONArray jChannels = jOfficial.getJSONArray("channels");
                        for (int k = 0; k < jChannels.length(); k++) {
                            JSONObject jChannel = jChannels.getJSONObject(k);
                            if (jChannel.getString("type").equals("Facebook"))
                                site.setFacebookId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter"))
                                site.setTwitterId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube"))
                                site.setYoutubeId(jChannel.getString("id"));
                        }
                        official.setChannel(site);
                    }

                    if (jOfficial.has("photoUrl"))
                        official.setPhotoUrl(jOfficial.getString("photoUrl"));
                    mainActivity.runOnUiThread(() -> mainActivity.updateOfficialArray(official));

                }

            }
        }
        catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }

    }
}



