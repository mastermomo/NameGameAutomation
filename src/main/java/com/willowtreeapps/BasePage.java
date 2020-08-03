package com.willowtreeapps;

import org.assertj.swing.assertions.Assertions;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created on 5/23/17.
 */
public class BasePage {
    int triesTally = 0; // Keeps track of the internal tries tally.
    int correctTally = 0; // Keeps track of the internal correct tally.
    String correctName; // Keeps track of the correct selection's name.
    List<WebElement> images; // Keeps track of the correct selection's photo.


    public WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage validateAttribute(String css, String attr, String regex) {
        return validateAttribute(By.cssSelector(css), attr, regex);
    }

    public BasePage validateAttribute(By by, String attr, String regex) {
        return validateAttribute(driver.findElement(by), attr, regex);
    }

    public BasePage validateAttribute(WebElement element, String attr, String regex) {
        String actual = null;
        try {
            actual = element.getAttribute(attr);
            if (actual.equals(regex)) {
                return this; // test passes
            }
        } catch (Exception e) {
            Assertions.fail(String.format("Attribute not fount! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s] [Message: %s]",
                    attr,
                    regex,
                    actual,
                    element.toString(),
                    e.getMessage()), e);
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(actual);

        Assertions.assertThat(m.find())
                .withFailMessage("Attribute doesn't match! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s]",
                        attr,
                        regex,
                        actual,
                        element.toString())
                .isTrue();
        return this;
    }

    public BasePage validateText(String css, String text) {
        return validateText(By.cssSelector(css), text);
    }

    /**
     * Validate Text ignores white spaces
     */
    public BasePage validateText(By by, String text) {
        Assertions.assertThat(text).isEqualToIgnoringWhitespace(getText(by));
        return this;
    }

    public String getText(By by) {
        WebElement e = driver.findElement(by);
        return e.getTagName().equalsIgnoreCase("input")
                || e.getTagName().equalsIgnoreCase("select")
                || e.getTagName().equalsIgnoreCase("textarea")
                ? e.getAttribute("value")
                : e.getText();
    }

    public BasePage validatePresent(String css) {
        return validatePresent(By.cssSelector(css));
    }

    public BasePage validatePresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("Element not present: [Element: %s]", by.toString())
                .isGreaterThan(0);
        return this;
    }

    /**
     * This method compares the name present within the question to all images
     * and then chooses the class="name" that is the same as it. The photo
     * is then clicked.
     */
    public void correctPhotoSelection() {
        String correctName = driver.findElement(By.id("name")).getText();

        List<WebElement> images = driver.findElements(By.className("photo"));

        int k = 0;
        while (k < images.size()) {
            WebElement personImage = images.get(k).findElement(By.className("name"));

            if (personImage.getText().equals(correctName)) {
                images.get(k).click();
            }
            else {
            }
            k++;
        }
    }

    /**
     * This method compares the name present within the question to all images
     * and then chooses a class="name" that is NOT the same as it. The photo
     * is then clicked.
     */
    public void notCorrectPhotoSelection() {
        String correctName = driver.findElement(By.id("name")).getText();

        List<WebElement> images = driver.findElements(By.className("photo"));

        int k = 0;
        while (k < images.size()) {
            WebElement personImage = images.get(k).findElement(By.className("name"));

            if (!(images.get(k).equals(personImage))) {
                images.get(k).click();
                break; // Prevents multiple selections when an incorrect option is found.
            }
            else {
            }
            k++;
        }
    }
    /**
     * This method clicks on random images and tallies how many
     * correct and tries have been made. It also ensures that the same images
     * clicked are not then clicked again.
     */
    public void randomTenSelection() {
        String correctName = driver.findElement(By.id("name")).getText();

        List<WebElement> images = driver.findElements(By.className("photo"));

        ArrayList<Integer> imageNumber = new ArrayList<Integer>();
        Random rand = new Random();
        int randomSelection = rand.nextInt(images.size());

        int k = 0;
        while (k < images.size()) {
            WebElement personImage = images.get(randomSelection).findElement(By.className("name"));

            if (!(imageNumber.contains(randomSelection))) {
                imageNumber.add(randomSelection);
                k++;

                if (personImage.getText().equals(correctName)) {
                    images.get(randomSelection).click();
                    correctTally += 1;
                    triesTally += 1;
                    break;
                } else if (!(personImage.getText().equals(correctName))) {
                    images.get(randomSelection).click();
                    triesTally += 1;
                    break;
                } else {
                }
            }
            else {
            }
            rand.nextInt(images.size());
        }
    }

    /**
     * This method clicks on the correct image and ensures that
     * the image and its corresponding name are not on the next
     * set of images.
     */
    public void correctSelectionNotInNextSet() {
        correctPhotoSelection();

        String nextPageName = driver.findElement(By.id("name")).getText();
        List<WebElement> nextPageImages = driver.findElements(By.className("photo"));

        Assert.assertNotSame(correctName, nextPageName);
        Assert.assertNotEquals(images, nextPageImages);


//        String correctName = driver.findElement(By.id("name")).getText();
//
//        List<WebElement> images = driver.findElements(By.className("photo"));
//
//        int k = 0;
//        while (k < images.size()) {
//        WebElement personImage = images.get(k).findElement(By.className("name"));
//
//            if (personImage.getText().equals(correctName)) {
//                images.get(k).click();
//
//                sleep(5000);
//
//                List<WebElement> imagesPageTwo = driver.findElements(By.className("photo"));
//
//                for (int i = 0; i < imagesPageTwo.size(); i++) {
//                    WebElement personImagePageTwo = imagesPageTwo.get(i).findElement(By.className("name"));
//
//                    try {
//                        if (personImage.getText().equals(personImagePageTwo)) {
//                        }
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    public void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
