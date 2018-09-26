package bhanupro.hackernews.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import bhanupro.hackernews.MainActivity;
import bhanupro.hackernews.Model.CommentModel;
import bhanupro.hackernews.Model.NewsItem;
import bhanupro.hackernews.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context ctx;
    protected ArrayList<CommentModel> list = new ArrayList<>();

    public CommentAdapter(@NonNull Context context, ArrayList<CommentModel> list ) {
        ctx = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.comment_row,parent,false);

        return new CommentAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        final CommentModel item = list.get(position);
        if (item != null){
            if (item.getBy() != null) holder.userTv.setText(item.getBy());
            if (item.getComment() != null) holder.commentTv.setText(item.getComment());
            if (item.getTime() != null) holder.timeTv.setText(item.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_user_tv) TextView userTv;
        @BindView(R.id.comment_date_tv) TextView dateTv;
        @BindView(R.id.comment_time_tv) TextView timeTv;
        @BindView(R.id.comment_tv) TextView commentTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
