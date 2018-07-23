package com.qaprosoft.carina.core.tests;

import com.qaprosoft.carina.core.PagePath;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.webdriver.DriverPool;
import com.qaprosoft.carina.core.utils.Config;
import com.qaprosoft.carina.core.ws.CarinaTestJettyServer;
import com.qaprosoft.carina.core.ws.JettyServer;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;

public abstract class AbstractTest
{

	protected static final Logger LOGGER = Logger.getLogger(AbstractTest.class);

	protected static final Double INFELICITY_SECONDS = 0.02;
	protected static final Long DEFAULT_WEB_TIMEOUT = 2L;

	private static final String SELENIUM_HOST = Config.get("selenium_host");

	private WebDriver driver;
	protected PagePath pages;
	private JettyServer jettyServer;

	public AbstractTest()
	{
		this.jettyServer = new CarinaTestJettyServer("localhost", 8080);
		this.jettyServer.start();
		this.pages = new PagePath(this.jettyServer);
	}

	@BeforeTest
	public void executeBeforeTestMethod() throws MalformedURLException
	{
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		driver = DriverPool.getDriver("default", dc, SELENIUM_HOST);
	}

	@AfterTest
	public void executeAfterTestMethod()
	{
		if(this.driver != null)
		{
			this.driver.quit();
		}
	}

	protected WebDriver getDriver()
	{
		return this.driver;
	}

	protected void pause(double seconds)
	{
		try
		{
			Thread.sleep(Math.round(seconds * 1000));
		} catch (InterruptedException e)
		{
			LOGGER.error(e);
		}
	}
}
