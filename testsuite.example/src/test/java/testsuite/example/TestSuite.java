package testsuite.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddDeviceTest.class /**
 * DragDropTest.class, ,
 * AddDeviceWithAgreedExistSerialID.class,
 * AddDeviceWithDeniedExistSerialID.class, RemoveDeviceTest.class,
 * RemoveAllDevices.class
 **/
})
public class TestSuite {

}
