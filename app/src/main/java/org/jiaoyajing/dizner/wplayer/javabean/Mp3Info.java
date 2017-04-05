package org.jiaoyajing.dizner.wplayer.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "music")
public class Mp3Info {
    @Column(name = "_id", isId = true)
    private long id;
    @Column(name = "title")
    private String title;//标题
    @Column(name = "artist")
    private String artist;//艺术家
    @Column(name = "album")
    private String album;//专辑
    @Column(name = "albumId")
    private long albumId;//
    @Column(name = "duration")
    private long duration;//时长
    @Column(name = "size")
    private long size;//文件大小
    @Column(name = "url")
    private String url;//下载链接
    @Column(name = "showUrl")
    private String showUrl;//播放链接
    @Column(name = "IsMusic")
    private int IsMusic;//是否是音乐
    @Column(name = "songPic")
    private String songPic;
    @Column(name = "history")
    private boolean isHistory;
    @Column(name = "lrcUrl")
    private String lrcUrl;

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
    @Column(name = "isLike")
    private boolean isLike;

    public String getSongPic() {
        return songPic;
    }

    public void setSongPic(String songPic) {
        this.songPic = songPic;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    @Column(name = "albumPic")
    private String albumPic;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsMusic() {
        return IsMusic;
    }

    public void setIsMusic(int isMusic) {
        IsMusic = isMusic;
    }

    public Mp3Info(long id, String title, String artist, String album,
                   long albumId, long duration, long size, String url, int isMusic) {
        super();
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.size = size;
        this.url = url;
        IsMusic = isMusic;
    }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", IsMusic=" + IsMusic +
                ", songPic='" + songPic + '\'' +
                ", isHistory=" + isHistory +
                ", isLike=" + isLike +
                ", albumPic='" + albumPic + '\'' +
                '}';
    }

    public Mp3Info() {
        super();
    }

}
