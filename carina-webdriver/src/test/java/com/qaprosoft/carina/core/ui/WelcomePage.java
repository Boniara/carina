package com.qaprosoft.carina.core.ui;

import com.qaprosoft.carina.core.PagePath;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import com.qaprosoft.carina.core.ui.table.Table;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class WelcomePage extends AbstractPage
{

	@FindBy(className = "title")
	private ExtendedWebElement title;

	@FindBy(name = "submitForm")
	private FormElement formElement;

	@FindBy(css = "div.test")
	private SearchBlock searchBlock;

	@FindBy(tagName = "table")
	private Table table;

	@FindBy(xpath = "//*[@id = 'status']/div")
	private ExtendedWebElement status;

	public WelcomePage(WebDriver driver)
	{
		super(driver);
		setPageAbsoluteURL(PagePath.welcomePage);
	}

	public ExtendedWebElement getTitle()
	{
		return title;
	}

	public FormElement getFormElement()
	{
		return formElement;
	}

	public SearchBlock getSearchBlock()
	{
		return searchBlock;
	}

	public Table getTable()
	{
		return table;
	}

	public ExtendedWebElement getStatus()
	{
		return status;
	}
}
