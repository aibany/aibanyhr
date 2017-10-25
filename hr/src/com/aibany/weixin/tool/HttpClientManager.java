package com.aibany.weixin.tool;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientManager {
	public static CloseableHttpClient getSSLHttpClient() {
			try {
				SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
						null, new TrustStrategy() {
							// 信任所有
							public boolean isTrusted(X509Certificate[] chain,
									String authType) throws CertificateException {
								return true;
							}
						}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				return HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
			return HttpClients.createDefault();
	}
      
    public static HttpPost getPostMethod(String url) {  
        HttpPost pmethod = new HttpPost(url);  
        pmethod.addHeader("Connection", "keep-alive");  
        pmethod.addHeader("Accept", "*/*");  
        pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  
        pmethod.addHeader("Host", "mp.weixin.qq.com");  
        pmethod.addHeader("X-Requested-With", "XMLHttpRequest");  
        pmethod.addHeader("Cache-Control", "max-age=0");  
        pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
        return pmethod;  
    }  
  
    public static HttpGet getGetMethod(String url) {  
        HttpGet pmethod = new HttpGet(url);  
        pmethod.addHeader("Connection", "keep-alive");  
        pmethod.addHeader("Cache-Control", "max-age=0");  
        pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
        pmethod.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");  
        return pmethod;  
    }  
}