package com.E2Execel.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class CheckConnectivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected)
            Log.i("NET", "Connected" + isConnected);
        else
            Log.i("NET", "Not Connected" + isConnected);
    }


}
