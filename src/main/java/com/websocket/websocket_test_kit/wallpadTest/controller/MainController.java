/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController extends Application {
  private static final Logger log = LoggerFactory.getLogger(MainController.class);

  private Stage primaryStage;
  private BorderPane rootLayout;

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  @Override
  public void start(final Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("서버 <-> 월패드 테스트");

      this.initRootLayout();

    } catch (Exception e) {
      log.error("start() : " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void initRootLayout(){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(Thread.currentThread().getContextClassLoader()
          .getResource("ui/RootLayout.fxml"));
      this.rootLayout = (BorderPane) fxmlLoader.load();

      Scene scene = new Scene(this.rootLayout);
      this.primaryStage.setScene(scene);
      this.primaryStage.show();
    } catch (Exception e) {
      log.error("initRootLayout() : " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void startUi(String[] args){
    launch(args);
  }
}
