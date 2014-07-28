package com.tod.android.xkcdreader;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Margolin on 7/21/2014.
 */
public class WhatIfLoaderFragment extends Fragment {
    ListView loadlist;
    Document whatifdoc;
    Async task;
    Elements titles;
    ProgressBar progress;
    Elements imagelinks;
    Elements articlelinks;
    Elements articledates;
    ArrayList<String> d = new ArrayList<String>();
    ArrayList<String> e = new ArrayList<String>();
    ArrayList<String> f = new ArrayList<String>();
    ArrayList<String> g = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.listviewlayout, container, false);
        setHasOptionsMenu(true);
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
                whatifdoc = Jsoup.connect("https://what-if.xkcd.com/archive/")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.19 Safari/537.36")
                        .get();
                //final Elements paragraphs = doc.select("p").prepend("\\n\\n");
                titles = whatifdoc.select("h1");
                imagelinks = whatifdoc.select("img.archive-image");
                articlelinks = whatifdoc.select("div.archive-entry>a");
                articledates = whatifdoc.select("h2");
                for (Element title : titles) {
                    d.add(title.text());
                }
                for (Element image : imagelinks) {
                    e.add(image.absUrl("src"));
                }
                for (Element link : articlelinks) {
                    f.add(link.absUrl("href"));
                }
                for (Element date : articledates) {
                    g.add(date.text());
                }
                Collections.reverse(d);
                Collections.reverse(e);
                Collections.reverse(f);
                Collections.reverse(g);
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
            MySimpleArrayAdapter customAdapter = new MySimpleArrayAdapter(getActivity().getBaseContext() , d,e,f,g);
            loadlist.setAdapter(customAdapter);
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

