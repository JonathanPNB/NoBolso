package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import unifimes.tcc.nobolso.R;


public class RelatoriosActivity extends AppCompatActivity {

    String[] filtros = new String[]{"Geral","Receita","Despesa"};
    String[] meses = new String[]{"Todos", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
    String[] tipos = new String[]{"Lista","Gráfico"};
    private int anoMin = 2015;
    List<String> anos = new ArrayList<>();
   String[] categorias = new String[]{"Selecione a Categoria","Educação","Lazer","Veículos","Saúde","Alimentação","Moradia"};
    Calendar c = Calendar.getInstance();
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
        getMenuInflater().inflate(R.menu.menu_relatorios, menu);
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
        }
        else if (id == android.R.id.home) {//execução do botão voltar da ActionBar
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_novaTransacao:
                intent = new Intent(this, CadtransacaoActivity.class);
                startActivity(intent);
                break;
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
        //popular o Spinner "Filtrar por"
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinner1adap = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, filtros);
        spinner1adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(spinner1adap);


        //popular o Spinner "Ano"
        int anoAtual = c.get(Calendar.YEAR);
        anos.add(0,"Todos");
        for(int i = anoMin;i <= anoAtual; i++) {
            anos.add(Integer.toString(i));
        }
        Spinner spinnerAno = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> spinner3adap = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item,anos);
        spinner3adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAno.setAdapter(spinner3adap);

        //popular o Spinner "Mês"
        Spinner spinnerMes = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> spinner2adap = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, meses);
        spinner2adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMes.setAdapter(spinner2adap);

        //popular o Spinner "Tipo de Relatório"
        Spinner spinnerTipo = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> spinner5adap = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, tipos);
        spinner5adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipo.setAdapter(spinner5adap);

        //popular o Spinner "Categorias", método provisório
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> spinner4adap = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, categorias);
        spinner4adap.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinner4adap);
    }
}
