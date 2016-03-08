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
import unifimes.tcc.nobolso.entity.Transacao;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe de insercao da transacao no banco de dados
 */
public class TransacaoDAO {
    public static final String NOME_TABELA_DESPESA = "Despesa";
    public static final String NOME_TABELA_RECEITA = "Receita";
    private String NOME_TABELA;

    public static final String COLUNA_ID = "id_categoria";
    public static final String COLUNA_DESCRICAO = "descricao";
    public static final String COLUNA_VALOR = "valor";
    public static final String COLUNA_DATA = "data";

    public static final String COLUNA_CATEGORIA = "descricao";

    private SQLiteDatabase bd;
    BDCore aux;

    /*   private static TransacaoDAO instance;

       public static TransacaoDAO getInstance(Context context) {
           if (instance == null)
               instance = new TransacaoDAO(context);
           return instance;
       }
   */
    public TransacaoDAO(Context context) {
        aux = new BDCore(context);
        bd = aux.getWritableDatabase();
    }

    public void salvar(Context context, Transacao tran) {
        ContentValues values = contentValuesTransacao(tran);
        if (tran.getTipo() == 0)
            NOME_TABELA = NOME_TABELA_DESPESA;
        else
            NOME_TABELA = NOME_TABELA_RECEITA;

        try {
            bd.insert(NOME_TABELA, null, values);
            //   bd.insert("Receita", null, 1,250.0,2016-02-02,"TESTE1",1);
            Log.i("INFO", "Dados inseridos com sucesso. - " + values);
            Toast.makeText(context, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
    }

    private ContentValues contentValuesTransacao(Transacao tran) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_VALOR, tran.getValor());
        values.put(COLUNA_ID, tran.getTipo());
        values.put(COLUNA_CATEGORIA, tran.getCategoria());
        values.put(COLUNA_DESCRICAO, tran.getDescricao());
        values.put(COLUNA_DATA, tran.getData());

        return values;
    }
/*
    public void inserir(Usuario usuario){
        ContentValues valores = new ContentValues();
        valores.put("nome", usuario.getNome());
        valores.put("email", usuario.getEmail());
        valores.put("senha", usuario.getSenha());

        bd.insert("usuario", null, valores);
    }


    public void atualizar(Usuario usuario){
        ContentValues valores = new ContentValues();
        valores.put("nome", usuario.getNome());
        valores.put("email", usuario.getEmail());

        bd.update("usuario", valores, "_id = ?", new String[]{""+usuario.getId()});
    }


    public void deletar(Usuario usuario){
        bd.delete("usuario", "_id = "+usuario.getId(), null);
    }*/

    public List<Transacao> buscar() {
        List<Transacao> list = new ArrayList<>();
        String[] Tabelas = {NOME_TABELA_DESPESA, NOME_TABELA_RECEITA};//despesa = 0 / receita = 1

        for (int i = 0; i < Tabelas.length; i++) {

            Cursor cursor = bd.query(Tabelas[i].toString(), null, null, null, null, null, "data ASC");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Transacao tr = new Transacao();
                    tr.setId(cursor.getInt(0));
                    tr.setValor(cursor.getDouble(1));
                    tr.setData(cursor.getString(2));
                    tr.setDescricao(cursor.getString(3));
                    tr.setTipo(cursor.getInt(4));//0=despesa / 1=receita
                    if (cursor.getInt(4) == 0)
                        tr.setCategoria("Despesa");
                    else
                        tr.setCategoria("Receita");

                    list.add(tr);
                    Log.w("[BD]Buscar Transação", tr.toString());

                } while (cursor.moveToNext());
            }
        }
        return (list);
    }

    public ArrayList<Transacao> listar(String categoria) {
        ArrayList<Transacao> list = new ArrayList<>();
        String selectQuery;

        selectQuery = "SELECT * FROM "+categoria;

        Cursor cursor = bd.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                Transacao transacao = new Transacao();
                transacao.setDescricao(cursor.getString(3));
                transacao.setValor(cursor.getDouble(1));
                if (cursor.getInt(4) == 0)
                    transacao.setCategoria("Despesa");
                else
                    transacao.setCategoria("Receita");

                list.add(transacao);
            }while(cursor.moveToNext());
        }
        return list;
    }
}
