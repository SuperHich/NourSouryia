package com.noursouryia.externals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
/**
 * Sa7i7 Al Boukhari
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArray = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
    
    public JSONArray getJSONFromUrl(String url) {
    	
    	int statusCode = -1;
        // Making HTTP request
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
 
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            statusCode = httpResponse.getStatusLine().getStatusCode();
            is = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        if(statusCode == HttpStatus.SC_OK)
        {
        	// try parse the string to a JSON object
        	try {
        		jArray = new JSONArray(json);            
        	} catch (JSONException e) {
        		Log.e("JSON Parser", "Error parsing data " + e.toString());
        		return null;
        	}
        }
 
        // return JSON Array
        return jArray;
 
    }
    
    public JSONObject getJSONObjectFromUrl(String url) {

    	int statusCode = -1;
    	// Making HTTP request
    	try {
    		HttpClient httpClient = new DefaultHttpClient();
    		HttpGet httpGet = new HttpGet(url);

    		HttpResponse httpResponse = httpClient.execute(httpGet);
    		HttpEntity httpEntity = httpResponse.getEntity();
    		statusCode = httpResponse.getStatusLine().getStatusCode();
    		is = httpEntity.getContent();

    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	} catch (ClientProtocolException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(
    				is, "iso-8859-1"), 8);
    		StringBuilder sb = new StringBuilder();
    		String line = null;
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "\n");
    		}
    		is.close();
    		json = sb.toString();
    		Log.e("JSON", json);
    	} catch (Exception e) {
    		Log.e("Buffer Error", "Error converting result " + e.toString());
    	}

    	if(statusCode == HttpStatus.SC_OK)
    	{
    		// try parse the string to a JSON object
    		try {
    			jObj = new JSONObject(json);            
    		} catch (JSONException e) {
    			Log.e("JSON Parser", "Error parsing data " + e.toString());
    			return null;
    		}
    	}

    	// return JSON Object 
    	return jObj;

    }
    
    public JSONObject getJSONObjectFromUrl(String url, List<NameValuePair> params) {
    	
    	int statusCode = NSManager.DEFAULT_VALUE;
        // Making HTTP request
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            statusCode = httpResponse.getStatusLine().getStatusCode();
            is = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        
        if(statusCode == HttpStatus.SC_OK)
        {
        	// try parse the string to an Integer value
        	try {
        		jObj = new JSONObject(json);            
        	} catch (JSONException e) {
        		Log.e("JSON Parser", "Error parsing data " + e.toString());
        	}
        }
 
        // return status code / response
        return jObj;
 
    }
    
//    public int getIntegerFromUrl(String url, List<NameValuePair> params) {
//    	
//    	int statusCode = NSManager.DEFAULT_VALUE;
//        // Making HTTP request
//        try {
//            HttpClient httpClient = getNewHttpClient();
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
// 
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            statusCode = httpResponse.getStatusLine().getStatusCode();
//            is = httpEntity.getContent();
// 
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// 
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            json = sb.toString();
//            Log.e("JSON", json);
//        } catch (Exception e) {
//            Log.e("Buffer Error", "Error converting result " + e.toString());
//        }
//        
//        if(statusCode == HttpStatus.SC_OK)
//        {
//        	// try parse the string to an Integer value
//        	try {
//        		statusCode = Integer.parseInt(json);            
//        	} catch (NumberFormatException e) {
//        		Log.e("JSON Parser", "Error parsing data " + e.toString());
//        	}
//        }
// 
//        // return status code / response
//        return statusCode;
// 
//    }
    
    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}