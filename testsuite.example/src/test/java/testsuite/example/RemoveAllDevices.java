package testsuite.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RemoveAllDevices {
	AppiumDriver wd;
	TouchAction touchAction;
	WebElement element;
	String totalDevice;

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
		System.out.println("TEST REMOVE DEVICE:");
	}

	@After
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void removeAllDevices() {
		WebElement ele = wd.findElement(By.name("Allow"));
		Assert.assertFalse(ele.isSelected());
		WebDriverWait wait = new WebDriverWait(wd, 4);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Allow")));
		wd.findElement(By.name("Allow")).click();
		// wait.until(ExpectedConditions.presenceOfElementLocated(By
		// .name("Dismiss")));
		// wd.findElement(By.xpath("//android.widget.Button[1]")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.name("Get Started")));
		WebElement e = wd.findElement(By.name("Get Started"));
		Assert.assertEquals("Get Started", e.getText());
		e.click();
		wd.findElement(By.name("Desc")).click();
		wd.findElement(By.name("Continue")).click();
		wd.findElement(By.name("Continue")).click();
		wd.findElement(By.name("Continue")).click();
		wd.findElement(By.name("Continue")).click();
		getAllDevicesAndRemoveThem();
	}

	public void getAllDevicesAndRemoveThem() {
		WebElement layout = wd.findElement(By
				.id("com.enclaveit.mgecontroller.activities:id/FrameLayout1"));
		System.out.print(layout.getTagName());
		List<WebElement> listDevice = layout.findElements(By
				.className("android.widget.Button"));
		for (WebElement e : listDevice) {
			e.click();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String text = wd.getPageSource();
			// System.out.println(text);
			if (wd.getPageSource().contains(
					"com.enclaveit.mgecontroller.activities:id/btnGangSwitch")) {
				wd.findElement(
						By.id("com.enclaveit.mgecontroller.activities:id/btnGangSwitch"))
						.click();
			} else {
				if (!text
						.contains("com.enclaveit.mgecontroller.activities:id/btnGangSwitch")
						&& !text.contains("com.enclaveit.mgecontroller.activities:id/btnBin")) {
					continue;
				}
			}
			wd.findElement(
					By.id("com.enclaveit.mgecontroller.activities:id/btnBin"))
					.click();
			wd.findElement(By.name("Remove")).click();

		}
		getTotalDevicesAfterAction();
	}

	public void getTotalDevicesAfterAction() {
		element = wd
				.findElement(By
						.id("com.enclaveit.mgecontroller.activities:id/txtTotalDeviceFloorPlan"));

		String numDeviceActived = StringUtils.substringAfter(element.getText(),
				"(");
		numDeviceActived = StringUtils.substringBefore(numDeviceActived, "/");
		System.out.println("Number of activated devices: " + numDeviceActived);

		String totalDeviceAfter = StringUtils.substringBefore(
				element.getText(), ")");
		totalDeviceAfter = StringUtils.substringAfter(totalDeviceAfter, "/");
		System.out.println("After removing, total devices are: "
				+ totalDeviceAfter);
		assertEquals("0", totalDeviceAfter);
	}

	private boolean existsElement(String id) {
		try {
			wd.findElement(By.id(id));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

}
