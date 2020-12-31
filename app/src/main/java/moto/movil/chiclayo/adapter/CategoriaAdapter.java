package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import moto.movil.chiclayo.CourierActivity;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.TiendaActivity;
import moto.movil.chiclayo.entity.Categoria;

/**
 * By: El Bryant
 */

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Categoria> categorias;
    
    public CategoriaAdapter(Activity activity, ArrayList<Categoria> categorias) {
        this.activity = activity;
        this.categorias = categorias;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaAdapter.ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Categoria categoria = categorias.get(position);
        Picasso.get().load(categoria.getImagen()).placeholder(R.drawable.boton_indigo).into(holder.ivCategoria);
        holder.tvIdCategoria.setText(categoria.getIdCategoria());
        holder.tvNombreCategoria.setText(categoria.getNombre());
        holder.rlayCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoria.getIdCategoria().equals("4")) {
                    Intent intent = new Intent(activity, CourierActivity.class);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, TiendaActivity.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, Pair.create((View) holder.tvNombreCategoria, "tvNombreCategoria"));
                    intent.putExtra("idCategoria", categoria.getIdCategoria());
                    intent.putExtra("nombreCategoria", categoria.getNombre());
                    intent.putExtra("iconoCategoria", categoria.getIcono());
                    activity.startActivity(intent, options.toBundle());
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return categorias.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoria;
        RelativeLayout rlayCategoria;
        TextView tvIdCategoria, tvNombreCategoria;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoria = (ImageView) itemView.findViewById(R.id.ivCategoria);
            tvIdCategoria = (TextView) itemView.findViewById(R.id.tvIdCategoria);
            rlayCategoria = (RelativeLayout) itemView.findViewById(R.id.rlayCategoria);
            tvNombreCategoria = (TextView) itemView.findViewById(R.id.tvNombreCategoria);
        }
    }
}
