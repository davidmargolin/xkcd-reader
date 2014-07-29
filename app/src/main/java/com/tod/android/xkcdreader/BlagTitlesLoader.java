package com.tod.android.xkcdreader;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Margolin on 7/21/2014.
 */
public class BlagTitlesLoader extends Fragment{
    private ListView loadlist;
    private Document whatifdoc;
    private Async task;
    private Elements articles;
    private String url;
    private ProgressBar progress;
    private ArrayList<String> d = new ArrayList<String>();
    private ArrayList<String> e = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.listviewlayout, container, false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        getActivity().getActionBar().setTitle(bundle.getString("TITLE"));
        url = bundle.getString("LINK");
        loadlist = (ListView)v.findViewById(R.id.listView);
        progress = (ProgressBar)v.findViewById(R.id.progressBar);
        task = new Async();
        task.execute();
        return v;
    }

    private class Async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                whatifdoc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.19 Safari/537.36")
                        .get();
                //final Elements paragraphs = doc.select("p").prepend("\\n\\n");
                articles = whatifdoc.select("h1.entry-title>a");
                for (Element article : articles) {
                    d.add(article.text());
                }
                for (Element link : articles) {
                    e.add(link.absUrl("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            loadlist.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            loadlist.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, d));
            loadlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("LINK", e.get(position));
                    intent.putExtra("TITLE", d.get(position));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }

            });
            progress.setVisibility(View.GONE);
            loadlist.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStop() {
        task.cancel(true);
        super.onStop();
    }

}
