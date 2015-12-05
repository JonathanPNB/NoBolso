package unifimes.tcc.nobolso.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 29/06/2015.
 * Classe para conexao e criacao do banco de dados
 */
public class BDCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "NoBolso";
    private static final int VERSAO_BD = 9;
    private static final String CRIAR_TABELAS = "CREATE TABLE [Categoria] (\n" +
            "  [id_categoria] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "  [descricao] TEXT NOT NULL, \n" +
            "  [tipo] TINYINT NOT NULL);\n" +
            "\n" +
            "\n" +
            "CREATE TABLE [Despesa] (\n" +
            "  [id_despesa] BIGINT NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            "  [valor] FLOAT(0, 2) NOT NULL, \n" +
            "  [data] DATE NOT NULL, \n" +
            "  [descricao] TEXT, \n" +
            "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n" +
            "\n" +
            "\n" +
            "CREATE TABLE [Receita] (\n" +
            "  [id_receita] BIGINT NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            "  [valor] FLOAT(0, 2) NOT NULL, \n" +
            "  [data] DATE NOT NULL, \n" +
            "  [descricao] TEXT, \n" +
            "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n";

    private static final String[] NOME_TABELAS = {"Receita", "Despesa", "Categoria"};
    private static BDCore instance;

    public BDCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    public static BDCore getInstance(Context context) {
        if(instance == null)
            instance = new BDCore(context);

        return instance;
    }

    /**
     * Cria o banco de dados
     *
     * @param bd - banco de dados que está conectado
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        try {
            bd.execSQL(CRIAR_TABELAS);
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
        }
    }

    /**
     * Deleta a tabela e cria novamente.
     *
     * @param bd - banco de dados que está conectado
     * @param i - nao lembro
     * @param i1 - nao lembro
     */
    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        Log.e("NOMES",getNomeTabelas());
        for(int a = 0; a<NOME_TABELAS.length;a++) {
            bd.execSQL("DROP TABLE IF EXISTS " + getNomeTabelas());
    }
        this.onCreate(bd);
    }

    public static String getNomeTabelas() {
        return NOME_TABELAS.toString();
    }

    public List<String> getCategorias(){
        List<String> labels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NOME_TABELAS[2];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
}
