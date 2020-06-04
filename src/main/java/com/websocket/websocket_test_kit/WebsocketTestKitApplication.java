package com.websocket.websocket_test_kit;

import com.websocket.websocket_test_kit.wallpadTest.controller.MainController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketTestKitApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebsocketTestKitApplication.class, args);
        MainController mainController = new MainController();
        mainController.startUi(args);
    }
}
