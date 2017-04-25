package unifimes.tcc.nobolso.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.dao.TransacaoDAO;
import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Categoria;
import unifimes.tcc.nobolso.entity.Transacao;
import unifimes.tcc.nobolso.utilidade.Utilidade;


public class CadtransacaoActivity extends AppCompatActivity {
    String opcao, categoria;
    List<String> listaCategorias = new ArrayList<>();
    public static EditText edtData;

    Intent intent;
    Spinner spnTipotrans, spnCategoria;
    EditText edtValor, edtDescricao;
    Button btnData;
    BDCore bd;

    @Override
    @NotNull
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadtransacao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar
        bd = BDCore.getInstance(this);

        spnTipotrans = (Spinner) findViewById(R.id.spinner_tipo);
        edtValor = (EditText) findViewById(R.id.editText_valorTransacao);
        spnCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        edtDescricao = (EditText) findViewById(R.id.editText_descricao);
        btnData = (Button) findViewById(R.id.button_data);
        edtData = (EditText) findViewById(R.id.editText_data);

        edtData.setText(Utilidade.formataNumero(Utilidade.getDia(), 2) + "/" + Utilidade.formataNumero(Utilidade.getMes(), 2) +
                "/" + Utilidade.getAno());
        this.spinnerAdapter();

        ativarComponentes(false);

        if (!bd.tabelaExiste(BDCore.NOME_TABELA_RECEITA)) {//se a tabela receita nao existir ela é criada
            bd.criaTabela(BDCore.NOME_TABELA_RECEITA);
        }
        if (!bd.tabelaExiste(BDCore.NOME_TABELA_DESPESA)) {//se a tabela receita nao existir ela é criada
            bd.criaTabela(BDCore.NOME_TABELA_DESPESA);
        }
        if (!bd.tabelaExiste(BDCore.NOME_TABELA_CATEGORIA)) {//se a tabela receita nao existir ela é criada
            bd.criaTabela(BDCore.NOME_TABELA_CATEGORIA);
        }

        edtValor.setKeyListener(Utilidade.getKeylistenerNumber());
        edtValor.addTextChangedListener(new Utilidade.mascaraMoeda(edtValor, this));
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
            case android.R.id.home://execução do botão voltar da ActionBar
                NavUtils.navigateUpFromSameTask(this);
                break;
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

            edtData.setText(Utilidade.formataNumero(Utilidade.getDia(), 2) + "/" + Utilidade.formataNumero(Utilidade.getMes(), 2) + "/" +
                    Utilidade.getAno());
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, Utilidade.getAno(), Utilidade.getMes() - 1,
                    Utilidade.getDia());
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            edtData.setText(Utilidade.formataNumero(day, 2) + "/" + Utilidade.formataNumero(month + 1, 2) + "/" + year);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        new Utilidade(getBaseContext()).escondeTeclado(getBaseContext(), v);
    }

    private void spinnerAdapter() {
        final CategoriaDAO catDAO = new CategoriaDAO(getBaseContext());
        listaCategorias.add("Selecione a Categoria");

        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, listaCategorias);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item,
                getResources().getStringArray(R.array.array_tipo_transacao)) {
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnTipotrans.setAdapter(adapter);
        spnTipotrans.setSelection(adapter.getCount());

        //Método do Spinner para capturar o item selecionado
        spnTipotrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                opcao = parent.getItemAtPosition(posicao).toString();

                if (opcao.equalsIgnoreCase("Receita") || opcao.equalsIgnoreCase("Despesa")) {//habilitar botoes
                    ativarComponentes(true);
                    edtValor.requestFocus();
                    new Utilidade(getBaseContext()).alteraKeyboard(getBaseContext(), true);

                    listaCategorias.clear();
                    List<Categoria> lista = catDAO.listar(spnTipotrans.getSelectedItemPosition());
                    for (int i = 0; i < lista.size(); i++) {
                        categoria = lista.get(i).getDescricao();
                        listaCategorias.add(categoria);
                    }
                }
                arrayAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnCategoria.setAdapter(arrayAdapter2);
        spnCategoria.setSelection(0);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_limpar:
                limparTela();
                break;
            case R.id.button_salvarTransacao:
                TransacaoDAO db = new TransacaoDAO(getBaseContext());

                if (spnCategoria.getSelectedItemPosition() >= 0 && edtValor.length() > 0) {
                    Transacao tran = new Transacao();
                    tran.setTipo(spnTipotrans.getSelectedItemPosition());//0=despesa / 1=receita
                    if (edtDescricao.getText().length() > 0) {
                        tran.setDescricao(edtDescricao.getText().toString());
                    } else {
                        tran.setDescricao("Sem Descrição");
                    }

                    tran.setCategoria(spnCategoria.getSelectedItem().toString());
                    if (tran.getTipo() == 0) {//despesa
                        tran.setValor(new BigDecimal(edtValor.getText().toString().replaceAll("[.]", "").replaceAll("[,]", ".")).negate());
                    } else {//receita
                        tran.setValor(new BigDecimal(edtValor.getText().toString().replaceAll("[.]", "").replaceAll("[,]", ".")));
                    }

                /*    String aux = edtData.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataTransacao = null;
                    try {
                        dataTransacao = sdf.parse(aux);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf.applyPattern("yyyy-MM-dd");
                    aux = sdf.format(dataTransacao);*/
                    tran.setData(Utilidade.formataData("dd/MM/yyyy", "yyyy-MM-dd", edtData.getText().toString()));
                    Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), tran.toString());

                    db.salvar(tran);
                    this.alertDialogNovaTransacao();
                } else {
                    Toast.makeText(CadtransacaoActivity.this, R.string.erro_campovazio, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                new Utilidade(getBaseContext()).escondeTeclado(getBaseContext(), v);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (spnTipotrans.getSelectedItemPosition() < 2) {
            this.alertDialogDescartarTransacao();
        } else {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        edtData.setText(Utilidade.formataNumero(Utilidade.getDia(), 2) + "/" + Utilidade.formataNumero(Utilidade.getMes(), 2) + "/" +
                Utilidade.getAno());
        edtDescricao.setText("");
    }

    public void alertDialogNovaTransacao() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        i = new Intent(getBaseContext(), CadtransacaoActivity.class);
                        startActivity(i);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        break;
                }

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja Cadastrar outra Transação?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    public void alertDialogDescartarTransacao() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("As alterações serão perdidas.\nDeseja Continuar?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }
}
