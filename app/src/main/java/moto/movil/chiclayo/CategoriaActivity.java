package moto.movil.chiclayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arthurivanets.bottomsheets.BottomSheet;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import moto.movil.chiclayo.adapter.CategoriaAdapter;
import moto.movil.chiclayo.adapter.UbicacionBottomSheet;
import moto.movil.chiclayo.entity.Categoria;
import moto.movil.chiclayo.publico.Funciones;
import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class CategoriaActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener {
    ArrayList<Categoria> categorias;
    CategoriaAdapter categoriaAdapter;
    DrawerLayout dlayMenu;
    EditText etBusqueda;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView ivMenu, ivUbicacion, ivBusqueda;
    LinearLayout llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    public static BottomSheet bottomSheet;
    public static Double latitud = 0.0, longitud = 0.0;
    public static String direccionDestino = "";
    RecyclerView rvCategorias;
    TextView tvNombreUsuario, tvNombreCliente, tvUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        prefUtil = new PrefUtil(this);
        Places.initialize(this, "AIzaSyAd4MY2O9ntD3N9Vl6MWn-ySDG7DQ4iQw0");
        initComponents();
        initListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    prefUtil.saveGenericValue("latitud_origen", String.valueOf(location.getLatitude()));
                    prefUtil.saveGenericValue("longitud_origen", String.valueOf(location.getLongitude()));
                    direccionDestino = getCountryInfo(location).getAddressLine(0);
                    latitud = getCountryInfo(location).getLatitude();
                    longitud = getCountryInfo(location).getLongitude();
                }
            }
        });
        rvCategorias.setLayoutManager(new GridLayoutManager(this, 2));
        tvNombreUsuario.setText("Hola\n" + prefUtil.getStringValue("nombres_cliente"));
        tvNombreCliente.setText("Hola\n" + prefUtil.getStringValue("nombres_cliente"));
        obtenerCategorias();
    }
    
    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        etBusqueda = (EditText) findViewById(R.id.etBusqueda);
        ivBusqueda = (ImageView) findViewById(R.id.ivBusqueda);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivUbicacion = (ImageView) findViewById(R.id.ivUbicacion);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        rvCategorias = (RecyclerView) findViewById(R.id.rvCategorias);
        tvNombreCliente = (TextView) findViewById(R.id.tvNombreCliente);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
        tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);
    }

    public void initListener() {
        etBusqueda.setOnEditorActionListener(this);
        ivMenu.setOnClickListener(this);
        ivUbicacion.setOnClickListener(this);
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
        tvUbicacion.setOnClickListener(this);
    }

    public void obtenerCategorias() {
        Log.i("obtenerCategorias", "CategoriaActivity");
        categorias = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/motomovil-web/wsApp/obtener_categorias.php");
                Log.i("obtenerCategorias", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    categorias.add(new Categoria(jsonArray.getJSONObject(i).getString("id_categoria"), jsonArray.getJSONObject(i).getString("nombre"),
                                            jsonArray.getJSONObject(i).getString("icono"), jsonArray.getJSONObject(i).getString("imagen")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        categoriaAdapter = new CategoriaAdapter(CategoriaActivity.this, categorias);
                        rvCategorias.setAdapter(categoriaAdapter);
                    }
                });
            }
        };
        tr.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.ivUbicacion:
            case R.id.tvUbicacion:
                showCustomBottomSheet();
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                Intent intent = new Intent(CategoriaActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navCategorias:
                dlayMenu.closeDrawer(GravityCompat.START);
                break;
            case R.id.navNotificaciones:
                intent = new Intent(CategoriaActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                break;
            case R.id.navPedidos:
                intent = new Intent(CategoriaActivity.this, MisPedidosActivity.class);
                startActivity(intent);
                break;
            case R.id.navPerfil:
                intent = new Intent(CategoriaActivity.this, MiPerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.navPreguntas:
                intent = new Intent(CategoriaActivity.this, PreguntasActivity.class);
                startActivity(intent);
                break;
            case R.id.navPromociones:
                intent = new Intent(CategoriaActivity.this, PromocionesActivity.class);
                startActivity(intent);
                break;
        }
        dlayMenu.closeDrawer(GravityCompat.START);
        return true;
    }
    
    public void showCustomBottomSheet() {
        bottomSheet = new UbicacionBottomSheet(this);
        bottomSheet.show();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("onActivityResult", "Place: " + place.getName() + ", " + place.getId());
                FindLatLong(place.getId());
                prefUtil.saveGenericValue("direccionEntrega", place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("onActivityResult", status.getStatusMessage());
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
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getJSONArray("results") != null) {
                        JSONArray destination_addresses = jsonObject.getJSONArray("results");
                        JSONObject geometry = (JSONObject) destination_addresses.get(0);
                        String lat = String.valueOf(geometry.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        String lng = String.valueOf(geometry.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        prefUtil.saveGenericValue("latitud_destino", "" + lat);
                        prefUtil.saveGenericValue("longitud_destino", "" + lng);
                        Toast.makeText(CategoriaActivity.this, lat + ", " + lng, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Errors", String.valueOf(error));
            }
        });
        queue.add(sr);
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.etBusqueda:
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etBusqueda.getWindowToken(), 0);
                    Intent intent = new Intent(CategoriaActivity.this, TiendaActivity.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create((View) etBusqueda, "tvNombreCategoria"), Pair.create((View) ivBusqueda, "ivBusqueda"));
                    intent.putExtra("idCategoria", "0");
                    intent.putExtra("nombreCategoria", etBusqueda.getText().toString());
                    intent.putExtra("iconoCategoria", "https://vespro.io/motomovil-web/icon/categoria/busqueda.png");
                    startActivity(intent, options.toBundle());
                }
        }
        return false;
    }
    
    private Address getCountryInfo(Location location) {
        Address address = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage;
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            errorMessage = "IOException>>" + ioException.getMessage();
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "IllegalArgumentException>>" + illegalArgumentException.getMessage();
        }
        if (addresses != null && !addresses.isEmpty()) {
            address = addresses.get(0);
        }
        return address;
    }
}