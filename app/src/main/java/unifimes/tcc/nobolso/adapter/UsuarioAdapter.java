package unifimes.tcc.nobolso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import unifimes.tcc.nobolso.R;
import unifimes.tcc.nobolso.entity.Usuario;

/**
 * Created by Jonathan on 08/07/2015.
 * Classe usuarioadpater, apenas para testes
 */
public class UsuarioAdapter extends BaseAdapter {
    private Context context;
    private List<Usuario> list;

    public UsuarioAdapter(Context context, List<Usuario> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0).getId();
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.usuario, null);

        TextView tv = (TextView) layout.findViewById(R.id.nome);
        tv.setText(list.get(position).getNome());

        return layout;
    }

}
