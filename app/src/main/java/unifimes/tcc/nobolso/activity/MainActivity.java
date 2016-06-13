package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.math.BigDecimal;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.VisaoGeralAdapter;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class MainActivity extends AppCompatActivity {
    private ListView lista;
    Bundle bundle = new Bundle();
    Intent intent;
    TransacaoDAO tDAO;

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
                //       finish();
                break;
            case R.id.button_calendario:
    /*            BDCore bd = BDCore.getInstance(this);
                bd.clearDB();
                Toast.makeText(getBaseContext(), "DataBase Limpa com Sucesso", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
*/
                intent = new Intent(this, CalendarioActivity.class);
                startActivity(intent);
         //       finish();

                break;
            case R.id.button_relatorios:
                intent = new Intent(this, RelatoriosActivity.class);
                startActivity(intent);
                //        finish();
                break;
            case R.id.button_transacoes:
                intent = new Intent(this, ListaTransacoesActivity.class);
                bundle.putString("tipoRelatório", "Geral");
                intent.putExtras(bundle);
                startActivity(intent);
                //       finish();
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

        switch (id) {
            case R.id.action_sobre:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listViewVisaoGeralAdapter() {
        lista = (ListView) findViewById(R.id.lstprincipal);

        BigDecimal saldoAcumulado, totalReceita, totalDespesa, saldoMes;

        totalReceita = Utilidade.somaValores(tDAO.transacoesMes("Receita", Utilidade.getMes(), Utilidade.getAno()));
        totalDespesa = Utilidade.somaValores(tDAO.transacoesMes("Despesa", Utilidade.getMes(), Utilidade.getAno()));
        saldoMes = totalReceita.add(totalDespesa);

        saldoAcumulado = Utilidade.somaValores(tDAO.buscarTodasTransacoes()).subtract(saldoMes);

        Log.e("listVisaoGeralAdapter", "buscarTodasTransacoes = " + Utilidade.somaValores(tDAO.buscarTodasTransacoes()) +
                " / saldoAcumulado = " + saldoAcumulado.toString());
        Log.e("listVisaoGeralAdapter", Utilidade.getMes() + "/" + Utilidade.getAno());

        String[] tipos = {"Saldo Acumulado", "Entrada", "Saída", "Saldo do Mês"};
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
                            bundle.putString("tipoRelatório", "Receita");
                            bundle.putString("Título", "Receitas " + Utilidade.getMes() + "/" + Utilidade.getAno());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //       finish();
                            break;
                        case 2:
                            bundle.putString("tipoRelatório", "Despesa");
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
}
