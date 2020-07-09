/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.service;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;

@Service("LogConsoleService")
public class LogConsoleServiceImpl implements LogConsoleService{
  private static final Logger log = LoggerFactory.getLogger(LogConsoleService.class);

  private TextArea textArea;

  @Override
  public void writeConsoleLog(final String text) {
    if(textArea != null){
      textArea.appendText(text+"\n");
    } else {
      log.error("console is no set");
    }
  }

  @Override
  public void printErrorLogToConsoleTextArea(final TextArea taConsole, final Exception e) {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    taConsole.setText(e.toString() + "\n" + stringWriter.toString());
  }

  public void setTextArea(final TextArea textArea) {
    this.textArea = textArea;
  }
}
