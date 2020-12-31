package moto.movil.chiclayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class NotificacionesActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dlayMenu;
    ImageView ivMenu;
    LinearLayout llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        tvNombreUsuario.setText(prefUtil.getStringValue("nombres_cliente"));
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
    }
    
    public void initListener() {
        ivMenu.setOnClickListener(this);
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                Intent intent = new Intent(NotificacionesActivity.this, AccesoActivity.class);
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
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.navNotificaciones:
                dlayMenu.closeDrawer(GravityCompat.START);
                break;
            case R.id.navPedidos:
                intent = new Intent(NotificacionesActivity.this, MisPedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                intent = new Intent(NotificacionesActivity.this, MiPerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(NotificacionesActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(NotificacionesActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }
}