package bhanupro.hackernews.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CommentModel extends RealmObject {
    String time,by,comment;
    @PrimaryKey
    String id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
