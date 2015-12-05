package unifimes.tcc.nobolso.entity;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe da entidade categoria
 */
public class Categoria {

    private int id, tipo;
    private String descricao;

    public int getId() {
        return id;
    }


    public Categoria(String desc, int tipo) {
        super();
        this.descricao = desc;
        this.tipo = tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getId() + " " +  getDescricao() + " " + getTipo();
    }
}
