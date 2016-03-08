package unifimes.tcc.nobolso.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import unifimes.tcc.nobolso.R;

public class CadcategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadcategoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar
    }
}
