package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class FinishedMatchException extends UpdateMatchResultException {

  public FinishedMatchException(Match match) {
    super("Cannot update finished match, match ID: " + match.getId());
  }
}
