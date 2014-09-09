package testsuite.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import static org.junit.Assert.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Test Add a new device with an existed serial ID **/
public class AddDeviceWithAgreedExistSerialID {
	AppiumDriver wd;
	TouchAction touchAction;
	WebElement element;
	String totalDevice;
	String numDeviceActived;
	List<WebElement> listBefore, listAfter;

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
		System.out.println("TEST ADD A NEW DUPLICATED DEVICE'S ID THEN AGREE TO ADD:");
	}

	@After
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void addNewDeviceWithExistID() {
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
		WebElement layout = wd.findElement(By
				.id("com.enclaveit.mgecontroller.activities:id/FrameLayout1"));
		listBefore = layout.findElements(By.className("android.widget.Button"));
		// 2nd
		wd.findElement(By.name("Add Device")).click();
		wd.scrollTo("Light/Lighting Control");
		wd.findElement(By.name("Light/Lighting Control")).click();
		wd.findElement(By.name("Done")).click();
		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name("1")).click();
		}
		wd.findElement(By.name("Next")).click();
		wd.findElement(By.name("Garage")).click();

		wd.findElement(By.name("Add Device")).click();
		wd.scrollTo("Light/Lighting Control");
		wd.findElement(By.name("Light/Lighting Control")).click();
		wd.findElement(By.name("Done")).click();
		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name("1")).click();
		}
		wd.findElement(By.name("Next")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Continue")));
		wd.findElement(By.name("Continue")).click();
		wd.findElement(By.name("Garage")).click();
		getTotalDeviceAfterAdd();

		listAfter = layout.findElements(By.className("android.widget.Button"));
		findNewestDevice();
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
		
		totalDevice = StringUtils.substringBefore(
				element.getText(), ")");
		totalDevice = StringUtils.substringAfter(totalDevice, "/");
		System.out.println("Total devices after adding a duplicate device: "
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

	public void findNewestDevice() {
		HashMap<Point, WebElement> listElement = new HashMap<Point, WebElement>();

		System.out.print("System before ...\t");
		System.out.println(listBefore.size());

		for (WebElement element : listBefore) {
			listElement.put(element.getLocation(), element);
			System.out.print(element.getLocation() + "\t");
		}
		System.out.println();
		// --------
		System.out.print("System After ...\t");
		System.out.println(listAfter.size());
		for (WebElement element : listAfter) {
			System.out.print(element.getLocation() + "\t");
		}
		// ------
		System.out.println("\n Inserted elements: \n"
				+ (listAfter.size() - listBefore.size()));
		for (WebElement elementAfter : listAfter) {
			if (!listElement.containsKey(elementAfter.getLocation())) {
				System.out.println(elementAfter.getLocation());
			}
		}
		Assert.assertEquals(2, listAfter.size()-listBefore.size());

	}
}
