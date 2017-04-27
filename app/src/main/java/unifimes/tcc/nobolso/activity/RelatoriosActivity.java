package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.entity.Categoria;
import unifimes.tcc.nobolso.utilidade.Utilidade;


public class RelatoriosActivity extends AppCompatActivity {

    List<String> listaAnos = new ArrayList<>();
    List<String> listaCategorias = new ArrayList<>();
    List<String> listaFiltros = new ArrayList<>();
    CategoriaDAO catDAO;
    TransacaoDAO tDAO;
    boolean displayInicial = true;
    Bundle bundle = new Bundle();

    Spinner spinnerTipoFiltro;
    Spinner spinnerCategoria;
    Spinner spinnerAno;
    Spinner spinnerMes;
    Spinner spinnerTipoRelatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        spinnerTipoFiltro = (Spinner) findViewById(R.id.spinner_tipo_filtro);
        spinnerAno = (Spinner) findViewById(R.id.spinner3);
        spinnerMes = (Spinner) findViewById(R.id.spinner2);
        spinnerTipoRelatorio = (Spinner) findViewById(R.id.spinner_tipo_relatorio);
        spinnerCategoria = (Spinner) findViewById(R.id.spinner4);

        tDAO = new TransacaoDAO(this);
        popSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //      getMenuInflater().inflate(R.menu.menu_relatorios, menu);
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

    public void onClick(View v) {
        Intent intent;

        int filtroSel = spinnerTipoFiltro.getSelectedItemPosition();
        int tipoSel = spinnerTipoRelatorio.getSelectedItemPosition();
        int mesSel = spinnerMes.getSelectedItemPosition();
        int anoSel = spinnerAno.getSelectedItemPosition();
        int categoriaSel = spinnerCategoria.getSelectedItemPosition();

        switch (v.getId()) {
            case R.id.button_geraRelatorio:
                if (mesSel > 0 && anoSel == 0) {
                    Toast.makeText(RelatoriosActivity.this, R.string.erro_selecione_ano, Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putString("filtroSel", "tb"+spinnerTipoFiltro.getItemAtPosition(filtroSel).toString());
                bundle.putInt("mesSel", mesSel);
                if (anoSel > 0) {
                    bundle.putInt("anoSel", Integer.parseInt(spinnerAno.getItemAtPosition(anoSel).toString()));
                } else {
                    bundle.putInt("anoSel", anoSel);
                }

               // bundle.putString("tipoSel", spinnerTipoRelatorio.getItemAtPosition(tipoSel).toString());
                bundle.putString("categoriaSel", spinnerCategoria.getItemAtPosition(categoriaSel).toString());

                if (tipoSel == 0) {//gráfico
                    intent = new Intent(this, GraficoActivity.class);
                    intent.putExtras(bundle);
                } else {//relatório em lista
                    bundle.putString("Título", "Visualizar");
                    bundle.putString("tipoRelatório", "lista");
                    bundle.putString("relatorioLista", spinnerTipoFiltro.getItemAtPosition(filtroSel).toString());

                    intent = new Intent(this, ListaTransacoesActivity.class);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void popSpinner() {
        String categoria;
        catDAO = new CategoriaDAO(getApplicationContext());

        //popular o Spinner "Filtrar por"
       String[] tiposFiltros = getResources().getStringArray(R.array.array_tipoFiltroRelatorio);
        for (int i = 0; i < tiposFiltros.length; i++) {
            listaFiltros.add(tiposFiltros[i]);
        }
        final ArrayAdapter<String> spntipoAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item,
                listaFiltros);
        spntipoAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipoFiltro.setAdapter(spntipoAdapter);

        //popular o Spinner "Ano"
        listaAnos.add(0, "Todos");
        for (int i = 2015; i <= Utilidade.getAno(); i++) {
            listaAnos.add(Integer.toString(i));
        }

        ArrayAdapter<String> spinner3adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaAnos);
        spinner3adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAno.setAdapter(spinner3adap);

        //popular o Spinner "Mês"
        ArrayAdapter<String> spinner2adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item,
                getResources().getStringArray(R.array.array_meses));
        spinner2adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMes.setAdapter(spinner2adap);

        //popular o Spinner "Tipo de Relatório"
        ArrayAdapter<String> spinner5adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item,
                getResources().getStringArray(R.array.array_formatoRelatorio));
        spinner5adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipoRelatorio.setAdapter(spinner5adap);

        //popular o Spinner "Categorias"
        listaCategorias.add(0, "Todas");
        List<Categoria> lista = catDAO.listar(2);
        for (int i = 0; i < lista.size(); i++) {
            categoria = lista.get(i).getDescricao();
            listaCategorias.add(categoria);
        }

        final ArrayAdapter<String> spinner4adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaCategorias);
        spinner4adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinner4adap);

        spinnerTipoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!displayInicial) {
                    String categoria;
                    int tipo_filtro;
                    String tipoSelecionado = parent.getItemAtPosition(position).toString();
                    if (tipoSelecionado.equalsIgnoreCase(getResources().getStringArray(R.array.array_tipoFiltroRelatorio)[0])) { //despesa
                        tipo_filtro = 0;
                        spinnerCategoria.setClickable(true);
                        spinnerCategoria.setEnabled(true);
                    } else if (tipoSelecionado.equalsIgnoreCase(getResources().getStringArray(R.array.array_tipoFiltroRelatorio)[1])){//receita
                        tipo_filtro = 1;
                        spinnerCategoria.setClickable(true);
                        spinnerCategoria.setEnabled(true);
                    } else {
                        tipo_filtro = 2;
                        spinnerCategoria.setClickable(false);
                        spinnerCategoria.setEnabled(false);
                    }

                    listaCategorias.clear();
                    listaCategorias.add(0, "Todas");
                    List<Categoria> lista = catDAO.listar(tipo_filtro);
                    for (int i = 0; i < lista.size(); i++) {
                        categoria = lista.get(i).getDescricao();
                        listaCategorias.add(categoria);
                    }
                    spinner4adap.notifyDataSetChanged();
                }
                displayInicial = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoRelatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!displayInicial) {
                    String tipoSelecionado = parent.getItemAtPosition(position).toString();
                    if (tipoSelecionado.equalsIgnoreCase("Lista")) {
                        listaFiltros.clear();
                        listaFiltros.add(0,"Todos");
                        String[] tiposFiltros = getResources().getStringArray(R.array.array_tipoFiltroRelatorio);
                        for (int i = 0; i < tiposFiltros.length; i++) {
                            listaFiltros.add(tiposFiltros[i]);
                        }
                        spinnerCategoria.setClickable(false);
                        spinnerCategoria.setSelection(0);
                        spinnerCategoria.setEnabled(false);
                        spntipoAdapter.notifyDataSetChanged();
                    } else {
                        listaFiltros.clear();
                        String[] tiposFiltros = getResources().getStringArray(R.array.array_tipoFiltroRelatorio);
                        for (int i = 0; i < tiposFiltros.length; i++) {
                            listaFiltros.add(tiposFiltros[i]);
                        }
                        spinnerCategoria.setClickable(true);
                        spinnerCategoria.setSelection(0);
                        spinnerCategoria.setEnabled(true);
                        spntipoAdapter.notifyDataSetChanged();
                    }
                }
                displayInicial = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
