/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.controller;

import com.neovisionaries.ws.client.WebSocket;
import com.websocket.websocket_test_kit.util.UtilQueryBuilder;
import com.websocket.websocket_test_kit.wallpadTest.data.ConnectInfo;
import com.websocket.websocket_test_kit.websocket.service.ConnectServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Getter;

@Component
@Getter
public class MainController {
  private static final Logger log = LoggerFactory.getLogger(MainController.class);

  @FXML
  public TextField tfIp;
  @FXML
  public TextField tfPort;
  @FXML
  public TextField tfSite;
  @FXML
  public TextField tfDong;
  @FXML
  public TextField tfHo;
  @FXML
  public TextField tfWpIp;
  @FXML
  public TextField tfToken;
  @FXML
  public Button btnConnectServer;

  @Autowired
  private ConnectServerService connectServerService;

  private WebSocket webSocket;

  @FXML
  public void initialize() {
    tfToken.setText("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJsYWJzX3RlYW0iLCJ1c2VyX2RldGFpbCI6eyJvYXV0aF91c2VyX2luZm8iOnsibWVtYmVySWQiOiJsYWJzX3RlYW0iLCJuYW1lIjoi7Jew6rWs7IaMIiwiYmlydGhkYXkiOm51bGwsImhvbWVQaG9uZU51bWJlciI6IiIsImNlbGxQaG9uZU51bWJlciI6IjAxMC00OTA0LTg2NjkiLCJlbWFpbCI6IiIsImdlbmRlciI6bnVsbCwiYXV0aG9yaXplZERhdGUiOm51bGx9fSwic2NvcGUiOlsidHJ1c3QiLCJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTkxMzYyMDgxLCJqdGkiOiIwODgzNTc2YS1mODQyLTQ4ODAtODFjNi1jMDhkY2ZlZTcyNzMiLCJjbGllbnRfaWQiOiJ0ZXN0In0.shKdXSf-lFqah2mVqVIJZN8M0qawPYP4wD6ukiDGwz8");
  }

  @FXML
  private void handleBtnConnectServer(ActionEvent event) {
    log.info("=================== Start Server Connection ===================");
    webSocket = connectServerService.connectWebsocketToServer(ConnectInfo.builder()
        .schema("wss")
        .ip(tfIp.getText())
        .port(tfPort.getText())
        .requestUrl(UtilQueryBuilder.buildConnectServerRequestUri(tfDong.getText(), tfHo.getText()))
        .query(UtilQueryBuilder.buildConnectServerQuery(tfSite.getText(), tfWpIp.getText(), tfToken.getText()))
        .build());
    log.info("=================== End Server Connection ===================");
  }

  public void disconnectWebsocket(){
    if (this.webSocket != null && this.webSocket.isOpen()){
      log.info("Disconnect Websocket: "+webSocket.getURI());
      this.webSocket.disconnect();
    }
  }
}
