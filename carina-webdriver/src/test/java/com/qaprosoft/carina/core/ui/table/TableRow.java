package com.qaprosoft.carina.core.ui.table;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class TableRow extends AbstractUIObject
{

	@FindBy(xpath = "./td[@inject='username']")
	private ExtendedWebElement usernameCell;

	@FindBy(xpath = "./td[2]")
	private ExtendedWebElement emailCell;

	@FindBy(xpath = "./td[3]")
	private ExtendedWebElement codeCell;

	@FindBy(xpath = "./td[4]")
	private ExtendedWebElement itemCell;

	@FindBy(xpath = "./td[5]")
	private ExtendedWebElement showLink;

	public TableRow(WebDriver driver, SearchContext searchContext)
	{
		super(driver, searchContext);
	}

	public ExtendedWebElement getUsernameCell()
	{
		return usernameCell;
	}

	public ExtendedWebElement getEmailCell()
	{
		return emailCell;
	}

	public ExtendedWebElement getCodeCell()
	{
		return codeCell;
	}

	public ExtendedWebElement getItemCell()
	{
		return itemCell;
	}

	public ExtendedWebElement getShowLink()
	{
		return showLink;
	}
}
