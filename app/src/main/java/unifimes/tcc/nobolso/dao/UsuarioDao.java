package unifimes.tcc.nobolso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Usuario;

/**
 * Created by Jonathan on 15/07/2015.
 * Classe para insercao de usuario no banco de dados, apenas para testes
 */
public class UsuarioDao {
    private SQLiteDatabase bd;

    public UsuarioDao(Context context) {
        BDCore aux = new BDCore(context);
        bd = aux.getWritableDatabase();
    }
/*
    public String inserirUsuario(Usuario user) {
        ContentValues valores = new ContentValues();
        long resultado = -1;
        try {
            valores.put("nome", user.getNome());
            valores.put("login", user.getLogin());
            valores.put("senha", user.getSenha());

            if(this.buscarUsuario(user.getLogin()) == null) {
                resultado = bd.insert(BDCore.getNomeTabelaUsuario(), null, valores);
            } else {
                return "Login já existe";
            }

            if (resultado < 0) {
                return "Erro ao inserir valor no banco de dados.";
            } else
                return "SUCESSO";
        }catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
        } finally {
            bd.close();
        }
        return "Erro Desconhecido";
    }

    // Busca o usuario pelo login "select * from Usuarios where login=?"
    public Usuario buscarUsuario(String login) {
        Usuario user = null;
        Cursor c = bd.query("Usuarios", new String[] {"_id","nome","login","senha"}, "login='" + login + "'", null, null, null, null);

        if (c.moveToNext()) {
            user = new Usuario();
            user.setId(c.getInt(0));
            user.setNome(c.getString(1));
            user.setLogin(c.getString(2));
            user.setSenha(c.getString(3));
        }
        return user;
    }


    public void atualizar(Usuario user) {
        ContentValues valores = new ContentValues();
        valores.put("nome", user.getNome());
        valores.put("login", user.getLogin());

        bd.update(BDCore.getNomeTabelaUsuario(), valores, "_id = ?", new String[]{"" + user.getId()});
    }

    public void deletar(Usuario user) {
        bd.delete(BDCore.getNomeTabelaUsuario(), "_id = ?", new String[]{"" + user.getId()});
    }

    public List<Usuario> buscar() {
        List<Usuario> list = new ArrayList<Usuario>();
        String[] colunas = {"_id", "nome", "login", "senha"};
        Cursor cursor = bd.query(BDCore.getNomeTabelaUsuario(), colunas, null,
                null, null, null, "nome ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Usuario u = new Usuario();
                u.setId(cursor.getInt(0));
                u.setNome(cursor.getString(1));
                u.setLogin(cursor.getString(2));
                u.setSenha(cursor.getString(3));
                list.add(u);

            }while(cursor.moveToNext());
        }
        return list;
    }*/
}
