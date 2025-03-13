package pl.project.sportradarexercise.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TeamNameFormatter {

  public static String format(String name) {
    return Arrays.stream(name.trim().split("\\s+"))
        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
        .collect(Collectors.joining(" "));
  }

}
