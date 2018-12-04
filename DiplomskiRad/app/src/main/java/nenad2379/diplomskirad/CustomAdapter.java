package nenad2379.diplomskirad;

/**
 * Created by Korsn on 11.11.2018..
 */

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> Item;
    LinkedList<String> SubItem;
    LinkedList<Integer> SubItem2;
    //int flags[];
    LayoutInflater inflter;
    TextView subitem2;

    public CustomAdapter(Context applicationContext, ArrayList<String> Item, LinkedList<String> SubItem, LinkedList<Integer> SubItem2 ) {
        this.context = applicationContext;
        this.Item = Item;
        this.SubItem = SubItem;
        this.SubItem2 = SubItem2;
        //this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Item.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        view = inflter.inflate(R.layout.activity_list_item, null);
        TextView item = (TextView) view.findViewById(R.id.item);
        TextView subitem = (TextView) view.findViewById(R.id.subitem);
        subitem2 = (TextView) view.findViewById(R.id.subitem2);
        //ImageView image = (ImageView) view.findViewById(R.id.image);
        item.setText("Naziv proizvoda: " + Item.get(i));
        subitem.setText("Upotrebljivo do: " + SubItem.get(i));
        subitem2.setText("Trenutno stanje: " + String.valueOf(SubItem2.get(i)));
        //image.setImageResource(flags[i]);


        return view;
    }


}
