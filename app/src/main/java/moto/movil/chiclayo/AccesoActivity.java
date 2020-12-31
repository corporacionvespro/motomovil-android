package moto.movil.chiclayo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.json.JSONArray;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class AccesoActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    Button btnAcceder;
    EditText etDni, etClave;
    FrameLayout flayCargando;
    PrefUtil prefUtil;
    TextView tvRegistrarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
    }

    public void initComponents() {
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        etClave = (EditText) findViewById(R.id.etClave);
        etDni = (EditText) findViewById(R.id.etDni);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
        tvRegistrarme = (TextView) findViewById(R.id.tvRegistrarme);
    }

    public void initListener() {
        btnAcceder.setOnClickListener(this);
        etClave.setOnFocusChangeListener(this);
        etDni.setOnFocusChangeListener(this);
        tvRegistrarme.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAcceder:
                flayCargando.setVisibility(View.VISIBLE);
                acceder();
                break;
            case R.id.tvRegistrarme:
                Intent intent = new Intent(AccesoActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.etClave:
                etClave.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etDni:
                etDni.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }

    public void acceder() {
        Log.i("acceder", "AccesoActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/acceso_cliente.php?dni_cliente=" + etDni.getText().toString() + "&clave="
                        + etClave.getText().toString());
                Log.i("acceder", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String dni = jsonArray.getJSONObject(i).getString("dni_cliente");
                                    String apellidos = jsonArray.getJSONObject(i).getString("apellidos");
                                    String nombres = jsonArray.getJSONObject(i).getString("nombres");
                                    String celular = jsonArray.getJSONObject(i).getString("celular");
                                    String correo = jsonArray.getJSONObject(i).getString("correo");
                                    prefUtil.saveGenericValue(PrefUtil.LOGIN_STATUS, "1");
                                    prefUtil.saveGenericValue("dni_cliente", dni);
                                    prefUtil.saveGenericValue("apellidos_cliente", apellidos);
                                    prefUtil.saveGenericValue("nombres_cliente", nombres);
                                    prefUtil.saveGenericValue("celular_cliente", celular);
                                    prefUtil.saveGenericValue("correo_cliente", correo);
                                    Intent intent = new Intent(AccesoActivity.this, CategoriaActivity.class);
                                    startActivity(intent);
                                    flayCargando.setVisibility(View.GONE);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        tr.start();
    }
}