package unifimes.tcc.nobolso.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.database.BDCore;

/**
 * Created by Jonathan on 28/11/2015.
 * Criacao da tela de categorias
 */
public class CategoriasActivity extends AppCompatActivity {

    Spinner spinner6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Spinner element
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        loadSpinnerData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadSpinnerData() {
        // database handler
        BDCore db = new BDCore(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getCategorias();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner6.setAdapter(dataAdapter);
    }
}
