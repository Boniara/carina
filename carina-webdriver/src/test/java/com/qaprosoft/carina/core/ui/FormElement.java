package com.qaprosoft.carina.core.ui;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class FormElement extends AbstractUIObject
{

	@FindBy(id = "usernameInput")
	private ExtendedWebElement usernameInput;

	@FindBy(name = "email")
	private ExtendedWebElement emailInput;

	@FindBy(xpath = ".//*[@style]")
	private ExtendedWebElement generateCodeButton;

	@FindBy(css = ".input-container input#codeGenerateInput")
	private ExtendedWebElement codeToCopyInput;

	@FindBy(id = "codeInput")
	private ExtendedWebElement codeInput;

	@FindBy(xpath = ".//*[@export = 'item']")
	private ExtendedWebElement itemSelect;

	@FindBy(className = "test")
	private ExtendedWebElement submitButton;

	public FormElement(WebDriver driver, SearchContext searchContext)
	{
		super(driver, searchContext);
	}

	public ExtendedWebElement getUsernameInput()
	{
		return usernameInput;
	}

	public ExtendedWebElement getEmailInput()
	{
		return emailInput;
	}

	public ExtendedWebElement getGenerateCodeButton()
	{
		return generateCodeButton;
	}

	public ExtendedWebElement getCodeToCopyInput()
	{
		return codeToCopyInput;
	}

	public ExtendedWebElement getCodeInput()
	{
		return codeInput;
	}

	public ExtendedWebElement getItemSelect()
	{
		return itemSelect;
	}

	public ExtendedWebElement getSubmitButton()
	{
		return submitButton;
	}

	public void fillForm(String username, String email, String item)
	{
		usernameInput.type(username);
		emailInput.type(email);
		generateCodeButton.hover();
		String code = codeToCopyInput.getAttribute("value");
		codeInput.type(code);
		itemSelect.select(item);
		submitButton.click();
	}
}
