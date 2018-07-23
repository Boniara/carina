package com.qaprosoft.carina.core.ws;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

import static com.qaprosoft.carina.core.ws.utils.FileUtils.buildResourcesPath;

public class JettyServer
{

	protected static final Logger LOGGER = Logger.getLogger(JettyServer.class);

	private final String host;
	private final int port;
	private final Server server;

	private ContextHandlerCollection handlers;

	private static final String WEB_CONTEXT_PATH = "carina";
	private static final String WELCOME_PAGE = "index.html";

	private static final String URL_TEMPLATE = "http://%s:%s/%s";
	private static final String RELATIVE_PATH_TEMPLATE = "%s/%s";
	private static final String WEB_RESOURCES_PATH = buildResourcesPath("web").toAbsolutePath().toString();

	protected JettyServer(String host, int port)
	{
		this.host = host;
		this.port = port;
		this.server = new Server();

		this.handlers = new ContextHandlerCollection();

		ServletContextHandler defaultContext = addHandler(WEB_CONTEXT_PATH, WEB_RESOURCES_PATH);

		defaultContext.setInitParameter("hostname", host);
		defaultContext.setInitParameter("port", String.valueOf(port));

		server.setHandler(handlers);

		//  Add servlets
		//addServlet(defaultContext, "servletURL", ServletClass.class);
	}

	public void start()
	{
		HttpConfiguration httpConfig = new HttpConfiguration();

		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
		http.setPort(port);
		http.setIdleTimeout(20000);

		HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
		httpsConfig.addCustomizer(new SecureRequestCustomizer());

		server.setConnectors(new Connector[]{http});

		try {
			server.start();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private ServletContextHandler addHandler(String contextPath, String resourcesPath) {

		contextPath = contextPath.startsWith("/") ? contextPath : String.format("/%s", contextPath);
		ServletContextHandler context = new ServletContextHandler();
		ResourceHandler staticResource = new ResourceHandler();

		staticResource.setDirectoriesListed(true);
		staticResource.setWelcomeFiles(new String[] {WELCOME_PAGE});
		staticResource.setResourceBase(resourcesPath);

		MimeTypes mimeTypes = new MimeTypes();
		mimeTypes.addMimeMapping("appcache", "text/cache-manifest");
		staticResource.setMimeTypes(mimeTypes);

		context.setContextPath(contextPath);
		context.setAliasChecks(ImmutableList.of(new ContextHandler.ApproveAliases(), new AllowSymLinkAliasChecker()));

		context.setHandler(new HandlerList(staticResource, context.getHandler()));
		this.handlers.addHandler(context);

		return context;
	}

	private void addServlet(ServletContextHandler context, String url, Class<? extends Servlet> servletClass) {
		context.addServlet(new ServletHolder(servletClass), url);
	}

	public String buildURL(String relativeURL) {
		return String.format(URL_TEMPLATE, host, port, buildPath(relativeURL));
	}

	private String buildPath(String relativeURL) {
		return String.format(RELATIVE_PATH_TEMPLATE, WEB_CONTEXT_PATH, relativeURL);
	}
}
