package bhanupro.hackernews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bhanupro.hackernews.Adapters.NewsAdapter;
import bhanupro.hackernews.Helpers.Constants;
import bhanupro.hackernews.Model.NewsItem;
import bhanupro.hackernews.Views.DetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements NewsAdapter.ClickListener {

    @BindView(R.id.news_rv) RecyclerView rv;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    ArrayList<String> topStoriesList = new ArrayList<>();
    ArrayList<NewsItem> StoriesList = new ArrayList<>();

    public Realm realm;
    NewsAdapter adapter;
    DownloadJson downloadAsyc;
    RealmResults<NewsItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Stories");
        toolbar.setSubtitle("Updated 0 minutes ago");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

        realm = Realm.getDefaultInstance();
        rv.setLayoutManager( new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        items = realm.where(NewsItem.class).findAll();
        if (items.size() == 0){
            fetchTopStories();
            items = realm.where(NewsItem.class).findAll();
        }
        progressBar.setVisibility(View.GONE);
        fetchTopStories();
        adapter = new NewsAdapter(MainActivity.this,items,false);
        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        if (downloadAsyc != null) downloadAsyc.cancel(true);
        super.onStop();

    }

    private void fetchTopStories(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONArray array = new JSONArray(response);
                                if (array != null){
                                    topStoriesList.clear();
                                    for (int i=0;i<array.length();i++){
                                        topStoriesList.add(array.getString(i));
                                        //fetchNewsItem(array.getString(i));
                                    }
                                    downloadAsyc= new DownloadJson();
                                    downloadAsyc.execute();
                                }
                                progressBar.setVisibility(View.GONE);
                                Log.e("list","top stories:: "+topStoriesList.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(request);
    }

    //https://hacker-news.firebaseio.com/v0/item/8863.json?print=pretty/
    private void fetchNewsItem(final String id){

    }

    @Override
    public void onItemClick(NewsItem item,String total) {
        Log.e("onClick","on item clicked");
        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        String url = "";
        if (item.getUrl() != null){
            url = item.getUrl();
        }
        i.putExtra("URL",url);
        i.putExtra("COMMENT",item.getTotalComment());
        i.putExtra("TOTAL",total);
        i.putExtra("TITLE",item.getTitle());
        i.putExtra("DATE",item.getTime());

        startActivity(i);
    }

    private class DownloadJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  Void doInBackground(Void... params) {
            for (int j=0;j<topStoriesList.size();j++){
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                final String id = topStoriesList.get(j);

                String url = "https://hacker-news.firebaseio.com/v0/item/"+id+".json?print=pretty/";

                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject object = new JSONObject(response);
                                    //Log.e("object","json object is:: "+object.toString());
                                    //realm.beginTransaction();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            NewsItem newsItem = realm.where(NewsItem.class)
                                                    .equalTo("id",id)
                                                    .findFirst();

                                            if (newsItem == null){
                                                final NewsItem item = realm.createObject(NewsItem.class,id);
                                                try {
                                                    item.setBy(object.getString("by"));
                                                    item.setTitle(object.getString("title"));
                                                    item.setUrl(object.getString("url"));
                                                    item.setTime(object.getString("time"));
                                                    item.setUpvote(object.getString("descendants"));
                                                    JSONArray array = object.getJSONArray("kids");
                                                    if (array != null){
                                                        item.setTotalComment(array.toString());
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {

                                                try {
                                                    newsItem.setBy(object.getString("by"));
                                                    newsItem.setTitle(object.getString("title"));
                                                    newsItem.setUrl(object.getString("url"));
                                                    newsItem.setTime(object.getString("time"));
                                                    newsItem.setUpvote(object.getString("descendants"));
                                                    JSONArray array = object.getJSONArray("kids");
                                                    if (array != null){
                                                        newsItem.setTotalComment(array.toString());
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(request);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            items = realm.where(NewsItem.class).findAll();
            adapter.notifyDataSetChanged();
        }

    }
}
