package moto.movil.chiclayo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import moto.movil.chiclayo.adapter.HistorialAdapter;
import moto.movil.chiclayo.entity.Pedido;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class HistorialActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Pedido> pedidos;
    Button btnConsultarHistorial;
    Calendar calendar = Calendar.getInstance();
    CardView cvFechaDesde, cvFechaHasta;
    FrameLayout flayCargando;
    HistorialAdapter historialAdapter;
    int anioDesde, mesDesde, diaDesde, anioHasta, mesHasta, diaHasta, ultimoAnio, ultimoMes, ultimoDia;
    LinearLayout llayFechaDesde, llayFechaHasta;
    PrefUtil prefUtil;
    RecyclerView rvHistorial;
    String fechaDesde, fechaHasta;
    TextView tvTitular, tvFechaDesde, tvFechaHasta;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        ultimoAnio = calendar.get(Calendar.YEAR);
        ultimoMes = calendar.get(Calendar.MONTH);
        ultimoDia = calendar.get(Calendar.DAY_OF_MONTH);
        if (getIntent().getExtras() != null) {
            fechaDesde = getIntent().getStringExtra("fecha_desde");
            fechaHasta = getIntent().getStringExtra("fecha_hasta");
            tvTitular.setText(getIntent().getStringExtra("titular"));
            if (getIntent().getStringExtra("titular").equals("Entre fechas")) {
                cvFechaDesde.setVisibility(View.VISIBLE);
                cvFechaHasta.setVisibility(View.VISIBLE);
                llayFechaDesde.setVisibility(View.VISIBLE);
                llayFechaHasta.setVisibility(View.VISIBLE);
                btnConsultarHistorial.setVisibility(View.VISIBLE);
                rvHistorial.setVisibility(View.GONE);
            }
        }
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));
        cargarHistorial();
    }
    
    public void initComponents() {
        btnConsultarHistorial = (Button) findViewById(R.id.btnConsultarHistorial);
        cvFechaDesde = (CardView) findViewById(R.id.cvFechaDesde);
        cvFechaHasta = (CardView) findViewById(R.id.cvFechaHasta);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
        tvFechaDesde = (TextView) findViewById(R.id.tvFechaDesde);
        tvFechaHasta = (TextView) findViewById(R.id.tvFechaHasta);
        llayFechaDesde = (LinearLayout) findViewById(R.id.llayFechaDesde);
        llayFechaHasta = (LinearLayout) findViewById(R.id.llayFechaHasta);
        rvHistorial = (RecyclerView) findViewById(R.id.rvHistorial);
        tvTitular = (TextView) findViewById(R.id.tvTitular);
    }
    
    public void initListener() {
        btnConsultarHistorial.setOnClickListener(this);
        tvFechaDesde.setOnClickListener(this);
        tvFechaHasta.setOnClickListener(this);
        cvFechaDesde.setOnClickListener(this);
        cvFechaHasta.setOnClickListener(this);
    }
    
    public void cargarHistorial() {
        Log.i("cargarhistorial", "HistorialActivity");
        pedidos = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_historial_cliente.php?dni_cliente=" + prefUtil.getStringValue("dni_cliente") + "&fecha_desde=" + fechaDesde
                        + "&fecha_hasta=" + fechaHasta);
                Log.i("cargarHistorial", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String idPedido = jsonArray.getJSONObject(i).getString("id_pedido");
                                    String fechaSolicitado = jsonArray.getJSONObject(i).getString("fecha_solicitado");
                                    String horaSolicitado = jsonArray.getJSONObject(i).getString("hora_solicitado");
                                    String fechaEntregado = jsonArray.getJSONObject(i).getString("fecha_entregado");
                                    String horaEntregado = jsonArray.getJSONObject(i).getString("hora_entregado");
                                    String direccionOrigen = jsonArray.getJSONObject(i).getString("direccion_origen");
                                    String latitudOrigen = jsonArray.getJSONObject(i).getString("latitud_origen");
                                    String longitudOrigen = jsonArray.getJSONObject(i).getString("longitud_origen");
                                    String direccionEntrega = jsonArray.getJSONObject(i).getString("direccion_entrega");
                                    String latitudDestino = jsonArray.getJSONObject(i).getString("latitud_destino");
                                    String longitudDestino = jsonArray.getJSONObject(i).getString("longitud_destino");
                                    Double total = jsonArray.getJSONObject(i).getDouble("total");
                                    String dniRepartidor = jsonArray.getJSONObject(i).getString("dni_repartidor");
                                    String idTienda = jsonArray.getJSONObject(i).getString("id_tienda");
                                    String estado = jsonArray.getJSONObject(i).getString("estado");
                                    String nombreRepartidor = jsonArray.getJSONObject(i).getString("nombre_repartidor");
                                    String nombreTienda = jsonArray.getJSONObject(i).getString("nombre_tienda");
                                    pedidos.add(new Pedido(idPedido, fechaSolicitado, horaSolicitado, fechaEntregado, horaEntregado, direccionOrigen, latitudOrigen, longitudOrigen, direccionEntrega, latitudDestino,
                                            longitudDestino, total, dniRepartidor, idTienda, estado,nombreRepartidor, nombreTienda));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        historialAdapter = new HistorialAdapter(HistorialActivity.this, pedidos);
                        rvHistorial.setAdapter(historialAdapter);
                        MisPedidosActivity.flayCargando.setVisibility(View.GONE);
                        flayCargando.setVisibility(View.GONE);
                    }
                });
            }
        };
        tr.start();
    }
    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.transition.explode);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConsultarHistorial:
                flayCargando.setVisibility(View.VISIBLE);
                fechaDesde = tvFechaDesde.getText().toString();
                fechaHasta = tvFechaHasta.getText().toString();
                rvHistorial.setVisibility(View.VISIBLE);
                cargarHistorial();
                break;
            case R.id.tvFechaDesde:
            case R.id.cvFechaDesde:
                getFechaDesde();
                break;
            case R.id.tvFechaHasta:
            case R.id.cvFechaHasta:
                getFechaHasta();
                break;
        }
    }
    
    public void getFechaDesde() {
        DatePickerDialog dialogPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                MostrarDatosFechaDesde(year, month, day);
            }
        }, ultimoAnio, ultimoMes, ultimoDia);
        dialogPicker.show();
    }
    
    @SuppressLint("SetTextI18n")
    public void MostrarDatosFechaDesde(int year, int month, int day){
        diaDesde = day;
        mesDesde = month;
        anioDesde = year;
        tvFechaDesde.setText(year + "-" + (month + 1) + "-" + day);
    }
    
    public void getFechaHasta(){
        DatePickerDialog dialogPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                MostrarDatosFechaHasta(year, month, day);
            }
        }, ultimoAnio, ultimoMes, ultimoDia);
        dialogPicker.show();
    }
    
    @SuppressLint("SetTextI18n")
    public void MostrarDatosFechaHasta(int year, int month, int day){
        diaHasta = day;
        mesHasta = month;
        anioHasta = year;
        tvFechaHasta.setText(year + "-" + (month + 1) + "-" + day);
    }
}
