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

import moto.movil.chiclayo.PedidoActivity;
import moto.movil.chiclayo.ProductoActivity;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.entity.CarritoProducto;
import static moto.movil.chiclayo.ProductoActivity.btnConfirmarPedido;
import static moto.movil.chiclayo.ProductoActivity.productosCarrito;
import static moto.movil.chiclayo.ProductoActivity.totalPedido;
import static moto.movil.chiclayo.ProductoActivity.tvTotalPedido;

/**
 * By: El Bryant
 */

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    Activity activity;
    ArrayList<CarritoProducto> productos;
    
    public CarritoAdapter(Activity activity, ArrayList<CarritoProducto> productos) {
        this.activity = activity;
        this.productos = productos;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_carrito, parent, false);
        return new CarritoAdapter.ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CarritoProducto producto = productos.get(position);
        holder.tvIdProducto.setText(producto.getIdProducto());
        holder.tvNombreProducto.setText(producto.getNombre());
        holder.tvPrecioProducto.setText("S/ " + String.format("%.2f", producto.getPrecio()));
        holder.tvCantidadProducto.setText("" + producto.getCantidad());
        holder.ivMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPedido -= productosCarrito.get(position).getPrecio();
                tvTotalPedido.setText("S/ " + String.format("%.2f", ProductoActivity.totalPedido));
                if (totalPedido == 0.0) {
                    tvTotalPedido.setVisibility(View.GONE);
                    btnConfirmarPedido.setVisibility(View.GONE);
                } else {
                    tvTotalPedido.setVisibility(View.VISIBLE);
                    btnConfirmarPedido.setVisibility(View.VISIBLE);
                }
                holder.tvCantidadProducto.setText("" + (productosCarrito.get(position).getCantidad() - 1));
                if (productosCarrito.get(position).getCantidad() > 1) {
                    productosCarrito.get(position).setCantidad(productosCarrito.get(position).getCantidad() - 1);
                } else {
                    productosCarrito.remove(position);
                    holder.llayItemCarrito.removeAllViews();
                    PedidoActivity.carritoAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return productos.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMenos;
        LinearLayout llayItemCarrito;
        TextView tvIdProducto, tvNombreProducto, tvPrecioProducto, tvCantidadProducto;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMenos = (ImageView) itemView.findViewById(R.id.ivMenos);
            llayItemCarrito = (LinearLayout) itemView.findViewById(R.id.llayItemCarrito);
            tvCantidadProducto = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
            tvIdProducto = (TextView) itemView.findViewById(R.id.tvIdProducto);
            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = (TextView) itemView.findViewById(R.id.tvPrecioProducto);
        }
    }
}
