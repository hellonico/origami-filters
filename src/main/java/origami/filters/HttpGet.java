package origami.filters;

import org.opencv.core.Mat;
import origami.Filter;
import origami.Origami;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HttpGet implements Filter {
    String url;
    int interval = -1;
    Filter f = e->e;

    public HttpGet() {

    }

    public String getUrl() {
        return url;
    }

    ScheduledExecutorService executor;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
        if(interval>0) {
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(()->{urlToFilter(url);}, 0, interval, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        executor.shutdown();
    }

    public void setUrl(String url) {
        this.url = url;
        urlToFilter(this.url);
    }

    public void urlToFilter(String url) {
        try {
            URL _url = new URL(url);
            URLConnection conn = _url.openConnection();
            String result = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
            this.f = Origami.StringToFilter(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mat apply(Mat mat) {
        return f.apply(mat);
    }
}
