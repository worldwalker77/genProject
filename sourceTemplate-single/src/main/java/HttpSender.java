package ${groupId}.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * Created by hongxing.zhou on 2015/12/7.
 */
public class HttpSender {
    private static final CloseableHttpClient client;

    static {
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager();
        phccm.setMaxTotal(400);
        phccm.setDefaultMaxPerRoute(100);
        client = HttpClients.custom().setConnectionManager(phccm).build();
    }
    public String send(String url, String json) throws Exception {
        HttpEntity respEntity = null;
        try {
            HttpPost post = new HttpPost(url);
            post.addHeader("User-Agent", "Mozilla/5.0");
//            String json = JSON.toJSONString(paramPairs);
            StringEntity myEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            post.setEntity(myEntity);
            CloseableHttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            if (!IsSuccessStatusCode(status)) {
                response.close();
                throw new Exception(String.format("Error HTTP Code %d", status));
            }
            respEntity = response.getEntity();
            BufferedReader rd = new BufferedReader(new InputStreamReader(respEntity.getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
        finally {
            if (respEntity != null){
                EntityUtils.consume(respEntity);
            }
        }
    }
    private static boolean IsSuccessStatusCode(int status) {
        return status == HttpStatus.SC_OK || status == HttpStatus.SC_NO_CONTENT;
    }
}
