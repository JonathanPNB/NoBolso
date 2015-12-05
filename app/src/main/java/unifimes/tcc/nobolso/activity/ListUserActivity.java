package unifimes.tcc.nobolso.activity;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.List;

import unifimes.tcc.nobolso.adapter.UsuarioAdapter;
import unifimes.tcc.nobolso.dao.UsuarioDao;
import unifimes.tcc.nobolso.entity.Usuario;

public class ListUserActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

  /*      UsuarioDao userBd = new UsuarioDao(this);

        List<Usuario> list = userBd.buscar();
        setListAdapter(new UsuarioAdapter(this, list));
    */}
}
