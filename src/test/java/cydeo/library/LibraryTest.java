package cydeo.library;

import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.Test;
import utilities.ConfigReader;

@SerenityTest
public class LibraryTest {

    @Test
    public void test1(){

        System.out.println(ConfigReader.getProperty("base.url"));
        System.out.println(ConfigReader.getProperty("base.path"));
        System.out.println(ConfigReader.getProperty("librarian.username"));
        System.out.println(ConfigReader.getProperty("librarian.password"));

    }
}
