package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class CalendarioActivity extends AppCompatActivity {

    CalendarView calendario;
    Bundle bundle = new Bundle();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        calendario = (CalendarView) findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getBaseContext(), Utilidade.formataNumero(dayOfMonth,2)+"/"+Utilidade.formataNumero(month+1,2)+"/"+
                        year,Toast.LENGTH_SHORT).show();

                String dataSelecionada = Utilidade.formataNumero(dayOfMonth,2)+"/"+Utilidade.formataNumero(month+1,2)+"/"+year;

                bundle.putString("tipoRelatório", "tData");
                bundle.putString("Título","Transações "+dataSelecionada);
                bundle.putInt("diaSel",dayOfMonth);
                bundle.putInt("mesSel",month+1);
                bundle.putInt("anoSel",year);

                alertDialogConfirmacao(dataSelecionada);
            }
        });
    }

    public void alertDialogConfirmacao(String dataSelecionada) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        intent = new Intent(getApplicationContext(), ListaTransacoesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja Visualizar as Transações do dia "+dataSelecionada+" ?").
                setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

}
