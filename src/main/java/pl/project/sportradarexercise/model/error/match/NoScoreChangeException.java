package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class NoScoreChangeException extends UpdateMatchResultException {

  public NoScoreChangeException(Match match) {
    super("Cannot update no score result match " + match.getId());
  }
}
