package cydeo.spartan;

import org.junit.jupiter.api.Test;
import utilities.ConfigReader;

public class PropertiesTest {

    @Test
    public void test1(){

        System.out.println("ConfigReader.getProperty(\"spartan.editor.username\") = " + ConfigReader.getProperty("spartan.editor.username"));
        System.out.println("ConfigReader.getProperty(\"serenity.project.name\") = " + ConfigReader.getProperty("serenity.project.name"));
    }
}
