/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.websocket.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.websocket.websocket_test_kit.wallpadTest.controller.MainController;
import com.websocket.websocket_test_kit.wallpadTest.data.AutoResponseMessage;
import com.websocket.websocket_test_kit.wallpadTest.data.ConnectInfo;
import com.websocket.websocket_test_kit.wallpadTest.data.WebsocketResponse;
import com.websocket.websocket_test_kit.wallpadTest.service.LogConsoleService;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service("ConnectServerService")
public class ConnectServerServiceImpl implements ConnectServerService {
  private static final Logger log = LoggerFactory.getLogger(ConnectServerServiceImpl.class);

  @Autowired
  private AutoResponseMessage autoResponseMessage;
  @Autowired
  private LogConsoleService logConsoleService;
  @Autowired
  private MainController mainController;
  @Autowired
  private RestTemplate restTemplate;

  private final Gson gson = new Gson();
  
  @Override
  public WebSocket connectWebsocketToServer(final ConnectInfo connectInfo)
      throws IOException, WebSocketException {
    SSLContext sslContext = this.createSSLContext();
    WebSocket webSocket = null;

    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    webSocket = connect(sslSocketFactory, connectInfo);
    log.info("Connect Success!!");
    logConsoleService.writeConsoleLog("Connect Success!!");

    return webSocket;
  }

  // Create the and initialize the SSLContext
  private SSLContext createSSLContext() {
    try {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore
          .load(this.getClass().getClassLoader().getResourceAsStream("hyundaitel.jks"),
              "vbfgrt45".toCharArray());
      //client.jks(truststore) 에 위의 https 서버의 인증서를 미리 import 해놓았다.


      // Create trust manager
      TrustManagerFactory trustManagerFactory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);
      TrustManager[] tm = trustManagerFactory.getTrustManagers();

      // Initialize SSLContext
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, tm, null);

      return sslContext;
    } catch (Exception ex) {
      ex.printStackTrace();
      logConsoleService.writeConsoleLog(ex.getMessage());
    }

    return null;
  }

  private WebSocket connect(final SSLSocketFactory socketFactory,
      final ConnectInfo connectInfo) throws IOException, WebSocketException {
    log.info("connect to server: " + connectInfo.getFullInfo());
    logConsoleService.writeConsoleLog("connect to server >> " + connectInfo.getFullInfo());
    return new WebSocketFactory()
        .setConnectionTimeout(5000)
        .setVerifyHostname(false)
        .setSSLSocketFactory(socketFactory)
        .createSocket(connectInfo.getFullInfo())
        .addListener(new WebSocketAdapter() {
//          바이너리 메세지가 온다면 이쪽 구현
          @Override
          public void onBinaryMessage(WebSocket websocket, byte[] binary) {
            log.info("binary message: " + new String(binary));
          }
          @Override
          public void onTextMessage(WebSocket websocket, String message) {
            logConsoleService.writeConsoleLog("============================ message ============================");
            logConsoleService.writeConsoleLog("<<<<<< message from server: " + message);
            log.info("============================ message ============================");
            log.info("<<<<<< message from server: " + message);
            if (isNotify(message)) {
              logConsoleService.writeConsoleLog("is Notify Message // no response to server");
              log.info("is Notify Message // no response to server");
            } else {
              WebsocketResponse websocketResponse =
                  autoResponseMessage.buildResponseMessage(Integer.parseInt(mainController.getTfStatus().getText()), message);
              websocketResponse.setStatus(Integer.parseInt(mainController.getTfStatus().getText()));
              Gson gson = new Gson();
              String response = gson.toJson(websocketResponse);
              StringBuffer stringBuffer = new StringBuffer(response);
              stringBuffer.insert(response.length()-1, ",\"data\":"+autoResponseMessage.getData());
              logConsoleService.writeConsoleLog("response status : "+autoResponseMessage.getStatus());
              logConsoleService.writeConsoleLog("response data : "+autoResponseMessage.getData());
              logConsoleService.writeConsoleLog(">>>>>> response to server: " + stringBuffer);
              log.info(">>>>>> response to server: " + stringBuffer);
              websocket.sendText(stringBuffer.toString());
            }

            // deviceEvent 체크되있을 시
            if (mainController.isDeviceEvent()) {
              UriComponents uriComponents = UriComponentsBuilder
                  .fromHttpUrl("https://"+mainController.getIp()+":30001/device")
                  .queryParam("access_token", mainController.getAccessToken())
                  .build();

              Map<String, Object> reqBody = null;
              try {
                Map<String, Object> responseData = gson.fromJson(mainController.getResponseData(), Map.class);
                reqBody = new HashMap<>();
                reqBody.put("siteId", mainController.getSiteId());
                reqBody.put("dong", mainController.getDong());
                reqBody.put("ho", mainController.getHo());
                reqBody.put("deviceId", mainController.getDeviceId());
                reqBody.put("state", "NORMAL");
                reqBody.put("devicePropertyList", responseData.get("properties"));
              } catch (JsonSyntaxException e) {
                log.error("error >> ", e);
                logConsoleService.printErrorLogToConsoleTextArea(e);
              }

              log.info("device event send >> reqBody: {}", gson.toJson(reqBody));
              logConsoleService.writeConsoleLog("device event send >> reqBody: "+gson.toJson(reqBody));
              ResponseEntity<Object> deviceEventResponse = null;
              try {
                deviceEventResponse = restTemplate.exchange(
                    uriComponents.toString(),
                    HttpMethod.POST,
                    new HttpEntity<>(reqBody, null),
                    Object.class);
              } catch (RestClientException e) {
                log.error("fail to event req >> ", e);
                logConsoleService.printErrorLogToConsoleTextArea(e);
              }

              if (deviceEventResponse != null) {
                log.info("event response << statusCode: {}", deviceEventResponse.getStatusCode());
                logConsoleService.writeConsoleLog("event response << statusCode: "+deviceEventResponse.getStatusCode());
              }
            }

            logConsoleService.writeConsoleLog("============================ message ============================");
            log.info("============================ message ============================");
          }
        })
        .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
        .connect();
  }

  private boolean isNotify(String message) {
    String type = message.split("\"type\":\"")[1].split("\"")[0];
    if (type.equalsIgnoreCase("notify")) {
      return true;
    }
    return false;
  }
}
