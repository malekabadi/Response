package com.example.respons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetCheck extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (isNetworkDown) {
            Intent i = new Intent(NewActivity.MainAvtivity, NoNet.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NewActivity.MainAvtivity.startActivity(i);
        } //else if (NoNet._NoNet != null) NoNet._NoNet.finish();


        if (CallSoap.isConnectionAvailable(context)) {
            ;//if (NoNet._NoNet != null) NoNet._NoNet.finish();
        } else {
            Intent i = new Intent(NewActivity.MainAvtivity, NoNet.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NewActivity.MainAvtivity.startActivity(i);
        }
    }
}