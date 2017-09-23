package uet.com.haku.losslessdownloader.Entity;

import java.util.ArrayList;

/**
 * Created by phamn on 9/21/2017.
 */

public class Mp3Profile {

    private String title;
    private String singer;
    private ArrayList<Mp3Quality> downloadLink;
    private String urlPageDownload;
    private String quality;
    private String time;

    public Mp3Profile(String title, String singer, ArrayList<Mp3Quality> downloadLink, String urlPageDownload, String quality, String time) {
        this.title = title;
        this.singer = singer;
        this.downloadLink = downloadLink;
        this.urlPageDownload = urlPageDownload;
        this.quality = quality;
        this.time = time;
    }

    public Mp3Profile() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public ArrayList<Mp3Quality> getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(ArrayList<Mp3Quality> downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getUrlPageDownload() {
        return urlPageDownload;
    }

    public void setUrlPageDownload(String urlPageDowload) {
        this.urlPageDownload = urlPageDowload;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
