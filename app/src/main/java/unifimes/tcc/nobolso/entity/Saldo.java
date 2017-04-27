package unifimes.tcc.nobolso.entity;

import java.math.BigDecimal;

/**
 * Created by Jonathan on 24/04/2017.
 */

public class Saldo {

    private int id, mes, ano;
    private BigDecimal valorSaldo;

    public Saldo() {
    }

    public Saldo(int id, int mes, int ano, BigDecimal valorSaldo) {
        super();
        this.id = id;
        this.mes = mes;
        this.ano = ano;
        this.valorSaldo = valorSaldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public BigDecimal getValorSaldo() {
        return valorSaldo;
    }

    public void setValorSaldo(BigDecimal valorSaldo) {
        this.valorSaldo = valorSaldo;
    }

    @Override
    public String toString() {
        return "Saldo: "+getId() + " - " + getMes() + "/" + getAno() + " - R$" + getValorSaldo() +"\n";
    }
}
