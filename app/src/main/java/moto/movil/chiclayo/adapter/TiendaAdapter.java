package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import moto.movil.chiclayo.ProductoActivity;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.entity.Tienda;
import moto.movil.chiclayo.publico.CircleTransform;

/**
 * By: El Bryant
 */

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Tienda> tiendas;
    
    public TiendaAdapter(Activity activity, ArrayList<Tienda> tiendas) {
        this.activity = activity;
        this.tiendas = tiendas;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_tienda, parent, false);
        return new TiendaAdapter.ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Tienda tienda = tiendas.get(position);
        Picasso.get().load(tienda.getImagenTienda()).placeholder(R.drawable.boton_verde).into(holder.ivImagenTienda);
        Picasso.get().load(tienda.getLogoTienda()).transform(new CircleTransform()).into(holder.ivLogoTienda);
        holder.tvIdTienda.setText(tienda.getIdTienda());
        holder.tvNombreTienda.setText(tienda.getNombreTienda());
        holder.tvDireccionTienda.setText(tienda.getDireccionTienda());
        holder.tvLatitudTienda.setText(tienda.getLatitudTienda());
        holder.tvLongitudTienda.setText(tienda.getLongitudTienda());
        holder.cvTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductoActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                        Pair.create((View) holder.ivImagenTienda, "ivImagenTienda"),
                        Pair.create((View) holder.tvNombreTienda, "tvNombreTienda"));
                intent.putExtra("id_tienda", tienda.getIdTienda());
                intent.putExtra("logo_tienda", tienda.getLogoTienda());
                intent.putExtra("nombre_tienda", tienda.getNombreTienda());
                intent.putExtra("imagen_tienda", tienda.getImagenTienda());
                intent.putExtra("direccion_tienda", tienda.getDireccionTienda());
                intent.putExtra("latitud_tienda", tienda.getLatitudTienda());
                intent.putExtra("longitud_tienda", tienda.getLongitudTienda());
                activity.startActivity(intent, options.toBundle());
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return tiendas.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvTienda;
        ImageView ivLogoTienda, ivImagenTienda;
        TextView tvIdTienda, tvNombreTienda, tvDireccionTienda, tvLatitudTienda, tvLongitudTienda;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvTienda = (CardView) itemView.findViewById(R.id.cvTienda);
            ivImagenTienda = (ImageView) itemView.findViewById(R.id.ivTienda);
            ivLogoTienda = (ImageView) itemView.findViewById(R.id.ivLogoTienda);
            tvIdTienda = (TextView) itemView.findViewById(R.id.tvIdTienda);
            tvNombreTienda = (TextView) itemView.findViewById(R.id.tvNombreTienda);
            tvDireccionTienda = (TextView) itemView.findViewById(R.id.tvDireccionTienda);
            tvLatitudTienda = (TextView) itemView.findViewById(R.id.tvLatitudTienda);
            tvLongitudTienda = (TextView) itemView.findViewById(R.id.tvLongitudTienda);
        }
    }
}
