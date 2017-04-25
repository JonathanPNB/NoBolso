package unifimes.tcc.nobolso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import unifimes.tcc.nobolso.database.BDCore;
import unifimes.tcc.nobolso.entity.Saldo;
import unifimes.tcc.nobolso.utilidade.Utilidade;

/**
 * Created by Jonathan on 24/04/2017.
 */

public class SaldoDAO {

    public SaldoDAO saldoDAO;
    BDCore aux;
    private SQLiteDatabase bd;
    private Context ctx;
    private String LOG_TAG = getClass().getSimpleName()+"/"+ Utilidade.classeChamadora();

    public SaldoDAO(Context context) {
        this.aux = BDCore.getInstance(context);
        this.bd = aux.getWritableDatabase();
        this.ctx = context;
    }

    public void salvar(Saldo saldo) {
        ContentValues values = contentValuesSaldo(saldo);

        try {
            bd.insert(BDCore.NOME_TABELA_SALDO, null, values);
            Log.e(LOG_TAG, "Dados inseridos com sucesso. - " + values);
            Toast.makeText(ctx, "Dados inseridos com sucesso. " + values, Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, e.getMessage());
            return;
        }
    }

    private ContentValues contentValuesSaldo(Saldo saldo) {
        ContentValues values = new ContentValues();
        saldoDAO = new SaldoDAO(ctx);
        values.put(BDCore.COLUNA_MES, saldo.getMes());
        values.put(BDCore.COLUNA_ANO, saldo.getAno());
        values.put(BDCore.COLUNA_VALOR_SALDO, String.valueOf(saldo.getValorSaldo()));

        return values;
    }

}
