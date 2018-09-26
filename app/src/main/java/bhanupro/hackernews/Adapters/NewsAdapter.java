package bhanupro.hackernews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import bhanupro.hackernews.Helpers.Constants;
import bhanupro.hackernews.MainActivity;
import bhanupro.hackernews.Model.NewsItem;
import bhanupro.hackernews.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class NewsAdapter extends RealmRecyclerViewAdapter<NewsItem,NewsAdapter.NewsViewHolder> {

    private Context ctx;
    protected OrderedRealmCollection<NewsItem> adapterData;
    ClickListener mClickListener;
    String total = "0";

    public NewsAdapter(@NonNull MainActivity context, @Nullable OrderedRealmCollection<NewsItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
        ctx = context;
        adapterData = data;
        mClickListener = context;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.stories_row_item,parent,false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        final NewsItem item = getData().get(position);
        total = "0";
        if (item != null){
            holder.title.setText(item.getTitle());
            holder.byUser.setText(item.getBy());
            if (item.getUpvote() == null){
                holder.upvotes.setText("0");
            }else if (item.getUpvote().equalsIgnoreCase("")){
                holder.upvotes.setText("0");
            }else {
                holder.upvotes.setText(item.getUpvote());
            }
            if (item.getUrl() != null) holder.url.setText(item.getUrl());

            if (item.getTotalComment() != null){
                try {
                    JSONArray array = new JSONArray(item.getTotalComment());
                    if (array != null) total = String.valueOf(array.length());
                   // holder.comments.setText(array.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //holder.comments.setText(item.getTotalComment().length());
            if (item.getTime()!= null){
                //Long time = Long.valueOf(item.getTime());
                try {
                    holder.time.setText(Constants.convertDate(item.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null){
                    mClickListener.onItemClick(item,total);
                }
            }
        });

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title_tv) TextView title;
        @BindView(R.id.item_up_votes_tv) TextView upvotes;
        @BindView(R.id.item_url_tv) TextView url;
        @BindView(R.id.item_posted_time_tv) TextView time;
        @BindView(R.id.item_posted_user_tv) TextView byUser;
        @BindView(R.id.item_total_comments_tv) TextView comments;
        @BindView(R.id.row_layout) RelativeLayout rowLayout;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface ClickListener{
        void onItemClick(NewsItem item,String total);
    }
}
