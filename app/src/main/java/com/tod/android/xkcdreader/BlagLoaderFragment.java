package com.tod.android.xkcdreader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
public class BlagLoaderFragment extends Fragment {
    private ListView loadlist;
    private Document whatifdoc;
    private Async task;
    private Elements months;
    Elements links;
    private ProgressBar progress;
    private ArrayList<String> d = new ArrayList<String>();
    private ArrayList<String> e = new ArrayList<String>();
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
                whatifdoc = Jsoup.connect("http://blog.xkcd.com/")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.19 Safari/537.36")
                        .get();
                //final Elements paragraphs = doc.select("p").prepend("\\n\\n");
                months = whatifdoc.select("aside#archives>ul>li>a");
                for (Element month : months) {
                    d.add(month.text());
                }
                for (Element month : months) {
                    e.add(month.absUrl("href"));
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
                    BlagTitlesLoader dFragment = new BlagTitlesLoader();
					// Creating a Bundle object
					Bundle data = new Bundle();

					// Setting the index of the currently selected item of mDrawerList
					data.putString("LINK", e.get(position));
                    data.putString("TITLE", d.get(position));
					// Setting the position to the fragment
					dFragment.setArguments(data);

                    // Getting reference to the FragmentManager
                    FragmentManager fragmentManager = getFragmentManager();
                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, dFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                    ft.addToBackStack("blag");
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
