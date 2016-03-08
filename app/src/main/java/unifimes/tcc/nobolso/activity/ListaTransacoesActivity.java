package unifimes.tcc.nobolso.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.TransacaoAdapter;
import unifimes.tcc.nobolso.dao.TransacaoDAO;

public class ListaTransacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listatransacoes);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        TransacaoDAO tDAO = new TransacaoDAO(this);

        ListView lvReceita = (ListView) findViewById(R.id.listView);
        ListView lvDespesa = (ListView) findViewById(R.id.listView2);

        lvReceita.setAdapter(new TransacaoAdapter(this, tDAO.listar("Receita")));
        lvDespesa.setAdapter(new TransacaoAdapter(this, tDAO.listar("Despesa")));

    }

}