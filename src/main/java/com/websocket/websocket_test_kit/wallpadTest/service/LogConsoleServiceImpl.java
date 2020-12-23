/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.service;

import com.websocket.websocket_test_kit.wallpadTest.controller.MainController;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service("LogConsoleService")
@Slf4j
public class LogConsoleServiceImpl implements LogConsoleService{

  @Autowired
  private MainController mainController;

  @Override
  public void writeConsoleLog(final String text) {
    this.mainController.getTaConsole().appendText(text+"\n");
  }

  @Override
  public void printErrorLogToConsoleTextArea(final Exception e) {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    this.mainController.getTaConsole().appendText("\n"+e.toString() + "\n" + stringWriter.toString());
  }
}
