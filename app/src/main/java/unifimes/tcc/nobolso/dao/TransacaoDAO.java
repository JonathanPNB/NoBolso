package unifimes.tcc.nobolso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Transacao;
import unifimes.tcc.nobolso.utilidade.Utilidade;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe de insercao da transacao no banco de dados
 */
public class TransacaoDAO {

    private String NOME_TABELA;
    private SQLiteDatabase bd;
    BDCore aux;
    public CategoriaDAO catDAO;
    // public Utilidade util;
    private Context ctx;

    public TransacaoDAO(Context context) {
        this.aux = BDCore.getInstance(context);
        this.bd = aux.getWritableDatabase();
        this.ctx = context;
    }

    public void salvar(Transacao tran) {
        ContentValues values = contentValuesTransacao(tran);
        //     util = new Utilidade(ctx);

        if (tran.getTipo() == 0)
            NOME_TABELA = BDCore.NOME_TABELA_DESPESA;
        else
            NOME_TABELA = BDCore.NOME_TABELA_RECEITA;

        try {
            bd.insert(NOME_TABELA, null, values);
            Log.e("CADASTRO", "Dados inseridos com sucesso. - " + values);
            Toast.makeText(ctx, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
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

    public ArrayList<Transacao> buscarTodasTransacoes() {
        ArrayList<Transacao> list = new ArrayList<>();
        String[] Tabelas = {BDCore.NOME_TABELA_DESPESA, BDCore.NOME_TABELA_RECEITA};//despesa = 0 / receita = 1

        for (int i = 0; i < Tabelas.length; i++) {

            Cursor cursor = bd.query(Tabelas[i], null, null, null, null, null, "data ASC");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Transacao tr = new Transacao();
                    tr.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                    tr.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                    tr.setValor(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR)))));
                    tr.setData(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DATA)));
                    tr.setCategoria(catDAO.buscaNome(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID_CATEGORIA))));

                    list.add(tr);
                    Log.e("[BD]Buscar Transação", tr.toString());

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        //    catDAO.fecharConexao();
        return list;
    }

    public ArrayList<Transacao> transacoesMes(String categoria, int mes, int ano) {
        ArrayList<Transacao> list = new ArrayList<>();
        String selectQuery;
        catDAO = new CategoriaDAO(ctx);
        String[] periodo = new String[]{Utilidade.formataNumero(mes, 2), String.valueOf(ano)};
        Cursor cursor;

        Log.e("transacoesMes", "Args: " + categoria + " - " + mes + "/" + ano);

        selectQuery = "SELECT * FROM " + categoria + " WHERE strftime('%m', " + BDCore.COLUNA_DATA + ") = ? " +
                "AND strftime('%Y', " + BDCore.COLUNA_DATA + ") = ?";

        cursor = bd.rawQuery(selectQuery, periodo);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Transacao transacao = new Transacao();
                transacao.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                transacao.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                transacao.setValor(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR)))));
                transacao.setData(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DATA)));
                transacao.setCategoria(catDAO.buscaNome(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID_CATEGORIA))));

                list.add(transacao);
                Log.e("[BD]Listar Transação", String.valueOf(cursor.getInt(0)) + " / " + cursor.getDouble(cursor.getColumnIndex("valor")) + " / " +
                        cursor.getString(cursor.getColumnIndex("data")) + " / " + cursor.getString(cursor.getColumnIndex("descricao")) + " / " +
                        cursor.getInt(cursor.getColumnIndex("id_categoria")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //    catDAO.fecharConexao();
        return list;
    }

    public ArrayList<Transacao> relatorioTransacao(String tipoRelatorio, int mes, int ano, String categoria) {
        ArrayList<Transacao> list = new ArrayList<>();
        catDAO = new CategoriaDAO(ctx);
        String selectQuery;
        // String[] Tabelas = {BDCore.NOME_TABELA_DESPESA, BDCore.NOME_TABELA_RECEITA};//despesa = 0 / receita = 1
        String nomeTransacao = "";
        Cursor cursor;
        String dataInicial, dataFinal;

        if (tipoRelatorio.equalsIgnoreCase("Despesa")) {//despesa
            nomeTransacao = "despesa";
        } else if (tipoRelatorio.equalsIgnoreCase("Receita")) {//receita
            nomeTransacao = "receita";
        }

        if (mes == 0) {//todos os meses do ano
            dataInicial = ano + "-" + Utilidade.formataNumero(1, 2) + "-01";
            dataFinal = ano + "-" + Utilidade.formataNumero(12, 2) + "-" + Utilidade.ultimoDiadoMes(12);
        } else {//pegar apenas o mes selecionado
            dataInicial = ano + "-" + Utilidade.formataNumero(mes, 2) + "-01";
            dataFinal = ano + "-" + Utilidade.formataNumero(mes, 2) + "-" + Utilidade.ultimoDiadoMes(mes);
        }

        if (ano == 0) {//todos os anos
            dataInicial = "2015" + "-" + Utilidade.formataNumero(1, 2) + "-01";
            dataFinal = Utilidade.getAno() + "-" + Utilidade.formataNumero(12, 2) + "-" + Utilidade.ultimoDiadoMes(12);
        }

        int idCategoria;
        if (!categoria.equalsIgnoreCase("Todas")) {
            idCategoria = catDAO.buscaId(categoria);
            selectQuery = "SELECT * FROM " + nomeTransacao + " WHERE " + BDCore.COLUNA_ID_CATEGORIA + " = " + idCategoria +
                    " and " + BDCore.COLUNA_DATA + " between '" + dataInicial + "' " +
                    "AND '" + dataFinal + "'";
        } else {
            selectQuery = "SELECT * FROM " + nomeTransacao + " WHERE " + BDCore.COLUNA_DATA + " between '" + dataInicial + "' " +
                    "AND '" + dataFinal + "'";
        }

        Log.e("relatorioTransacao", "Args: " + tipoRelatorio + " - " + mes + "/" + ano + " - " + categoria);

        //  if (!tipoRelatorio.equalsIgnoreCase("Todos")) {//despesa ou receita
        //data between '2016-05-01' and '2016-05-31'


        //     String[] colunas = new String[]{BDCore.COLUNA_ID, BDCore.COLUNA_DESCRICAO, BDCore.COLUNA_VALOR,
        //           BDCore.COLUNA_DATA, BDCore.COLUNA_ID_CATEGORIA};
        Log.e("relatorioTransacao", nomeTransacao + " - " + selectQuery);
        cursor = bd.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Transacao transacao = new Transacao();
                transacao.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                transacao.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                transacao.setValor(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR)))));
                transacao.setData(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DATA)));
                transacao.setCategoria(catDAO.buscaNome(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID_CATEGORIA))));

                list.add(transacao);
            } while (cursor.moveToNext());
        }
        cursor.close();
      /*  } else {//todas as transacoes

            for (int i = 0; i < Tabelas.length; i++) {
                selectQuery = "SELECT * FROM " + Tabelas[i] + " WHERE " + BDCore.COLUNA_DATA + " between '" + dataInicial + "' " +
                        "AND '" + dataFinal + "'";
                cursor = bd.rawQuery(selectQuery, null);

                Log.e("relatorioTransacao", "Transações - " + selectQuery);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        Transacao tr = new Transacao();
                        tr.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                        tr.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                        tr.setValor(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR)))));
                        tr.setData(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DATA)));
                        tr.setCategoria(catDAO.buscaNomeCategoria(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID_CATEGORIA))));

                        list.add(tr);
                        Log.w("[BD]Buscar Transação", tr.toString());

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }*/
        return list;
    }

    public ArrayList<Transacao> transacoesDia(String categoria, int dia, int mes, int ano) {
        ArrayList<Transacao> list = new ArrayList<>();
        String selectQuery;
        catDAO = new CategoriaDAO(ctx);
        String[] periodo = new String[]{Utilidade.formataNumero(dia, 2), Utilidade.formataNumero(mes, 2), String.valueOf(ano)};
        Cursor cursor;

        Log.e("transacoesDia", "Args: " + dia + "/" + mes + "/" + ano);

            selectQuery = "SELECT * FROM " + categoria + " WHERE strftime('%d', " + BDCore.COLUNA_DATA + ") = ? " +
                    "AND strftime('%m', " + BDCore.COLUNA_DATA + ") = ? " + "AND strftime('%Y', " + BDCore.COLUNA_DATA + ") = ?";

            cursor = bd.rawQuery(selectQuery, periodo);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Transacao transacao = new Transacao();
                    transacao.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                    transacao.setDescricao(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DESCRICAO)));
                    transacao.setValor(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR)))));
                    transacao.setData(cursor.getString(cursor.getColumnIndex(BDCore.COLUNA_DATA)));
                    transacao.setCategoria(catDAO.buscaNome(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID_CATEGORIA))));

                    list.add(transacao);
                    Log.e("[BD]Listar Transação", String.valueOf(cursor.getInt(0)) + " / " + cursor.getDouble(cursor.getColumnIndex("valor")) + " / " +
                            cursor.getString(cursor.getColumnIndex("data")) + " / " + cursor.getString(cursor.getColumnIndex("descricao")) + " / " +
                            cursor.getInt(cursor.getColumnIndex("id_categoria")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        return list;
    }

    private ContentValues contentValuesTransacao(Transacao tran) {
        ContentValues values = new ContentValues();
        catDAO = new CategoriaDAO(ctx);
        values.put(BDCore.COLUNA_VALOR, String.valueOf(tran.getValor()));
        values.put(BDCore.COLUNA_DATA, tran.getData());
        values.put(BDCore.COLUNA_DESCRICAO, tran.getDescricao());
        values.put(BDCore.COLUNA_ID_CATEGORIA, catDAO.buscaId(tran.getCategoria()));//pegar o nome da categoria e buscar seu id na tabela Categorias

        return values;
    }
}
