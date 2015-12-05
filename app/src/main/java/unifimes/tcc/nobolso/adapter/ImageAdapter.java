package unifimes.tcc.nobolso.adapter;

/**
 * Created by Jonathan on 09/07/2015.
 * Classe do imageadapter, para colocar a "setinha" junto com texto
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import unifimes.tcc.nobolso.R;

public class ImageAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] items;
    boolean[] arrows;
    int layoutId;
    int textId;
    int image1Id, image2Id;

    public ImageAdapter(Activity context, int layoutId, int textId, int image1Id, int image2Id, String[] items, boolean[] arrows) {
        super(context, layoutId, items);

        this.context = context;
        this.items = items;
        this.arrows = arrows;
        this.layoutId = layoutId;
        this.textId = textId;
        this.image1Id = image1Id;
        this.image2Id = image2Id;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(layoutId, null);
        TextView label = (TextView) row.findViewById(textId);

        label.setText(items[pos]);

        if (arrows[0]) {
     //       ImageView icon1 = (ImageView) row.findViewById(image1Id);
            ImageView icon2 = (ImageView) row.findViewById(image2Id);
            icon2.setImageResource(R.drawable.arrow);
   /*         if(pos == 0)
                icon1.setImageResource(R.drawable.barra_gastos);
            else
                icon1.setImageResource(R.drawable.abc_ic_menu_paste_mtrl_am_alpha);
     */   }

        return (row);
    }
}