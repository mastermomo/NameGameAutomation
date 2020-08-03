package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void validateTitleIsPresent() {
//        This test isn't strong enough as it only checks for that
//        the title isn't null. It should check for the title being "name game".
//        There's another method in BasePage that will allow for this, so I'll provide my code below.
//
//        WebElement title = driver.findElement(By.cssSelector("h1"));
//        Assert.assertTrue(title != null);

        validateText(By.cssSelector("h1"), "name game");
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        //Wait for page to load
        //sleep(6000);

        sleep(35000); // Changed sleep() from 6000 to 35000 ms to give enough time for all elements
                                // on the page to load.

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        sleep(6000);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }

    /**
     * Validates that the streak counter increases when the
     * correct photo is clicked.
     */
    public void validateClickingCorrectPhotoIncreasesStreakCounter() {
        sleep(35000);

        int initialStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        correctPhotoSelection();

        sleep(6000);

        int finalStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        Assert.assertTrue(finalStreakCount > initialStreakCount);
    }

    /**
     * After a user gets 2 names correct in a row, this validates that
     * the streak counter resets after a third wrong answer.
     */
    public void validateStreakCounterResetsAfterIncorrectAnswer() {
        sleep(35000);

        int initialStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        correctPhotoSelection();

        sleep(6000);

        correctPhotoSelection();

        sleep(6000);

        notCorrectPhotoSelection();

        sleep(6000);

        int finalStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        Assert.assertTrue(finalStreakCount == initialStreakCount);
    }

    /**
     * Randomly chooses 10 different selections and makes
     * sure that the correct and tries counters are
     * tallying properly.
     */
    public void validateTriesAndCorrectCountersAfterTenRandomSelections() {
        sleep(35000);

        int initialTriesCounter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        int initialCorrectCounter = Integer.parseInt(driver.findElement(By.className("correct")).getText());

        // Had trouble getting to ten tries with this test. The tally
        // works fine when I'd test for a lower number of tries. It just an
        // issue getting to 10. I've noticed though that when I do
        // get to ten, it's mainly when I give my desktop a break on
        // running tests. I don't know if it's a case of that and/or my
        // randomTenSelection() method's big O
        // causing the issue, but if I move forwards to the final
        // interview, I'd like to talk about this test and what I could do to
        // get to 10 tries. I'll laugh if it's actually an issue in code.
        // A super BIG apology for this.
        for (int k = 0; k < 10; k++) {
            sleep(5000);
            randomTenSelection();
        }

        sleep(6000);

        int finalTriesCounter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        int finalCorrectCounter = Integer.parseInt(driver.findElement(By.className("correct")).getText());

        // Helps to keep track of the tries and correct tallies if the test fails on the Asserts or
        // if the test doesn't reach the full 10 clicks.
        //
        //System.out.println("Tries: " + finalTriesCounter + "\n" + "Correct: " + finalCorrectCounter);


        Assert.assertTrue(finalTriesCounter == triesTally);
        Assert.assertTrue(finalCorrectCounter == correctTally);
    }

    /**
     * Checks to make sure that the correct photo and name do not
     * reappear in the next line up of photos.
     */
    public void validateNextNamesAndPhotosAreDifferentThanCorrectSelection() {
        sleep(35000);

        correctSelectionNotInNextSet();

        sleep(10000);
    }

    /**
     * Unfortunately I didn't have enough time to code for this bonus test as
     * my mind was being "boggled", as they say, by the previous test. I still
     * plan on learning how to implement the code, so if I'm moved to the next
     * phase of the interview process, I'd love to talk about this test.
     */
    public void validateIncorrectSelectionIncreaseCorrectPhotosFrequency() {
    }
}
