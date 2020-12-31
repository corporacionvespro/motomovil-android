package moto.movil.chiclayo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class SplashActivity extends AppCompatActivity {
    AlertDialog alert = null;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    PrefUtil prefUtil;
    public static int i = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        prefUtil = new PrefUtil(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertNoGps();
            } else {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        prefUtil.saveGenericValue("latitud_origen", String.valueOf(location.getLatitude()));
                        prefUtil.saveGenericValue("longitud_origen", String.valueOf(location.getLongitude()));
                        Log.i("ubicación:", "" + getCountryInfo(location).getAddressLine(0));
                    }
                });
                CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        Intent intent;
                        if (prefUtil.getStringValue(PrefUtil.LOGIN_STATUS).equals("1")) {
                            intent = new Intent(SplashActivity.this, CategoriaActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, AccesoActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                };
                countDownTimer.start();
            }
        }
    }
    
    public void AlertNoGps() {
        if (alert == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("El GPS está desactivado, ¿desea activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            CountDownTimer countDownTimer = new CountDownTimer(3000, i) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    i = 1000;
                                }
                                @Override
                                public void onFinish() {
                                    CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                                        @Override
                                        public void onTick(long l) {
                                        }
                                        @Override
                                        public void onFinish() {
                                            Intent intent;
                                            if (prefUtil.getStringValue(PrefUtil.LOGIN_STATUS).equals("1")) {
                                                intent = new Intent(SplashActivity.this, CategoriaActivity.class);
                                            } else {
                                                intent = new Intent(SplashActivity.this, AccesoActivity.class);
                                            }
                                            startActivity(intent);
                                            finish();
                                        }
                                    };
                                    countDownTimer.start();
                                }
                            };
                            countDownTimer.start();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alert = builder.create();
            alert.show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertNoGps();
                } else {
                    Intent intent;
                    if (prefUtil.getStringValue(PrefUtil.LOGIN_STATUS).equals("1")) {
                        intent = new Intent(SplashActivity.this, CategoriaActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, AccesoActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.i("Permiso denegado", "ubicación");
            }
        }
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