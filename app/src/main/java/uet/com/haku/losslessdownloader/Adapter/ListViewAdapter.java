package uet.com.haku.losslessdownloader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uet.com.haku.losslessdownloader.Entity.Mp3Quality;
import uet.com.haku.losslessdownloader.R;

/**
 * Created by phamn on 9/23/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Mp3Quality> listUrlDownload;
    private LayoutInflater inflater;

    public ListViewAdapter(ArrayList<Mp3Quality> listUrlDownload, Context context) {
        this.listUrlDownload = listUrlDownload;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listUrlDownload.size();
    }

    @Override
    public Object getItem(int i) {
        return listUrlDownload.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Mp3Quality mp3Quality = listUrlDownload.get(i);
        TextView textView;
        if (view == null) {
            view = inflater.inflate(R.layout.listview_item,null);
        }
        textView  = view.findViewById(R.id.txt_quality_download);
        textView.setText(mp3Quality.getQuality());
        return view;
    }
}
