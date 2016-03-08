package unifimes.tcc.nobolso.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jonathan on 29/06/2015.
 * Classe para conexao e criacao do banco de dados
 */
public class BDCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "NoBolso.db";
    private static final int VERSAO_BD = 16;
    public static final String CRIAR_TABELA_CATEGORIA =
            "CREATE TABLE [Categoria] (\n" +
            "  [id_categoria] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "  [descricao] TEXT NOT NULL, \n" +
            "  [tipo] TINYINT NOT NULL);\n";

    public static final String CRIAR_TABELA_DESPESA =
                    "CREATE TABLE [Despesa] (\n" +
                    "  [id_despesa] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [valor] FLOAT(0, 2) NOT NULL, \n" +
                    "  [data] DATE NOT NULL, \n" +
                    "  [descricao] TEXT, \n" +
                    "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n";

    public static final String CRIAR_TABELA_RECEITA =
                    "CREATE TABLE [Receita] (\n" +
                    "  [id_receita] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [valor] FLOAT(0, 2) NOT NULL, \n" +
                    "  [data] DATE NOT NULL, \n" +
                    "  [descricao] TEXT, \n" +
                    "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n";


    private static final String[] NOME_TABELAS = {"Receita", "Despesa", "Categoria"};
  //  private static BDCore instance;

    public BDCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }
/*
    public static BDCore getInstance(Context context) {
        if(instance == null)
            instance = new BDCore(context);

        return instance;
    }
*/
    /**
     * Cria o banco de dados
     *
     * @param bd - banco de dados que está conectado
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        try {
            bd.execSQL(CRIAR_TABELA_CATEGORIA);
            bd.execSQL(CRIAR_TABELA_DESPESA);
            bd.execSQL(CRIAR_TABELA_RECEITA);
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
        Log.e("CRIAR_TABELAS", "Tabelas Criadas com Sucesso!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
        for(int a = 0; a<NOME_TABELAS.length;a++) {
            bd.execSQL("DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
            Log.w("ATUALIZA DB", "DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
    }
        this.onCreate(bd);
    }

    public void clearDB() {
        SQLiteDatabase bd = this.getReadableDatabase();
        for(int a = 0; a<NOME_TABELAS.length;a++) {
            Log.w("NOMES", NOME_TABELAS[a]);
            bd.execSQL("DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
        }
        this.onCreate(bd);
    }

    public boolean tabelaExiste(String tabela){
        SQLiteDatabase bd = this.getReadableDatabase();
        boolean tabelaExiste = false;
        try{
            @SuppressWarnings("unused")
            Cursor cursor = bd.query(tabela, null, null, null, null, null, null);
            tabelaExiste = true;
        }
        catch (SQLiteException e){
            if (e.getMessage().toString().contains("no such table")){
                tabelaExiste = false;
            }
        }
        return tabelaExiste;
    }

    public void executaQuery(String query) {
        SQLiteDatabase bd = this.getReadableDatabase();
        try{
            bd.execSQL(query);
        }
        catch (SQLiteException e){
            Log.e("executaQuery",e.getMessage().toString());
            return;
        }
    }

    public void criaTabela(String tabela) {
        SQLiteDatabase bd = this.getReadableDatabase();
        try {
            if(tabela.equalsIgnoreCase("receita"))
                bd.execSQL(BDCore.CRIAR_TABELA_RECEITA);
            else if(tabela.equalsIgnoreCase("despesa"))
                bd.execSQL(BDCore.CRIAR_TABELA_DESPESA);
            else if(tabela.equalsIgnoreCase("categoria"))
                bd.execSQL(BDCore.CRIAR_TABELA_CATEGORIA);
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
        Log.i("criaTabela","Tabela "+tabela+" Criada com Sucesso");
    }
}
