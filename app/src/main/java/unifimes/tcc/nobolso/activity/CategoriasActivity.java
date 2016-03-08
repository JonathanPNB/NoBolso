package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.database.BDCore;

/**
 * Created by Jonathan on 28/11/2015.
 * Criacao da tela de categorias
 */
public class CategoriasActivity extends AppCompatActivity {
    ListView listReceitas, listDespesas;
    private AlertDialog alerta;

    ArrayAdapter<String> receitaAdapter;
    ArrayAdapter<String> despesaAdapter;

    List<String> catDespesas;
    List<String> catReceitas;

    CategoriaDAO catDAO = null;
    BDCore bd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        // Spinner element
        listReceitas = (ListView) findViewById(R.id.listView_receitas);
        listDespesas = (ListView) findViewById(R.id.listView_despesas);
        loadListCategorias();
        carregarFloatingButton();
     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    alertDialogPersonalizado();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        bd = new BDCore(this);
        if (!bd.tabelaExiste("Receita")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Receita");
        }
        if (!bd.tabelaExiste("Despesa")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Despesa");
        }
        if (!bd.tabelaExiste("Categoria")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Categoria");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadListCategorias() {
        catDAO = new CategoriaDAO(this);
        // Spinner Drop down elements
        catDespesas = catDAO.listar(0);
        catReceitas = catDAO.listar(1);

        // Creating adapter for spinner
        receitaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, catReceitas);
        despesaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, catDespesas);

        // Drop down layout style - list view with radio button
        receitaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        despesaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // attaching data adapter to spinner
        listReceitas.setAdapter(receitaAdapter);
        listDespesas.setAdapter(despesaAdapter);
    }

    private void carregarFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
  //      LayoutInflater li = getLayoutInflater(); //inflamos o layout activity_cadcategoria.xml na view
    //    View  view = li.inflate(R.layout.activity_cadcategoria, null);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CadcategoriaActivity.class);
                startActivity(intent);
            }

        });
  /*      AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle("Título");
        builder.setView(view);
        alerta=builder.create();
        alerta.show();
    */}
/*
    public void testCRUD(int tipo) {
        catDAO = new CategoriaDAO(this);
        Categoria cat;
        if (tipo == 0) {
            cat = new Categoria("Lazer", 0);
            catDespesas.add("Lazer");

        } else {
            cat = new Categoria("Salário", 1);
            catReceitas.add("Salário");
        }

        catDAO.salvar(this, cat);
    }

    /*protected void mostraAlertBox(String titulo, String texto) {
        new AlertDialog.Builder(this).setTitle(titulo)
                .setMessage(Html.fromHtml(texto))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
    */
    public void alertDialogPersonalizado() {
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater(); //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.activity_dbteste, null); //definimos para o botão do layout um clickListener
/*
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {//Despesa
            public void onClick(View arg0) {
                testCRUD(0);
                despesaAdapter.notifyDataSetChanged();
                alerta.dismiss();

            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {//Receita
            public void onClick(View arg0) {
                testCRUD(1);
                receitaAdapter.notifyDataSetChanged();//atualiza a tela depois de uma alteração
                alerta.dismiss();

            }
        });

        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {//Limpar DB
            public void onClick(View arg0) { //exibe um Toast informativo.
                bd = new BDCore(getBaseContext());
                bd.clearDB();
                catReceitas.clear();
                catDespesas.clear();
                receitaAdapter.notifyDataSetChanged();
                Toast.makeText(CategoriasActivity.this, "Database limpa com sucesso.", Toast.LENGTH_SHORT).show(); //desfaz o alerta.
                alerta.dismiss();
            }
        });

        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            //    BDCore db = new BDCore(getBaseContext());
            //    db.clearDB();
            //    catReceitas.clear();
            //    catDespesas.clear();
            //    receitaAdapter.notifyDataSetChanged();

                Intent intent = new Intent(CategoriasActivity.this, ListaTransacoesActivity.class);
                startActivity(intent);
             //   Toast.makeText(CategoriasActivity.this, "Não funciona.", Toast.LENGTH_SHORT).show(); //desfaz o alerta.
                alerta.dismiss();
            }
        });*/
        view.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {//Limpar DB
            public void onClick(View arg0) { //exibe um Toast informativo.
                Toast.makeText(CategoriasActivity.this, "Teste loko.", Toast.LENGTH_SHORT).show(); //desfaz o alerta.
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Título");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }
}
