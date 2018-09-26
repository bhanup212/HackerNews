package bhanupro.hackernews.Views;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bhanupro.hackernews.Adapters.CommentAdapter;
import bhanupro.hackernews.Adapters.NewsAdapter;
import bhanupro.hackernews.Model.CommentModel;
import bhanupro.hackernews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    //https://hacker-news.firebaseio.com/v0/item/2921983.json?print=pretty
    @BindView(R.id.comment_rv) RecyclerView commentRv;
    ArrayList<String> mList = new ArrayList<>();
    ArrayList<CommentModel> comments = new ArrayList<>();
    CommentAdapter mAdapter;
    DownloadJson downloadAysc;

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String commentList = getArguments().getString("COMMENT");
        mList.clear();
        downloadAysc = new DownloadJson();
        if (commentList != null){
            try {
                JSONArray commentArray = new JSONArray(commentList);
                if (commentArray != null){
                    for (int i = 0;i< commentArray.length();i++){
                        mList.add(commentArray.getString(i));
                    }
                    downloadAysc.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        commentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommentAdapter(getContext(),comments);
        commentRv.setAdapter(mAdapter);

        //fetchComments("2921983");
    }

    @Override
    public void onStop() {
        if (downloadAysc != null) downloadAysc.cancel(true);
        super.onStop();
    }

    private void fetchComments(String id) {
        String url = "https://hacker-news.firebaseio.com/v0/item/"+id+".json?print=pretty/";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                CommentModel m = new CommentModel();
                                JSONObject object = new JSONObject(response);
                                String time = object.getString("time");
                                String user = object.getString("by");
                                String comment = object.getString("text");

                                Log.e("comment data","comment data:: "+user+" "+comment+" "+time);
                                m.setBy(user);
                                m.setComment(comment);
                                m.setTime(time);
                                comments.add(m);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Server error",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);

    }
    private class DownloadJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  Void doInBackground(Void... params) {
            for (int j=0;j<mList.size();j++){
                String url = "https://hacker-news.firebaseio.com/v0/item/"+mList.get(j)+".json?print=pretty/";
                Log.e("comment url","comment url: "+url);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    try {
                                        CommentModel m = new CommentModel();
                                        JSONObject object = new JSONObject(response);
                                        String time = object.getString("time");
                                        String user = object.getString("by");
                                        String comment = object.getString("text");

                                        Log.e("comment data","comment data:: "+user+" "+comment+" "+time);

                                        m.setTime(time); m.setComment(comment); m.setBy(user);
                                        comments.add(m);
                                        mAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Server error",Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(request);
            }
            return null;
    }

    @Override
    protected void onPostExecute(Void args) {

    }
}
}
