package unifimes.tcc.nobolso.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

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
    private TreeMap<String, BigDecimal> mapValores;

    public GraficoAdapter(Context ctx, int[] listaCores, TreeMap<String, BigDecimal> mapValores) {
        this.ctx = ctx;
        this.listaCores = listaCores;
        this.mapValores = mapValores;
    }

    @Override
    public int getCount() {
        return mapValores.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e("getItem",mapValores.keySet().toString());
        return mapValores.get(mapValores.keySet().toString());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View layout;
        listaCategorias = new ArrayList<>();
        listaValores = new ArrayList<>();
        somaValores = BigDecimal.ZERO;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.linha_detalhes_relatorio, null);
        } else {
            layout = view;
        }

        View cor = layout.findViewById(R.id.corIdent);
        TextView categoria = (TextView) layout.findViewById(R.id.textView_catGrafico);
        TextView valor = (TextView) layout.findViewById(R.id.textView_valorGrafico);
        TextView porcentagem = (TextView) layout.findViewById(R.id.textView_porcentGrafico);

        for (String chave : mapValores.keySet()) {
            if (chave != null) {
                somaValores = somaValores.add(mapValores.get(chave));
                listaCategorias.add(chave);
                listaValores.add(mapValores.get(chave));
            }
        }
        categoria.setText(listaCategorias.get(position));
        valor.setText(String.valueOf("R$ " + Utilidade.formataMoeda(listaValores.get(position))));
        porcentagem.setText(String.valueOf(Utilidade.calculaPorcentagem(listaValores.get(position), somaValores) + "%"));
        cor.setBackgroundColor(this.listaCores[position]);
        Log.e("getView", "mapValores = " + listaCategorias.get(position) + " / " + listaValores.get(position));

        return layout;
    }
}
