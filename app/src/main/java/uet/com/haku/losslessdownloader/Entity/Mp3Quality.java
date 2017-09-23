package uet.com.haku.losslessdownloader.Entity;

/**
 * Created by phamn on 9/23/2017.
 */

public class Mp3Quality {
    private String url;
    private String quality;

    public Mp3Quality() {
    }

    public Mp3Quality(String url, String quality) {
        this.url = url;
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
