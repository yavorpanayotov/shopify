package com.ingco.shopify

import org.openqa.selenium.By.xpath
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.concurrent.TimeUnit.SECONDS

fun main() {
    val driver = chromeDriver()
    login(driver)

    val productKey = "ID6538"

    driver.findElement(xpath("//a[@href='/admin/products']")).click()
    driver.findElement(xpath(PRODUCTS_SEARCH)).sendKeys(productKey)

    driver.findElement(xpath("//a[contains(@href, '/admin/products') and contains(text(), '$productKey')]")).click()
}

private fun login(driver: ChromeDriver) {
    driver.get(SHOPIFY_COM)
    driver.findElement(xpath(LOGIN)).click()
    driver.findElement(xpath(INPUT_TEXT)).sendKeys("ingco.bg")
    driver.findElement(xpath(SUBMIT_BUTTON)).click()

    driver.findElement(xpath(INPUT_EMAIL)).sendKeys("info@ingco.bg")
    val wait = WebDriverWait(driver, 5)
    wait.until {
        driver.findElement(xpath(SUBMIT_BUTTON)).isEnabled
    }
    driver.findElement(xpath(SUBMIT_BUTTON)).click()

    driver.findElement(xpath("//input[@id='account_password']")).sendKeys("")
    driver.findElement(xpath(SUBMIT_BUTTON)).click()
}

private fun chromeDriver(): ChromeDriver {
    System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver")
    val driver = ChromeDriver()
    driver.manage().timeouts().implicitlyWait(30, SECONDS)
    return driver
}