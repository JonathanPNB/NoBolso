package unifimes.tcc.nobolso.entity;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe da entidade transação
 */
public class Transacao {
    private int id, tipo;
    private double valor;
    private String categoria, data, descricao;

    public Transacao() {
        super();
    }
    public Transacao(String desc, int tipo, String categoria, double valor) {
        super();
        this.tipo = tipo;
        this.valor = valor;
        this.categoria = categoria;
        this.descricao = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getId() + " - " + getTipo() + " - " + getDescricao() + " - " + getCategoria()
                + " - " + getData() + " - " + getValor();
    }
}
