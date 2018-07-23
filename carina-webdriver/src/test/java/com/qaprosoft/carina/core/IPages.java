package com.qaprosoft.carina.core;

import com.qaprosoft.carina.core.ws.JettyServer;

public interface IPages
{

	String WELCOME_PAGE = "index.html";

	default String buildPath(JettyServer jettyServer, String relativePath)
	{
		return jettyServer.buildURL(relativePath);
	}
}
