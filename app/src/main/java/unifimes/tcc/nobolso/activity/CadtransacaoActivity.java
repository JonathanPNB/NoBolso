package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Transacao;


public class CadtransacaoActivity extends AppCompatActivity {
    String opcao, categoria;
    List<String> listaCategorias = new ArrayList<>();
    int tipo_relatorio;
    public static EditText edtData;
    private static final Calendar c = Calendar.getInstance();
    private static int year = c.get(Calendar.YEAR);
    private static int month = c.get(Calendar.MONTH);
    private static int day = c.get(Calendar.DAY_OF_MONTH);

    Spinner spnTipotrans, spnCategoria;
    EditText edtValor, edtDescricao;
    Button btnData;
    BDCore bd;
/*
    private static TransacaoDAO instance;

    public static TransacaoDAO getInstance(Context context) {
        if(instance == null)
            instance = new TransacaoDAO(context);
        return instance;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadtransacao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar
        bd = new BDCore(this);

        spnTipotrans = (Spinner) findViewById(R.id.spinner_tipo);
        edtValor = (EditText) findViewById(R.id.editText_valorTransacao);
        spnCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        edtDescricao = (EditText) findViewById(R.id.editText_descricao);
        btnData = (Button) findViewById(R.id.button_data);

        this.spinnerAdapter();
        edtData = (EditText) findViewById(R.id.editText_data);
        edtData.setText(day + "/" + (month + 1) + "/" + year);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.array_tipo_transacao, R.layout.spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnTipotrans.setAdapter(adapter);

        ativarComponentes(false);

        if(!bd.tabelaExiste("Receita")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Receita");
        }
        if(!bd.tabelaExiste("Despesa")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Despesa");
        }
        if(!bd.tabelaExiste("Categoria")) {//se a tabela receita nao existir ela é criada
            bd.criaTabela("Categoria");
        }
/*        edtValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtValor.addTextChangedListener(new TextWatcher() {

            private boolean isUpdating = false;
            // Pega a formatacao do sistema, se for brasil R$ se EUA US$
            private NumberFormat nf = NumberFormat.getCurrencyInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int after) {
                // Evita que o método seja executado varias vezes.
                // Se tirar ele entre em loop
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                isUpdating = true;
                String str = s.toString();
                // Verifica se já existe a máscara no texto.
                boolean hasMask = (str.indexOf("R$") > -1 &&
                (str.indexOf(".") > -1 || str.indexOf(",") > -1));
                // Verificamos se existe máscara
                if (hasMask) {
                    // Retiramos a máscara.
                    Log.e("ANTES",str);
                   String novo = str.replaceAll("R$", "").replaceAll("\\s+","");
                    Log.e("DEPOIS",novo);
                }


                try {
                    // Transformamos o número que está escrito no EditText em
                    // monetário.
                    str = nf.format(Double.parseDouble(str) / 100);
                //    Log.e("onTextChanged",str);
                    edtValor.setText(str);
                    edtValor.setSelection(edtValor.getText().length());
                } catch (NumberFormatException e) {
                    s = "";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Não utilizamos
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não utilizamos
            }
        });*/
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
  //      getMenuInflater().inflate(R.menu.menu_cadtransacao, menu);
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

            edtData.setText(day + "/" + (month + 1) + "/" + year);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            edtData.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void spinnerAdapter() {
       // final BDCore db = new BDCore(getBaseContext());
        final CategoriaDAO catDAO = new CategoriaDAO(getBaseContext());
        listaCategorias.add(0, "Selecione a Categoria");

        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaCategorias);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
    /*
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);

        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        spnCategoria.setAdapter(arrayAdapter2);

        //Método do Spinner para capturar o item selecionado
        spnTipotrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                opcao = parent.getItemAtPosition(posicao).toString();

                if (!opcao.equals(parent.getItemAtPosition(0).toString())) {//habilitar botoes
                    ativarComponentes(true);
                }
                else {
                    limparTela();
                }
                if(opcao.equals(parent.getItemAtPosition(1).toString())) {//Receita
                    tipo_relatorio = 1;
                }
                else if(opcao.equals(parent.getItemAtPosition(2).toString())) {//Despesa
                    tipo_relatorio = 0;
                }
                listaCategorias.clear();
                listaCategorias.add(0, "Selecione a Categoria");
                spnCategoria.setSelection(0);
                for (String palavra : catDAO.listar(tipo_relatorio)) {//0=despesa 1=receita 2=ambos
                    categoria = palavra.toString();
                    Log.i("DB", "Tipo Relatorio: " + tipo_relatorio + " / Categoria: "+categoria);
                    listaCategorias.add(categoria);
                }
                arrayAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                //            categoria = parent.getItemAtPosition(posicao).toString();
                //imprime um Toast na tela com o nome que foi selecionado
                //            Toast.makeText(CadtransacaoActivity.this, categoria, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_limpar:
                limparTela();
                break;
            case R.id.button_salvarTransacao:
                TransacaoDAO db = new TransacaoDAO(getBaseContext());

                if(spnCategoria.getSelectedItemPosition() > 0 && edtValor.length() > 0) {
                    Transacao tran = new Transacao();
                    tran.setTipo((spnTipotrans.getSelectedItemPosition() - 2) * (-1));//0=despesa / 1=receita
                    if(edtDescricao.getText().length() > 0)
                        tran.setDescricao(edtDescricao.getText().toString());
                    else
                        tran.setDescricao("Sem Descrição");

                    tran.setCategoria(spnCategoria.getSelectedItem().toString());
                    tran.setValor(Double.parseDouble(edtValor.getText().toString()));

                    String aux=edtData.getText().toString();
                    SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
                    Date dataTransacao = null;
                    try {
                        dataTransacao = sdf.parse(aux);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf.applyPattern("yyyy-MM-dd");
                    aux=sdf.format(dataTransacao);
                    tran.setData(aux);
                    Log.i("CADASTRO", tran.toString());

                    db.salvar(this,tran);
                    alertDialogOutraTransacao();
                }
                else
                    Toast.makeText(CadtransacaoActivity.this, R.string.erro_campovazio, Toast.LENGTH_SHORT).show();
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

    private void ativarComponentes(boolean flag) {
        edtValor.setClickable(flag);
        edtValor.setEnabled(flag);

        spnCategoria.setClickable(flag);

        btnData.setEnabled(flag);
        btnData.setClickable(flag);

        edtDescricao.setClickable(flag);
        edtDescricao.setEnabled(flag);
    }

    private void limparTela() {
        ativarComponentes(false);
        spnTipotrans.setSelection(0);
        edtValor.setText("");
        spnCategoria.setSelection(0);
        edtData.setText(day + "/" + (month + 1) + "/" + year);
        edtDescricao.setText("");
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void alertDialogOutraTransacao() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        limparTela();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja Cadastrar outra Transação?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }
}
