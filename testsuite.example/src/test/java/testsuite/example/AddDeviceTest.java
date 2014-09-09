package testsuite.example;

import static org.junit.Assert.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import au.com.bytecode.opencsv.CSVReader;
import testsuite.datadriven.DataDrivenCSVInput;

/** Test Add a new device **/

public class AddDeviceTest {
	DataDrivenCSVInput dataInput;
	AppiumDriver wd;
	TouchAction touchAction;
	WebElement element;
	String totalDevice;
	String numDeviceActived;
	List<String[]> nextLine;

	@Before
	public void setUp() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appium-version", "1.0");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "4.4");
		capabilities
				.setCapability(
						"app",
						"/Users/enclaveit/Movies/workspace/testsuite.example/MGEController_20140615.apk");
		try {
			wd = new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"),
					capabilities);
			touchAction = new TouchAction(wd);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		System.out.println("TEST ADD DEVICE:");
		dataInput = new DataDrivenCSVInput();
		nextLine = dataInput
				.runMasterFile(
						"Data_master.csv",
						"testsuite.example.AddDeviceTest","Data_input.csv");
	}

	@After
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void addDevice() {
		WebDriverWait wait = new WebDriverWait(wd, 4);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Allow")));
		wd.findElement(By.name("Allow")).click();
		// wait.until(ExpectedConditions.presenceOfElementLocated(By
		// .name("Dismiss")));
		// wd.findElement(By.xpath("//android.widget.Button[1]")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.name("Get Started")));
		WebElement e = wd.findElement(By.name("Get Started"));
		e.click();
		wd.findElement(By.name("Desc")).click();
		wd.findElement(By.name("Continue")).click();
		wd.findElement(By.name("Continue")).click();
		WebElement button1 = wd.findElement(By.name("Continue"));
		button1.click();
		wd.findElement(By.name("Continue")).click();
		e = wd.findElement(By
				.id("com.enclaveit.mgecontroller.activities:id/mainLayout"));

		// Add a new device
		getTotalDeviceBeforeAdd();
		wd.findElement(By.name("Add Device")).click();
		wd.scrollTo("Light/Lighting Control");
		wd.findElement(By.name("Light/Lighting Control")).click();
		wd.findElement(By.name("Done")).click();

		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name(nextLine.get(0)[1])).click();
		}
		wd.findElement(By.name("Next")).click();
		// wd.findElement(By.name("Done")).click();
		wd.findElement(By.name("Garage")).click();
		getTotalDeviceAfterAdd();
	}

	/** Get the number of devices before adding a new one **/
	public void getTotalDeviceBeforeAdd() {
		element = wd
				.findElement(By
						.id("com.enclaveit.mgecontroller.activities:id/txtTotalDeviceFloorPlan"));
		numDeviceActived = StringUtils.substringBefore(element.getText(), "/");
		numDeviceActived = StringUtils.substringAfter(numDeviceActived, "(");
		System.out
				.println("Activated devices before adding a duplicate device: "
						+ numDeviceActived);

		totalDevice = StringUtils.substringBefore(element.getText(), ")");
		totalDevice = StringUtils.substringAfter(totalDevice, "/");
		System.out.println("Total devices before adding a duplicate device: "
				+ totalDevice);
	}

	/** Get the number of devices after adding a new one **/
	public void getTotalDeviceAfterAdd() {
		element = wd
				.findElement(By
						.id("com.enclaveit.mgecontroller.activities:id/txtTotalDeviceFloorPlan"));

		numDeviceActived = StringUtils.substringAfter(element.getText(), "(");
		numDeviceActived = StringUtils.substringBefore(numDeviceActived, "/");
		System.out.println("Now, activated devices are: " + numDeviceActived);

		String totalDeviceAfter = StringUtils.substringBefore(
				element.getText(), ")");
		totalDeviceAfter = StringUtils.substringAfter(totalDeviceAfter, "/");
		System.out.println("Total devices after adding a duplicate device: "
				+ totalDeviceAfter);
		assertNotEquals(totalDeviceAfter, totalDevice);
	}

	/** Test expected location of an element **/
	public void testElementLocation(WebElement e, int locationX, int locationY) {
		Point elementPos = e.getLocation();
		// System.out.print(elementPos);
		assertEquals(locationX, elementPos.getX());
		assertEquals(locationY, elementPos.getY());
	}

	/** Test size of two button **/
	private void testSizeButton(WebElement element1, WebElement element2) {

		Dimension size1 = element1.getSize();
		Dimension size2 = element2.getSize();
		assertEquals(size1.getHeight(), size2.getHeight());
		Assert.assertNotSame(size1.getWidth(), size2.getWidth());

	}

	/** Test swipe **/
	public void swipe(Double startX, Double startY, Double endX, Double endY,
			Double duration) {
		JavascriptExecutor js = (JavascriptExecutor) wd;
		HashMap<String, Double> swipeObject = new HashMap<String, Double>();
		swipeObject.put("startX", startX);
		swipeObject.put("startY", startY);
		swipeObject.put("endX", endX);
		swipeObject.put("endY", endY);
		swipeObject.put("duration", duration);
		js.executeScript("mobile: swipe", swipeObject);
	}

	/** Test double click **/
	public void doubleClick(WebElement e) {
		HashMap<String, Double> tap = new HashMap<String, Double>();
		tap.put("tapCount", new Double(2));
		tap.put("touchCount", new Double(1));
		tap.put("duration", new Double(0.5));
		tap.put("x", new Double(e.getLocation().getX()));
		tap.put("y", new Double(e.getLocation().getY()));
		wd.executeScript("mobile: tap", tap);
	}
}
