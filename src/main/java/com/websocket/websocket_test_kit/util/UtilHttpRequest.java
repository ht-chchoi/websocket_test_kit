/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.util;

import com.websocket.websocket_test_kit.wallpadTest.controller.MainController;
import com.websocket.websocket_test_kit.wallpadTest.data.ConnectInfo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilHttpRequest {
  private static final Logger log = LoggerFactory.getLogger(UtilHttpRequest.class);

  public static HttpURLConnection getHttpURLConnection(ConnectInfo connectInfo)
      throws IOException {
    StringBuffer stringBuffer = new StringBuffer(connectInfo.getSchema());
    stringBuffer.append("://");
    stringBuffer.append(connectInfo.getIp());
    stringBuffer.append(":");
    stringBuffer.append(connectInfo.getPort());
    stringBuffer.append("/");
    stringBuffer.append(connectInfo.getRequestUrl());
    stringBuffer.append("?");
    stringBuffer.append(connectInfo.getQuery());

    log.info("req info  >>  "+stringBuffer.toString());
    URL url = new URL(stringBuffer.toString());

    HttpURLConnection httpURLConnection = null;
    if(connectInfo.getSchema().equals("https")){
      httpURLConnection = (HttpsURLConnection) url.openConnection();
    } else {
      httpURLConnection = (HttpURLConnection) url.openConnection();
    }

    return httpURLConnection;
  }

  public static void closeConnect(HttpsURLConnection httpsURLConnection){
    if(httpsURLConnection != null){
      httpsURLConnection.disconnect();
    }
  }
  public static void closeConnect(DataOutputStream outputStream, BufferedReader reader){
    if(outputStream != null){
      try{outputStream.close();} catch (IOException e){e.printStackTrace();}
    }
    if(reader != null){
      try{reader.close();} catch (IOException e){e.printStackTrace();}
    }
  }



  //    사설 SSL 인증서 모두 허용 세팅
  public static void trustAllHttpsCertificates() throws Exception {
    TrustManager[] trustAllCerts = new TrustManager[1];
    TrustManager tm = new miTM();
    trustAllCerts[0] = tm;

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }
  public static class miTM implements TrustManager, X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
    public boolean isServerTrusted(X509Certificate[] certs) {
      return true;
    }
    public boolean isClientTrusted(X509Certificate[] certs) {
      return true;
    }
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
      return;
    }
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
      return;
    }
  }
}
