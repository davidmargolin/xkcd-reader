package com.tod.android.xkcdreader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
 * Created by Margolin on 7/22/2014.
 */
public class ComicListFragment extends Fragment {
     ListView loadlist;
     Document whatifdoc;
     Async task;
     Elements articles;
     ProgressBar progress;
     Integer maxnum;
     ArrayList<String> d = new ArrayList<String>();
     ArrayList<String> e = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.listviewlayout, container, false);
        loadlist = (ListView)v.findViewById(R.id.listView);
        progress = (ProgressBar)v.findViewById(R.id.progressBar);
        NavActivity.setlistfrag(true);
        Bundle bundle = getArguments();
        try {
            maxnum = bundle.getInt("maxnum");
        }catch (Exception e){
            Log.d("maxnum error", "no maxnum registered");
        }
        getActivity().invalidateOptionsMenu();
        task = new Async();
        task.execute();

        return v;
    }

    class Async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                whatifdoc = Jsoup.connect("https://xkcd.com/archive/")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.19 Safari/537.36")
                        .get();
                //final Elements paragraphs = doc.select("p").prepend("\\n\\n");
                articles = whatifdoc.select("div#middleContainer>a");
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
                    ReaderFragment iFragment = new ReaderFragment();
                    // Getting reference to the FragmentManager

                    Bundle data = new Bundle();

                    // Setting the index of the currently selected item of mDrawerList
                    data.putString("LINK", e.get(position));
                    data.putString("TITLE", d.get(position));
                    if (maxnum != null){
                        data.putInt("listmaxnum", maxnum);}
                    // Setting the position to the fragment
                    iFragment.setArguments(data);

                    // Getting reference to the FragmentManager
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, iFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // Committing the transaction
                    ft.commit();
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
