package uet.com.haku.losslessdownloader;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import uet.com.haku.losslessdownloader.Entity.Mp3Profile;


/**
 * Created by phamn on 9/20/2017.
 */

public class LoadData extends AsyncTask<String,Void,String>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String s = "see you again";
            String searchKey = s.replace(" ","+");
            String url = "http://search.chiasenhac.vn/search.php?s="+searchKey+"&cat=music";
            Document document = Jsoup.connect(url).userAgent(Config.USER_AGENT).timeout(60000).get();
            Elements elements = document.select("table.tbtable tr");
            for (Element element : elements)
            {

                Elements elem = element.select("div.tenbh");
                Elements elem_ = element.select("span.gen");

                if (elem.size() >0 && elem_.size() >0)
                {
                    Elements elem3 = elem.get(0).getElementsByTag("a");
                    if (elem3.size() > 0)
                    {
                        Mp3Profile mp3Profile = new Mp3Profile();


                        String title = elem3.get(0).text();
                        String link = elem3.get(0).attr("href");
                        String singer = elem.text().replace(title,"").trim();

                        String timeQuality[] = elem_.get(0).text().split(" ");
                        mp3Profile.setTitle(title);
                        mp3Profile.setUrlPageDownload(link.replace(".html","_download.html"));
                        mp3Profile.setSinger(singer);

                        if (timeQuality.length >= 2)
                        {
                            mp3Profile.setTime(timeQuality[0]);
                            mp3Profile.setQuality(timeQuality[1]);
                        }
                        else {
                            mp3Profile.setTime(Config.UNKNOWN);
                            mp3Profile.setQuality(Config.UNKNOWN);
                        }

                        System.out.println(title);
                        System.out.println(link);
                        System.out.println(elem_.get(0).text());
                        System.out.println(singer);



                        Document documentDownload = Jsoup.connect(link.replace(".html","_download.html")).userAgent(Config.USER_AGENT).timeout(60000).get();
                        Elements elementsDownload = documentDownload.select("div.tip-text a[href]");
                        for (Element iEle : elementsDownload)
                        {

                            String attr = iEle.attr("href").replace(" ","");
                            if (attr.matches(".+\\[128kbps_MP3]\\.mp3$")) System.out.println(attr);
                            else if (attr.matches(".+\\[320kbps_MP3]\\.mp3$")) System.out.println(attr);
                            else if (attr.matches(".+\\[500kbps_M4A]\\.m4a$")) System.out.println(attr);
                            else if (attr.matches(".+\\[Lossless_FLAC]\\.flac$")) System.out.println(attr);
                            else if (attr.matches(".+\\[32kbps_M4A]\\.m4a$")) System.out.println(attr);
                        }
                    }

                }

                System.out.println("======================");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}