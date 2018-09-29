 package com.demo.apple.gaddress;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

 public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

     private GoogleApiClient mGoogleApiClient;
     private int PLACE_PICKER_REQUEST = 1;
     AppCompatTextView addressTv;
     ImageView imageView;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressTv=findViewById(R.id.addresstv);
        imageView=findViewById(R.id.imageView);
         mGoogleApiClient = new GoogleApiClient
                 .Builder(this)
                 .addApi(Places.GEO_DATA_API)
                 .addApi(Places.PLACE_DETECTION_API)
                 .enableAutoManage(this, this)
                 .build();
    }

     public void getAddress(View view) {
         PathInterpolator pathInterpolator;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", 100f);
             animation.setDuration(2000);
             animation.start();
         }
         //imageView.setEnabled(false);
         PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
         try {
             startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
         } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // imageView.setEnabled(true);
             Snackbar.make(imageView,e.getMessage() + "", Snackbar.LENGTH_LONG).show();
             e.printStackTrace();
         }
     }
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationY", 0f);
             animation.setDuration(2000);
             animation.start();
         }
        // addressTv.setEnabled(true);
         if (requestCode == PLACE_PICKER_REQUEST) {
             if (resultCode == RESULT_OK) {
                 Place place = PlacePicker.getPlace(data, this);
                 StringBuilder stBuilder = new StringBuilder();
                 String placename = String.format("%s", place.getName());
                 String latitude = String.valueOf(place.getLatLng().latitude);
                 String longitude = String.valueOf(place.getLatLng().longitude);
                 String address = String.format("%s", place.getAddress());
                 String[] addressArr=address.split(",");
                /* stBuilder.append("Name: ");
                 stBuilder.append(placename);
                 stBuilder.append("\n");
                 stBuilder.append("Latitude: ");
                 stBuilder.append(latitude);
                 stBuilder.append("\n");
                 stBuilder.append("Logitude: ");
                 stBuilder.append(longitude);
                 stBuilder.append("\n");*/
                 stBuilder.append("Address: \n");
                 for (String addr:addressArr
                      ) {
                     stBuilder.append(addr.trim());
                     stBuilder.append("\n");
                 }

                 addressTv.setText(stBuilder.toString());
             }
         }
     }
     @Override
     protected void onStart() {
         super.onStart();
         mGoogleApiClient.connect();
     }

     @Override
     protected void onStop() {
         mGoogleApiClient.disconnect();
         super.onStop();
     }

     @Override
     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
         addressTv.setEnabled(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationY", 0f);
             animation.setDuration(2000);
             animation.start();
         }
         Snackbar.make(addressTv, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();

     }
 }
