package task.application.com.colette.model.local.realm.datamodels;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class FavoriteMedia extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private RealmList<MediaItem> mediaList;

    public FavoriteMedia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<MediaItem> getRealmList() {
        return mediaList;
    }

    public void setRealmList(RealmList<MediaItem> mediaList) {
        this.mediaList = mediaList;
    }
}

