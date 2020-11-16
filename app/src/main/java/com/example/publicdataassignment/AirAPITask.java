package com.example.publicdataassignment;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AirAPITask extends AsyncTask<AddressModel, Void, AirAPIResponse> {
    private WeakReference<ShowActivity> activityReference;
    private String gu;

    AirAPITask(ShowActivity context) {
        activityReference = new WeakReference<ShowActivity>(context);
    }


    @Override
    protected AirAPIResponse doInBackground(AddressModel... AddressModels) {
        AddressModel addressModel = AddressModels[0];
        AirAPIResponse result = null;
        ShowActivity activity = activityReference.get();
        ArrayList<String> addresses = new ArrayList<String>();
        addresses.add(addressModel.getGu());
        addresses.add(addressModel.getDong());

        for(String address : addresses) {
            AirAPIResponse candidate = null;
            try {
                candidate = new AirAPIHandler.Builder(activity).build().requestAirAPI(address);
            } catch (IOException e) {
                String errString = Log.getStackTraceString(e);
                Log.e("API-GEOAPI", errString);
                Toast.makeText(activityReference.get(), "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();
            }
            if(candidate != null) {
                result = candidate;
                gu = address;
            }
        }
        return result;
    }

    protected void onPostExecute(AirAPIResponse result) {
        ShowActivity activity = activityReference.get();
        int overAllstatus = 0;
        if(result != null) {
            overAllstatus = result.getOverallStatus();
        }
        activity.setContentView(R.layout.activity_show);
        activity.initLayout(gu, overAllstatus, result);
    }

}
