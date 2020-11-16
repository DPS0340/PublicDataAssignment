package com.example.publicdataassignment;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class ReverseGeocodeTask extends AsyncTask<LocationModel, Void, AddressModel>{
    private WeakReference<SelectActivity> activityReference;

    ReverseGeocodeTask(SelectActivity context) {
        activityReference = new WeakReference<>(context);
    }


    @Override
    protected AddressModel doInBackground(LocationModel... locationModels) {
        LocationModel locationModel = locationModels[0];
        AddressModel result = null;
        SelectActivity activity = activityReference.get();
        try {
            result = new ReverseGeocoder.Builder(activity).build().requestReverseGeoApi(locationModel.getLatitude(), locationModel.getLongitude());
        } catch (IOException e) {
            String errString = Log.getStackTraceString(e);
            Log.e("API-GEOAPI", errString);
            Toast.makeText(activityReference.get(), "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    protected void onPostExecute(AddressModel result) {
        SelectActivity activity = activityReference.get();
        activity.goShowActivity(result);
    }
}
