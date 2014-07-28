package com.tod.android.xkcdreader;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Margolin on 12/23/13.
 */
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList titles;
    private final ArrayList links;
    private final ArrayList images;
    private final ArrayList dates;
        public MySimpleArrayAdapter(Context context,  ArrayList titles, ArrayList images,ArrayList links,ArrayList dates) {
            super(context, R.layout.whatifcardview, titles);
            this.context = context;
            this.images = images;
            this.dates = dates;
            this.links = links;
            this.titles = titles;
        }
    static class ViewHolder {
        TextView title;
        LinearLayout button;
        ImageView image;
        TextView date;
    }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewHolder holder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.whatifcardview, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.titletext);
                holder.image = (ImageView) convertView.findViewById(R.id.thumbnailview);
                holder.button = (LinearLayout) convertView.findViewById(R.id.mainview);
                holder.date= (TextView) convertView.findViewById(R.id.datetext);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
//            TextView textView = (TextView) rowView.findViewById(R.id.name);
            holder.title.setText(titles.get(position).toString());
            holder.date.setText(dates.get(position).toString());
            Picasso.with(context)
                    .load(images.get(position).toString())
                    .into(holder.image);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), WebViewActivity.class);
                    intent.putExtra("LINK", links.get(position).toString());
                    intent.putExtra("TITLE", titles.get(position).toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            return convertView;
}}
