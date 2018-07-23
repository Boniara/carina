package com.qaprosoft.carina.core.ui.table;

import com.qaprosoft.carina.core.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Table  extends AbstractUIObject
{

	@FindBy(css = ".test")
	private List<TableRow> tableRows;

	public Table(WebDriver driver, SearchContext searchContext)
	{
		super(driver, searchContext);
	}

	public List<TableRow> getTableRows()
	{
		return tableRows;
	}
}
