package testsuite.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

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
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddDeviceWithDeniedExistSerialID {
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
		System.out
				.println("TEST ADD A NEW DUPLICATED DEVICE'S ID THEN DENY TO ADD:");
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

		WebElement layout = wd.findElement(By
				.id("com.enclaveit.mgecontroller.activities:id/FrameLayout1"));
		listBefore = layout.findElements(By.className("android.widget.Button"));
		// Add a new device
		wd.findElement(By.name("Add Device")).click();
		wd.scrollTo("Light/Lighting Control");
		wd.findElement(By.name("Light/Lighting Control")).click();
		wd.findElement(By.name("Done")).click();
		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name("1")).click();
		}
		wd.findElement(By.name("Next")).click();
		// wd.findElement(By.name("Done")).click();
		wd.findElement(By.name("Garage")).click();
		getTotalDeviceBeforeAdd();
		// 2nd
		wd.findElement(By.name("Add Device")).click();
		wd.scrollTo("Light/Lighting Control");
		wd.findElement(By.name("Light/Lighting Control")).click();
		wd.findElement(By.name("Done")).click();
		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name("1")).click();
		}
		wd.findElement(By.name("Next")).click();
		// wd.findElement(By.name("Done")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.name("Cancel")));
		for (int i = 0; i < 3; i++) {
			wd.findElement(By.name("Cancel")).click();
		}
		// wd.findElement(By.name("Garage")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		listAfter = layout.findElements(By.className("android.widget.Button"));
		getTotalDeviceAfterAdd();
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
		Assert.assertEquals(totalDevice, totalDeviceAfter);
	}

	public void findNewestDevice() {
		HashMap<Point, WebElement> listElement = new HashMap<Point, WebElement>();

		System.out.print("System before ...\t");
		System.out.println(listBefore.size());
		// Print list before's element
		for (WebElement element : listBefore) {
			listElement.put(element.getLocation(), element);
			System.out.print(element.getLocation() + "\t");
		}
		System.out.println();
		// Print list after's element
		System.out.print("System After ...\t");
		System.out.println(listAfter.size());
		for (WebElement element : listAfter) {
			System.out.print(element.getLocation() + "\t");
		}
		// Check the same size and each element
		if (listAfter.size() - listBefore.size() == 0) {
			System.out.println("There are no duplicated element.");
			Assert.assertSame(listBefore.size(), listAfter.size());
			for (int i = 0; i < listAfter.size(); i++) {
				Assert.assertEquals(listBefore.get(i), listAfter.get(i));
			}
		}

		// Check if size are not same
		if (listAfter.size() - listBefore.size() > 0) {
			System.out.println("\n Inserted elements: \n"
					+ (listAfter.size() - listBefore.size()));
			for (WebElement elementAfter : listAfter) {
				if (!listElement.containsKey(elementAfter.getLocation())) {
					System.out.print(elementAfter.getLocation());
				}
			}
			Assert.assertEquals(1, listAfter.size() - listBefore.size());
		}
	}

}
