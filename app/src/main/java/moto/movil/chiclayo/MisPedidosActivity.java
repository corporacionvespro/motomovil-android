package moto.movil.chiclayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import java.util.Calendar;
import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class MisPedidosActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dlayMenu;
    ImageView ivMenu;
    int anioDesde, anioHasta, mesDesde, mesHasta, diaDesde, diaHasta;
    LinearLayout llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    public static FrameLayout flayCargando;
    RelativeLayout rlayHoy, rlaySemana, rlayMes, rlayAnio, rlayEntreFechas;
    TextView tvNombreUsuario, tvHoy, tvSemana, tvMes, tvAnio, tvEntreFechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pedidos);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        tvNombreUsuario.setText("Hola\n" + prefUtil.getStringValue("nombres_cliente"));
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        rlayAnio = (RelativeLayout) findViewById(R.id.rlayAnio);
        rlayEntreFechas = (RelativeLayout) findViewById(R.id.rlayEntreFechas);
        rlayHoy = (RelativeLayout) findViewById(R.id.rlayHoy);
        rlayMes = (RelativeLayout) findViewById(R.id.rlayMes);
        rlaySemana = (RelativeLayout) findViewById(R.id.rlaySemana);
        tvAnio = (TextView) findViewById(R.id.tvAnio);
        tvEntreFechas = (TextView) findViewById(R.id.tvEntreFechas);
        tvHoy = (TextView) findViewById(R.id.tvHoy);
        tvMes = (TextView) findViewById(R.id.tvMes);
        tvSemana = (TextView) findViewById(R.id.tvSemana);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
    }
    
    public void initListener() {
        ivMenu.setOnClickListener(this);
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
        rlayAnio.setOnClickListener(this);
        rlayEntreFechas.setOnClickListener(this);
        rlayHoy.setOnClickListener(this);
        rlayMes.setOnClickListener(this);
        rlaySemana.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        Intent intent;
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                intent = new Intent(MisPedidosActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.rlayAnio:
                flayCargando.setVisibility(View.VISIBLE);
                anioDesde = calendar.get(Calendar.YEAR);
                mesDesde = 1;
                diaDesde = 1;
                anioHasta = calendar.get(Calendar.YEAR);
                mesHasta = calendar.get(Calendar.MONTH);
                diaHasta = calendar.get(Calendar.DAY_OF_MONTH);
                consultarHistorial("Este año", tvAnio);
                break;
            case R.id.rlayHoy:
                flayCargando.setVisibility(View.VISIBLE);
                anioDesde = calendar.get(Calendar.YEAR);
                mesDesde = calendar.get(Calendar.MONTH) + 1;
                diaDesde = calendar.get(Calendar.DAY_OF_MONTH);
                anioHasta = calendar.get(Calendar.YEAR);
                mesHasta = calendar.get(Calendar.MONTH);
                diaHasta = calendar.get(Calendar.DAY_OF_MONTH);
                consultarHistorial("Hoy", tvHoy);
                break;
            case R.id.rlayMes:
                flayCargando.setVisibility(View.VISIBLE);
                anioDesde = calendar.get(Calendar.YEAR);
                mesDesde = calendar.get(Calendar.MONTH) + 1;
                diaDesde = 1;
                anioHasta = calendar.get(Calendar.YEAR);
                mesHasta = calendar.get(Calendar.MONTH);
                diaHasta = calendar.get(Calendar.DAY_OF_MONTH);
                consultarHistorial("Este mes", tvMes);
                break;
            case R.id.rlaySemana:
                flayCargando.setVisibility(View.VISIBLE);
                anioHasta = calendar.get(Calendar.YEAR);
                mesHasta = calendar.get(Calendar.MONTH);
                diaHasta = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                anioDesde = calendar.get(Calendar.YEAR);
                mesDesde = calendar.get(Calendar.MONTH) + 1;
                diaDesde = calendar.get(Calendar.DAY_OF_MONTH);
                consultarHistorial("Esta semana", tvSemana);
                break;
            case R.id.rlayEntreFechas:
                flayCargando.setVisibility(View.VISIBLE);
                anioHasta = calendar.get(Calendar.YEAR);
                mesHasta = calendar.get(Calendar.MONTH);
                diaHasta = calendar.get(Calendar.DAY_OF_MONTH);
                anioDesde = calendar.get(Calendar.YEAR);
                mesDesde = calendar.get(Calendar.MONTH);
                diaDesde = calendar.get(Calendar.DAY_OF_MONTH);
                consultarHistorial("Entre fechas", tvEntreFechas);
                break;
                
        }
    }
    
    public void consultarHistorial(String titular, TextView textView) {
        String fechaDesde = anioDesde + "-" + mesDesde + "-" + diaDesde;
        String fechaHasta = anioHasta + "-" + (mesHasta + 1) + "-" + diaHasta;
        Intent intent = new Intent(MisPedidosActivity.this, HistorialActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create((View) textView, textView.getTransitionName()));
        intent.putExtra("fecha_desde", fechaDesde);
        intent.putExtra("fecha_hasta", fechaHasta);
        intent.putExtra("titular", titular);
        startActivity(intent, options.toBundle());
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navCategorias:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navNotificaciones:
                intent = new Intent(MisPedidosActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                dlayMenu.closeDrawer(GravityCompat.START);
                break;
            case R.id.navPerfil:
                intent = new Intent(MisPedidosActivity.this, MiPerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(MisPedidosActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(MisPedidosActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }
}