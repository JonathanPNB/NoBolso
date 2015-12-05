package unifimes.tcc.nobolso.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import unifimes.tcc.nobolso.R;


public class CadtransacaoActivity extends AppCompatActivity {
    Spinner spn1, spn2, spinner_tipo;
    String opcao, categoria;
    List<String> categorias = new ArrayList<>();
    public static EditText SelectedDateView;
    private static final Calendar c = Calendar.getInstance();
    private static int year = c.get(Calendar.YEAR);
    private static int month = c.get(Calendar.MONTH);
    private static int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadtransacao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        this.spinnerAdapter();
        SelectedDateView = (EditText) findViewById(R.id.editText_data);
        SelectedDateView.setText(day + "/" + (month + 1) + "/" + year);

        spinner_tipo = (Spinner) findViewById(R.id.spinner_tipo);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.array_tipo, R.layout.spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_tipo.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadtransacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home://execução do botão voltar da ActionBar
                NavUtils.navigateUpFromSameTask(this);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            SelectedDateView.setText(day + "/" + (month + 1) + "/" + year);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            SelectedDateView.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void spinnerAdapter() {
        //Adicionando Nomes no ArrayList
        categorias.add("Selecione a Categoria");
        categorias.add("Educação");
        categorias.add("Lazer");
        categorias.add("Veículos");
        categorias.add("Saúde");
        categorias.add("Alimentação");
        categorias.add("Moradia");

        //Identifica o Spinner no layout
        spn1 = (Spinner) findViewById(R.id.spinner_tipo);
        spn2 = (Spinner) findViewById(R.id.spinner_categoria);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, categorias);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
    /*
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);

        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        spn2.setAdapter(arrayAdapter2);

        //Método do Spinner para capturar o item selecionado
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                opcao = parent.getItemAtPosition(posicao).toString();
                /*if (!opcao.equals(parent.getItemAtPosition(0).toString())) {
                    EditText edtvalor = (EditText) findViewById(R.id.editText_valorTransacao);
                    EditText edtdesc = (EditText) findViewById(R.id.editText_descricao);
                    Spinner spncategoria = (Spinner) findViewById(R.id.spinner_categoria);

                    edtvalor.setClickable(true);
                    edtvalor.setEnabled(true);

                    edtdesc.setClickable(true);
                    edtdesc.setEnabled(true);

                    spncategoria.setClickable(true);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                categoria = parent.getItemAtPosition(posicao).toString();
                //imprime um Toast na tela com o nome que foi selecionado
                //  Toast.makeText(CadtransacaoActivity.this, categoria, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_limpar:
                Spinner tipo_trans = (Spinner) findViewById(R.id.spinner_tipo);
                EditText valor = (EditText) findViewById(R.id.editText_valorTransacao);
                Spinner categoria = (Spinner) findViewById(R.id.spinner_categoria);
                EditText descricao = (EditText) findViewById(R.id.editText_descricao);

                tipo_trans.setSelection(0);
                valor.setText("");
                categoria.setSelection(0);
                SelectedDateView.setText(day + "/" + (month + 1) + "/" + year);
                descricao.setText("");
                break;
            case R.id.button_salvarTransacao:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
