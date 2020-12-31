package moto.movil.chiclayo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class CourierActivity  extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.btnAceptar) Button btnAceptar;
    @BindView(R.id.btnCerrarTutorial) Button btnCerrarTutorial;
    @BindView(R.id.etCelularDestino) EditText etCelularDestino;
    @BindView(R.id.etCelularOrigen) EditText etCelularOrigen;
    @BindView(R.id.etDepartamento) EditText etDepartamento;
    @BindView(R.id.etDescripcion) EditText etDescripcion;
    @BindView(R.id.etDireccionOrigen) EditText etDireccionOrigen;
    @BindView(R.id.etDistrito) EditText etDistrito;
    @BindView(R.id.etNombreDestino) EditText etNombreDestino;
    @BindView(R.id.etNombreOrigen) EditText etNombreOrigen;
    @BindView(R.id.etProvincia) EditText etProvincia;
    @BindView(R.id.etValorProducto) EditText etValorProducto;
    @BindView(R.id.flayTutorial) FrameLayout flayTutorial;
    @BindView(R.id.rgDeposito) RadioGroup rgDeposito;
    @BindView(R.id.rbtEfectivo) RadioButton rbtEfectivo;
    @BindView(R.id.rbtDeposito) RadioButton rbtDeposito;
    @BindView(R.id.tvFechaRecojo) TextView tvFechaRecojo;
    @BindView(R.id.tvHoraRecojo) TextView tvHoraRecojo;
    Calendar calendar = Calendar.getInstance();
    DrawerLayout dlayMenu;
    LinearLayout llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    String tipo_pago = "EFECTIVO";
    TextView tvNombreUsuario;
    final int hora = calendar.get(Calendar.HOUR_OF_DAY), minuto = calendar.get(Calendar.MINUTE);
    int anioDesde, mesDesde, diaDesde, ultimoAnio, ultimoMes, ultimoDia;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public static TextView tvDireccionDestino;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        prefUtil = new PrefUtil(this);
        ButterKnife.bind(this);
        initComponents();
        initListener();
        if (prefUtil.getStringValue("tutorial").equals("hecho")) {
            flayTutorial.setVisibility(View.GONE);
        }
        ultimoAnio = calendar.get(Calendar.YEAR);
        ultimoMes = calendar.get(Calendar.MONTH);
        ultimoDia = calendar.get(Calendar.DAY_OF_MONTH);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        tvFechaRecojo.setText("Fecha de recojo: " + df.format(date));
        if (hora < 10 && minuto < 10) {
            tvHoraRecojo.setText("Hora de recojo: " + "0" + hora + ":" + "0" + minuto);
        } else if (hora < 10 && minuto >= 10) {
            tvHoraRecojo.setText("Hora de recojo: " + "0" + hora + ":" + minuto);
        } else if (hora >= 10 && minuto < 10) {
            tvHoraRecojo.setText("Hora de recojo: " + hora + ":" + "0" + minuto);
        } else {
            tvHoraRecojo.setText("Hora de recojo: " + hora + ":" + minuto);
        }
        tvNombreUsuario.setText(prefUtil.getStringValue("nombres_cliente"));
        etNombreOrigen.setText(prefUtil.getStringValue("nombres_cliente") + " " + prefUtil.getStringValue("apellidos_cliente"));
        etCelularOrigen.setText(prefUtil.getStringValue("celular_cliente"));
        etDireccionOrigen.setText(CategoriaActivity.direccionDestino);
    }

    @OnClick(R.id.tvFechaRecojo) void  getFechaDesde() {
        DatePickerDialog dialogPicker = new DatePickerDialog(this, (datePicker, year, month, day) -> MostrarDatosFechaDesde(year, month, day), ultimoAnio, ultimoMes, ultimoDia);
        dialogPicker.show();
    }

    @OnClick(R.id.tvHoraRecojo) void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String horaFormateada = "";
            String minutoFormateado = "";
            if (hourOfDay < 10) {
                horaFormateada = "0" + hourOfDay;
            } else {
                horaFormateada = String.valueOf(hourOfDay);
            }
            if (minute < 10) {
                minutoFormateado = "0" + minute;
            } else {
                minutoFormateado = String.valueOf(minute);
            }
            tvHoraRecojo.setText("Hora de recojo: " + horaFormateada + DOS_PUNTOS + minutoFormateado);
        }, hora, minuto, false);
        recogerHora.show();
    }

    @OnClick(R.id.tvDireccionDestino) void buscarDireccionDestino() {
        Intent intent = new Intent(CourierActivity.this, DireccionActivity.class);
        intent.putExtra("courier", "si");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @OnClick(R.id.btnCerrarTutorial) void cerrarTutorial() {
        prefUtil.saveGenericValue("tutorial", "hecho");
        flayTutorial.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnAceptar) void pedirCourier() {
        Log.i("pedirCourier", "CourierActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("http://vespro.io/motomovil-web/wsApp/enviar_courier.php?dni_cliente=" + prefUtil.getStringValue("dni_cliente") + "&telefono_origen="
                        + etCelularOrigen.getText().toString() + "&direccion_origen=" + etDireccionOrigen.getText().toString() + "&latitud_origen=" + prefUtil.getStringValue("latitud_origen")
                        + "&longitud_origen=" + prefUtil.getStringValue("longitud_origen") + "&tipo_pago=" + tipo_pago + "&nombre_destino=" + etNombreDestino.getText().toString() + "&telefono_destino="
                        + etCelularDestino.getText().toString() + "&fecha_recojo=" + tvFechaRecojo.getText().toString().substring(tvFechaRecojo.getText().toString().length() - 4,
                        tvFechaRecojo.getText().toString().length()) + "-" + tvFechaRecojo.getText().toString().substring(tvFechaRecojo.getText().toString().length() - 7,
                        tvFechaRecojo.getText().toString().length() - 5) + "-" + tvFechaRecojo.getText().toString().substring(tvFechaRecojo.getText().toString().length() - 10,
                        tvFechaRecojo.getText().toString().length() - 8) + "&hora_recojo=" + tvHoraRecojo.getText().toString().substring(tvHoraRecojo.getText().toString().length() - 5,
                        tvHoraRecojo.getText().toString().length()) + "&direccion_destino=" + tvDireccionDestino.getText().toString() + "&latitud_destino=" + prefUtil.getStringValue("latitud_destino")
                        + "&longitud_destino=" + prefUtil.getStringValue("longitud_destino") + "&departamento=" + etDepartamento.getText().toString() + "&provincia=" + etProvincia.getText().toString()
                        + "&distrito=" + etDistrito.getText().toString() + "&valor_producto=" + etValorProducto.getText().toString() + "&descripcion=" + etDescripcion.getText().toString());
                Log.i("pedirCourier", result);
                runOnUiThread(() -> {
                    int r = segundo(result);
                    if (r > 0) {
                        Intent intent = new Intent(CourierActivity.this, ListoActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });
            }
        };
        tr.start();
    }

    @SuppressLint("SetTextI18n")
    public void MostrarDatosFechaDesde(int year, int month, int day){
        diaDesde = day;
        mesDesde = month;
        anioDesde = year;
        tvFechaRecojo.setText(day + "-" + (month + 1) + "-" + year);
    }

    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        tvDireccionDestino = (TextView) findViewById(R.id.tvDireccionDestino);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
    }

    public void initListener() {
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
        rbtDeposito.setOnClickListener(this);
        rbtEfectivo.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                Intent intent = new Intent(CourierActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.rbtDeposito:
                rgDeposito.setVisibility(View.VISIBLE);
                break;
            case R.id.rbtEfectivo:
                rgDeposito.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navCategorias:
                intent = new Intent(CourierActivity.this, CategoriaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navNotificaciones:
                intent = new Intent(CourierActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navPedidos:
                intent = new Intent(CourierActivity.this, MisPedidosActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navPerfil:
                intent = new Intent(CourierActivity.this, MiPerfilActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navPreguntas:
                intent = new Intent(CourierActivity.this, PreguntasActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navPromociones:
                intent = new Intent(CourierActivity.this, PromocionesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        dlayMenu.closeDrawer(GravityCompat.START);
        return false;
    }
}
