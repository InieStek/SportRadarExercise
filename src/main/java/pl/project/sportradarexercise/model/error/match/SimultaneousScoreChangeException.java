package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class SimultaneousScoreChangeException extends UpdateMatchResultException {

  public SimultaneousScoreChangeException(Match match) {
    super("Cannot update simultaneous score change " + match.getId());
  }
}
