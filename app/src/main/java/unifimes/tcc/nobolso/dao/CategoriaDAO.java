package unifimes.tcc.nobolso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
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
    public static final String COLUNA_ID = "id_categoria";
    public static final String COLUNA_DESCRICAO = "descricao";
    public static final String COLUNA_TIPO = "tipo";

    private SQLiteDatabase bd = null;


    private static CategoriaDAO instance;

    public static CategoriaDAO getInstance(Context context) {
        if(instance == null)
            instance = new CategoriaDAO(context);
        return instance;
    }

    private CategoriaDAO(Context context) {
        BDCore conn = BDCore.getInstance(context);
        bd = conn.getWritableDatabase();
    }

    public void salvar(Context context, Categoria cat) {
        ContentValues values = gerarContentValeuesVeiculo(cat);
        try {
            bd.insert(NOME_TABELA, null, values);
            Log.i("INFO", "Dados inseridos com sucesso. - " + values);
            Toast.makeText(context, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
        }
    }

    public List<Categoria> recuperarTodos() {
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

    private ContentValues gerarContentValeuesVeiculo(Categoria veiculo) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_DESCRICAO, veiculo.getDescricao());
        values.put(COLUNA_TIPO, veiculo.getTipo());

        return values;
    }
}
