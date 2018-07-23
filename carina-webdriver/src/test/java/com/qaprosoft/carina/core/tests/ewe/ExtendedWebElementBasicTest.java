package com.qaprosoft.carina.core.tests.ewe;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.tests.AbstractTest;
import com.qaprosoft.carina.core.ui.WelcomePage;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExtendedWebElementBasicTest extends AbstractTest
{

	private WelcomePage welcomePage;

	@BeforeMethod
	public void setup()
	{
		welcomePage = new WelcomePage(getDriver());
		welcomePage.open();
		getDriver().manage().addCookie(new Cookie("env", "testing"));
		getDriver().navigate().refresh();
	}

	@DataProvider(name = "getInstance")
	public Object[][] getInstanceParameters()
	{
		return new Supplier[][]
				{
						{
							() -> new ExtendedWebElement(getDriver().findElement(By.className("title")), "title", By.className("title"))
						},
						{
							() -> new ExtendedWebElement(getDriver().findElement(By.className("title")), "title")
						}
				};
	}

	@Test(dataProvider = "getInstance")
	public void verifyInstanceTest(final Supplier<ExtendedWebElement> extendedWebElementSupplier)
	{
		ExtendedWebElement extendedWebElement = extendedWebElementSupplier.get();
		Assert.assertNotNull(extendedWebElement, "ExtendedWebElement instance is null");
		Assert.assertNotNull(welcomePage.getTitle(), "ExtendedWebElement instance is null");
		String titleElementText = extendedWebElement.getText();
		String welcomePageTitleText = welcomePage.getTitle().getText();
		Assert.assertEquals(titleElementText, "Welcome to carina framework testing pages", "ExtendedWebElement has not correct ui element");
		Assert.assertEquals(welcomePageTitleText, "Welcome to carina framework testing pages", "ExtendedWebElement has not correct ui element");
	}

	@DataProvider(name = "getGetters")
	public Object[][] getGetters()
	{
		return new Object[][]
				{
						{
								(Function<ExtendedWebElement, Object>) ExtendedWebElement::getBy, "getBy"
						},
						{
								(Function<ExtendedWebElement, Object>) ExtendedWebElement::getElement, "getElement"
						},
						{
								(Function<ExtendedWebElement, Object>) ExtendedWebElement::getName, "getName"
						},
						{
								(Function<ExtendedWebElement, Object>) ExtendedWebElement::toString, "toString"
						},
						{
								(Function<ExtendedWebElement, Object>) ExtendedWebElement::getNameWithLocator, "getNameWithLocator"
						}
				};
	}


	@Test(dataProvider = "getGetters")
	public void verifyGettersTest(final Function<ExtendedWebElement, Object> getter, String name)
	{
		final By targetBy = By.id("submitButton");
		final String targetName = "submitButton";
		final ExtendedWebElement target = new ExtendedWebElement(getDriver().findElement(targetBy), targetName, targetBy);
		Object result = getter.apply(target);
		String message = String.format("%s function doesn't work correctly", name);
		switch(name)
		{
			case "getBy":
				By locator = (By) result;
				Assert.assertEquals(locator, targetBy, message);
				break;
			case "getElement":
				WebElement element = (WebElement) result;
				element.click();
				Assert.assertTrue(welcomePage.getStatus().clickIfPresent(2), message);
				break;
			case "getName":
			case "toString":
				String elementName = (String) result;
				Assert.assertEquals(elementName , targetName, message);
				break;
			case "getNameWithLocator":
				String elementNameWithLocator = (String) result;
				Assert.assertEquals(elementNameWithLocator , String.format("%s (%s)",targetName, targetBy), message);
				break;
			default:
				break;
		}
	}

	@DataProvider(name = "getSetters")
	public Object[][] getSetters()
	{
		final By targetBy = By.id("submitButton");
		final String targetName = "submitButton";
		return new Object[][]
				{
						{
								(Consumer<ExtendedWebElement>) ewe -> ewe.setBy(targetBy), "setBy"
						},
						{
								(Consumer<ExtendedWebElement>) ewe -> ewe.setElement(new ExtendedWebElement(getDriver().findElement(targetBy), targetName, targetBy).getElement()), "setElement"
						},
						{
								(Consumer<ExtendedWebElement>) ewe -> ewe.setName(targetName), "setName"
						}
				};
	}

	@Test(dataProvider = "getSetters")
	public void verifySettersTest(final Consumer<Object> setter, String name)
	{
		By targetBy = By.id("usernameInput");
		String targetName = "usernameInput";
		String message = String.format("%s function doesn't work correctly", name);
		ExtendedWebElement target = null;
		WebElement element = null;
		switch(name)
		{
			case "setElement":
				By testBy = By.id("submitButton");
				element = getDriver().findElement(testBy);
				break;
			case "setBy":
				element = getDriver().findElement(targetBy);
				targetBy = By.id("submitButton");
				break;
			case "setName":
				targetName = "submitButton";
				targetBy = By.id("submitButton");
				element = getDriver().findElement(targetBy);
				break;
			default:
				break;
		}
		target = new ExtendedWebElement(element, targetName, targetBy);
		setter.accept(target);
		Assert.assertEquals(target.getName(), targetName, "Incorrect name from getter");
		target.click();
		Assert.assertTrue(welcomePage.getStatus().isPresent(2), message);
	}

	@DataProvider(name = "getIsPresentParameters")
	public Object[][] getIsPresentParameters()
	{
		return new Function[][]
				{
						{
							(Function<ExtendedWebElement, Boolean>) ewe -> ewe.isPresent(DEFAULT_WEB_TIMEOUT)
						},
						{
							(Function<ExtendedWebElement, Boolean>) ewe -> ewe.isPresent(ewe.getBy(), DEFAULT_WEB_TIMEOUT)
						}
				};
	}

	@Test(dataProvider = "getIsPresentParameters")
	public void verifyIsPresentTest(final Function<ExtendedWebElement, Boolean> func) throws ExecutionException, InterruptedException {
		Assert.assertTrue(CollectionUtils.isEmpty(welcomePage.getTable().getTableRows()), "Defaultdata is not correct");
		Assert.assertFalse(func.apply(welcomePage.getStatus()), "Is present don't work correctly");
		WebElement submitButton = welcomePage.getFormElement().getSubmitButton().getElement();
		CompletableFuture<Boolean> isPresentFuture = CompletableFuture.supplyAsync(() -> func.apply(welcomePage.getStatus()));
		pause(DEFAULT_WEB_TIMEOUT - INFELICITY_SECONDS);
		submitButton.click();
		Assert.assertTrue(isPresentFuture.get(), "Is present don't work correctly");
		pause(DEFAULT_WEB_TIMEOUT * 2);
		Assert.assertFalse(func.apply(welcomePage.getStatus()), "Is present don't work correctly");
	}

	@Test
	public void verifyGetElementTest()
	{
		WebElement submitButton = welcomePage.getFormElement().getSubmitButton().getElement();
		Assert.assertNotNull(submitButton, "Cannot find web element");
		submitButton.click();
		Assert.assertTrue(welcomePage.getStatus().isElementWithTextPresent("Response data", 2), "Cannot find web element correctly");
		WebElement statusElement = welcomePage.getStatus().getElement();
		Assert.assertEquals(statusElement.getText(), "Response data", "Cannot find web element correctly");
	}
}
