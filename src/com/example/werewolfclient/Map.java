package com.example.werewolfclient;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Map extends Fragment {
	// ...
	MapView m;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// inflat and return the layout
		View v = inflater.inflate(R.layout.fragment_map, container, false);
		m = (MapView) v.findViewById(R.id.mapView);
		m.onCreate(savedInstanceState);

		GoogleMap theMap = m.getMap();
		if (theMap == null)
			Log.v("mape", "was null");
		Log.v("mape", "the position: " + theMap.getCameraPosition());

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		CameraUpdate center=
				CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
		
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		theMap.moveCamera(center);
		theMap.animateCamera(zoom);
		Log.v("mape", "the position: " + theMap.getCameraPosition());


		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}
}