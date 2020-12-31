package moto.movil.chiclayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import moto.movil.chiclayo.config.Config;
import moto.movil.chiclayo.entity.Categoria;
import moto.movil.chiclayo.entity.Producto;
import moto.movil.chiclayo.publico.PaymentDetails;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.ProductoActivity.totalPedido;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class CerrarPedidoActivity extends AppCompatActivity implements View.OnClickListener {
    boolean listoParaConfirmar = true;
    Button btnAceptar;
    CheckBox chkEfectivo, chkTarjeta;
    EditText etDireccionDestino, etRuc, etCelularCliente;
    FrameLayout flayCargando;
    ImageView ivMenosCubiertos, ivMasCubiertos, ivMenosVasos, ivMasVasos;
    LinearLayout llayRuc, llayRazonSocial;
    PrefUtil prefUtil;
    private static final int PAYPAL_REQUEST_CODE = 7777;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);
    String amount = "";
    Switch swFactura;
    TextView tvCantidadCubiertos, tvCantidadVasos, tvRazonSocial;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_pedido);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        etDireccionDestino.setText(CategoriaActivity.direccionDestino);
        etCelularCliente.setText(prefUtil.getStringValue("celular_cliente"));
    
        //start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    public void initComponents() {
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        chkEfectivo = (CheckBox) findViewById(R.id.chkEfectivo);
        chkTarjeta = (CheckBox) findViewById(R.id.chkTarjeta);
        etCelularCliente = (EditText) findViewById(R.id.etCelularCliente);
        etDireccionDestino = (EditText) findViewById(R.id.etDireccionDestino);
        etRuc = (EditText) findViewById(R.id.etRuc);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
        ivMasCubiertos = (ImageView) findViewById(R.id.ivMasCubiertos);
        ivMasVasos = (ImageView) findViewById(R.id.ivMasVasos);
        ivMenosCubiertos = (ImageView) findViewById(R.id.ivMenosCubiertos);
        ivMenosVasos = (ImageView) findViewById(R.id.ivMenosVasos);
        llayRazonSocial = (LinearLayout) findViewById(R.id.llayRazonSocial);
        llayRuc = (LinearLayout) findViewById(R.id.llayRuc);
        swFactura = (Switch) findViewById(R.id.swFactura);
        tvCantidadCubiertos = (TextView) findViewById(R.id.tvCantidadCubiertos);
        tvCantidadVasos = (TextView) findViewById(R.id.tvCantidadVasos);
        tvRazonSocial = (TextView) findViewById(R.id.tvRazonSocial);
    }
    
    public void initListener() {
        btnAceptar.setOnClickListener(this);
        chkEfectivo.setOnClickListener(this);
        chkTarjeta.setOnClickListener(this);
        etRuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (etRuc.getText().length() == 11) {
                    flayCargando.setVisibility(View.VISIBLE);
                    consultarRuc();
                }
            }
        });
        ivMasCubiertos.setOnClickListener(this);
        ivMasVasos.setOnClickListener(this);
        ivMenosCubiertos.setOnClickListener(this);
        ivMenosVasos.setOnClickListener(this);
        swFactura.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAceptar:
                flayCargando.setVisibility(View.VISIBLE);
                if (etDireccionDestino.getText().length() < 1) {
                    listoParaConfirmar = false;
                    etDireccionDestino.setError("Debe definir una dirección de entrega");
                } else {
                    listoParaConfirmar = true;
                }
                if (etCelularCliente.getText().length() != 9) {
                    listoParaConfirmar = false;
                    etCelularCliente.setError("Debe indicar un teléfono de referencia válido");
                } else {
                    listoParaConfirmar = true;
                }
                if (swFactura.isChecked()) {
                    if (etRuc.getText().length() != 11) {
                        listoParaConfirmar = false;
                        etRuc.setError("Debe definir un RUC válido");
                    } else {
                        listoParaConfirmar = true;
                    }
                    if (tvRazonSocial.getText().length() < 1) {
                        listoParaConfirmar = false;
                        tvRazonSocial.setError("Debe definir una razón social");
                    } else {
                        listoParaConfirmar = true;
                    }
                }
                if (listoParaConfirmar) {
                    if (chkEfectivo.isChecked()) {
                        completarPedido();
                    } else if (chkTarjeta.isChecked()) {
                        processPayment();
                    }
                } else {
                    flayCargando.setVisibility(View.GONE);
                    Toast.makeText(this, "No se ha podido completar el pedido", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chkEfectivo:
                if (chkEfectivo.isChecked()) {
                    chkTarjeta.setChecked(false);
                } else {
                    chkTarjeta.setChecked(true);
                }
                break;
            case R.id.chkTarjeta:
                if (chkTarjeta.isChecked()) {
                    chkEfectivo.setChecked(false);
                } else {
                    chkEfectivo.setChecked(true);
                }
                break;
            case R.id.ivMasCubiertos:
                tvCantidadCubiertos.setText("" + (Integer.parseInt(tvCantidadCubiertos.getText().toString()) + 1));
                break;
            case R.id.ivMasVasos:
                tvCantidadVasos.setText("" + (Integer.parseInt(tvCantidadVasos.getText().toString()) + 1));
                break;
            case R.id.ivMenosCubiertos:
                if (!tvCantidadCubiertos.getText().toString().equals("0")) {
                    tvCantidadCubiertos.setText("" + (Integer.parseInt(tvCantidadCubiertos.getText().toString()) - 1));
                }
                break;
            case R.id.ivMenosVasos:
                if (!tvCantidadVasos.getText().toString().equals("0")) {
                    tvCantidadVasos.setText("" + (Integer.parseInt(tvCantidadVasos.getText().toString()) - 1));
                }
                break;
            case R.id.swFactura:
                if (swFactura.isChecked()) {
                    llayRazonSocial.setVisibility(View.VISIBLE);
                    llayRuc.setVisibility(View.VISIBLE);
                } else {
                    llayRazonSocial.setVisibility(View.GONE);
                    llayRuc.setVisibility(View.GONE);
                }
        }
    }
    
    private void processPayment() {
        amount = totalPedido.toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD", "Compras por Motomóvil para Android", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("Payment Details", paymentDetails)
                                .putExtra("Amount", amount));
                        completarPedido();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
    
    public void consultarRuc() {
        Log.i("consultarRuc", "CerrarPedidoActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://bootcampdojo.com/consulta_ruc.php?ruc=" + etRuc.getText().toString());
                Log.i("consultarRuc", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.length() > 0) {
                                    String razon_social = jsonObject.getString("razon_social");
                                    tvRazonSocial.setText(razon_social);
                                    tvRazonSocial.setEnabled(false);
                                    flayCargando.setVisibility(View.GONE);
                                } else {
                                    tvRazonSocial.setText("");
                                    tvRazonSocial.setEnabled(true);
                                    Toast.makeText(CerrarPedidoActivity.this, "No se encontraron datos de RUC", Toast.LENGTH_SHORT).show();
                                    flayCargando.setVisibility(View.GONE);
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
    
    public void completarPedido() {
        Log.i("completarPedido", "CerrarPedidoActivity");
        Date date = new Date();
        DateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat hora = new SimpleDateFormat("HH:mm");
        final String fechaSolicitado = fecha.format(date);
        final String horaSolicitado = hora.format(date);
        final String direccionOrigen = ProductoActivity.direccionTienda;
        final String latitudOrigen = ProductoActivity.latitudTienda;
        final String longitudOrigen = ProductoActivity.longitudTienda;
        final String direccionEntrega = etDireccionDestino.getText().toString();
        final Double latitudDestino = CategoriaActivity.latitud;
        final Double longitudDestino = CategoriaActivity.longitud;
        String ruc = "";
        String razonSocial = "";
        if (swFactura.isChecked()) {
            ruc = etRuc.getText().toString();
            razonSocial = tvRazonSocial.getText().toString();
        }
        final Double total = totalPedido;
        final String dniCliente = prefUtil.getStringValue("dni_cliente");
        final String idTienda = ProductoActivity.idTienda;
        final String idEstado = "5";
        final String nombreCliente = prefUtil.getStringValue("nombres_cliente") + " " + prefUtil.getStringValue("apellidos_cliente");
        final String nombreTienda = ProductoActivity.nombreTienda;
        final String finalRuc = ruc;
        final String finalRazonSocial = razonSocial;
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/registrar_pedido.php?fecha_solicitado=" + fechaSolicitado + "&hora_solicitado=" + horaSolicitado + "&direccion_origen="
                        + direccionOrigen + "&latitud_origen=" + latitudOrigen + "&longitud_origen=" + longitudOrigen + "&direccion_entrega=" + direccionEntrega + "&latitud_destino=" + latitudDestino
                        + "&longitud_destino=" + longitudDestino + "&ruc=" + finalRuc + "&razon_social=" + finalRazonSocial + "&total=" + total + "&dni_cliente=" + dniCliente + "&id_tienda=" + idTienda + "&id_estado="
                        + idEstado + "&nombre_cliente=" + nombreCliente + "&nombre_tienda=" + nombreTienda);
                Log.i("completarPedido", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            Intent intent = new Intent(CerrarPedidoActivity.this, ListoActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
