package uet.com.haku.losslessdownloader.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uet.com.haku.losslessdownloader.Entity.Mp3Profile;
import uet.com.haku.losslessdownloader.Entity.Mp3Quality;
import uet.com.haku.losslessdownloader.R;

/**
 * Created by phamn on 9/21/2017.
 */

public class RclViewAdapter extends RecyclerView.Adapter<RclViewAdapter.HeaderViewHolder> {
    private ArrayList<Mp3Profile> data;
    private Context context;

    public RclViewAdapter(ArrayList<Mp3Profile> data, Context context) {
        this.data = data;
        this.context = context;

    }

    @SuppressLint("InflateParams")
    @Override
    public HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerItem = null;
        if (inflater != null) {
            headerItem = inflater.inflate(R.layout.item,null);
        }
        return new HeaderViewHolder(headerItem);
    }

    @Override
    public void onBindViewHolder(final HeaderViewHolder holder, final int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.singer.setText(data.get(position).getSinger());
        holder.time.setText(data.get(position).getTime());
        holder.quality.setText(data.get(position).getQuality());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Mp3Quality> mp3Qualities = data.get(position).getDownloadLink();
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialog_view = inflater.inflate(R.layout.list_download_dialog,null);
                ListView listView = dialog_view.findViewById(R.id.listview);
                ListViewAdapter listViewAdapter = new ListViewAdapter(mp3Qualities,holder.itemView.getContext());
                listView.setAdapter(listViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mp3Qualities.get(i).getUrl()));
                       holder.itemView.getContext().startActivity(intent);
                    }
                });

                builder.setView(dialog_view);
                builder.create();
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title, singer, time, quality;

         HeaderViewHolder(View itemView) {
            super(itemView);
            title  = itemView.findViewById(R.id.title);
            singer  = itemView.findViewById(R.id.singer);
            time  = itemView.findViewById(R.id.time);
            quality  = itemView.findViewById(R.id.quality);

        }
    }
}
