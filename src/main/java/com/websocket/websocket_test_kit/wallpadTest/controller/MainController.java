/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.controller;

import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

@Component
public class MainController {
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

  @FXML
  public void initialize(){
    tfToken.setText("1234");
  }

}
