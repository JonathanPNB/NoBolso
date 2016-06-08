package unifimes.tcc.nobolso.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class Calendario extends AppCompatActivity {

    CalendarView calendario;
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
            }
        });
    }

}
