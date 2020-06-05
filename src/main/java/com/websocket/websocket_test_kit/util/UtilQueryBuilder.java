/*
 * Copyright (c) 2020. (주)현대통신
 *
 * 이 프로그램은 저작권법에 따라 보호됩니다. 이 프로그램의 일부나 전부를 무단으로
 * 복제하거나 배포하는 경우에는 저작권의 침해가 되므로 주의하시기 바랍니다.
 */
package com.websocket.websocket_test_kit.util;

public class UtilQueryBuilder {
  public static String buildConnectServerRequestUri(final String dong, final String ho){
    StringBuffer stringBuffer = new StringBuffer("wallpad/");
    stringBuffer.append(dong);
    stringBuffer.append("/");
    stringBuffer.append(ho);
    stringBuffer.append("/1");

    return stringBuffer.toString();
  }

  public static String buildConnectServerQuery(final String siteId, final String wpIp,
      final String accessToken){
    StringBuffer stringBuffer = new StringBuffer("siteId=");
    stringBuffer.append(siteId);
    stringBuffer.append("&version=1&ip=");
    stringBuffer.append(wpIp);
    stringBuffer.append("&id=1&access_token=");
    stringBuffer.append(accessToken);

    return stringBuffer.toString();
  }
}
