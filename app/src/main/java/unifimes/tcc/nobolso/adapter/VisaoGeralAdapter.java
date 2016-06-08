package unifimes.tcc.nobolso.adapter;

/**
 * Created by Jonathan on 09/07/2015.
 * Classe do imageadapter, para colocar a "setinha" junto com texto
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.utilidade.Utilidade;

public class VisaoGeralAdapter extends BaseAdapter {
    private Context ctx;
    private String[] lista;
    private BigDecimal[] valores;

    public VisaoGeralAdapter(Context ctx, String[] lista, BigDecimal[] valores) {
        this.ctx = ctx;
        this.lista = lista;
        this.valores = valores;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int position) {
        return lista[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View layout;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.linha_visao_geral, null);
        } else {
            layout = view;
        }

        TextView tipo = (TextView) layout.findViewById(R.id.textView_catGrafico);
    //    View separador1 = layout.findViewById(R.id.separator4);
        tipo.setText(this.lista[position]);

        TextView valorAtual = (TextView) layout.findViewById(R.id.textView_valorGrafico);
        switch (position) {
            case 0:
                valorAtual.setText(String.valueOf("R$ " + Utilidade.formataMoeda(this.valores[position])));
                if(this.valores[position].compareTo(BigDecimal.ZERO) < 0) {//se this.valores[position] for < que BigDecimal.ZERO
                    valorAtual.setTextColor(Color.RED);
                }
                break;
            case 1:
                valorAtual.setText(String.valueOf("R$ " + Utilidade.formataMoeda(this.valores[position])));
                break;
            case 2:
                valorAtual.setText(String.valueOf("R$ "+ Utilidade.formataMoeda(this.valores[position])));
                valorAtual.setTextColor(Color.RED);
                break;
            case 3:
                if(this.valores[position].compareTo(BigDecimal.ZERO) >= 0) {
                    valorAtual.setText(String.valueOf("R$ "+ Utilidade.formataMoeda(this.valores[position])));
                    valorAtual.setTextColor(Color.GREEN);
                } else {
                    valorAtual.setText(String.valueOf("R$ "+ Utilidade.formataMoeda(this.valores[position])));
                    valorAtual.setTextColor(Color.RED);
                }
                default:
                    break;
        }

        return layout;
    }
}