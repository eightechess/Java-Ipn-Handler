package com.curl.app;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RetrieveFormData {
	   
	   //Method collects all data received and pairs them as Key-Value Pair
	   @RequestMapping("/ipnlistener")
	   @ResponseBody
	   public void handler(@RequestParam Map<String, String> params) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		   this.PostIpnReturn(params);
	   }
	   
	   	   	
	public String PostIpnReturn(Map<String, String> params) throws ClientProtocolException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {		
		SSLContextBuilder builder = new SSLContextBuilder();
	    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	            builder.build());
	    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
	            sslsf).build();
	    

	    HttpPost post = new HttpPost("https://sandbox.tranzcore.com/verify");
	    //Change to code below for live environment
	    // HttpPost post = new HttpPost("https://sandbox.tranzcore.com/verify");
	    
	    List<NameValuePair> nvpList = new ArrayList<>(params.size());
	    for (Map.Entry<String, String> entry : params.entrySet()) {
	    	nvpList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	    }
	    
	    post.setEntity(new UrlEncodedFormEntity(nvpList)); 
        HttpResponse response = httpclient.execute(post);   
        String returnData = EntityUtils.toString(response.getEntity());
        if(returnData.equalsIgnoreCase("VERIFIED")) {
        	//If ipn response is verified
        	// check transaction statue ie "apstatus"
        	//if apstatus="Completed" you can update the transaction
        }
        //Uncomment code below to to see specific error response
        //System.out.println("IPN Verification response: "+ returnData);
		return nvpList.toString();
		

		
	}
}
