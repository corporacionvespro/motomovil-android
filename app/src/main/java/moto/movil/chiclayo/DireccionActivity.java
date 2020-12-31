package moto.movil.chiclayo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import moto.movil.chiclayo.adapter.PlaceAutoSuggestAdapter;

/**
 * By: El Bryant
 */

public class DireccionActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    AutoCompleteTextView autoCompleteTextView;
    Button btnAceptar;
    CardView cvDireccionDestino;
    Double latitud = 0.0, longitud = 0.0;
    Marker markerDestino;
    private GoogleMap mMap;
    String direccionDestino;
    SupportMapFragment mapFragment;
    TextView tvDireccionDestino;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initComponents();
        initListener();
        Places.initialize(this, "AIzaSyAd4MY2O9ntD3N9Vl6MWn-ySDG7DQ4iQw0");
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(this, android.R.layout.simple_list_item_1));
        mapFragment.getMapAsync(this);
        Typeface comfortaa_bold = Typeface.createFromAsset(getAssets(), "fonts/comfortaa_bold.ttf");
        btnAceptar.setTypeface(comfortaa_bold);
        tvDireccionDestino.setTypeface(comfortaa_bold);
        tvDireccionDestino.setText(CategoriaActivity.direccionDestino);
    }
    
    public void initComponents() {
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        cvDireccionDestino = (CardView) findViewById(R.id.cvDireccionDestino);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        tvDireccionDestino = (TextView) findViewById(R.id.tvDireccionDestino);
    }
    
    public void initListener() {
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            mMap.clear();
            String description = (String) parent.getItemAtPosition(position);
            FindLatLong(description);
        });
        btnAceptar.setOnClickListener(this);
        cvDireccionDestino.setOnClickListener(this);
        tvDireccionDestino.setOnClickListener(this);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = null;
                if (data != null) {
                    place = Autocomplete.getPlaceFromIntent(data);
                    Log.i("onActivityResult", "Place: " + place.getName() + ", " + place.getId());
                    FindLatLong(place.getId());
                    direccionDestino = place.getName();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = null;
                if (data != null) {
                    status = Autocomplete.getStatusFromIntent(data);
                    if (status.getStatusMessage() != null) {
                        Log.i("onActivityResult", status.getStatusMessage());
                    }
                }
            }
        }
    }
    
    @SuppressLint("DefaultLocale")
    public void FindLatLong(String place_id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/geocode/json")
                .buildUpon()
                .appendQueryParameter("key", "AIzaSyAd4MY2O9ntD3N9Vl6MWn-ySDG7DQ4iQw0")
                .appendQueryParameter("place_id", URLEncoder.encode(place_id))
                .build().toString();
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray destination_addresses = jsonObject.getJSONArray("results");
                JSONObject geometry = (JSONObject) destination_addresses.get(0);
                latitud = geometry.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                longitud = geometry.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                LatLng destino = new LatLng(latitud, longitud);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(destino)
                        .zoom(15)
                        .bearing(mMap.getCameraPosition().bearing)
                        .tilt(mMap.getCameraPosition().tilt)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                tvDireccionDestino.setText(direccionDestino);
                if (markerDestino != null) {
                    markerDestino.setPosition(destino);
                } else {
                    MarkerOptions markerOptions = new MarkerOptions().position(destino);
                    markerDestino = mMap.addMarker(markerOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.i("Errors", String.valueOf(error)));
        queue.add(sr);
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ubicacionDestino = new LatLng(CategoriaActivity.latitud, CategoriaActivity.longitud);
        markerDestino = mMap.addMarker(new MarkerOptions().position(ubicacionDestino).title("Ubicación de destino"));
        if (markerDestino != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerDestino.getPosition())
                    .zoom(15)
                    .bearing(mMap.getCameraPosition().bearing)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        new CountDownTimer(500, 250) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                CategoriaActivity.bottomSheet.dismiss();
            }
        }.start();
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDireccionDestino:
                final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                final RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-6.940396, -79.969844),
                        new LatLng(-6.629670, -79771765)
                );
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setLocationBias(bounds)
                        .build(DireccionActivity.this);
                startActivityForResult(intent, 1);
                break;
            case R.id.btnAceptar:
                if (getIntent().getExtras() != null) {
                    String courier = getIntent().getExtras().getString("courier");
                    if (courier.equals("si")) {
                        CategoriaActivity.direccionDestino = tvDireccionDestino.getText().toString();
                        CategoriaActivity.latitud = latitud;
                        CategoriaActivity.longitud = longitud;
                        CourierActivity.tvDireccionDestino.setText(tvDireccionDestino.getText().toString());
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                } else {
                    CategoriaActivity.direccionDestino = tvDireccionDestino.getText().toString();
                    CategoriaActivity.latitud = latitud;
                    CategoriaActivity.longitud = longitud;
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    new CountDownTimer(500, 250) {
                        @Override
                        public void onTick(long l) {
                        }
                        @Override
                        public void onFinish() {
                            CategoriaActivity.bottomSheet.dismiss();
                        }
                    }.start();
                }
                break;
        }
    }
}
