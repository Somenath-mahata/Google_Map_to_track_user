package com.example.google_map_to_track_user;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // private ActivityMapsBinding binding;
    SearchView searchView;
    /* LatLng sydney = new LatLng(17.4562631, 78.3680144);
     LatLng delhi = new LatLng(28.63491293918908, 77.20381331717657);
     LatLng nagaland = new LatLng(26.04743362843828, 94.59096722368784);
     LatLng telangana = new LatLng(17.90739749850922, 79.22062700274728);*/
    ArrayList<LatLng> arrayList = new ArrayList<>();

    private Button hybrid,terrain,satellite,polyline;
    Double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Intent intent = getIntent();
        arrayList = getIntent().getParcelableArrayListExtra("list");
//        Log.d("rajaprasad", "onCreate: "+arrayList.size());
        hybrid = findViewById(R.id.idbtnhybridmap);
        terrain = findViewById(R.id.idbtnterrainmap);
        satellite = findViewById(R.id.idbtnsatellitemap);

        searchView = findViewById(R.id.searchview);

        polyline=findViewById(R.id.btnpoyline);
       /* arrayList.add(sydney);
        arrayList.add(delhi);
        arrayList.add(nagaland);
        arrayList.add(new LatLng(29.821078334717516, 69.56040497660507));*/


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                List<Address> addressList = null;
                if(location!=null||location.equals("")){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(location);

                    mMap.addMarker(markerOptions);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);



        // multiple maps
        hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        polyline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("rajaprasad232", "onCreate: "+arrayList.size());


                // BitmapDescriptor descriptor = new BitmapDescriptor();
                mMap.addPolyline((new PolylineOptions())
                        .addAll(arrayList)
                        .width(10)
                        .color(Color.parseColor("#0000FF"))
                        .startCap(new CustomCap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        .endCap(new CustomCap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))



                        .geodesic(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0),22f));
                //.addMarker(new MarkerOptions().position(arrayList.get(arrayList.size()-1)));
            }
        });

    }
//new CustomCap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.addPolyline((new PolylineOptions())
                .addAll(arrayList)
                .width(10)
                .color(Color.parseColor("#FF0BE2E2"))
                /*.startCap(new CustomCap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                .endCap(new CustomCap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
*/


                .geodesic(true));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0),22f));
        mMap.addMarker(new MarkerOptions().position(arrayList.get(0)).title("Start point"));
        mMap.addMarker(new MarkerOptions().position(arrayList.get(arrayList.size()-1)).title("End point"));
        //mMap.addMarker(new MarkerOptions().position(arrayList.get(arrayList.size()-1));
        //mMap.addMarker(new MarkerOptions().)
        // mMap.addMarker(new MarkerOptions().position(arrayList.get(0)).title("Start point"));
        //mMap.addMarker(new MarkerOptions().position(arrayList.get(arrayList.size())).title("End point"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(0)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0),22f));
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }


}
