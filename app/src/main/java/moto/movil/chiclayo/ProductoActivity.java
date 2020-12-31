package moto.movil.chiclayo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import java.util.ArrayList;
import moto.movil.chiclayo.adapter.ProductoAdapter;
import moto.movil.chiclayo.entity.CarritoProducto;
import moto.movil.chiclayo.entity.CarritoSubproducto;
import moto.movil.chiclayo.entity.Producto;
import moto.movil.chiclayo.publico.CircleTransform;
import static moto.movil.chiclayo.publico.Funciones.primero;
import static moto.movil.chiclayo.publico.Funciones.segundo;

/**
 * By: El Bryant
 */

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Producto> productos;
    DrawerLayout dlayMenu;
    ImageView ivLogoTienda, ivImagenTienda;
    NavigationView nvMenu;
    ProductoAdapter productoAdapter;
    public static ArrayList<CarritoProducto> productosCarrito = new ArrayList<>();
    public static ArrayList<CarritoSubproducto> subproductosCarrito;
    public static Button btnConfirmarPedido;
    public static Double totalPedido = 0.0;
    public static String idTienda, direccionTienda, latitudTienda, longitudTienda, nombreTienda;
    public static TextView tvTotalPedido;
    RecyclerView rvProductos;
    TextView tvNombreTienda, tvSubcategorias;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initComponents();
        initListener();
        if (getIntent().getExtras() != null) {
            idTienda = getIntent().getStringExtra("id_tienda");
            Picasso.get().load(getIntent().getStringExtra("logo_tienda")).transform(new CircleTransform()).into(ivLogoTienda);
            Picasso.get().load(getIntent().getStringExtra("imagen_tienda")).into(ivImagenTienda);
            tvNombreTienda.setText(getIntent().getStringExtra("nombre_tienda"));
            direccionTienda = getIntent().getStringExtra("direccion_tienda");
            latitudTienda = getIntent().getStringExtra("latitud_tienda");
            longitudTienda = getIntent().getStringExtra("longitud_tienda");
            nombreTienda = getIntent().getStringExtra("nombre_tienda");
        }
        if (idTienda != null && !idTienda.equals("0") && !idTienda.equals("")) {
            cargarProductos();
            obtenerSubcategorias();
        }
        if (totalPedido > 0.0) {
            tvTotalPedido.setVisibility(View.VISIBLE);
            tvTotalPedido.setText("S/ " + String.format("%.2f", totalPedido));
            btnConfirmarPedido.setVisibility(View.VISIBLE);
        }
    }
    
    public void initComponents() {
        btnConfirmarPedido = (Button) findViewById(R.id.btnConfirmarPedido);
        dlayMenu = (DrawerLayout) findViewById(R.id.dlayMenu);
        ivImagenTienda = (ImageView) findViewById(R.id.ivImagenTienda);
        ivLogoTienda = (ImageView) findViewById(R.id.ivLogoTienda);
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        rvProductos = (RecyclerView) findViewById(R.id.rvProductos);
        tvNombreTienda = (TextView) findViewById(R.id.tvNombreTienda);
        tvSubcategorias = (TextView) findViewById(R.id.tvSubcategorias);
        tvTotalPedido = (TextView) findViewById(R.id.tvTotalPedido);
    }
    
    public void initListener() {
        btnConfirmarPedido.setOnClickListener(this);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
    }
    
    public void cargarProductos() {
        Log.i("cargarProductos", "ProductoActivity");
        productos = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_productos.php?id_tienda=" + idTienda);
                Log.i("cargarProductos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String idProducto = jsonArray.getJSONObject(i).getString("id_producto");
                                    String nombreProducto = jsonArray.getJSONObject(i).getString("nombre");
                                    Double precioProducto = jsonArray.getJSONObject(i).getDouble("precio");
                                    String descripcionBreve = jsonArray.getJSONObject(i).getString("descripcion_breve");
                                    String descripcionDetallada = jsonArray.getJSONObject(i).getString("descripcion_detallada");
                                    productos.add(new Producto(idProducto, nombreProducto, precioProducto, descripcionBreve, descripcionDetallada));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        productoAdapter = new ProductoAdapter(ProductoActivity.this, productos);
                        rvProductos.setAdapter(productoAdapter);
                    }
                });
            }
        };
        tr.start();
    }
    
    public void obtenerSubcategorias() {
        Log.i("obtenerSubcategorias", "ProductoActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/motomovil-web/wsApp/obtener_subcategorias_tienda.php?id_tienda=" + idTienda);
                Log.i("obtenerSubcategorias", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String subcategoria = jsonArray.getJSONObject(i).getString("nombre");
                                        tvSubcategorias.setText(tvSubcategorias.getText().toString() + " - " + subcategoria);
                                    }
                                    tvSubcategorias.setText(tvSubcategorias.getText().toString().substring(2, tvSubcategorias.getText().toString().length()));
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
    
    @Override
    public void onBackPressed() {
        productosCarrito.clear();
        totalPedido = 0.0;
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmarPedido:
                Intent intent = new Intent(ProductoActivity.this, PedidoActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create((View) tvNombreTienda, tvNombreTienda.getTransitionName()));
                intent.putExtra("nombreTienda", tvNombreTienda.getText().toString());
                startActivity(intent, options.toBundle());
                break;
        }
    }
}
