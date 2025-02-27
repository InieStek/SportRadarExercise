package pl.project.sportradarexercise.model.scoreboard;

import java.util.List;
import pl.project.sportradarexercise.model.match.Match;

public class Scoreboard {

  private final int id;
  private List<Match> matches;

  public Scoreboard(int id, List<Match> matches) {
    this.id = id;
    this.matches = matches;
  }

  public int getId() {
    return id;
  }

  public List<Match> getMatches() {
    return matches;
  }

  public void setMatches(List<Match> matches) {
    this.matches = matches;
  }
}
