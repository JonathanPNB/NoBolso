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
import unifimes.tcc.nobolso.entity.Saldo;
import unifimes.tcc.nobolso.utilidade.Utilidade;

/**
 * Created by Jonathan on 24/04/2017.
 */

public class SaldoDAO {

    BDCore aux;
    private SQLiteDatabase bd;
    private Context ctx;

    public SaldoDAO(Context context) {
        this.aux = BDCore.getInstance(context);
        this.bd = aux.getWritableDatabase();
        this.ctx = context;
    }

    public void salvar(Saldo saldo) {//fazer calculo do saldo ao dar update
        Saldo saldoAntigo = this.existeSaldo(saldo);
        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(),String.valueOf(saldoAntigo+"-"+saldo));
        ContentValues values = contentValuesSaldo(saldoAntigo, saldo);

        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(),String.valueOf(saldoAntigo));
        try {
            if(saldoAntigo.getId() >= 0) {//já existe saldo para o mes informado
                bd.update(BDCore.NOME_TABELA_SALDO, values, BDCore.COLUNA_MES + " =  ? AND "+BDCore.COLUNA_ANO + " = ?",
                        new String[]{String.valueOf(saldo.getMes()),String.valueOf(saldo.getAno())});

                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), values.toString()+" ID: "+saldo.getId());
                Toast.makeText(ctx, "Dados alterados com sucesso. " + values, Toast.LENGTH_SHORT).show();
            } else {
                bd.insert(BDCore.NOME_TABELA_SALDO, null, values);
                Log.e(getClass().getSimpleName() + "/" + Utilidade.classeChamadora(), "Dados inseridos com sucesso. - " + values);
                Toast.makeText(ctx, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
            return;
        }
    }
/*
    public void alterar(Saldo saldo) {
        ContentValues novosValores = contentValuesSaldo(saldo);

            try {
                bd.update(BDCore.NOME_TABELA_SALDO, novosValores, BDCore.COLUNA_ID + " =  ?",
                        new String[]{String.valueOf(saldo.getId())});

                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), novosValores.toString()+" ID: "+saldo.getId());
                Toast.makeText(ctx, "Dados alterados com sucesso. " + novosValores, Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), e.getMessage());
                return;
            }
    }
*/
    public Saldo existeSaldo(Saldo saldo) {
        Saldo saldoAntigo = new Saldo();
        String[] campos = {BDCore.COLUNA_ID, BDCore.COLUNA_MES, BDCore.COLUNA_ANO, BDCore.COLUNA_VALOR_SALDO};
        String where = BDCore.COLUNA_MES+"="+saldo.getMes()+" AND "+BDCore.COLUNA_ANO+"="+saldo.getAno();

        Cursor cursor = bd.query(BDCore.NOME_TABELA_SALDO, campos, where, null, null, null, null);

        if(cursor.moveToFirst()) {
            saldoAntigo.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
            saldoAntigo.setMes(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_MES)));
            saldoAntigo.setAno(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ANO)));
            saldoAntigo.setValorSaldo(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR_SALDO)))));
        } else {
            saldoAntigo.setId(-1);
            saldoAntigo.setValorSaldo(new BigDecimal(String.valueOf(BigDecimal.ZERO)));
        }

        return saldoAntigo;
    }

    public ArrayList<Saldo> buscarSaldos() {
        ArrayList<Saldo> list = new ArrayList<>();

        Cursor cursor = bd.query(BDCore.NOME_TABELA_SALDO, null, null, null, null, null, "ano,mes ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Saldo saldo = new Saldo();
                saldo.setId(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ID)));
                saldo.setMes(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_MES)));
                saldo.setAno(cursor.getInt(cursor.getColumnIndex(BDCore.COLUNA_ANO)));
                saldo.setValorSaldo(new BigDecimal(String.valueOf(cursor.getDouble(cursor.getColumnIndex(BDCore.COLUNA_VALOR_SALDO)))));

                list.add(saldo);
                Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), saldo.toString());

            } while (cursor.moveToNext());
        }
        else {
            Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), "cursor.getCount() < 0");
        }
        cursor.close();
        return list;
    }

    private ContentValues contentValuesSaldo(Saldo saldoAntigo, Saldo saldoNovo) {
        ContentValues values = new ContentValues();
        BigDecimal novoSaldo = saldoAntigo.getValorSaldo().add(saldoNovo.getValorSaldo());
        values.put(BDCore.COLUNA_MES, saldoNovo.getMes());
        values.put(BDCore.COLUNA_ANO, saldoNovo.getAno());
        values.put(BDCore.COLUNA_VALOR_SALDO, String.valueOf(novoSaldo));

        Log.e(getClass().getSimpleName()+"/"+Utilidade.classeChamadora(), values.toString());
        return values;
    }

}
