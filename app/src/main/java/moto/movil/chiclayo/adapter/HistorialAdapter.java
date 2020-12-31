package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.entity.Pedido;

/**
 * By: El Bryant
 */

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Pedido> pedidos;
    
    public HistorialAdapter(Activity activity, ArrayList<Pedido> pedidos) {
        this.activity = activity;
        this.pedidos = pedidos;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_historial, parent, false);
        return new HistorialAdapter.ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvIdPedido.setText(pedido.getIdPedido());
        holder.tvFechaSolicitado.setText(pedido.getFechaSolicitado());
        holder.tvHoraSolicitado.setText(pedido.getHoraSolicitado());
        holder.tvFechaEntregado.setText(pedido.getFechaEntregado());
        holder.tvHoraEntregado.setText(pedido.getHoraEntregado());
        holder.tvDireccionOrigen.setText(pedido.getDireccionOrigen());
        holder.tvLatitudOrigen.setText(pedido.getLatitudOrigen());
        holder.tvLongitudOrigen.setText(pedido.getLongitudOrigen());
        holder.tvDireccionEntrega.setText(pedido.getDireccionEntrega());
        holder.tvLatitudDestino.setText(pedido.getLatitudDestino());
        holder.tvLongitudDestino.setText(pedido.getLongitudDestino());
        holder.tvTotal.setText("S/ " + String.format("%.2f", pedido.getTotal()));
        holder.tvDniRepartidor.setText(pedido.getDniRepartidor());
        holder.tvIdTienda.setText(pedido.getIdTienda());
        holder.tvEstado.setText(pedido.getEstado());
        holder.tvNombreRepartidor.setText(pedido.getNombreRepartidor());
        holder.tvNombreTienda.setText(pedido.getNombreTienda());
    }
    
    @Override
    public int getItemCount() {
        return pedidos.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvFechaSolicitado, tvHoraSolicitado, tvFechaEntregado, tvHoraEntregado, tvDireccionOrigen, tvLatitudOrigen, tvLongitudOrigen, tvDireccionEntrega, tvLatitudDestino, tvLongitudDestino,
                tvTotal, tvDniRepartidor, tvIdTienda, tvEstado, tvNombreRepartidor, tvNombreTienda;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdPedido = (TextView) itemView.findViewById(R.id.tvIdPedido);
            tvFechaSolicitado = (TextView) itemView.findViewById(R.id.tvFechaSolicitado);
            tvHoraSolicitado = (TextView) itemView.findViewById(R.id.tvHoraSolicitado);
            tvFechaEntregado = (TextView) itemView.findViewById(R.id.tvFechaEntregado);
            tvHoraEntregado = (TextView) itemView.findViewById(R.id.tvHoraEntregado);
            tvDireccionOrigen = (TextView) itemView.findViewById(R.id.tvDireccionOrigen);
            tvLatitudOrigen = (TextView) itemView.findViewById(R.id.tvLatitudOrigen);
            tvLongitudOrigen = (TextView) itemView.findViewById(R.id.tvLongitudOrigen);
            tvDireccionEntrega = (TextView) itemView.findViewById(R.id.tvDireccionEntrega);
            tvLatitudDestino = (TextView) itemView.findViewById(R.id.tvLatitudDestino);
            tvLongitudDestino = (TextView) itemView.findViewById(R.id.tvLongitudDestino);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
            tvDniRepartidor = (TextView) itemView.findViewById(R.id.tvdniRepartidor);
            tvIdTienda = (TextView) itemView.findViewById(R.id.tvIdTienda);
            tvEstado = (TextView) itemView.findViewById(R.id.tvEstado);
            tvNombreRepartidor = (TextView) itemView.findViewById(R.id.tvNombreRepartidor);
            tvNombreTienda = (TextView) itemView.findViewById(R.id.tvNombreTienda);
        }
    }
}
