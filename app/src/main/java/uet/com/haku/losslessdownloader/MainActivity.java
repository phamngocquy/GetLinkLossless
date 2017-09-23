package uet.com.haku.losslessdownloader;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import uet.com.haku.losslessdownloader.Adapter.RclViewAdapter;
import uet.com.haku.losslessdownloader.Entity.Mp3Profile;
import uet.com.haku.losslessdownloader.Entity.Mp3Quality;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Mp3Profile> data;
    private ProgressBar progressBar;
    private RclViewAdapter rclViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rclView);
        progressBar = findViewById(R.id.prg_bar);

        data = new ArrayList<>();
        rclViewAdapter = new RclViewAdapter(data, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rclViewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    int totalItemCount = manager.getItemCount();
                    int lastVisibleItem = manager.findLastVisibleItemPosition();
                    if (totalItemCount <= (lastVisibleItem + 1) && !Config.THREAD_RUNNING && !Config.END_PAGE) {
                        new LoadData().execute(Config.CURRENT_SEARCH, String.valueOf(Config.CURRENT_PAGE));
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.mn_search);


        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (!Config.THREAD_RUNNING) {

                    data.clear();
                    Config.CURRENT_PAGE = 1;
                    Config.CURRENT_SEARCH = "";
                    Config.END_PAGE = false;
                    new LoadData().execute(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Config.THREAD_RUNNING = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url;
                if (strings.length == 1) {
                    String searchKey = strings[0].replace(" ", "+");
                    url = "http://search.chiasenhac.vn/search.php?s=" + searchKey + "&cat=music";
                    Config.CURRENT_SEARCH = url;
                    Config.CURRENT_PAGE = 1;
                } else {
                    Config.CURRENT_PAGE += 1;
                    url = Config.CURRENT_SEARCH + "&page=" + Config.CURRENT_PAGE;
                    Log.d("url", url);
                }
                Log.d("page", String.valueOf(Config.CURRENT_PAGE));
                Document document = Jsoup.connect(url).userAgent(Config.USER_AGENT).timeout(60000).get();
                Elements elements = document.select("table.tbtable tr");
                if (elements.text().contains("Không được phép")) Config.END_PAGE = true;
                for (Element element : elements) {

                    Elements elem = element.select("div.tenbh");
                    Elements elem_ = element.select("span.gen");

                    if (elem.size() > 0 && elem_.size() > 0) {
                        Elements elem3 = elem.get(0).getElementsByTag("a");
                        if (elem3.size() > 0) {
                            Mp3Profile mp3Profile = new Mp3Profile();
                            ArrayList<Mp3Quality> listLinkDownload = new ArrayList<>();

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
                                    Mp3Quality mp3Quality = new Mp3Quality(attr, "128kbps");
                                    listLinkDownload.add(mp3Quality);
                                } else if (attr.matches(".+\\[320kbps_MP3]\\.mp3$")) {
                                    Mp3Quality mp3Quality = new Mp3Quality(attr, "320kbps");
                                    listLinkDownload.add(mp3Quality);
                                } else if (attr.matches(".+\\[500kbps_M4A]\\.m4a$")) {
                                    Mp3Quality mp3Quality = new Mp3Quality(attr, "500kbps");
                                    listLinkDownload.add(mp3Quality);

                                } else if (attr.matches(".+\\[Lossless_FLAC]\\.flac$")) {
                                    Mp3Quality mp3Quality = new Mp3Quality(attr, "Lossless");
                                    listLinkDownload.add(mp3Quality);
                                } else if (attr.matches(".+\\[32kbps_M4A]\\.m4a$")) {
                                    Mp3Quality mp3Quality = new Mp3Quality(attr, "32kbps");
                                    listLinkDownload.add(mp3Quality);
                                }

                            }
                            mp3Profile.setDownloadLink(listLinkDownload);
                            data.add(mp3Profile);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Config.THREAD_RUNNING = false;
            //super.onPostExecute(s);
            rclViewAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }
}
