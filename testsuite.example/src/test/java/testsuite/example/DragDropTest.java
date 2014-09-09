package testsuite.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

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

/** Test drag and drop an element **/
public class DragDropTest {
	AppiumDriver wd;
	TouchAction touchAction;
	WebElement element;
	String totalDevice;
	String numDeviceActived;

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
		System.out.println("TEST DRAG AND DROP DEVICE:");
	}

	@Test
	public void dragDropTest() {
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
		WebElement button1 = wd.findElement(By.name("Continue"));

		WebElement button2 = wd.findElement(By.name("Dashboard"));
		testSizeButton(button1, button2);
		testElementLocation(button1, 2307, 36);
		assertTrue(button1.isDisplayed());
		assertTrue(button1.isEnabled());
		assertFalse(button1.isSelected());
		Assert.assertEquals("true", button1.getAttribute("focusable"));
		button1.click();
		wd.findElement(By.name("Continue")).click();
		e = wd.findElement(By
				.id("com.enclaveit.mgecontroller.activities:id/mainLayout"));

		// Test to zoom--NOT OK NO RESULT
		touchAction.tap(e, 1000, 500);
		MultiTouchAction mutiTouch = new MultiTouchAction(wd);
		mutiTouch.add(touchAction);
		mutiTouch.add(touchAction);
		wd.performMultiTouchAction(mutiTouch);

		// Test drag and drop element
		e = wd.findElement(By.xpath("//android.widget.Button[26]"));
		Point pointE = e.getLocation();
		touchAction.press(e).waitAction(2000).moveTo(1100, 400).release()
				.perform();
		wd.findElement(By.name("Done")).click();
		for (int i = 1; i < 9; i++) {
			wd.findElement(By.name("8")).click();
		}
		wd.findElement(By.name("Next")).click();
		wd.findElement(By.name("Garage")).click();
		System.out.println("Before drag-drop: " + pointE);
		System.out.println("After drag-drop: " + e.getLocation());
		Assert.assertNotSame(pointE, e.getLocation());
	}

	@After
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
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

}
