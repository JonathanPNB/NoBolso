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

/**
 * Created by Jonathan on 14/11/2015.
 * Classe para inserir/remover categoria no banco de dados
 */
public class CategoriaDAO {
    public static final String NOME_TABELA = "Categoria";
    public static final String COLUNA_DESCRICAO = "descricao";
    public static final String COLUNA_TIPO = "tipo";

    private SQLiteDatabase bd = null;

    public CategoriaDAO(Context context) {
        BDCore conn = new BDCore(context);
        bd = conn.getWritableDatabase();
    }
    public List<String> listar(int tipo){
        List<String> list = new ArrayList<>();
        String selectQuery;

        // Select All Query
        if(tipo != 2)
            selectQuery = "SELECT * FROM Categoria where tipo = "+tipo;
       else
            selectQuery = "SELECT * FROM Categoria";

        Cursor cursor = bd.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                list.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }

        return(list);
    }

    public void salvar(Context context, Categoria cat) {
        ContentValues values = contentValuesCategoria(cat);
        try {
            bd.insert(NOME_TABELA, null, values);
            Log.i("INFO", "Dados inseridos com sucesso. - " + values);
            Toast.makeText(context, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
    }
/*
    public List<Ca'tegoria> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = bd.rawQuery(queryReturnAll, null);

        return construirVeiculoPorCursor(cursor);
    }

    public void deletar(Categoria cat) {

        String[] valoresParaSubstituir = {
                String.valueOf(cat.getId())
        };

        bd.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }

    public void editar(Categoria cat) {
        ContentValues valores = gerarContentValeuesVeiculo(cat);

        String[] valoresParaSubstituir = {
                String.valueOf(cat.getId())
        };

        bd.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }

    public void fecharConexao() {
        if(bd != null && bd.isOpen())
            bd.close();
    }


    private List<Categoria> construirVeiculoPorCursor(Cursor cursor) {
        List<Categoria> categorias = new ArrayList<>();
        if(cursor == null)
            return categorias;

        try {

            if (cursor.moveToFirst()) {
                do {

            //        int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexDescricao = cursor.getColumnIndex(COLUNA_DESCRICAO);
                    int indexTipo = cursor.getColumnIndex(COLUNA_TIPO);

          //          int id = cursor.getInt(indexID);
                    String descricao = cursor.getString(indexDescricao);
                    int tipo = cursor.getInt(indexTipo);

                    Categoria cat = new Categoria(descricao, tipo);

                    categorias.add(cat);

                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }
        return categorias;
    }
*/
    private ContentValues contentValuesCategoria(Categoria cat) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_DESCRICAO, cat.getDescricao());
        values.put(COLUNA_TIPO, cat.getTipo());

        return values;
    }
}
