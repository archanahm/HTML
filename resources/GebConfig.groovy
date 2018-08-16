/*
 This is the Geb configuration file.
 See: http://www.gebish.org/manual/current/configuration.html
 */


import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Platform
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.Dimension

def deviceSever = "http://10.241.120.115"
def androidServer = deviceServer
def iosServer = deviceServer

waiting { timeout = 2 }

baseUrl = ""
FirefoxProfile profile = new FirefoxProfile();
autoClearCookies = false
environments {

    /********** WEB **********/

    chrome {
        System.setProperty('webdriver.chrome.driver' ,System.getProperty("user.dir") + "\\src\\test\\resources\\chromedriver.exe")


        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
//		options.addArguments("--start-fullscreen");
//		options.addArguments("--kiosk");
        options.addArguments("--no-sandbox")
        driver = { new ChromeDriver(options) }
    }
    chromePrevVer {
        println("Using Previous version of ChromeDriver.exe file")
        System.setProperty('webdriver.chrome.driver' ,System.getProperty("user.dir") + "\\src\\test\\resources\\chromedriver_prev.exe")
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox")
        driver = { new ChromeDriver(options) }
    }
    ie {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\IEDriverServer.exe")
       // System.setProperty("webdriver.ie.driver", "C:\\webdriver\\IEDriverServer.exe") //Jenkins
        driver = { new InternetExplorerDriver(capabilities) }
    }
    ie32 {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\IEDriverServerWin32\\IEDriverServer.exe")
        //System.setProperty("webdriver.ie.driver", "C:\\webdriver\\IEDriverServerWin32\\IEDriverServer.exe")
        driver = { new InternetExplorerDriver(capabilities) }
    }
    firefox {
        driver = { new FirefoxDriver() }
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            driver = { new FirefoxDriver() }
        } else {
            profile.setEnableNativeEvents(true)
            //			driver = { new FirefoxDriver(new FirefoxBinary(new File("/usr/sbin/firefox")), profile) }
            driver = { new FirefoxDriver() }
        }
    }
    macChrome {
        System.setProperty("webdriver.chrome.driver", "/Applications/Google\\ Chrome.app/Contents/MacOS/Google\\ Chrome")
        driver = { new ChromeDriver() }
    }
    iPhoneSIT {
        println("iPhoneSIT!!!!")

        DesiredCapabilities capabilities = new DesiredCapabilities()
        capabilities.setCapability("automationName", "XCUITest")
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS")
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "nova testing’s iPhone")
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari")
        capabilities.setCapability("udid" , "65216daded79f9336cde725b36b2ceab4a60830e")
//        capabilities.setCapability("udid", "9b83eb671cc19da9532d196a6a00dd3d9ac89d3b")
        capabilities.setCapability("app", "com.nova.-SafariLauncher")
        capabilities.setCapability("safariAllowPopups", false)
        capabilities.setCapability("safariIgnoreFraudWarning", true)
        capabilities.setCapability("newCommandTimeout", 30000);
        println(capabilities)
        driver = { new IOSDriver(new URL("http://10.241.120.84:4724/wd/hub"), capabilities) }

    }
    iPadAir2{
        println("iPadAir2!!!!")
        DesiredCapabilities capabilities = new DesiredCapabilities()
        capabilities.setCapability("automationName", "XCUITest")
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS")
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nova iPad Air2")
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari")
        capabilities.setCapability("udid", "bd3db5bb1dd830c2bbecd3e5106db8fde061eef1")
        capabilities.setCapability("app", "com.nova.-SafariLauncher")
        capabilities.setCapability("safariAllowPopups", false)
        capabilities.setCapability("safariIgnoreFraudWarning", true)
        capabilities.setCapability("newCommandTimeout", 30000);
        println(capabilities)
       driver = { new IOSDriver(new URL("http://10.241.120.234:4723/wd/hub"), capabilities) }
    }
    galaxyNote8{
        println("galaxyNote8!!!!")
        DesiredCapabilities capabilities = new DesiredCapabilities()
        capabilities.setCapability("Version", "4.4")
       capabilities.setCapability("udid" , "41037d090ccf30f7")
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Kuoni(GT-N5110)")
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome")
    //  driver = { new AndroidDriver(new URL("http://10.241.120.51:4723/wd/hub"), capabilities) }
    //  driver = { new AndroidDriver(new URL("http://0.0.0.0:4730/wd/hub"), capabilities) }
     driver = { new AndroidDriver(new URL("http://10.241.120.234:4730/wd/hub"), capabilities) }
    }

    phantomjs {
        println("phantomjs!!!!")
        def phantomJsDriver;
        try {
            System.setProperty('phantomjs.binary.path', System.getProperty("user.dir") + "/src/test/resources/driver/phantomjs$archiveExtension")
            Capabilities caps = DesiredCapabilities.phantomjs()
            phantomJsDriver = new PhantomJSDriver(PhantomJSDriverService.createDefaultService(caps), caps)
            phantomJsDriver.manage().window().setSize(new Dimension(1028, 768))
        } catch (Exception e) {
            e.printStackTrace()
        }
        driver = {phantomJsDriver}
    }
}
