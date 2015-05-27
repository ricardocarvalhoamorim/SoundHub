package com.soundhub.ricardo.soundhub.models;

public class SoundCloudUser {

    /**
     * id": 12148579,
     "kind": "user",
     "permalink": "majorlazer",
     "username": "Major Lazer [OFFICIAL]",
     "last_modified": "2015/03/23 16:38:32 +0000",
     "uri": "https://api.soundcloud.com/users/12148579",
     "permalink_url": "http://soundcloud.com/majorlazer",
     "avatar_url": "https://i1.sndcdn.com/avatars-000132918679-9r45tu-large.jpg"
     */

    private String id;
    private String kind;
    private String permalink;
    private String username;
    private String uri;
    private String permalink_url;
    private String avatar_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public void setPermalink_url(String permalink_url) {
        this.permalink_url = permalink_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
