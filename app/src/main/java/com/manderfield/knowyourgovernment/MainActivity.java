package com.manderfield.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<Officials> RecycleList = new ArrayList<>();
    private myAdapter adapt;

    private static TextView locationMain;
    private LocateActivity locator;

    private static MainActivity mainActivity;

    private TextView networkWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

    //set up recycler and adapter
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        adapt = new myAdapter(RecycleList, this);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //location textview
        locationMain = (TextView) findViewById(R.id.main_location);

        //networkwarning textview
        networkWarning = (TextView) findViewById(R.id.main_network_warning);

        //check internet
        if (checkNetworkConnection()){
            locator = new LocateActivity(this);
        } else {
            networkwarningShow("Cannot connect to network");
        }

        if (locationMain.getText().toString().equals("No Data For Location")) networkwarningShow("close app and reopen!");

    }
    ///Create Menu with about and manual location entry menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_Info:
                Intent intent = new Intent(this, About.class);
                this.startActivity(intent);
                return true;

            case R.id.menuSearch:
                menuSearch();

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Officials o =RecycleList.get(position);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", locationMain.getText().toString());
        intent.putExtra("official", o);
        startActivityForResult(intent, 1);
    }
    public void menuSearch(){

        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);

        if (!checkNetworkConnection()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Content Cannot Be Found Without A Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State or Zip code");
        builder.setTitle("Enter Location:");
        builder.setView(view);
        //User searched a location
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get dialogue text view in main activity layout
                EditText inputTextView = (EditText) view.findViewById(R.id.dialogue);
                //get user input from dialogue
                String input = inputTextView.getText().toString().trim();
                if (input.length() != 0) {
                    DataLoader dl = new DataLoader(mainActivity, input);
                    new Thread(dl).start();
                }
                else{
                    doNoAnswer(input);
                }
            }

        });
        // User cancelled the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void doNoAnswer(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("No data for specified symbol/name");
        builder.setTitle("No Data Found: " + symbol);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void setMainLocation(double lat, double lon){

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            Log.d(TAG, "getting address");
            addresses = geocoder.getFromLocation(lat,lon, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null){
            Log.d("addresses", "Null");

        }else{
            locationMain.setText(addresses.get(0).getAddressLine(1));
        }

        DataLoader dl = new DataLoader(this,locationMain.getText().toString());

       new Thread(dl).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateOfficialArray(Officials official){
        RecycleList.add(official);
        adapt.notifyDataSetChanged();
    }

    public void clearList(){
        RecycleList.clear();
    }

    public void setLocationText(String location){
        locationMain.setText(location);
    }

    public Boolean checkNetworkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return true;
    }

    public void networkwarningShow(String message){
        //show popupwarning
        networkWarning.setVisibility(View.VISIBLE);
        networkWarning.setText(message);
    }

    public void networkwarningClose(){
        //get rid of popupwarning
        networkWarning.setVisibility(View.INVISIBLE);
    }
    public void downloadFailed() {
        RecycleList.clear();
        adapt.notifyDataSetChanged();
    }
}