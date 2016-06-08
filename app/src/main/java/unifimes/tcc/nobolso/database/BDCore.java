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

    private static BDCore mInstance = null;

    private static final String NOME_BD = "NoBolso.db";
    private static final int VERSAO_BD = 24;
    public static final String NOME_TABELA_CATEGORIA = "Categoria";
    public static final String NOME_TABELA_RECEITA = "Receita";
    public static final String NOME_TABELA_DESPESA = "Despesa";

    public static final String[] NOME_TABELAS = {NOME_TABELA_RECEITA, NOME_TABELA_DESPESA, NOME_TABELA_CATEGORIA/*, NOME_TABELA_SALDO*/};
    private static final String[] CATEGORIAS_RECEITA_PADRAO = {"Salário", "Rendimentos", "Bônus", "Comissão", "Férias",
            "13º Salário","Reembolso", "Outras Receitas"};
    private static final String[] CATEGORIAS_DESPESA_PADRAO = {"Lazer", "Alimentação", "Automóveis", "Casa", "Impostos",
            "Saúde", "Educação", "Transporte", "Seguros", "Gastos Pessoais", "Gastos Esporádicos"};

    public static final String COLUNA_ID = "id";
    public static final String COLUNA_ID_CATEGORIA = "id_categoria";
    public static final String COLUNA_DESCRICAO = "descricao";
    public static final String COLUNA_TIPO = "tipo";
    public static final String COLUNA_DATA = "data";
    public static final String COLUNA_VALOR = "valor";
    public static final String COLUNA_VISIVEL = "visivel";

    private static final String CRIAR_TABELA_CATEGORIA =
            "CREATE TABLE [Categoria] (\n" +
                    "  [id] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [descricao] TEXT NOT NULL, \n" +
                    "  [tipo] TINYINT NOT NULL, \n" +
                    "  [visivel] INTEGER NOT NULL DEFAULT 'true');";

    private static final String CRIAR_TABELA_DESPESA =
            "CREATE TABLE [Despesa] (\n" +
                    "  [id] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [valor] DOUBLE(0, 2) NOT NULL, \n" +
                    "  [data] DATE NOT NULL, \n" +
                    "  [descricao] TEXT, \n" +
                    "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n";

    private static final String CRIAR_TABELA_RECEITA =
            "CREATE TABLE [Receita] (\n" +
                    "  [id] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [valor] DOUBLE(0, 2) NOT NULL, \n" +
                    "  [data] DATE NOT NULL, \n" +
                    "  [descricao] TEXT, \n" +
                    "  [id_categoria] INTEGER NOT NULL CONSTRAINT [id_categoria] REFERENCES [Categoria]([id_categoria]) ON DELETE RESTRICT);\n";

    private String INSERIR_CATEGORIAS_PADRAO;

    private BDCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    public static BDCore getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new BDCore(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Cria o banco de dados
     *
     * @param bd - banco de dados que está conectado
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        try {
            bd.execSQL(this.CRIAR_TABELA_CATEGORIA);
            bd.execSQL(this.CRIAR_TABELA_DESPESA);
            bd.execSQL(this.CRIAR_TABELA_RECEITA);
    //        bd.execSQL(this.CRIAR_TABELA_SALDO);

            for (String despesa : this.CATEGORIAS_DESPESA_PADRAO) {
                INSERIR_CATEGORIAS_PADRAO = "INSERT INTO " + NOME_TABELA_CATEGORIA + " (\n" +
                        COLUNA_DESCRICAO + "," + COLUNA_TIPO + "," + COLUNA_VISIVEL + ") VALUES('"+despesa+"',0, 1)";
            //    Log.e("onCreateDEBUG",despesa.toString());
                bd.execSQL(INSERIR_CATEGORIAS_PADRAO);
            }
            for (String receita : this.CATEGORIAS_RECEITA_PADRAO) {
                INSERIR_CATEGORIAS_PADRAO = "INSERT INTO " + NOME_TABELA_CATEGORIA + " (\n" +
                        COLUNA_DESCRICAO + "," + COLUNA_TIPO + "," + COLUNA_VISIVEL + ") VALUES('"+receita+"',1, 1)";
                bd.execSQL(INSERIR_CATEGORIAS_PADRAO);
            }
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
     * @param i  -
     * @param i1 -
     */
    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        for (int a = 0; a < NOME_TABELAS.length; a++) {
            bd.execSQL("DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
            Log.w("ATUALIZA DB", "DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
        }
        this.onCreate(bd);
    }

    public void clearDB() {
        SQLiteDatabase bd = this.getReadableDatabase();
        for (int a = 0; a < NOME_TABELAS.length; a++) {
            Log.w("NOMES", NOME_TABELAS[a]);
            bd.execSQL("DROP TABLE IF EXISTS " + NOME_TABELAS[a]);
        }
        this.onCreate(bd);
    }

    public boolean tabelaExiste(String tabela) {
        SQLiteDatabase bd = this.getReadableDatabase();
        boolean tabelaExiste = false;
        try {
            @SuppressWarnings("unused")
            Cursor cursor = bd.query(tabela, null, null, null, null, null, null);
            tabelaExiste = true;
        } catch (SQLiteException e) {
            if (e.getMessage().toString().contains("no such table")) {
                tabelaExiste = false;
            }
        }
        return tabelaExiste;
    }

    public void criaTabela(String tabela) {
        SQLiteDatabase bd = this.getReadableDatabase();
        try {
            if (tabela.equalsIgnoreCase("receita"))
                bd.execSQL(this.CRIAR_TABELA_RECEITA);
            if (tabela.equalsIgnoreCase("despesa"))
                bd.execSQL(this.CRIAR_TABELA_DESPESA);
            if (tabela.equalsIgnoreCase("categoria"))
                bd.execSQL(this.CRIAR_TABELA_CATEGORIA);
     /*       if (tabela.equalsIgnoreCase("saldo"))
                bd.execSQL(this.CRIAR_TABELA_SALDO);
*/
        } catch (SQLiteException e) {
            Log.e("ERRO", e.getMessage());
            return;
        }
        Log.i("criaTabela", "Tabela " + tabela + " Criada com Sucesso");
    }
}
