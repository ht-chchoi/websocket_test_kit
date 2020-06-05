/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.controller;

import com.neovisionaries.ws.client.WebSocket;
import com.websocket.websocket_test_kit.util.UtilQueryBuilder;
import com.websocket.websocket_test_kit.wallpadTest.data.AutoResponseMessage;
import com.websocket.websocket_test_kit.wallpadTest.data.ConnectInfo;
import com.websocket.websocket_test_kit.websocket.service.ConnectServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Getter;

@Component
@Getter
public class MainController {
  private static final Logger log = LoggerFactory.getLogger(MainController.class);

  @FXML
  private TextField tfIp;
  @FXML
  private TextField tfPort;
  @FXML
  private TextField tfSite;
  @FXML
  private TextField tfDong;
  @FXML
  private TextField tfHo;
  @FXML
  private TextField tfWpIp;
  @FXML
  private TextField tfToken;
  @FXML
  private Button btnConnectServer;
  @FXML
  private Button btnDisconnect;
  @FXML
  private TextArea taDataToResponse;
  @FXML
  private Button btnAcceptData;
  @FXML
  private Button btnCancelData;
  @FXML
  private TextArea taConsole;


  @Autowired
  private ConnectServerService connectServerService;
  @Autowired
  private AutoResponseMessage autoResponseMessage;

  private WebSocket webSocket;

  @FXML
  public void initialize() {
    tfToken.setText("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJsYWJzX3RlYW0iLCJ1c2VyX2RldGFpbCI6eyJvYXV0aF91c2VyX2luZm8iOnsibWVtYmVySWQiOiJsYWJzX3RlYW0iLCJuYW1lIjoi7Jew6rWs7IaMIiwiYmlydGhkYXkiOm51bGwsImhvbWVQaG9uZU51bWJlciI6IiIsImNlbGxQaG9uZU51bWJlciI6IjAxMC00OTA0LTg2NjkiLCJlbWFpbCI6IiIsImdlbmRlciI6bnVsbCwiYXV0aG9yaXplZERhdGUiOm51bGx9fSwic2NvcGUiOlsidHJ1c3QiLCJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTkxMzYyMDgxLCJqdGkiOiIwODgzNTc2YS1mODQyLTQ4ODAtODFjNi1jMDhkY2ZlZTcyNzMiLCJjbGllbnRfaWQiOiJ0ZXN0In0.shKdXSf-lFqah2mVqVIJZN8M0qawPYP4wD6ukiDGwz8");
    taDataToResponse.setText("{\"result\" : 200}");
    autoResponseMessage.setData("{\"result\" : 200}");
    btnAcceptData.setDisable(true);
  }

  @FXML
  private void handleBtnConnectServer(ActionEvent event) {
    log.info("=================== Start Server Connection ===================");
    this.disconnectWebsocket();
    webSocket = connectServerService.connectWebsocketToServer(ConnectInfo.builder()
        .schema("wss")
        .ip(tfIp.getText())
        .port(tfPort.getText())
        .requestUrl(UtilQueryBuilder.buildConnectServerRequestUri(tfDong.getText(), tfHo.getText()))
        .query(UtilQueryBuilder.buildConnectServerQuery(tfSite.getText(), tfWpIp.getText(), tfToken.getText()))
        .build());
    if (webSocket != null && webSocket.isOpen()){
      btnConnectServer.setDisable(true);
      btnDisconnect.setDisable(false);
      btnAcceptData.setDisable(false);
    }
    log.info("=================== End Server Connection ===================");
  }

  @FXML
  private void handleBtnDisconnect(ActionEvent event){
    this.disconnectWebsocket();
  }

  @FXML
  private void handleBtnAcceptData(ActionEvent event){
    autoResponseMessage.setData(taDataToResponse.getText());
    btnAcceptData.setDisable(true);
    taDataToResponse.setDisable(true);
    btnCancelData.setDisable(false);
  }

  @FXML
  private void handleBtnCancelData(ActionEvent event){
    autoResponseMessage.setData("");
    btnAcceptData.setDisable(false);
    taDataToResponse.setDisable(false);
    btnCancelData.setDisable(true);
  }

  public void disconnectWebsocket(){
    if (this.webSocket != null && this.webSocket.isOpen()){
      log.info("Disconnect Websocket: "+webSocket.getURI());
      this.webSocket.disconnect();
      btnConnectServer.setDisable(false);
      btnDisconnect.setDisable(true);
    }
  }
}
