package moto.movil.chiclayo.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import com.arthurivanets.bottomsheets.BaseBottomSheet;
import com.arthurivanets.bottomsheets.config.BaseConfig;
import com.arthurivanets.bottomsheets.config.Config;
import moto.movil.chiclayo.CategoriaActivity;
import moto.movil.chiclayo.DireccionActivity;
import moto.movil.chiclayo.R;
import moto.movil.chiclayo.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class UbicacionBottomSheet extends BaseBottomSheet {
    Activity activity;
    CardView cvIngresarDireccion;
    static Double latitud = 0.0, longitud = 0.0;
    LinearLayout llayUbicacionActual;
    PrefUtil prefUtil;
    
    public UbicacionBottomSheet(@NonNull Activity hostActivity, @NonNull BaseConfig config) {
        super(hostActivity, config);
        this.activity = hostActivity;
    }
    public UbicacionBottomSheet(@NonNull Activity hostActivity) {
        this(hostActivity, new Config.Builder(hostActivity).build());
        this.activity = hostActivity;
        prefUtil = new PrefUtil(hostActivity);
    }
    
    @NonNull
    @Override
    protected View onCreateSheetContentView(@NonNull final Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.bottomsheet_direccion, this, false);
        cvIngresarDireccion = (CardView) v.findViewById(R.id.cvIngresarDireccion);
        llayUbicacionActual = (LinearLayout) v.findViewById(R.id.llayUbicacionActual);
        llayUbicacionActual.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prefUtil.saveGenericValue("latitudEntrega", CategoriaActivity.latitud.toString());
                prefUtil.saveGenericValue("longitudEntrega", CategoriaActivity.longitud.toString());
            }
        });
        cvIngresarDireccion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DireccionActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, Pair.create((View) cvIngresarDireccion, "cvDireccionDestino"));
                activity.startActivity(intent, options.toBundle());
            }
        });
        return v;
    }
}
