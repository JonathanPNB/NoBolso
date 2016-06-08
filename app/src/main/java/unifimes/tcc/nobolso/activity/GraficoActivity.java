package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.math.BigDecimal;
import java.util.ArrayList;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.GraficoAdapter;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.entity.Transacao;

public class GraficoActivity extends AppCompatActivity {

    TransacaoDAO tDAO;
    CategoriaDAO catDAO;
    PieGraph pg;
    TextView textoErro;
    ListView listView;
    int[] allColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tDAO = new TransacaoDAO(this);
        catDAO = new CategoriaDAO(this);
        allColors = this.getResources().getIntArray(R.array.cores);

        String tipoSel = getIntent().getExtras().getString("tipoSel");
        int mesSel = getIntent().getExtras().getInt("mesSel");
        int anoSel = getIntent().getExtras().getInt("anoSel");
        String categoriaSel = getIntent().getExtras().getString("categoriaSel");

        pg = (PieGraph) findViewById(R.id.pieGraph);
        textoErro = (TextView) findViewById(R.id.textView_erroGrafico);
        listView = (ListView) findViewById(R.id.listView3);

        this.geraGrafico(tDAO.relatorioTransacao(tipoSel, mesSel, anoSel, categoriaSel), tipoSel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home://execução do botão voltar da ActionBar
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void geraGrafico(ArrayList<Transacao> lista, String tipoTransacao) {
        pg.setVisibility(View.GONE);

        if (lista.size() > 0) {
            pg.setVisibility(View.VISIBLE);
            this.geraPieGraph(lista, tipoTransacao);
        } else {
            textoErro.setVisibility(View.VISIBLE);
        }
    }

    private void geraPieGraph(ArrayList<Transacao> lista, String tipoTransacao) {//FUNCIONANDO
        ArrayList<String> Categorias = new ArrayList<>();
        ArrayList<BigDecimal> Valores = new ArrayList<>();
        if (tipoTransacao.equalsIgnoreCase("Todos")) {
            Categorias.add("Despesa");
            Categorias.add("Receita");
            for (int i = 0; i < 2; i++) {//gerar o gráfico
                float valorTransacao = 0;
                valorTransacao += lista.get(i).getValor().floatValue();
                PieSlice slice = new PieSlice();
                if (i == 0) {//pegar apenas despesas
                    valorTransacao *= -1;
                }

                slice.setColor(allColors[i]);
                slice.setValue(valorTransacao);
                Valores.add(new BigDecimal(String.valueOf(valorTransacao)));
                pg.addSlice(slice);
                Log.e("geraPieGraph", "slice[" + i + "] = " + "-" + slice.getValue());

            }
        } else {
            for (int i = 0; i < lista.size(); i++) {//gerar o gráfico
                float valorTransacao = 0;
                valorTransacao += lista.get(i).getValor().floatValue();
                if (valorTransacao < 0) {
                    valorTransacao *= -1;
                }
                Categorias.clear();
          //      Categorias.add("Despesa");
            //    Categorias.add("Receita");
                PieSlice slice = new PieSlice();
                slice.setColor(allColors[i]);
                slice.setValue(valorTransacao);
                pg.addSlice(slice);
            }
        }
        listView.setAdapter(new GraficoAdapter(this, allColors, Categorias, Valores));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RelatoriosActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
