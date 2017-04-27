package unifimes.tcc.nobolso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Categoria;
import unifimes.tcc.nobolso.utilidade.Utilidade;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe para inserir/remover/alterar categorias no banco de dados
 */
public class CategoriaDAO {

    private SQLiteDatabase bd;
    BDCore aux;
    private Context ctx;

    public CategoriaDAO(Context context) {
        this.aux = BDCore.getInstance(context);
        this.bd = aux.getWritableDatabase();
        this.ctx = context;

    }

    public List<Categoria> listar(int tipo) {
        List<Categoria> list = new ArrayList<>();
        String selectQuery;

        // Select All Query
        if (tipo != 2)
            selectQuery = "SELECT * FROM " + BDCore.NOME_TABELA_CATEGORIA + " WHERE tipo = " + tipo + " and " +
                    BDCore.COLUNA_VISIVEL + " = 1 ORDER BY " + BDCore.COLUNA_DESCRICAO + " ASC";
        else
            selectQuery = "SELECT * FROM " + BDCore.NOME_TABELA_CATEGORIA + " WHERE " +
                    BDCore.COLUNA_VISIVEL + " = 1 ORDER BY " + BDCore.COLUNA_DESCRICAO + " ASC";

//        selectQuery = "SELECT * FROM "+BDCore.NOME_TABELA_CATEGORIA+" ORDER BY "+BDCore.COLUNA_DESCRICAO+" ASC";
        Cursor cursor = bd.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Categoria cat = new Categoria();
                cat.setId(cursor.getInt(0));
                cat.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                cat.setTipo(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_TIPO)));
                if (cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_VISIVEL)) > 0)
                    cat.setVisivel(true);
                else
                    cat.setVisivel(false);

    /*            Log.e("CatDAO/listar", "Visivel: " + cat.toString() + " Visivel: "
                        + cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_VISIVEL)));
*/
                list.add(cat);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public int buscaId(String categoria) {//se id > -1 categoria existe no BD
        String selectQuery;
        int resultado;

        selectQuery = "SELECT " + BDCore.COLUNA_ID + " FROM " + BDCore.NOME_TABELA_CATEGORIA + " where "
                + BDCore.COLUNA_DESCRICAO + " = ?";

        Cursor cursor = bd.rawQuery(selectQuery, new String[]{categoria});

        if (cursor.moveToFirst()) {  // moves the cursor to the first row in the result set...
            resultado = cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID));
        } else {
            resultado = -1;
        }

       //       Log.e("buscaId",String.valueOf(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID))));
        cursor.close();
        return resultado;
    }

    public String buscaNome(int id) {
        String selectQuery;
        String categoria;

        selectQuery = "SELECT " + BDCore.COLUNA_DESCRICAO + " FROM " + BDCore.NOME_TABELA_CATEGORIA + " where "
                + BDCore.COLUNA_ID + " = " + id;

  //      Log.e("buscaNomeCategoria", selectQuery);
        Cursor cursor = bd.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {  // moves the cursor to the first row in the result set...
            categoria = cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO));
        } else {
            categoria = "";
        }

    //    Log.e("buscaNome", cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
        cursor.close();
        return categoria;
    }

    public void salvar(Categoria cat) {
        ContentValues novosValores = contentValues(cat);
        int idCategoria = this.buscaId(cat.getDescricao());
        if (idCategoria >= 0) {//CATEGORIA JA EXISTE
            ContentValues oldValores = contentValues(this.buscaObj(idCategoria));//VERIFICA OS VALORES Q JA ESTAO GRAVADOS

            if (!oldValores.getAsBoolean(BDCore.COLUNA_VISIVEL)) {
                Toast.makeText(ctx, "Categoria já cadastrada.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    bd.update(BDCore.NOME_TABELA_CATEGORIA, novosValores, BDCore.COLUNA_ID + " =  ?",
                            new String[]{String.valueOf(idCategoria)});

                    Toast.makeText(ctx, "Dados inseridos com sucesso. " + novosValores, Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
                    return;
                }
            }
        } else {
            try {
                bd.insert(BDCore.NOME_TABELA_CATEGORIA, null, novosValores);

                Toast.makeText(ctx, "Dados inseridos com sucesso. " + novosValores, Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
                return;
            }
        }
    }

    public Categoria buscaObj(int id) {
        Categoria cat = new Categoria();
        String selectQuery;

     //   Log.e("buscaObjCategoria", "ID: " + id);
        selectQuery = "SELECT * FROM " + BDCore.NOME_TABELA_CATEGORIA + " WHERE " +
                BDCore.COLUNA_ID + " = " + id;

        Cursor cursor = bd.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cat.setId(cursor.getInt(0));
                cat.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                cat.setTipo(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_TIPO)));
                if (cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_VISIVEL)) > 0) {//inverte os valores true/false
                    cat.setVisivel(false);
                } else {
                    cat.setVisivel(true);
                }
    //            Log.e("buscaObjCategoria", "Visivel: " + cat.isVisivel() + " / " +
      //                  cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_VISIVEL))+" / ID: "+id);
            } while (cursor.moveToNext());
        }

        return cat;
    }

    public void remover(int id) {//A categoria não pode ser deletada do bd
        ContentValues valores = contentValues(this.buscaObj(id));
        try {
            bd.update(BDCore.NOME_TABELA_CATEGORIA, valores, BDCore.COLUNA_ID + " =  ?", new String[]{String.valueOf(id)});
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), "Categoria excluida com sucesso. " +
                    valores.toString());
        } catch (SQLiteException e) {
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
            return;
        }
    }

    public void alterar(Context context, Categoria cat) {
        ContentValues novosValores = contentValues(cat);
        if (cat.getId() >= 0) {//CATEGORIA JA EXISTE
            try {
                bd.update(BDCore.NOME_TABELA_CATEGORIA, novosValores, BDCore.COLUNA_ID + " =  ?",
                        new String[]{String.valueOf(cat.getId())});

                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), novosValores.toString()+" ID: "+cat.getId());
                Toast.makeText(context, "Dados alterados com sucesso. " + novosValores, Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
                return;
            }
        }
    }

    private ContentValues contentValues(Categoria cat) {
        ContentValues values = new ContentValues();
        int visivel;
        values.put(BDCore.COLUNA_DESCRICAO, cat.getDescricao());
        values.put(BDCore.COLUNA_TIPO, cat.getTipo());
        if (cat.isVisivel()) {
            visivel = 1;
        } else {
            visivel = 0;
        }
        values.put(BDCore.COLUNA_VISIVEL, visivel);

        return values;
    }
/*
    public void fecharConexao() {
        if(bd != null && bd.isOpen())
            bd.close();
    }*/
}
