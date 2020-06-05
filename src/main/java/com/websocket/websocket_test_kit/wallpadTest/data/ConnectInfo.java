/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.wallpadTest.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectInfo {
  private String schema;
  private String ip;
  private String port;
  private String requestUrl;
  private String query;

  public String getFullInfo(){
    StringBuffer stringBuffer = new StringBuffer(this.getSchema());
    stringBuffer.append("://");
    stringBuffer.append(this.getIp());
    stringBuffer.append(":");
    stringBuffer.append(this.getPort());
    stringBuffer.append("/");
    stringBuffer.append(this.getRequestUrl());
    stringBuffer.append("?");
    stringBuffer.append(this.getQuery());

    return stringBuffer.toString();
  }
}
