package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class NegativeResultException extends UpdateMatchResultException {

  public NegativeResultException(Match match) {
    super("Cannot update negative result match id " + match.getId());
  }
}
