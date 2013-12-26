package me.loki2302.activities;

import roboguice.util.Ln;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConnectivityService {
	@Inject
	private ConnectivityManager connectivityManager;
	
	public boolean isConnected() {
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo == null) {
			Ln.i("There's no active network");
			return false;
		}
		
		Ln.i("There IS an active network");
		if(!networkInfo.isConnected()) {
			Ln.i("Active network state - NOT connected");				
			return false;
		}
		
		Ln.i("Active network state - connected");			
		return true;
	}
}