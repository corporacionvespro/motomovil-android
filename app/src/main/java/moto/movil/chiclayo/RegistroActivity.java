package moto.movil.chiclayo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import org.json.JSONObject;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    Button btnRegistro;
    EditText etDni, etNombres, etApellidos, etCelular, etCorreo, etClave;
    FrameLayout flayCargando;
    PrefUtil prefUtil;
    static String verificado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
    }

    public void initComponents() {
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etCelular = (EditText) findViewById(R.id.etCelular);
        etClave = (EditText) findViewById(R.id.etClave);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etDni = (EditText) findViewById(R.id.etDni);
        etNombres = (EditText) findViewById(R.id.etNombres);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
    }

    public void initListener() {
        btnRegistro.setOnClickListener(this);
        etApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etApellidos.getText().length() > 0) {
                    etApellidos.setBackgroundResource(android.R.color.transparent);
                    etApellidos.setEnabled(false);
                } else {
                    etApellidos.setEnabled(true);
                }
                flayCargando.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etApellidos.setOnFocusChangeListener(this);
        etCelular.setOnFocusChangeListener(this);
        etClave.setOnFocusChangeListener(this);
        etCorreo.setOnFocusChangeListener(this);
        etDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etDni.getText().length() == 8) {
                    flayCargando.setVisibility(View.VISIBLE);
                    buscarDni();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etDni.setOnFocusChangeListener(this);
        etNombres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etNombres.getText().length() > 0) {
                    etNombres.setBackgroundResource(android.R.color.transparent);
                    etNombres.setEnabled(false);
                } else {
                    etNombres.setEnabled(true);
                }
                flayCargando.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etNombres.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistro:
                registrar();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.etApellidos:
                etApellidos.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etCelular:
                etCelular.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etCorreo:
                etCorreo.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etDni:
                etDni.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etNombres:
                etNombres.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.etClave:
                etClave.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }

    public void buscarDni() {
        Log.i("buscarDni", "RegistroActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://bootcampdojo.com/consulta_dni.php?dni=" + etDni.getText().toString());
                Log.i("buscarDni", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.length() > 0) {
                                    etApellidos.setText("");
                                    etNombres.setText("");
                                    String apellidos = jsonObject.getString("apellido_paterno") + " " + jsonObject.getString("apellido_materno");
                                    String nombres = jsonObject.getString("nombres");
                                    char[] cApellidos = apellidos.toLowerCase().toCharArray();
                                    cApellidos[0] = Character.toUpperCase(cApellidos[0]);
                                    for (int i = 0; i < apellidos.length(); i++) {
                                        if (cApellidos[i] == ' ') {
                                            cApellidos[i + 1] = Character.toUpperCase(cApellidos[i + 1]);
                                        }
                                        etApellidos.setText("" + etApellidos.getText().toString() + cApellidos[i]);
                                    }
                                    char[] cNombres = nombres.toLowerCase().toCharArray();
                                    cNombres[0] = Character.toUpperCase(cNombres[0]);
                                    for (int i = 0; i < nombres.length(); i++) {
                                        if (cNombres[i] == ' ') {
                                            cNombres[i + 1] = Character.toUpperCase(cNombres[i + 1]);
                                        }
                                        etNombres.setText("" + etNombres.getText().toString() + cNombres[i]);
                                    }
                                    etCelular.requestFocus();
                                    verificado = "1";
                                } else {
                                     etApellidos.setText("");
                                     etNombres.setText("");
                                     verificado = "0";
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

    public void registrar() {
        Log.i("registrar", "RegistroActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil/wsApp/registro_cliente.php?dni_cliente=" + etDni.getText().toString() + "&apellidos="
                        + etApellidos.getText().toString() + "&nombres=" + etNombres.getText().toString() + "&celular=" + etCelular.getText().toString() + "&correo="
                        + etCorreo.getText().toString() + "&clave=" + etClave.getText().toString() + "&verificado=" + verificado);
                Log.i("registrar", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            Toast.makeText(RegistroActivity.this, "Bienvenido a la comunidad Motomovil", Toast.LENGTH_SHORT).show();
                            prefUtil.saveGenericValue(PrefUtil.LOGIN_STATUS, "1");
                            prefUtil.saveGenericValue("dni_cliente", etDni.getText().toString());
                            prefUtil.saveGenericValue("nombres_cliente", etNombres.getText().toString());
                            prefUtil.saveGenericValue("apellidos_cliente", etApellidos.getText().toString());
                            prefUtil.saveGenericValue("celular_cliente", etCelular.getText().toString());
                            prefUtil.saveGenericValue("correo_cliente", etCorreo.getText().toString());
                            Intent intent = new Intent(RegistroActivity.this, CategoriaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        tr.start();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}