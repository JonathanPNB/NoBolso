package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.math.BigDecimal;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.VisaoGeralAdapter;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class MainActivity extends AppCompatActivity {
    private ListView lista;
    Bundle bundle = new Bundle();
    Intent intent;
    TransacaoDAO tDAO;

    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tDAO = new TransacaoDAO(this);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        this.listViewVisaoGeralAdapter();
        this.carregarFloatingButton();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_categorias:
                intent = new Intent(this, CategoriasActivity.class);
                startActivity(intent);
                break;
            case R.id.button_calendario:
                intent = new Intent(this, CalendarioActivity.class);
                startActivity(intent);
                break;
            case R.id.button_relatorios:
                intent = new Intent(this, RelatoriosActivity.class);
                startActivity(intent);
                break;
            case R.id.button_transacoes:
                intent = new Intent(this, ListaTransacoesActivity.class);
                bundle.putString("tipoRelatório", "Geral");
                intent.putExtras(bundle);
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
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();

        switch (id) {
            case R.id.action_limpar:
                this.alertDialogLimparDB(info);
               break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listViewVisaoGeralAdapter() {
        lista = (ListView) findViewById(R.id.lstprincipal);

        BigDecimal saldoAcumulado, totalReceita, totalDespesa, saldoMes;

        totalReceita = Utilidade.somaValores(tDAO.transacoesMes(BDCore.NOME_TABELA_RECEITA, Utilidade.getMes(), Utilidade.getAno()));
        totalDespesa = Utilidade.somaValores(tDAO.transacoesMes(BDCore.NOME_TABELA_DESPESA, Utilidade.getMes(), Utilidade.getAno()));
        saldoMes = totalReceita.add(totalDespesa);

        saldoAcumulado = Utilidade.somaValores(tDAO.buscarTodasTransacoes())/*.subtract(saldoMes)*/;

        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), "buscarTodasTransacoes = " + Utilidade.somaValores(tDAO.buscarTodasTransacoes()) +
                " / saldoAcumulado = " + saldoAcumulado.toString());
        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), Utilidade.getMes() + "/" + Utilidade.getAno());

        String[] tipos = {"Saldo Acumulado", "Receita", "Despesa", "Saldo do Mês"};
        BigDecimal[] valores = {saldoAcumulado, totalReceita, totalDespesa, saldoMes};

        lista.setAdapter(new VisaoGeralAdapter(getBaseContext(), tipos, valores));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                Intent intent;

                if (lista.getItemAtPosition(position) != null) {

                    intent = new Intent(getBaseContext(), ListaTransacoesActivity.class);

                    switch (position) {
                        case 1:
                            bundle.putString("tipoRelatório", BDCore.NOME_TABELA_RECEITA);
                            bundle.putString("Título", "Receitas " + Utilidade.getMes() + "/" + Utilidade.getAno());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //       finish();
                            break;
                        case 2:
                            bundle.putString("tipoRelatório", BDCore.NOME_TABELA_DESPESA);
                            bundle.putString("Título", "Despesas " + Utilidade.getMes() + "/" + Utilidade.getAno());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //      finish();
                            break;
                        case 3:
                            bundle.putString("tipoRelatório", "Geral");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //         finish();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void carregarFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), CadtransacaoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void mostraAlertBox(String titulo, String texto) {
        ImageView imagem = (ImageView) findViewById(R.id.imageView3);
        AlertDialog.Builder builder = new AlertDialog.Builder (this).setTitle(titulo).setMessage(texto)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setView(imagem);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < (System.currentTimeMillis() - 4000)) {
            toast = Toast.makeText(this, R.string.info_fecharapp, Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    public void alertDialogLimparDB(final AdapterView.AdapterContextMenuInfo info) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        BDCore bd = BDCore.getInstance(getApplicationContext());
                        bd.clearDB();
                        Toast.makeText(getBaseContext(), "DataBase Limpa com Sucesso", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Todos os dados serão perdidos.\n" +
                "Deseja Continuar?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }
}
