package unifimes.tcc.nobolso;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    /** Criação da Janela de Login
     * Ele pega os valores que estão nos campos de login e senha e compara com os valores corretos,
     * caso esteja correto ele passa para o MainActivity. Teste
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.button_login);
        Button cadastrar = (Button) findViewById(R.id.button_cadastrar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = ((EditText)findViewById(R.id.editText_usuario)).getText().toString();
                String senha = ((EditText)findViewById(R.id.editText_senha)).getText().toString();

                if(usuario.equals("admin") && senha.equals("admin")){
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);//cria um intent para a nova activity
                    startActivity(main);//inicia a nova activity
                    finish();//finaliza activity atual
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erro_loginsenha, Toast.LENGTH_SHORT).show();
                }
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
              Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
              startActivity(cadastro);
              finish();//finaliza activity atual
          }
        });
    }
}
