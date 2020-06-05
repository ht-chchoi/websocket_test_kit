package com.websocket.websocket_test_kit;

import com.websocket.websocket_test_kit.util.ConstVal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class WebsocketTestKitApplication extends Application {
  private static final Logger log = LoggerFactory.getLogger(WebsocketTestKitApplication.class);

  private ConfigurableApplicationContext context;
  private Parent rootNode;


  @Override
  public void init() throws Exception {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(
        WebsocketTestKitApplication.class);
    context = builder.run(this.getParameters().getRaw().toArray(new String[0]));
    FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader()
        .getResource("ui/MainPage.fxml"));
    fxmlLoader.setControllerFactory(context::getBean);
    rootNode = fxmlLoader.load();
  }

  @Override
  public void start(final Stage primaryStage) {
    primaryStage.setScene(new Scene(rootNode, ConstVal.APP_WINDOW_WIDTH,
        ConstVal.APP_WINDOW_HEIGHT));
    primaryStage.setTitle("Server <-> wallpad");
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  @Override
  public void stop() throws Exception {
    context.close();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
