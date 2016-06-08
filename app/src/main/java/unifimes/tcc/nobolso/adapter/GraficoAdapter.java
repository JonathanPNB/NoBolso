package unifimes.tcc.nobolso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.utilidade.Utilidade;

/**
 * Created by Jonathan on 07/06/2016.
 */
public class GraficoAdapter extends BaseAdapter {

    private Context ctx;
    private int[] listaCores;
    private ArrayList<String> listaCategorias;
    private ArrayList<BigDecimal> listaValores;
    private BigDecimal somaValores;

    public GraficoAdapter(Context ctx, int[] listaCores, ArrayList<String> listaCategorias, ArrayList<BigDecimal> listaValores) {
        this.ctx = ctx;
        this.listaCores = listaCores;
        this.listaCategorias = listaCategorias;
        this.listaValores = listaValores;
    }

    @Override
    public int getCount() {
        return listaCategorias.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCategorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View layout;
        somaValores = BigDecimal.ZERO;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.linha_detalhes_relatorio, null);
        } else {
            layout = view;
        }

        for (int i = 0; i< listaValores.size(); i++) {
            somaValores = somaValores.add(listaValores.get(i));
        }

        View cor = layout.findViewById(R.id.corIdent);
        TextView categoria = (TextView) layout.findViewById(R.id.textView_catGrafico);
        TextView valor = (TextView) layout.findViewById(R.id.textView_valorGrafico);
        TextView porcentagem = (TextView) layout.findViewById(R.id.textView_porcentGrafico);

        categoria.setText(this.listaCategorias.get(position));
        valor.setText(String.valueOf("R$ " + Utilidade.formataMoeda(this.listaValores.get(position))));
        porcentagem.setText(String.valueOf(Utilidade.calculaPorcentagem(listaValores.get(position), somaValores)+"%"));
        cor.setBackgroundColor(this.listaCores[position]);

        return layout;
    }
}
