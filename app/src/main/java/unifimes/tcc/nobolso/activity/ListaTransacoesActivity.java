package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.TransacaoAdapter;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class ListaTransacoesActivity extends AppCompatActivity {

    private ArrayAdapter<Object> selectedListViewAdapter;
    ListView listReceitas, listDespesas;
    TextView tvReceita, tvDespesa;

    TransacaoDAO tDAO;
 /*   BDCore bd;
    Bundle bundle = new Bundle();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listatransacoes);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        listReceitas = (ListView) findViewById(R.id.listView);
        listDespesas = (ListView) findViewById(R.id.listView2);

        String tipoRelatorio = null, tituloActivity = null;
        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra("tipoRelatório")) {
            tipoRelatorio = extras.getString("tipoRelatório");
        }
        if (getIntent().hasExtra("Título")) {
            tituloActivity = extras.getString("Título");
        }

        tDAO = new TransacaoDAO(this);

        tvReceita = (TextView) findViewById(R.id.textView7);
        tvDespesa = (TextView) findViewById(R.id.textView8);

        if (tipoRelatorio.equalsIgnoreCase("Geral")) {
            listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes("Receita", Utilidade.getMes(),
                    Utilidade.getAno())));
            listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes("Despesa", Utilidade.getMes(),
                    Utilidade.getAno())));
        } else {
            setTitle(tituloActivity);
            tvDespesa.setVisibility(View.GONE);
            tvReceita.setVisibility(View.GONE);
            if (tipoRelatorio.equals("Receita")) {
                listDespesas.setVisibility(View.GONE);
                listReceitas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(tipoRelatorio, Utilidade.getMes(),
                        Utilidade.getAno())));
            } else if (tipoRelatorio.equals("Despesa")) {
                listReceitas.setVisibility(View.GONE);
                listDespesas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(tipoRelatorio, Utilidade.getMes(),
                        Utilidade.getAno())));
            }
        }

        registerForContextMenu(listReceitas);
        registerForContextMenu(listDespesas);
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
            Log.e("selectedListViewAdapter", v.toString());
        } catch (ClassCastException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        String nomeSelecionado = (String) selectedListViewAdapter.getItem(info.position);
        //  int idCategoria = catDAO.buscaIdCategoria(nomeSelecionado);
        Log.e("INFO", "onContextItemSelected aberto. " + nomeSelecionado + " / " + item.toString());

        switch (item.getItemId()) {
            case R.id.opcaoEditar:
                if (selectedListViewAdapter != null) {
                    Log.e("INFO", "Opção Editar Selecionada. " + nomeSelecionado);
                    Toast.makeText(getBaseContext(), "Opção Editar Selecionada. = " + nomeSelecionado, Toast.LENGTH_SHORT).show();
                }
/*                if(selectedListViewAdapter != null) {
               if(idCategoria >= 0) {
                        Intent intent = new Intent(getBaseContext(), CadcategoriaActivity.class);
                        bundle.putInt("id_categoria",idCategoria);
                        bundle.putString("Titulo","Editar Categoria");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
  */
                break;
            case R.id.opcaoRemover:
                if (selectedListViewAdapter != null) {
                    Toast.makeText(getBaseContext(), "Opção Remover Selecionada", Toast.LENGTH_SHORT).show();
                }
  /*              if(selectedListViewAdapter != null) {
                    this.alertDialogExcluir(idCategoria,info);
                }
    */
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
      /*                  if (id >= 0) {
                            catDAO.remover(id);
                            selectedListViewAdapter.remove(selectedListViewAdapter.getItem(info.position));
                            selectedListViewAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Categoria Excluida com sucesso.", Toast.LENGTH_SHORT).show();
                        }
        */
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A transação será excluida e essa ação não poderá ser revertida." +
                "Deseja Continuar?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}