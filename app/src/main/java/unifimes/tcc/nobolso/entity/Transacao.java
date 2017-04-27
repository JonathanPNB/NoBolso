package unifimes.tcc.nobolso.entity;

import java.math.BigDecimal;

/**
 * Created by Jonathan on 14/11/2015.
 * Classe da entidade transação
 */
public class Transacao {
    private int id, tipo;
    private BigDecimal valor;
    private String categoria, data, descricao;

    public Transacao() {
    }

    public Transacao(String desc, int tipo, String categoria, BigDecimal valor) {
        super();
        this.tipo = tipo;//0=despesa / 1=receita
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
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
        return "Transação: "+getId() + " - " + getTipo() + " - " + getDescricao() + " - " + getCategoria()
                + " - " + getData() + " - " + getValor() + "\n";
    }
}
