package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Categoria;

/**
 * Created by Jonathan on 28/11/2015.
 * Criacao da tela de categorias
 */
public class CategoriasActivity extends AppCompatActivity {
    ListView listReceitas, listDespesas;
    //  private AlertDialog alerta;

    ArrayAdapter receitaAdapter, despesaAdapter;
    private ArrayAdapter<Object> selectedListViewAdapter;

    List<Categoria> catDespesas, catReceitas;

    CategoriaDAO catDAO = null;
    BDCore bd;
    Bundle bundle = new Bundle();

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
        registerForContextMenu(listReceitas);
        registerForContextMenu(listDespesas);

        bd = BDCore.getInstance(getApplicationContext());
        if (!bd.tabelaExiste("Receita")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Receita");
        }
        if (!bd.tabelaExiste("Despesa")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Despesa");
        }
        if (!bd.tabelaExiste("Categoria")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Categoria");
        }
    }

    @Override
    public void onResume() {
        loadListCategorias();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_geral, menu);
        menu.setHeaderTitle("Selecione");

        try {
            ListView selectedListView = (ListView) v;
            selectedListViewAdapter = (ArrayAdapter<Object>) selectedListView.getAdapter();
        } catch (ClassCastException e) {
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        String nomeSelecionado = (String) selectedListViewAdapter.getItem(info.position);
        int idCategoria = catDAO.buscaId(nomeSelecionado);

        switch (item.getItemId()) {
            case R.id.opcaoEditar:
                if (selectedListViewAdapter != null) {
                    if (idCategoria >= 0) {
                        Intent intent = new Intent(getBaseContext(), CadcategoriaActivity.class);
                        bundle.putInt("id_categoria", idCategoria);
                        bundle.putString("Titulo", "Editar Categoria");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.opcaoRemover:
                if (selectedListViewAdapter != null) {
                    this.alertDialogExcluir(idCategoria, info);
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void alertDialogExcluir(final int id, final AdapterView.AdapterContextMenuInfo info) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (id >= 0) {
                            catDAO.remover(id);
                            selectedListViewAdapter.remove(selectedListViewAdapter.getItem(info.position));
                            selectedListViewAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Categoria Excluida com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A categoria será excluida.\n" +
                "Deseja Continuar?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    private void loadListCategorias() {
        final List<String> listaReceitas = new ArrayList<>();
        List<String> listaDespesas = new ArrayList<>();
        String categoria;
        catDAO = new CategoriaDAO(this);
        catDespesas = catDAO.listar(0);
        catReceitas = catDAO.listar(1);

        for (int i = 0; i < catReceitas.size(); i++) {
            categoria = catReceitas.get(i).getDescricao();
            //  categoria = catReceitas.get(i).toString();
            listaReceitas.add(categoria);
        }

        for (int i = 0; i < catDespesas.size(); i++) {
            categoria = catDespesas.get(i).getDescricao();
            listaDespesas.add(categoria);
        }

        receitaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaReceitas);
        despesaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaDespesas);

        receitaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        despesaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        listReceitas.setAdapter(receitaAdapter);
        listDespesas.setAdapter(despesaAdapter);
/*
        listReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String nomeCategoria;
                if (listReceitas.getItemAtPosition(position) != null) {
         //           nomeCategoria = adapterView.getItemAtPosition(position).toString();
// Pega o item naquela posição
                    //                  Object o = listReceitas.getItemAtPosition(position);
                    //                listReceitas.getPositionForView(view);

// Create a piece of toast.
                    Toast.makeText(getBaseContext(), "O item está na posição " + position, Toast.LENGTH_SHORT).show();
                    //          catDAO.deletar(catDAO.buscaId(nomeCategoria));
                    //        listaReceitas.remove(position);
           //         alertDialogPersonalizado(position);
                }
                receitaAdapter.notifyDataSetChanged();//atualiza o listview sempre q um item é alterado
            }
        });*/
        // catDAO.fecharConexao();
    }

    private void carregarFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CadcategoriaActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
