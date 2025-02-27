package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class ExcessiveScoreChangeException extends UpdateMatchResultException {

  public ExcessiveScoreChangeException(Match match) {
    super("Cannot update multiple goals " + match.getId());
  }
}
