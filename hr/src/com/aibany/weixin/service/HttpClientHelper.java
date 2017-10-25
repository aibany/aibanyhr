package com.aibany.weixin.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientHelper {
	protected static Logger log = Logger.getLogger(HttpClientHelper.class);
	private boolean isSSL = false;
	private String charset = "utf-8";
	
	public HttpClientHelper(){
		this.isSSL = false;
	}
	
	public HttpClientHelper(boolean isSSL){
		this.isSSL = isSSL;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	protected CloseableHttpClient getSSLHttpClient() {
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
	
	public String retrieveGetReponsetText(String url){
		String result = null;
		CloseableHttpClient httpClient = null;
    	CloseableHttpResponse response = null;
		try {
			try{
				if (isSSL){
					httpClient = this.getSSLHttpClient();
				}else{
					httpClient = HttpClients.createDefault();					
				}
		        HttpGet httpGet = new HttpGet(url);   
		        response = httpClient.execute(httpGet);    
		        result = EntityUtils.toString(response.getEntity(),charset);
			}finally{
	    		if (response != null){
	    			response.close();
	    		}
	    		if (httpClient != null){
	    			httpClient.close();
	    		}
			}		
		}
		catch(Exception ex){
			log.error(ex.getLocalizedMessage(), ex);
		}
		return result;
	}
	
	public String retrievePostReponsetText(String url,HttpEntity httpEntity){
		String result = null;
		CloseableHttpClient httpClient = null;
    	CloseableHttpResponse response = null;
		try {
			try{
				if (isSSL){
					httpClient = this.getSSLHttpClient();
				}else{
					httpClient = HttpClients.createDefault();					
				}
		    	HttpPost httpPost = new HttpPost(url);
		        httpPost.setEntity(httpEntity);    
		        
		        response = httpClient.execute(httpPost);    
		        result = EntityUtils.toString(response.getEntity(),charset);
			}finally{
	    		if (response != null){
	    			response.close();
	    		}
	    		if (httpClient != null){
	    			httpClient.close();
	    		}
			}		
		}
		catch(Exception ex){
			log.error(ex.getLocalizedMessage(), ex);
		}
		return result;
	}	
}
