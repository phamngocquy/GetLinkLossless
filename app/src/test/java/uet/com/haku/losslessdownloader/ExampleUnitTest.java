package uet.com.haku.losslessdownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import uet.com.haku.losslessdownloader.Entity.Mp3Profile;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getLinkDownload() throws Exception {
        //new LoadData().execute("http://chiasenhac.vn/mp3/vietnam/v-pop/la-lung~thai-vu~tsvdrv7dqma2tm_download.html");
        Document document = Jsoup.connect("http://chiasenhac.vn/mp3/vietnam/v-pop/nga-tu-duong~ho-quang-hieu~ts3s55d7q4hhmt_download.html").userAgent(Config.USER_AGENT).timeout(60000).get();
        Elements elements = document.select("div.tip-text a[href]");
        // search in element:
        // [128kbps_MP3].mp3 [320kbps_MP3].mp3 [500kbps_M4A].m4a [Lossless_FLAC].flac [32kbps_M4A].m4a
        for (Element iEle : elements) {
            String attr = iEle.attr("href").replace(" ", "");
            if (attr.matches(".+\\[128kbps_MP3]\\.mp3$")) System.out.println(attr);
            else if (attr.matches(".+\\[320kbps_MP3]\\.mp3$")) System.out.println(attr);
            else if (attr.matches(".+\\[500kbps_M4A]\\.m4a$")) System.out.println(attr);
            else if (attr.matches(".+\\[Lossless_FLAC]\\.flac$")) System.out.println(attr);
            else if (attr.matches(".+\\[32kbps_M4A]\\.m4a$")) System.out.println(attr);
        }
        //String attr = element.attr("href");
        //System.out.println(attr);
    }

    @Test
    public void getList() throws IOException {
        ArrayList<Mp3Profile> data = new ArrayList<>();
        String s = "see you again";
        String searchKey = s.replace(" ", "+");
        String url = "http://search.chiasenhac.vn/search.php?s=" + searchKey + "&cat=music&page=2000";
        Document document = Jsoup.connect(url).userAgent(Config.USER_AGENT).timeout(60000).get();

        Elements elements = document.select("table.tbtable tr");
        System.out.println(elements);
        for (Element element : elements) {

            Elements elem = element.select("div.tenbh");
            Elements elem_ = element.select("span.gen");

            if (elem.size() > 0 && elem_.size() > 0) {
                Elements elem3 = elem.get(0).getElementsByTag("a");
                if (elem3.size() > 0) {
                    Mp3Profile mp3Profile = new Mp3Profile();
                    ArrayList<String> listLinkDownload = new ArrayList<>();

                    String title = elem3.get(0).text();
                    String link = elem3.get(0).attr("href");
                    String singer = elem.text().replace(title, "").trim();

                    String timeQuality[] = elem_.get(0).text().split(" ");
                    mp3Profile.setTitle(title);
                    mp3Profile.setUrlPageDownload(link.replace(".html", "_download.html"));
                    mp3Profile.setSinger(singer);

                    if (timeQuality.length >= 2) {
                        mp3Profile.setTime(timeQuality[0]);
                        mp3Profile.setQuality(timeQuality[1]);
                    } else {
                        mp3Profile.setTime(Config.UNKNOWN);
                        mp3Profile.setQuality(Config.UNKNOWN);
                    }


                    Document documentDownload = Jsoup.connect(link.replace(".html", "_download.html")).userAgent(Config.USER_AGENT).timeout(60000).get();
                    Elements elementsDownload = documentDownload.select("div.tip-text a[href]");
                    for (Element iEle : elementsDownload) {
                        String attr = iEle.attr("href").replace(" ", "");
                        if (attr.matches(".+\\[128kbps_MP3]\\.mp3$")) {
                            listLinkDownload.add(attr);
                            //  System.out.println(attr);
                        } else if (attr.matches(".+\\[320kbps_MP3]\\.mp3$")) {
                            listLinkDownload.add(attr);
                            // System.out.println(attr);
                        } else if (attr.matches(".+\\[500kbps_M4A]\\.m4a$")) {
                            listLinkDownload.add(attr);
                            // System.out.println(attr);
                        } else if (attr.matches(".+\\[Lossless_FLAC]\\.flac$")) {
                            listLinkDownload.add(attr);
                            //  System.out.println(attr);
                        } else if (attr.matches(".+\\[32kbps_M4A]\\.m4a$")) {
                            listLinkDownload.add(attr);
                            // System.out.println(attr);
                        }
                    }
                   // mp3Profile.setDownloadLink(listLinkDownload);
                    data.add(mp3Profile);
                }

            }

        }

        for (Mp3Profile i : data) {
            System.out.println(i.getTitle());
            System.out.println(i.getSinger());
            System.out.println(i.getTime());
            System.out.println(i.getQuality());
            System.out.println(i.getUrlPageDownload());

            System.out.println("==============================");
        }
    }
}