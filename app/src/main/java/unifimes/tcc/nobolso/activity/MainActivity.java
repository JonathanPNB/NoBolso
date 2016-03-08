package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.ImageAdapter;

public class MainActivity extends AppCompatActivity {
    boolean[] listImages = {true};
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        this.listViewImageAdapter();
        this.carregarFloatingButton();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_categorias:
                intent = new Intent(this, CategoriasActivity.class);
                startActivity(intent);
                break;
   /*         case R.id.button_calendario:
                Toast.makeText(getBaseContext(), "Botão ainda não funcional", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.button_relatorios:
                intent = new Intent(this, RelatoriosActivity.class);
                startActivity(intent);
                break;
            case R.id.button_transacoes:
                intent = new Intent(this, ListaTransacoesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listViewImageAdapter() {
        //ListView
        lista = (ListView) findViewById(R.id.lstprincipal);

        String[] tipos = new String[]{"Renda", "Gastos", "Saldo"};

        ArrayAdapter<String> adapter = new ImageAdapter(MainActivity.this, R.layout.activity_imageadapter1, R.id.text1,
                R.id.image1, R.id.image2, tipos, listImages);

        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                if (lista.getItemAtPosition(myItemInt) != null) {

// Pega o item naquela posição
                    Object o = lista.getItemAtPosition(myItemInt);

                    lista.getPositionForView(myView);

                    String pais = o.toString();

// Create a piece of toast.
                    Toast.makeText(getBaseContext(), pais, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void carregarFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), CadtransacaoActivity.class);
                startActivity(intent);
            }
        });
    }
}
