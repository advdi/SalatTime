package iak.advdi.salattime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakakkoii on 5/10/16.
 */
public class ListAdapter extends BaseAdapter {

    Context context;
    private List<SalatTime> salatList = null;
    private ArrayList<SalatTime> salatTimes = new ArrayList<>();
    LayoutInflater inflater;

    public ListAdapter(Context context, List<SalatTime> salatList) {
        this.context = context;
        this.salatList = salatList;
        inflater = LayoutInflater.from(context);
        this.salatTimes.addAll(salatList);
    }

    @Override
    public int getCount() {
        return salatList.size();
    }

    @Override
    public Object getItem(int i) {
        return salatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.list_item_salat, viewGroup, false);
        TextView salat_textview = (TextView) view.findViewById(R.id.list_item_salat_textview);
        salat_textview.setText(salatList.get(i).getDate_for());
        return view;
    }
}
