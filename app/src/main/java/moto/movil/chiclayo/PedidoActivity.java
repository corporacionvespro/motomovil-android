package moto.movil.chiclayo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import moto.movil.chiclayo.adapter.CarritoAdapter;
import moto.movil.chiclayo.entity.CarritoProducto;
import static moto.movil.chiclayo.ProductoActivity.productosCarrito;

/**
 * By: El Bryant
 */

public class PedidoActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<CarritoProducto> productos;
    Button btnRealizarPedido;
    public static CarritoAdapter carritoAdapter;
    RecyclerView rvCarrito;
    String nombreTienda;
    TextView tvNombreTienda;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initComponents();
        initListener();
        rvCarrito.setLayoutManager(new LinearLayoutManager(this));
        cargarProductos();
        if (getIntent().getExtras() != null) {
            nombreTienda = getIntent().getStringExtra("nombreTienda");
            tvNombreTienda.setText(nombreTienda);
        }
    }
    
    private void initListener() {
        btnRealizarPedido.setOnClickListener(this);
    }
    
    public void initComponents() {
        btnRealizarPedido = (Button) findViewById(R.id.btnRealizarPedido);
        rvCarrito = (RecyclerView) findViewById(R.id.rvCarrito);
        tvNombreTienda = (TextView) findViewById(R.id.tvNombreTienda);
    }
    
    public void cargarProductos() {
        Log.i("cargarProductos", "PedidoActivity");
        productos = new ArrayList<>();
        for (int i = 0; i < productosCarrito.size(); i++) {
            productos.add(new CarritoProducto(productosCarrito.get(i).getIdProducto(), productosCarrito.get(i).getNombre(), productosCarrito.get(i).getCantidad(),
                    productosCarrito.get(i).getPrecio()));
        }
        carritoAdapter = new CarritoAdapter(this, productos);
        rvCarrito.setAdapter(carritoAdapter);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRealizarPedido:
                Intent intent = new Intent(PedidoActivity.this, CerrarPedidoActivity.class);
                startActivity(intent);
                break;
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
