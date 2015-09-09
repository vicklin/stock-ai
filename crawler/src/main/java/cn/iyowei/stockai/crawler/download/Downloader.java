package cn.iyowei.stockai.crawler.download;

import cn.iyowei.stockai.crawler.exception.DownloadException;
import cn.iyowei.stockai.crawler.resolver.Resolver;
import cn.iyowei.stockai.crawler.resolver.ResponseType;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuguanglin on 15/9/1.
 */
public class Downloader {

    private static final Logger log = LoggerFactory.getLogger(Downloader.class);


    public static <T> T get(String uri, Resolver resolver, ResponseType type) throws DownloadException {
        log.debug("Get:" + uri);
        String encodedUri = encodeUri(uri);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(encodedUri);
        log.debug("getRequestLine", httpGet.getRequestLine());
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            return resolver.resolve(response, type);

        } catch (IOException e) {
            throw new DownloadException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new DownloadException(e);
            }
        }
    }

    private static String encodeUri(String uri) {
        int quoIndex = uri.indexOf("?");
        if (quoIndex < 0 || quoIndex == uri.length()) {
            return uri;
        }
        String url = uri.substring(0, quoIndex);
        String param = uri.substring(quoIndex + 1);
        String encodedUri = uri;
        try {
            encodedUri = url + "?" + URLEncoder.encode(param, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedUri;
    }


}
