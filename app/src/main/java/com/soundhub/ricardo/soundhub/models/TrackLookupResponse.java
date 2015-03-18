package com.soundhub.ricardo.soundhub.models;

/**
 * Created by ricardo on 18-03-2015.
 */
public class TrackLookupResponse {
    private String kind;
    private String id;
    private String duration;
    private String tag_list;
    private String title;
    private String uri;
    private String artwork_url;
    private String stream_url;
    private String favoritings_count;
    private String original_format;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTag_list() {
        return tag_list;
    }

    public void setTag_list(String tag_list) {
        this.tag_list = tag_list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArtwork_url() {
        return artwork_url;
    }

    public void setArtwork_url(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getFavoritings_count() {
        return favoritings_count;
    }

    public void setFavoritings_count(String favoritings_count) {
        this.favoritings_count = favoritings_count;
    }

    public String getOriginal_format() {
        return original_format;
    }

    public void setOriginal_format(String original_format) {
        this.original_format = original_format;
    }
}
