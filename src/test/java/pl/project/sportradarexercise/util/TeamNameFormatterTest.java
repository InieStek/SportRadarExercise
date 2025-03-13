package pl.project.sportradarexercise.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TeamNameFormatterTest {

  private static String singleWord;
  private static String multipleWords;
  private static String randomCase;
  private static String extraSpaces;

  @BeforeAll
  static void setup() {
    singleWord = "poland";
    multipleWords = "ivory coast";
    randomCase = "uNiTeD STATES";
    extraSpaces = " south   korea  ";
  }

  @Test
  void testSingleWordName(){
    assertEquals("Poland", TeamNameFormatter.format(singleWord));
  }

  @Test
  void testMultiWordName(){
    assertEquals("Ivory Coast", TeamNameFormatter.format(multipleWords));
  }

  @Test
  void testRandomCase(){
    assertEquals("United States", TeamNameFormatter.format(randomCase));
  }

  @Test
  void testExtraSpaces(){
    assertEquals("South Korea", TeamNameFormatter.format(extraSpaces));
  }
}