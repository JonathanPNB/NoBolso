package unifimes.tcc.nobolso.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.dao.CategoriaDAO;
import unifimes.tcc.nobolso.entity.Categoria;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class CadcategoriaActivity extends AppCompatActivity {

    Intent intent;
    CategoriaDAO catDAO = null;

    EditText edtNomeCategoria;
    RadioGroup radioGroup;
    int rdSelecionado;
    Button btnSalvar;
    RadioButton radioReceita;
    RadioButton radioDespesa;

    int idCategoria = -1;
    int tipo;
    String titulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadcategoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//habilita botão voltar na ActionBar

        catDAO = new CategoriaDAO(this);
        edtNomeCategoria = (EditText) findViewById(R.id.editText_nome_categoria);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnSalvar = (Button) findViewById(R.id.button5);
        radioReceita = (RadioButton) findViewById(R.id.radioButton_receita);
        radioDespesa = (RadioButton) findViewById(R.id.radioButton_despesa);

        Bundle extras = getIntent().getExtras();

        if(getIntent().hasExtra("id_categoria")){
            idCategoria = extras.getInt("id_categoria");
        }

        if(idCategoria >= 0) {
            if(getIntent().hasExtra("Titulo")) {
                titulo = extras.getString("Titulo");
            }
            this.editarCategoria();
        }
    }

    public void inserirCategoria(View v) {
        int tipo;
        new Utilidade(getBaseContext()).alteraKeyboard(getBaseContext(), false);

        if(edtNomeCategoria.getText().toString().trim().isEmpty()) {
            Toast.makeText(CadcategoriaActivity.this,R.string.erro_campovazio,Toast.LENGTH_SHORT).show();
            return;
        }
        rdSelecionado = radioGroup.getCheckedRadioButtonId();
        if (rdSelecionado == R.id.radioButton_despesa) {
            tipo = 0;//despesa
        } else if(rdSelecionado == R.id.radioButton_receita) {
            tipo = 1;
        } else {
            Toast.makeText(CadcategoriaActivity.this, R.string.erro_radiobutton, Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria cat = new Categoria(edtNomeCategoria.getText().toString(), tipo, true);

        catDAO.salvar(this, cat);
        intent = new Intent(getBaseContext(), CategoriasActivity.class);
        startActivity(intent);
       // catDAO.fecharConexao();
        finish();
    }

    public void editarCategoria() {
        setTitle(titulo);
        final Categoria antigo = catDAO.buscaObj(idCategoria);

        btnSalvar.setText("Alterar");
        edtNomeCategoria.setText(antigo.getDescricao());
        edtNomeCategoria.setSelection(edtNomeCategoria.getText().length());
        new Utilidade(getBaseContext()).alteraKeyboard(getBaseContext(), true);

        if(antigo.getTipo() == 0) {
            radioDespesa.setChecked(true);
        } else {
            radioReceita.setChecked(true);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rdSelecionado = radioGroup.getCheckedRadioButtonId();

                if (rdSelecionado == R.id.radioButton_despesa) {
                    tipo = 0;//despesa
                } else if(rdSelecionado == R.id.radioButton_receita) {
                    tipo = 1;
                }

                Categoria novo = new Categoria();
                novo.setId(idCategoria);
                novo.setTipo(tipo);
                novo.setVisivel(true);
                novo.setDescricao(edtNomeCategoria.getText().toString());

                catDAO.alterar(getBaseContext(),novo);
                new Utilidade(getBaseContext()).escondeTeclado(getBaseContext(), view);
                intent = new Intent(getBaseContext(), CategoriasActivity.class);
                startActivity(intent);
                finish();
            }
        });
     //   catDAO.fecharConexao();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CategoriasActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
