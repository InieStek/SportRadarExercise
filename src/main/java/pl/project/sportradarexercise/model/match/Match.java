package pl.project.sportradarexercise.model.match;

import java.time.Instant;

public class Match {

  private final int id;
  private final Team homeTeam;
  private final Team awayTeam;
  private MatchResult result;
  private final Instant kickOffTimestamp;
  private MatchStatus status;

  public Match(int id, Team homeTeam, Team awayTeam) {
    this.id = id;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.result = MatchResult.kickOff();
    this.kickOffTimestamp = Instant.now();
    this.status = MatchStatus.IN_PROGRESS;
  }

  public int getId() {
    return id;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public MatchResult getResult() {
    return result;
  }

  public void setResult(MatchResult result) {
    this.result = result;
  }

  public Instant getKickOffTimestamp() {
    return kickOffTimestamp;
  }

  public MatchStatus getStatus() {
    return status;
  }

  public void setStatus(MatchStatus status) {
    this.status = status;
  }

  public boolean isInProgress() {
    return status.equals(MatchStatus.IN_PROGRESS);
  }

  public boolean isFinished() {
    return status.equals(MatchStatus.FINISHED);
  }
}
