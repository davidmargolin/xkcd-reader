package com.tod.android.xkcdreader;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Margolin on 7/2/2014.
 */
public class ReaderFragment extends Fragment {
    private String imagelink;
    private String hypertext;
    private String comictitle;
    private Document subdepdoc;
    private Button previousbutton;
    private Button lastbutton;
    private Button firstbutton;
    private Boolean safetoshare = false;
    private EditText selfselect;
    private Button nextbutton;
    private String website;
    private ProgressBar progress;
    private TextView titletext;
    private Integer num = null;
    private Integer maxnum;
    private ColorDrawable back;
    private Boolean lastattempt = true;
    private Integer nextnum = 0;
    private Integer prevnum = 0;
    private Boolean failed = false;
    private int color;
    private Element comiclink;
    private Element hyperlink;
    private Async task;
    private TouchImageView comic;
private String link;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reader_fragment_layout, container, false);
        setHasOptionsMenu(true);
        website="http://xkcd.com";
        lastattempt = true;
        final Intent linkintent = getActivity().getIntent();
        final String action = linkintent.getAction();
        try {
            Bundle bundle = getArguments();
            website=bundle.getString("LINK");
            Integer testmaxnum = bundle.getInt("listmaxnum");
            if (testmaxnum != 0){
                    maxnum = testmaxnum;
            }
            if (website == "http://xkcd.com"){
                lastattempt = true;
            }else{
                lastattempt = false;
            }
        }catch (NullPointerException e){
            website="http://xkcd.com";
            lastattempt = true;
        }
        if (Intent.ACTION_VIEW.equals(action)) {
            if (!getActivity().getIntent().getAction().contains("failure.com")) {
                link = linkintent.getDataString();
                link = link.replace("m.", "");
                website = link;
                Toast.makeText(getActivity().getBaseContext(), link,
                        Toast.LENGTH_SHORT).show();
                lastattempt = false;
                getActivity().getIntent().setAction("failure.com");
            }
        }
        NavActivity.setlistfrag(false);
        getActivity().invalidateOptionsMenu();
        comic = (TouchImageView) v.findViewById(R.id.imageView2);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.imageback, typedValue, true);
        color = typedValue.data;
        back = new ColorDrawable(color);
        selfselect = (EditText) v.findViewById(R.id.editText);
        nextbutton = (Button) v.findViewById(R.id.nextbutton);
        titletext = (TextView) v.findViewById(R.id.textView);
        lastbutton = (Button) v.findViewById(R.id.lastbutton);
        firstbutton = (Button) v.findViewById(R.id.firstbutton);
        previousbutton = (Button) v.findViewById(R.id.previousbutton);
        progress = (ProgressBar) v.findViewById(R.id.progressBar);
        selfselect.setCursorVisible(false);
        selfselect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selfselect.setCursorVisible(true);
                return false;
            }
        });
        selfselect.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (maxnum != null) {
                        if (Integer.parseInt((selfselect.getText().toString())) > maxnum) {
                            selfselect.setText(maxnum.toString());
                        }
                    }
                    if (Integer.parseInt(selfselect.getText().toString()) == 0) {
                        selfselect.setText("1");
                    }
                    website = "http://xkcd.com/" + selfselect.getText();
                    selfselect.setCursorVisible(false);
                    task = new Async();
                    task.execute();

                    return true;
                }   if (keyCode == KeyEvent.KEYCODE_BACK) {
                    selfselect.clearFocus();
                    selfselect.setCursorVisible(false);

                    return false;
                }
                return false;
            }
        });

        lastbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                task.cancel(true);
                if (maxnum != null) {
                    website = "http://xkcd.com/";
                    num = maxnum;
                    selfselect.setText(num.toString());
                } else {
                    website = "http://xkcd.com/";
                    lastattempt = true;
                }
                selfselect.setCursorVisible(false);
                task = new Async();
                task.execute();
            }
        });
        firstbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                task.cancel(true);
                website = "http://xkcd.com/1";
                task = new Async();
                num = 1;
                selfselect.setCursorVisible(false);
                selfselect.setText(num.toString());
                task.execute();
            }
        });

        previousbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                task.cancel(true);
                if (num != null) {
                    prevnum = num - 1;
                    if (prevnum == 0) {
                        prevnum = 1;
                    }
                    num = prevnum;
                    website = "http://xkcd.com/" + num.toString();
                    task = new Async();
                    selfselect.setText(num.toString());
                    task.execute();
                } else {
                    website = "http://xkcd.com/";
                    lastattempt = true;
                    task = new Async();
                    task.execute();
                }
                selfselect.setCursorVisible(false);
            }
        });
        nextbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                task.cancel(true);
                if (num != null) {
                    nextnum = num + 1;
                    if (maxnum != null) {
                        if (nextnum > maxnum) {
                            nextnum = maxnum;
                        }
                    }
                    num = nextnum;
                    website = "http://xkcd.com/" + num.toString();
                    task = new Async();
                    selfselect.setText(num.toString());
                    task.execute();
                } else {
                    website = "http://xkcd.com/";
                    lastattempt = true;
                    task = new Async();
                    task.execute();
                }
                selfselect.setCursorVisible(false);
            }
        });
        task = new Async();
        task.execute();


        return v;
    }

    public class Async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
/*                Connection.Response response = Jsoup
                        .connect(website)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.19 Safari/537.36")
                        .timeout(10000)
                        .execute()
                        ;
                int statusCode = response.statusCode();
                if(statusCode == 200) {*/
                subdepdoc = Jsoup
//                        .parse(downloadHtml(website));
                        .connect(website)
                        .timeout(10000)
                        .get();
                //final Elements paragraphs = doc.select("p").prepend("\\n\\n");
                comiclink = subdepdoc.select("div:contains(image url)").first();
                String string = comiclink.text();
                String[] parts = string.split("/ Image URL \\(for hotlinking/embedding\\):");
                String[] newparts = parts[0].split("\\|< < Prev Random Next > >\\| \\|< < Prev Random Next > >\\| Permanent link to this comic: http://xkcd\\.com/");
                num = Integer.parseInt(newparts[1]);
                comictitle = subdepdoc.select("title").first().text().replace("xkcd: ", "");
                hyperlink = subdepdoc.select("img[^title]").first();
                hypertext = hyperlink.attr("title");

                if (parts[1].contains("png")) {
                    imagelink = parts[1].split("png")[0] + "png"; // 034556
                } else if (parts[1].contains("jpg")) {
                    imagelink = parts[1].split("jpg")[0] + "jpg";
                } else if (parts[1].contains("gif")) {
                    imagelink = parts[1].split("gif")[0] + "gif";
                }
                if (lastattempt) {
                    maxnum = num;
                }
            }catch (IOException e) {
                Log.e("something went wrong", e.toString());
                failed = true;
            }
            catch (NullPointerException e) {
                Log.e("something went wrong", e.toString());
                failed = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            failed = false;
            progress.setVisibility(View.VISIBLE);
            comic.setImageDrawable(back);
            safetoshare = false;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            comic.resetZoom();
            if (!failed) {
                try {
                    Picasso.with(getActivity().getApplicationContext())
                            .load(imagelink)
                            .error(R.drawable.error)
                            .into(comic);
                    selfselect.setText(num.toString());
                        titletext.setText(comictitle);
                    comic.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity());
                            TextView myMsg = new TextView(getActivity());
                            myMsg.setText(hypertext);
                            myMsg.setTextSize(18);
                            myMsg.setBackgroundColor(color);
                            if (color==getResources().getColor(R.color.White)) {
                                myMsg.setTextColor(getResources().getColor(R.color.halfverygray));
                            }else{
                                myMsg.setTextColor(getResources().getColor(R.color.White));
                            }
                            myMsg.setPadding(16, 16, 16, 16);
                            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                            popupBuilder.setView(myMsg);
                            popupBuilder.create().show();
                            return true;
                        }
                    });
                    safetoshare = true;
                } catch (NullPointerException e) {
                    Log.e("network failed", "no network access");
                    failed = true;

                }
            }
            if (failed) {
                titletext.setText("Error");
                Log.e("failed", "true");
                comic.resetZoom();
                Picasso.with(getActivity().getBaseContext())
                        .load(R.drawable.error)
                        .into(comic);
                comic.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

                safetoshare = false;
            }
            progress.setVisibility(View.GONE);
            lastattempt = false;
            selfselect.setCursorVisible(false);
        }
    }
    @Override
        public void onStop() {
        task.cancel(true);
        super.onStop();
    }
    @Override
    public void onPause() {
        task.cancel(true);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.random) {
            task.cancel(true);
            website = "http://c.xkcd.com/random/comic";
            task = new Async();
            task.execute();
            return true;
        }
        if (item.getItemId() == R.id.refresh) {
            task.cancel(true);
            task = new Async();
            task.execute();
            return true;
        }
        if (item.getItemId() == R.id.website) {
            task.cancel(true);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.xkcd.com/" + num.toString()));
            startActivity(browserIntent);
            return true;
        }
        if (item.getItemId() == R.id.explain) {
            task.cancel(true);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.explainxkcd.com/wiki/index.php/" + num.toString()));
            startActivity(browserIntent);
            return true;
        }
        if (item.getItemId() == R.id.share) {
            if (safetoshare) {
                try {
/*            Drawable mDrawable = comic.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    mBitmap, "Image Description", null);

            Uri uri = Uri.parse(path);*/
                    Uri bmpUri = getLocalBitmapUri(comic);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    intent.putExtra(Intent.EXTRA_TEXT, hypertext + "\n\n" + "http://xkcd.com/" + num);
                    intent.putExtra(Intent.EXTRA_SUBJECT, comictitle);
                    intent.putExtra(Intent.EXTRA_TITLE, comictitle);
                    Intent chooser = Intent.createChooser(intent, "Share");
                    startActivity(chooser);
                } catch (Exception e) {
                    Log.e("share failed", e.toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Can't Share",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Can't Share",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.list) {
            task.cancel(true);
            ComicListFragment hFragment = new ComicListFragment();
            // Getting reference to the FragmentManager
            FragmentManager fragmentManager = getFragmentManager();
            // Creating a fragment transaction
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (maxnum > 0) {
                    Bundle data = new Bundle();

                    // Setting the index of the currently selected item of mDrawerList

                    data.putInt("maxnum", maxnum);
                    // Setting the position to the fragment
                    hFragment.setArguments(data);
            }
            // Adding a fragment to the fragment transaction
            ft.replace(R.id.container, hFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("reader");
            // Committing the transaction
            ft.commit();


        }


        return super.onOptionsItemSelected(item);
    }




    // Returns the URI path to the Bitmap displayed in specified ImageView
    private String downloadHtml(String path) {
        InputStream is = null;
        try {
            String result = "";
            String line;

            URL url = new URL(path);
            is = url.openStream();  // throws an IOException
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            Log.e("Jsoup failed", e.toString());
            failed = true;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                Log.e("Jsoup failed", e.toString());
                failed = true;
            }
        }
        return "";
    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
