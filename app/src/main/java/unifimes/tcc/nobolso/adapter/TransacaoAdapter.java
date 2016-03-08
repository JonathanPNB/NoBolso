package unifimes.tcc.nobolso.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.entity.Transacao;

/**
 * Created by Jonathan on 15/02/2016.
 */
public class TransacaoAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Transacao> lista;
    DecimalFormat formatoMoeda = new DecimalFormat("###,###,###.00");

    public TransacaoAdapter(Context context, ArrayList<Transacao> lista) {
        this.ctx = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return lista.get(position).getId();
    }

    public View getView(int position, View view, ViewGroup parent) {

        Transacao transacao = lista.get(position);
        View layout;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.linha_transacoes, null);
        } else {
            layout = view;
        }

        int idReceita  = R.id.listView;

        TextView descricao = (TextView) layout.findViewById(R.id.textView_desc);
        if(transacao.getDescricao().length() < 40)
            descricao.setText(transacao.getDescricao());
        else
            descricao.setText(transacao.getDescricao());

        TextView categoria = (TextView) layout.findViewById(R.id.textView_Categoria);
        categoria.setText(transacao.getCategoria());

        TextView valor = (TextView) layout.findViewById(R.id.textView_Valor);
        if(idReceita == parent.getId()) {
            valor.setText(String.valueOf("R$ +" + formatoMoeda.format(transacao.getValor())));
        }
        else {
            valor.setText(String.valueOf("R$ -" + formatoMoeda.format(transacao.getValor())));
            valor.setTextColor(Color.RED);
        }

        return layout;
    }
}