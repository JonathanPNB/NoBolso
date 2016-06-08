package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.adapter.TransacaoAdapter;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class ListaTipoTransacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tipo_transacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//deixa a tela apenas na vertical
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        String tipoRelatorio = null, tituloActivity = null;

        if(getIntent().hasExtra("TipoRelatório")){
            Bundle extras = getIntent().getExtras();
            tipoRelatorio = extras.getString("TipoRelatório");
        }
        if(getIntent().hasExtra("Título")){
            Bundle extras = getIntent().getExtras();
            tituloActivity = extras.getString("Título");
        }

        setTitle(tituloActivity);
        TransacaoDAO tDAO = new TransacaoDAO(getBaseContext());

        ListView lista = (ListView) findViewById(R.id.listView_transacoesVisaoGeral);

        lista.setAdapter(new TransacaoAdapter(this, tDAO.transacoesMes(tipoRelatorio, Utilidade.getMes(), Utilidade.getAno())));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
        }
}
