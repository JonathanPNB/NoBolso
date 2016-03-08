package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;


public class RelatoriosActivity extends AppCompatActivity {

    String[] listaFiltros = new String[]{"Geral", "Receita", "Despesa"};
    String[] listaMeses = new String[]{"Todos", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
    String[] listaTipos = new String[]{"Lista", "Gráfico"};
    List<String> listaAnos = new ArrayList<>();
    List<String> listaCategorias = new ArrayList<>();
    Calendar c = Calendar.getInstance();
    CategoriaDAO catDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {//execução do botão voltar da ActionBar
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_relatorios:
                intent = new Intent(this, RelatoriosActivity.class);
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
        // database handler
     //   BDCore db = new BDCore(getBaseContext());
        String categoria;
        int tipo_relatorio;
        catDAO = new CategoriaDAO(getBaseContext());

        //popular o Spinner "Filtrar por"
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_tipo_relatorio);
        ArrayAdapter<String> spinner1adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaFiltros);
        spinner1adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(spinner1adap);


        //popular o Spinner "Ano"
        int anoAtual = c.get(Calendar.YEAR);
        listaAnos.add(0, "Todos");
        for (int i = 2015; i <= anoAtual; i++) {
            listaAnos.add(Integer.toString(i));
        }
        Spinner spinnerAno = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> spinner3adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaAnos);
        spinner3adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAno.setAdapter(spinner3adap);

        //popular o Spinner "Mês"
        Spinner spinnerMes = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> spinner2adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaMeses);
        spinner2adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMes.setAdapter(spinner2adap);

        //popular o Spinner "Tipo de Relatório"
        Spinner spinnerTipo = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> spinner5adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaTipos);
        spinner5adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipo.setAdapter(spinner5adap);


        //popular o Spinner "Categorias"
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinner4);
        listaCategorias.add(0, "Todas");
        if(spinner1.toString().equals(listaFiltros[2])) { //despesa
            tipo_relatorio = 0;
        }
        else if(spinner1.toString().equals(listaFiltros[1])) {//receita
            tipo_relatorio = 1;
        }
        else {
            tipo_relatorio = 2;
        }

        for (String palavra : catDAO.listar(tipo_relatorio)) {//0=despesa 1=receita 2=ambos
            categoria = palavra.toString();
            listaCategorias.add(categoria);
        }
        ArrayAdapter<String> spinner4adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaCategorias);
        spinner4adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinner4adap);
    }
}
