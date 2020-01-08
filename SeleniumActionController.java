package library.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import librery.Action;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.rational.test.ft.script.RationalTestScript;

public class SeleniumActionController extends RationalTestScript {

	private static JavascriptExecutor js = null;

	private static String message;
	public static String firstPage;

	private static String lastStyleObtained;
	private static WebDriverWait wait = new WebDriverWait(
			SeleniumUtility.getDriver(), Timeout.get10());

	public static boolean acceptAlert() {		

		boolean closed = false;

		try {
			if (itIsShowingAlert()) {
				wait.until(ExpectedConditions.alertIsPresent());

				SeleniumUtility.getDriver().switchTo().alert().accept();
				closed = true;
			}
		} catch (NoAlertPresentException Ex) {
			closed = false;
		}

		return closed;
	}

	public static WebElement searchObject(By elementExpression) {
		return searchObject(elementExpression, Timeout.get5());
	}	
	
	public static WebElement searchObject(By elementExpression, long seconds) {
		return wait.withTimeout(seconds, TimeUnit.SECONDS)
				.until(ExpectedConditions
						.presenceOfElementLocated(elementExpression));
	}

	public static List<WebElement> searchObjectsWithAttempts(
			By elementExpression, long seconds) {

		return wait.withTimeout(seconds, TimeUnit.SECONDS).until(
				ExpectedConditions
						.presenceOfAllElementsLocatedBy(elementExpression));
	}
	
	
	public static boolean changeWindow(int windowsQuantity) {

		wait.until(ExpectedConditions.numberOfWindowsToBe(windowsQuantity));
		firstPage = SeleniumUtility.getDriver().getWindowHandle();

		for (String window : SeleniumUtility.getDriver().getWindowHandles()) {

			SeleniumUtility.getDriver().switchTo().window(window);

			Set<String> winHandles = SeleniumUtility.getDriver()
					.getWindowHandles();
			// Loop through all handles
			for (String handle : winHandles) {
				if (!handle.equals(firstPage)) {
					SeleniumUtility.getDriver().switchTo().window(handle);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {						
						e.printStackTrace();
					}
					System.out.println("Title of the new window: "
							+ SeleniumUtility.getDriver().getTitle());
				}
			}
		}
		return SeleniumUtility.getDriver().getWindowHandle() != firstPage;
	}
	
	public static boolean changeWindow(String windowTitle,
			WebElement element, int windowsQuantity) {

		wait.until(ExpectedConditions.numberOfWindowsToBe(windowsQuantity));
		firstPage = SeleniumUtility.getDriver().getWindowHandle();

		for (String window : SeleniumUtility.getDriver().getWindowHandles()) {

			SeleniumUtility.getDriver().switchTo().window(window);

			if (!firstPage.equals(SeleniumUtility.getDriver()
					.getWindowHandle())) {

				wait.until(ExpectedConditions.titleContains(windowTitle));

				if (SeleniumUtility.getDriver().getTitle()
						.contains(windowTitle)) {

					wait.until(ExpectedConditions.visibilityOf(element));
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static void closeSecondaryWindow() {

		SeleniumUtility.getDriver().close();
		System.out.println("seleniumUtility focus value: "
				+ firstPage);
		SeleniumUtility.getDriver().switchTo().window(firstPage);
	}
	
	
	public static boolean consultDataTableByContains(By tableExpression,
			String... data) {

		boolean result = false;

		if (tableExpression == null || data.length == 0)
			return result;

		List<WebElement> rows = SeleniumUtilitys.getDriver().findElements(
				tableExpression);
		int validations = data.length;

		for (WebElement row : rows) {

			List<WebElement> columns = row.findElements(By.tagName("td"));

			int col_count = columns.size();

			for (int columnIndex = 0; columnIndex < col_count; columnIndex++) {

				String columnValue = columns.get(columnIndex).getText();

				for (int i = 0; i < data.length; i++) {
					if (columnValue.trim().toLowerCase()
							.contains(data[i].trim().toLowerCase())) {
						validations--;
						System.out.println("match column:" + columnIndex
								+ " = " + columnValue);
						break;
					}
				}
			}

			if (validations == 0) {
				markField(row);
				result = true;
				hoover(row);
				break;
			} else {
				validations = data.length;
			}

		}

		return result;
	}
	
	

	public static boolean consultDataTableByContains(String xpathTabla,
			String... data) {

		boolean result = false;

		if (xpathTable.isEmpty() || data.length == 0)
			return result;

		int validation = data.length;
		int rowSize = SeleniumUtility.getDriver()
				.findElements(By.xpath(xpathTable + "/tbody/tr")).size();

		int columnSize = 0;
		int startRowIndex = 1;

		if (rowSize > 1) {
			columnSize = SeleniumUtility.getDriver()
					.findElements(By.xpath(xpathTable + "/tbody/tr[2]/td"))
					.size();
			startRowIndex = 2;
		} else {
			columnSize = SeleniumUtility.getDriver()
					.findElements(By.xpath(xpathTable + "/tbody/tr[1]/td"))
					.size();
		}

		for (int rowIndex = startRowIndex; rowIndex <= rowSize; rowIndex++) {

			for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {

				String columnValue = SeleniumUtility
						.getDriver()
						.findElement(
								By.xpath(xpathTable + "/tbody/tr[" + rowIndex
										+ "]/td[" + columnIndex + "]"))
						.getText();

				for (int i = 0; i < data.length; i++) {
					if (columnValue.toLowerCase().trim()
							.contains(datos[i].toLowerCase())) {
						validation--;
						System.out.println("match row:" + rowIndex + " = "
								+ columnValue);
						break;
					}
				}
			}

			if (validation == 0) {
				markField(SeleniumUtility.getDriver().findElement(
						By.xpath(xpathTable + "/tbody/tr[" + rowIndex + "]")));
				result = true;
				hoover(SeleniumUtility.getDriver().findElement(
						By.xpath(xpathTable + "/tbody/tr[" + rowIndex + "]")));
				break;
			} else {
				validation = data.length;
			}
		}

		return result;
	}
	
	
	public static boolean consultDataTableByEqual(String xpathTable,
			String... data) {

		boolean result = false;

		if (xpathTable.isEmpty() || data.length == 0)
			return result;

		// List<String> datosTemp = new ArrayList<String>();
		// dataTemp.addAll(data);
		int validation = data.length;
		int rowSize = SeleniumUtility.getDriver()
				.findElements(By.xpath(xpathTable + "/tbody/tr")).size();

		int columnSize = SeleniumUtility.getDriver()
				.findElements(By.xpath(xpathTable + "/tbody/tr[2]/td")).size();

		for (int rowIndex = 2; rowIndex <= rowSize; rowIndex++) {

			for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {

				String columnValue = SeleniumUtility
						.getDriver()
						.findElement(
								By.xpath(xpathTable + "/tbody/tr[" + rowIndex
										+ "]/td[" + columnIndex + "]"))
						.getText();

				for (int i = 0; i < data.length; i++) {
					if (columnValue.equalsIgnoreCase(data[i])) {
						validation--;
						System.out.println("match row:" + rowIndex + " = "
								+ columnValue);
						break;
					}
				}
			}

			if (validation == 0) {
				markField(SeleniumUtility.getDriver().findElement(
						By.xpath(xpathTable + "/tbody/tr[" + rowIndex + "]")));
				result = true;
				hoover(SeleniumUtility.getDriver().findElement(
						By.xpath(xpathTable + "/tbody/tr[" + rowIndex + "]")));
				break;
			} else {
				validation = data.length;
			}
		}

		return result;
	}
	
	
	public static boolean consultInTable(String idRegistry, String xpath) {

		boolean result = false;
		List<WebElement> registry = SeleniumUtility.getDriver()
				.findElements(By.xpath(xpath));
		js = (JavascriptExecutor) SeleniumUtility.getDriver();

		try {
			for (WebElement row : registry) {
				if (row.getText().contains(idRegistry)) {
					getAction().moveToElement(row).build().perform();
					js.executeScript(
							"arguments[0].style.backgroundColor='yellow';"
									+ "arguments[0].style.color='red';",
							row.findElement(By.xpath("//*[contains(text(), '"
									+ idRegistry + "')]")));
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
