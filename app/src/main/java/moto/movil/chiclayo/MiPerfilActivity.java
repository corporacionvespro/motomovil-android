package moto.movil.chiclayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import moto.movil.chiclayo.publico.CircleTransform;
import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class MiPerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout dlayMenu;
    ImageView ivMenu, ivPerfil;
    LinearLayout llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    TextView tvNombreUsuario, tvDniPerfil, tvNombresPerfil, tvApellidosPerfil, tvCelularPerfil, tvCorreoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        tvNombreUsuario.setText("Hola\n" + prefUtil.getStringValue("nombres_cliente"));
        tvDniPerfil.setText(prefUtil.getStringValue("dni_cliente"));
        tvNombresPerfil.setText(prefUtil.getStringValue("nombres_cliente"));
        tvApellidosPerfil.setText(prefUtil.getStringValue("apellidos_cliente"));
        tvCelularPerfil.setText(prefUtil.getStringValue("celular_cliente"));
        tvCorreoPerfil.setText(prefUtil.getStringValue("correo_cliente"));
        if (!prefUtil.getStringValue("foto_cliente").equals("") && prefUtil.getStringValue("foto_cliente") != null) {
            Picasso.get().load(prefUtil.getStringValue("foto_cliente")).transform(new CircleTransform()).into(ivPerfil);
            ivPerfil.setPadding(0, 0, 0, 0);
        } else {
            ivPerfil.setImageDrawable(getDrawable(R.drawable.ic_perfil_defecto));
            ivPerfil.setPadding(0,100,0,0);
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivPerfil = (ImageView) findViewById(R.id.ivPerfil);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        tvApellidosPerfil = (TextView) findViewById(R.id.tvApellidosPerfil);
        tvCelularPerfil = (TextView) findViewById(R.id.tvCelularPerfil);
        tvCorreoPerfil = (TextView) findViewById(R.id.tvCorreoPerfil);
        tvDniPerfil = (TextView) findViewById(R.id.tvDniPerfil);
        tvNombresPerfil = (TextView) findViewById(R.id.tvNombresPerfil);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
    }
    
    public void initListener() {
        ivMenu.setOnClickListener(this);
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
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
                intent = new Intent(MiPerfilActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                intent = new Intent(MiPerfilActivity.this, MisPedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                dlayMenu.closeDrawer(GravityCompat.START);
                break;
            case R.id.navPreguntas:
                intent = new Intent(MiPerfilActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(MiPerfilActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        dlayMenu.closeDrawer(GravityCompat.START);
        return false;
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                Intent intent = new Intent(MiPerfilActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}