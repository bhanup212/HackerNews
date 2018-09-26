package bhanupro.hackernews.Model;

import java.util.ArrayList;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsItem extends RealmObject {
    @PrimaryKey
    String id;
    String by,totalComments,upvotes,time,title,url;
    String commentList;

    public NewsItem() {

    }

    public NewsItem(String id, String by, String totalComment, String upvote, String time, String title, String url, String commentList) {
        this.id = id;
        this.by = by;
        totalComments = totalComment;
        upvotes = upvote;
        this.time = time;
        this.title = title;
        this.url = url;
        this.commentList = commentList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getTotalComment() {
        return totalComments;
    }

    public void setTotalComment(String totalComment) {
        totalComments = totalComment;
    }

    public String getUpvote() {
        return upvotes;
    }

    public void setUpvote(String upvote) {
        upvotes = upvote;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommentList() {
        return commentList;
    }

    public void setCommentList(String commentList) {
        this.commentList = commentList;
    }
}
