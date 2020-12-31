package moto.movil.chiclayo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Objects;
import moto.movil.chiclayo.adapter.SubcategoriaAdapter;
import moto.movil.chiclayo.adapter.TiendaAdapter;
import moto.movil.chiclayo.entity.Subcategoria;
import moto.movil.chiclayo.entity.Tienda;
import moto.movil.chiclayo.publico.PrefUtil;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class TiendaActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener {
    ArrayList<Subcategoria> subcategorias;
    ArrayList<Tienda> tiendas;
    DrawerLayout dlayMenu;
    EditText etBusqueda;
    FrameLayout flayCargando;
    ImageView ivMenu, ivIconoCategoria, ivIconoBusqueda;
    LinearLayout llaySubcategoria, llayCerrarSesion;
    NavigationView nvMenu;
    PrefUtil prefUtil;
    public static TextView tvNombreSubcategoria, tvIdSubcategoria;
    RecyclerView rvSubcategorias, rvTiendas;
    static String idCategoria;
    SubcategoriaAdapter subcategoriaAdapter;
    TextView tvNombreUsuario, tvNombreCategoria;
    TiendaAdapter tiendaAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        prefUtil = new PrefUtil(this);
        initComponents();
        initListener();
        tvNombreUsuario.setText(prefUtil.getStringValue("nombres_cliente"));
        if (getIntent().getExtras() != null) {
            cargarSubcategorias();
            if (getIntent().getStringExtra("idCategoria").equals("0")) {
                ivIconoBusqueda.setVisibility(View.VISIBLE);
                ivIconoCategoria.setVisibility(View.GONE);
                buscarProductoTienda();
            } else {
                ivIconoCategoria.setVisibility(View.VISIBLE);
                ivIconoBusqueda.setVisibility(View.GONE);
                cargarTiendas();
            }
        }
    }
    
    public void initComponents() {
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        etBusqueda = (EditText) findViewById(R.id.etBusqueda);
        flayCargando = (FrameLayout) findViewById(R.id.flayCargando);
        ivIconoBusqueda = (ImageView) findViewById(R.id.ivIconoBusqueda);
        ivIconoCategoria = (ImageView) findViewById(R.id.ivIconoCategoria);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        llayCerrarSesion = (LinearLayout) findViewById(R.id.llayCerrarSesion);
        llaySubcategoria = (LinearLayout) findViewById(R.id.llaySubcategoria);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        rvSubcategorias = (RecyclerView) findViewById(R.id.rvSubcategorias);
        rvTiendas = (RecyclerView) findViewById(R.id.rvTiendas);
        tvIdSubcategoria = (TextView) findViewById(R.id.tvIdSubcategoria);
        tvNombreCategoria = (TextView) findViewById(R.id.tvNombreCategoria);
        tvNombreSubcategoria = (TextView) findViewById(R.id.tvNombreSubcategoria);
        tvNombreUsuario = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvNombreUsuario);
    }
    
    public void initListener() {
        etBusqueda.setOnEditorActionListener(this);
        ivMenu.setOnClickListener(this);
        llayCerrarSesion.setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
        rvSubcategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTiendas.setLayoutManager(new LinearLayoutManager(this));
        tvIdSubcategoria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                obtenerTiendas();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        tvNombreCategoria.setOnClickListener(this);
        tvNombreSubcategoria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subcategoriaAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    
    public void cargarSubcategorias() {
        Log.i("cargarSubcategorias", "TiendaActivity");
        if (getIntent().getExtras() != null) {
            idCategoria = getIntent().getStringExtra("idCategoria");
            tvNombreCategoria.setText(getIntent().getStringExtra("nombreCategoria"));
            Picasso.get().load(getIntent().getStringExtra("iconoCategoria")).into(ivIconoCategoria);
        }
        subcategorias = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_subcategorias.php?id_categoria=" + idCategoria);
                Log.i("cargarSubcategorias", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String idSubcategoria = jsonArray.getJSONObject(i).getString("id_subcategoria");
                                        String nombre = jsonArray.getJSONObject(i).getString("nombre");
                                        String imagen = jsonArray.getJSONObject(i).getString("imagen");
                                        subcategorias.add(new Subcategoria(idSubcategoria, nombre, imagen));
                                    }
                                    llaySubcategoria.setVisibility(View.VISIBLE);
                                } else {
                                    llaySubcategoria.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        subcategoriaAdapter = new SubcategoriaAdapter(TiendaActivity.this, subcategorias);
                        rvSubcategorias.setAdapter(subcategoriaAdapter);
                    }
                });
            }
        };
        tr.start();
    }
    
    public void cargarTiendas() {
        Log.i("cargarTiendas", "TiendaActivity");
        tiendas = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/cargar_tiendas.php?id_categoria=" + idCategoria);
                Log.i("cargarTiendas", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String idTienda = jsonArray.getJSONObject(i).getString("id_tienda");
                                    String nombreTienda = jsonArray.getJSONObject(i).getString("nombre");
                                    String direccionTienda = jsonArray.getJSONObject(i).getString("direccion");
                                    String latitudTienda = jsonArray.getJSONObject(i).getString("latitud");
                                    String longitudTienda = jsonArray.getJSONObject(i).getString("longitud");
                                    String logoTienda = jsonArray.getJSONObject(i).getString("logo");
                                    String imagenTienda = jsonArray.getJSONObject(i).getString("imagen_destacada");
                                    tiendas.add(new Tienda(idTienda, nombreTienda, direccionTienda, latitudTienda, longitudTienda, logoTienda, imagenTienda));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tiendaAdapter = new TiendaAdapter(TiendaActivity.this, tiendas);
                        rvTiendas.setAdapter(tiendaAdapter);
                        flayCargando.setVisibility(View.GONE);
                    }
                });
            }
        };
        tr.start();
    }
    
    public void obtenerTiendas() {
        Log.i("obtenerTiendas", "TiendaActivity");
        tiendas = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_tiendas.php?id_subcategoria=" + tvIdSubcategoria.getText().toString());
                Log.i("obtenerTiendas", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String idTienda = jsonArray.getJSONObject(i).getString("id_tienda");
                                    String nombreTienda = jsonArray.getJSONObject(i).getString("nombre");
                                    String direccionTienda = jsonArray.getJSONObject(i).getString("direccion");
                                    String latitudTienda = jsonArray.getJSONObject(i).getString("latitud");
                                    String longitudTienda = jsonArray.getJSONObject(i).getString("longitud");
                                    String logoTienda = jsonArray.getJSONObject(i).getString("logo");
                                    String imagenTienda = jsonArray.getJSONObject(i).getString("imagen_destacada");
                                    tiendas.add(new Tienda(idTienda, nombreTienda, direccionTienda, latitudTienda, longitudTienda, logoTienda, imagenTienda));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tiendaAdapter = new TiendaAdapter(TiendaActivity.this, tiendas);
                        rvTiendas.setAdapter(tiendaAdapter);
                        flayCargando.setVisibility(View.GONE);
                    }
                });
            }
        };
        tr.start();
    }
    
    public void buscarProductoTienda() {
        Log.i("buscarProductoTienda", "TiendaActivity");
        tiendas = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_producto_tienda.php?clave=" + etBusqueda.getText().toString());
                Log.i("buscarProductoTienda", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String idTienda = jsonArray.getJSONObject(i).getString("id_tienda");
                                    String nombreTienda = jsonArray.getJSONObject(i).getString("nombre");
                                    String direccionTienda = jsonArray.getJSONObject(i).getString("direccion");
                                    String latitudTienda = jsonArray.getJSONObject(i).getString("latitud");
                                    String longitudTienda = jsonArray.getJSONObject(i).getString("longitud");
                                    String logoTienda = jsonArray.getJSONObject(i).getString("logo");
                                    String imagenTienda = jsonArray.getJSONObject(i).getString("imagen_destacada");
                                    tiendas.add(new Tienda(idTienda, nombreTienda, direccionTienda, latitudTienda, longitudTienda, logoTienda, imagenTienda));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tiendaAdapter = new TiendaAdapter(TiendaActivity.this, tiendas);
                        rvTiendas.setAdapter(tiendaAdapter);
                        flayCargando.setVisibility(View.GONE);
                    }
                });
            }
        };
        tr.start();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                dlayMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.llayCerrarSesion:
                prefUtil.clearAll();
                Intent intent = new Intent(TiendaActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.tvNombreCategoria:
                if (Objects.requireNonNull(getIntent().getStringExtra("idCategoria")).equals("0")) {
                    etBusqueda.setText(tvNombreCategoria.getText().toString());
                    etBusqueda.setVisibility(View.VISIBLE);
                    tvNombreCategoria.setVisibility(View.GONE);
                }
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navCategorias:
                intent = new Intent(TiendaActivity.this, CategoriaActivity.class);
                startActivity(intent);
                break;
            case R.id.navNotificaciones:
                intent = new Intent(TiendaActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                break;
            case R.id.navPedidos:
                intent = new Intent(TiendaActivity.this, MisPedidosActivity.class);
                startActivity(intent);
                break;
            case R.id.navPerfil:
                intent = new Intent(TiendaActivity.this, MiPerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.navPreguntas:
                intent = new Intent(TiendaActivity.this, PreguntasActivity.class);
                startActivity(intent);
                break;
            case R.id.navPromociones:
                intent = new Intent(TiendaActivity.this, PromocionesActivity.class);
                startActivity(intent);
                break;
        }
        dlayMenu.closeDrawer(GravityCompat.START);
        return false;
    }
    
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.etBusqueda:
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    buscarProductoTienda();
                }
        }
        return false;
    }
}
