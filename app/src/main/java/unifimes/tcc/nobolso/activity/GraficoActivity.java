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
import java.util.TreeMap;

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

        String filtroSel = getIntent().getExtras().getString("filtroSel");
        int mesSel = getIntent().getExtras().getInt("mesSel");
        int anoSel = getIntent().getExtras().getInt("anoSel");
        String categoriaSel = getIntent().getExtras().getString("categoriaSel");

        pg = (PieGraph) findViewById(R.id.pieGraph);
        textoErro = (TextView) findViewById(R.id.textView_erroGrafico);
        listView = (ListView) findViewById(R.id.listView3);

        this.geraGrafico(tDAO.relatorioTransacao(filtroSel, mesSel, anoSel, categoriaSel)/*, tipoSel*/);
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

    private void geraGrafico(ArrayList<Transacao> lista/*, String tipoTransacao*/) {
        pg.setVisibility(View.GONE);

        if (lista.size() > 0) {
            pg.setVisibility(View.VISIBLE);
            this.geraPieGraph(lista/*, tipoTransacao*/);
        } else {
            textoErro.setVisibility(View.VISIBLE);
            return;
        }
    }

    private void geraPieGraph(ArrayList<Transacao> lista/*, String tipoTransacao*/) {//FUNCIONANDO
        Log.e("geraPieGraph", "Lista = " + lista.toString());
        TreeMap<String, BigDecimal> listaValores = new TreeMap<>();
      /*  if (tipoTransacao.equalsIgnoreCase("Todos")) {
            float valorReceitas = 0;
            float valorDespesas = 0;

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getValor().compareTo(BigDecimal.ZERO) < 0) {//despesas
                    valorDespesas += lista.get(i).getValor().floatValue();
                } else {
                    valorReceitas += lista.get(i).getValor().floatValue();
                }

                if (i == lista.size() - 1) {
                    PieSlice sliceDespesa = new PieSlice();
                    PieSlice sliceReceita = new PieSlice();
                    valorDespesas *= -1;
                    listaValores.put("Despesa", new BigDecimal(String.valueOf(valorDespesas)));
                    listaValores.put("Receita", new BigDecimal(String.valueOf(valorReceitas)));
                    sliceDespesa.setColor(allColors[0]);
                    sliceReceita.setColor(allColors[1]);
                    sliceDespesa.setValue(valorDespesas);
                    sliceReceita.setValue(valorReceitas);

                    pg.addSlice(sliceDespesa);
                    pg.addSlice(sliceReceita);

                    Log.e("geraPieGraph", "sliceDespesa= " + sliceDespesa.getValue() + " / sliceReceita= " + sliceReceita.getValue());
                }
            }
        } else {*/
       //     Log.e("geraPieGraph", tipoTransacao + " - " + lista.size());
            float valorTransacao;
            for (int i = 0; i < lista.size(); i++) {
                valorTransacao = lista.get(i).getValor().floatValue();
                if (valorTransacao < 0) {
                    valorTransacao *= -1;
                }
                if (!listaValores.containsKey(lista.get(i).getCategoria())) {//se a categoria não estiver no map é adicionada
                    listaValores.put(lista.get(i).getCategoria(), new BigDecimal(String.valueOf(valorTransacao)));
                } else {//se estiver o valor é incrementado
                    listaValores.put(lista.get(i).getCategoria(),
                            listaValores.get(lista.get(i).getCategoria()).add(new BigDecimal(String.valueOf(valorTransacao))));
                }
                Log.e("valcategoria", lista.get(i).getCategoria() + " = " + new BigDecimal(String.valueOf(valorTransacao)) +
                        " / lista.size = " + lista.size());
            }
            int quantidadeChaves = 0;
            for (String chave : listaValores.keySet()) {
                if (chave != null) {
                    Log.e("valcategoria2", chave + " = " + listaValores.get(chave));
                    PieSlice slice = new PieSlice();
                    slice.setColor(allColors[quantidadeChaves]);
                    slice.setValue(listaValores.get(chave).floatValue());
                    pg.addSlice(slice);
                }
                quantidadeChaves++;
            }
    //    }

        if (allColors.length > 0 && !listaValores.isEmpty()) {
            for (String chave : listaValores.keySet()) {
                if (chave != null) {
                    Log.e("listaValores", "Chave: " + chave);
                }
            }
            listView.setAdapter(new GraficoAdapter(this, allColors, listaValores));
        } else {
            pg.setVisibility(View.GONE);
            textoErro.setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RelatoriosActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
