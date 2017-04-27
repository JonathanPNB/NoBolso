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
import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class ListaTransacoesActivity extends AppCompatActivity {

    private ArrayAdapter<Object> selectedListViewAdapter;
    ListView listReceitas, listDespesas;
    TextView tvReceita, tvDespesa;

    TransacaoDAO tDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listatransacoes);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        listReceitas = (ListView) findViewById(R.id.listView);
        listDespesas = (ListView) findViewById(R.id.listView2);

        String tipoRelatorio = null;
        String tituloActivity = null;
        String categoriaSel = null;
        String relatorioLista = null;
        int diaSel = 0;
        int mesSel = 0;
        int anoSel = 0;
        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra("tipoRelatório")) {
            tipoRelatorio = extras.getString("tipoRelatório");
        }
        if (getIntent().hasExtra("Título")) {
            tituloActivity = extras.getString("Título");
        }
        if(getIntent().hasExtra("diaSel")) {
            diaSel = extras.getInt("diaSel");
        }
        if(getIntent().hasExtra("mesSel")) {
            mesSel = extras.getInt("mesSel");
        }
        if(getIntent().hasExtra("anoSel")) {
            anoSel = extras.getInt("anoSel");
        }
        if(getIntent().hasExtra("categoriaSel")) {
            categoriaSel = extras.getString("categoriaSel");
        }
        if(getIntent().hasExtra("relatorioLista")) {
            relatorioLista = extras.getString("relatorioLista");
        }

        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(),getIntent().getExtras().toString());

        tDAO = new TransacaoDAO(this);

        tvReceita = (TextView) findViewById(R.id.textView7);
        tvDespesa = (TextView) findViewById(R.id.textView8);

        if (tipoRelatorio.equalsIgnoreCase("Geral")) {
            listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(BDCore.NOME_TABELA_RECEITA,
                    Utilidade.getMes(), Utilidade.getAno())));
            listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(BDCore.NOME_TABELA_DESPESA,
                    Utilidade.getMes(), Utilidade.getAno())));
        } else if (tipoRelatorio.equalsIgnoreCase("tData")) {
            setTitle(tituloActivity);
            listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesDia(BDCore.NOME_TABELA_RECEITA,
                    diaSel, mesSel, anoSel)));
            listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesDia(BDCore.NOME_TABELA_DESPESA,
                    diaSel, mesSel, anoSel)));
        } else {
            setTitle(tituloActivity);
            tvDespesa.setVisibility(View.GONE);
            tvReceita.setVisibility(View.GONE);
            if (tipoRelatorio.equalsIgnoreCase(BDCore.NOME_TABELA_RECEITA)) {
                listDespesas.setVisibility(View.GONE);
                listReceitas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(tipoRelatorio, Utilidade.getMes(),
                        Utilidade.getAno())));
            } else if (tipoRelatorio.equalsIgnoreCase(BDCore.NOME_TABELA_DESPESA)) {
                listReceitas.setVisibility(View.GONE);
                listDespesas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(tipoRelatorio, Utilidade.getMes(),
                        Utilidade.getAno())));
            } else {//todas as transacoes - relatorios
                if (relatorioLista.equalsIgnoreCase(BDCore.NOME_TABELA_RECEITA)) {
                    tvDespesa.setVisibility(View.GONE);
                    listDespesas.setVisibility(View.GONE);
                    listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.relatorioTransacao(BDCore.NOME_TABELA_RECEITA, mesSel,
                            anoSel, categoriaSel)));

                } else if (relatorioLista.equalsIgnoreCase(BDCore.NOME_TABELA_DESPESA)) {
                    tvReceita.setVisibility(View.GONE);
                    listReceitas.setVisibility(View.GONE);
                    listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.relatorioTransacao(BDCore.NOME_TABELA_DESPESA, mesSel,
                            anoSel, categoriaSel)));
                } else {
                    tvDespesa.setVisibility(View.VISIBLE);
                    tvReceita.setVisibility(View.VISIBLE);
                    listReceitas.setAdapter(new TransacaoAdapter(this, tDAO.relatorioTransacao(BDCore.NOME_TABELA_RECEITA, mesSel,
                            anoSel, categoriaSel)));
                    listDespesas.setAdapter(new TransacaoAdapter(this, tDAO.relatorioTransacao(BDCore.NOME_TABELA_DESPESA, mesSel,
                            anoSel, categoriaSel)));
                }
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
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), v.toString());
        } catch (ClassCastException e) {
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
            return;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        String nomeSelecionado = (String) selectedListViewAdapter.getItem(info.position);
        //  int idCategoria = catDAO.buscaId(nomeSelecionado);
        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), "onContextItemSelected aberto. " + nomeSelecionado + " / " + item.toString());

        switch (item.getItemId()) {
            case R.id.opcaoEditar:
                if (selectedListViewAdapter != null) {
                    Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), "Opção Editar Selecionada. " + nomeSelecionado);
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