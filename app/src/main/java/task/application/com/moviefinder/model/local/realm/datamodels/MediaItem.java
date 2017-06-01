package task.application.com.moviefinder.model.local.realm.datamodels;

import com.androidtmdbwrapper.enums.MediaType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MediaItem extends RealmObject {

    @PrimaryKey
    private String tmdbId;
    private Date createdAt = new Date();
    private String mediaType;
    private String title;
    private String backDrop;

    public MediaItem() {
    }

    public MediaItem(String tmdbId, MediaType mediaType, String title, String backDrop) {
        this.tmdbId = tmdbId;
        this.createdAt = new Date();
        this.mediaType = mediaType.toString();
        this.title = title;
        this.backDrop = backDrop;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackDrop() {
        return backDrop;
    }

    public void setBackDrop(String backDrop) {
        this.backDrop = backDrop;
    }
}
