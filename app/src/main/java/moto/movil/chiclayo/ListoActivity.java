package moto.movil.chiclayo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * By: El Bryant
 */

public class ListoActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAceptar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listo);
        initComponents();
        initListener();
    }
    
    public void initComponents() {
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
    }
    
    public void initListener() {
        btnAceptar.setOnClickListener(this);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAceptar:
                Intent intent = new Intent(ListoActivity.this, CategoriaActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}