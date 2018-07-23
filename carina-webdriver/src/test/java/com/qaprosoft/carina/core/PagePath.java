package com.qaprosoft.carina.core;

import com.qaprosoft.carina.core.ws.JettyServer;

public class PagePath implements IPages
{

  public static String welcomePage;

  public PagePath(JettyServer appServer) {
    welcomePage = buildPath(appServer, WELCOME_PAGE);
  }

}
