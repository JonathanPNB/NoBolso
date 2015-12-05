package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.ImageAdapter;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.entity.Categoria;

public class MainActivity extends AppCompatActivity {
    boolean[] listImages = {true};
    private ListView lista;
 //   private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        this.listViewImageAdapter();

/*        TextView textViewSaldo = (TextView)findViewById(R.id.textView_saldoAtual);
        textViewSaldo.setBackgroundResource(R.drawable.styles_text_view);
  */
    }
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
    /*        case R.id.button_listar:
                intent = new Intent(this, ListUserActivity.class);
                startActivity(intent);
                break;*/
            case R.id.button_novaTransacao:
                intent = new Intent(this, CadtransacaoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_categorias:
                intent = new Intent(this, CategoriasActivity.class);
                startActivity(intent);
                break;
   /*         case R.id.button_calendario:
                Toast.makeText(getApplicationContext(), "Botão ainda não funcional", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.button_relatorios:
                intent = new Intent(this, RelatoriosActivity.class);
                startActivity(intent);
                break;
            case R.id.button5:
                testCRUD();
        //        Toast.makeText(getApplicationContext(), "Botão ainda não funcional", Toast.LENGTH_SHORT).show();
                break;
    /*        case R.id.button_sobre:
                Toast.makeText(getApplicationContext(), "Botão ainda não funcional", Toast.LENGTH_SHORT).show();
                break;*/
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

    public void listViewImageAdapter() {
        //ListView
        lista = (ListView) findViewById(R.id.lstprincipal);

        String[] tipos = new String[]{"Renda", "Gastos", "Saldo"};

        ArrayAdapter<String> adapter = new ImageAdapter(MainActivity.this, R.layout.activity_imageadapter1, R.id.text1,
                R.id.image1, R.id.image2, tipos , listImages);

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
                    Toast.makeText(getApplicationContext(), pais, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void testCRUD() {

        Categoria cat = new Categoria("Lazer", 0);
        CategoriaDAO catDAO =  CategoriaDAO.getInstance(getApplicationContext());

        catDAO.salvar(this,cat);
/*
        List<Categoria> veiculosNaBase = veiculoDAO.recuperarTodos();
        assertFalse(veiculosNaBase.isEmpty());

        Categoria veiculoRecuperado = veiculosNaBase.get(0);
        veiculoRecuperado.setModelo("Stilo");

        veiculoDAO.editar(veiculoRecuperado);

        Categoria veiculoEditado = veiculoDAO.recuperarTodos().get(0);

        assertSame(veiculoRecuperado.getId(), veiculoEditado.getId());
        assertNotSame(veiculo.getModelo(), veiculoEditado.getModelo());

        veiculoDAO.deletar(veiculoEditado);

        assertTrue(veiculoDAO.recuperarTodos().isEmpty());

        veiculoDAO.fecharConexao();
*/
    }
    /*protected void mostraAlertBox(String titulo, String texto) {
        new AlertDialog.Builder(this).setTitle(titulo)
                .setMessage(Html.fromHtml(texto))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
     protected void alertDialogPersonalizado() {
    //-pegamos nossa instancia da classe
                LayoutInflater li = getLayoutInflater(); //inflamos o layout alerta.xml na view
                View view = li.inflate(R.layout.activity_cadtransacao, null); //definimos para o botão do layout um clickListener
                view.findViewById(R.id.button_receita).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) { //exibe um Toast informativo.
                        Toast.makeText(MainActivity.this, "Cadastrar Receita", Toast.LENGTH_SHORT).show(); //desfaz o alerta.
                        alerta.dismiss();
                    }
                });
                view.findViewById(R.id.button_despesa).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) { //exibe um Toast informativo.
                        Toast.makeText(MainActivity.this, "Cadastrar Despesa", Toast.LENGTH_SHORT).show(); //desfaz o alerta.
                        alerta.dismiss();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Titulo");
                builder.setView(view);
                alerta = builder.create();
                alerta.show();
                }
     */
}
