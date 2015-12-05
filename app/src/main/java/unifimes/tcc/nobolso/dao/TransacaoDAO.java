package unifimes.tcc.nobolso.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import unifimes.tcc.nobolso.database.BDCore;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe de insercao da transacao no banco de dados
 */
public class TransacaoDAO {
    private SQLiteDatabase bd;

    public TransacaoDAO(Context context) {
        BDCore aux = new BDCore(context);
        bd = aux.getWritableDatabase();
    }
}
