package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.TiendaActivity;
import moto.movil.chiclayo.entity.Subcategoria;

/**
 * By: El Bryant
 */

public class SubcategoriaAdapter extends RecyclerView.Adapter<SubcategoriaAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Subcategoria> subcategorias;
    
    public SubcategoriaAdapter(Activity activity, ArrayList<Subcategoria> subcategorias) {
        this.activity = activity;
        this.subcategorias = subcategorias;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_subcategoria, null, false);
        return new SubcategoriaAdapter.ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Subcategoria subcategoria = subcategorias.get(position);
        Picasso.get().load(subcategoria.getImagen()).into(holder.ivSubcategoria);
        holder.tvIdSubcategoria.setText(subcategoria.getIdSubcategoria());
        holder.tvNombre.setText(subcategoria.getNombre());
        holder.llaySubcategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TiendaActivity.tvNombreSubcategoria.setText(holder.tvNombre.getText().toString());
                TiendaActivity.tvIdSubcategoria.setText(holder.tvIdSubcategoria.getText().toString());
                if (TiendaActivity.tvNombreSubcategoria.getText().toString().equals(subcategoria.getNombre())) {
                    holder.flayFondo.setVisibility(View.VISIBLE);
                } else {
                    holder.flayFondo.setVisibility(View.GONE);
                }
            }
        });
        if (TiendaActivity.tvNombreSubcategoria.getText().toString().equals(subcategoria.getNombre())) {
            holder.flayFondo.setVisibility(View.VISIBLE);
        } else {
            holder.flayFondo.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return subcategorias.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flayFondo;
        ImageView ivSubcategoria;
        LinearLayout llaySubcategoria;
        TextView tvIdSubcategoria, tvNombre;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flayFondo = (FrameLayout) itemView.findViewById(R.id.flayFondo);
            ivSubcategoria = (ImageView) itemView.findViewById(R.id.ivSubcategoria);
            llaySubcategoria = (LinearLayout) itemView.findViewById(R.id.llaySubcategoria);
            tvIdSubcategoria = (TextView) itemView.findViewById(R.id.tvIdSubcategoria);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreSubcategoria);
        }
    }
}
