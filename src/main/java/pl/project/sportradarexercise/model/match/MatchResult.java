package pl.project.sportradarexercise.model.match;

public class MatchResult {

  private int homeGoals;
  private int awayGoals;

  static public MatchResult kickOff() {
    return new MatchResult();
  }

  private MatchResult() {
    this.homeGoals = 0;
    this.awayGoals = 0;
  }

  public int getHomeGoals() {
    return homeGoals;
  }

  public int getAwayGoals() {
    return awayGoals;
  }

  public int getTotalGoals() {
    return homeGoals + awayGoals;
  }

  public void setHomeGoals(int homeGoals) {
    this.homeGoals = homeGoals;
  }

  public void setAwayGoals(int awayGoals) {
    this.awayGoals = awayGoals;
  }
}
