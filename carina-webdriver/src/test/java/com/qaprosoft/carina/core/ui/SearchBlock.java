package com.qaprosoft.carina.core.ui;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class SearchBlock extends AbstractUIObject
{

	@FindBy(id = "searchInput")
	private ExtendedWebElement searchInput;

	@FindBy(id = "checkbox")
	private ExtendedWebElement showSearchInputCheckbox;

	@FindBy(id = "radioShow")
	private ExtendedWebElement showCheckboxRadiobutton;

	@FindBy(id = "radioHover")
	private ExtendedWebElement hideCheckboxRadiobutton;

	public SearchBlock(WebDriver driver, SearchContext searchContext)
	{
		super(driver, searchContext);
	}

	public ExtendedWebElement getSearchInput()
	{
		return searchInput;
	}

	public ExtendedWebElement getShowSearchInputCheckbox()
	{
		return showSearchInputCheckbox;
	}

	public ExtendedWebElement getShowCheckboxRadiobutton()
	{
		return showCheckboxRadiobutton;
	}

	public ExtendedWebElement getHideCheckboxRadiobutton()
	{
		return hideCheckboxRadiobutton;
	}
}
