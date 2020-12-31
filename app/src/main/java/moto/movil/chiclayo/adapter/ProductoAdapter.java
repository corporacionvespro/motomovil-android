package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.entity.CarritoProducto;
import moto.movil.chiclayo.entity.Producto;
import static moto.movil.chiclayo.ProductoActivity.btnConfirmarPedido;
import static moto.movil.chiclayo.ProductoActivity.productosCarrito;
import static moto.movil.chiclayo.ProductoActivity.totalPedido;
import static moto.movil.chiclayo.ProductoActivity.tvTotalPedido;

/**
 * By: El Bryant
 */

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Producto> productos;
    private boolean existe = false;

    public ProductoAdapter(Activity activity, ArrayList<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_producto, parent, false);
        return new ProductoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Producto producto = productos.get(position);
        final String idProducto = producto.getIdProducto();
        final String nombreProducto = producto.getNombreProducto();
        final Double precioProducto = producto.getPrecioProducto();
        final String descripcionBreve = producto.getDescripcionBreve();
        final String descripcionDetallada = producto.getDescripcionDetallada();
        holder.tvIdProducto.setText(idProducto);
        holder.tvNombreProducto.setText(nombreProducto);
        holder.tvPrecio.setText("S/ " + String.format("%.2f", precioProducto));
        holder.tvDescripcionBreve.setText(descripcionBreve);
        holder.tvDescripcionDetallada.setText(descripcionDetallada);
        holder.ivAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existe = false;
                if (productosCarrito.size() > 0) {
                    for (int i = 0; i < productosCarrito.size(); i++) {
                        if (productosCarrito.get(i).getIdProducto().equals(producto.getIdProducto())) {
                            existe = true;
                        }
                    }
                }
                if (existe) {
                    productosCarrito.get(position).setCantidad(productosCarrito.get(position).getCantidad() + 1);
                } else {
                    productosCarrito.add(new CarritoProducto(producto.getIdProducto(), producto.getNombreProducto(), 1, producto.getPrecioProducto()));
                }
                totalPedido = totalPedido + producto.getPrecioProducto();
                tvTotalPedido.setText("S/ " + String.format("%.2f", totalPedido));
                btnConfirmarPedido.setVisibility(View.VISIBLE);
                tvTotalPedido.setVisibility(View.VISIBLE);
            }
        });
        holder.llayProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tvDescripcionDetallada.getVisibility() == View.VISIBLE) {
                    holder.tvDescripcionDetallada.setVisibility(View.GONE);
                } else if (holder.tvDescripcionDetallada.getVisibility() == View.GONE) {
                    holder.tvDescripcionDetallada.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAgregarCarrito;
        LinearLayout llayProducto;
        TextView tvNombreProducto, tvIdProducto, tvPrecio, tvDescripcionBreve, tvDescripcionDetallada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAgregarCarrito = (ImageView) itemView.findViewById(R.id.ivAgregarCarrito);
            llayProducto = (LinearLayout) itemView.findViewById(R.id.llayProducto);
            tvDescripcionBreve = (TextView) itemView.findViewById(R.id.tvDescripcionBreve);
            tvDescripcionDetallada = (TextView) itemView.findViewById(R.id.tvDescripcionDetallada);
            tvIdProducto = (TextView) itemView.findViewById(R.id.tvIdProducto);
            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecioProducto);
        }
    }
}
