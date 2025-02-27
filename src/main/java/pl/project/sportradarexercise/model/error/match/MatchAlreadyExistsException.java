package pl.project.sportradarexercise.model.error.match;

import pl.project.sportradarexercise.model.match.Match;

public class MatchAlreadyExistsException extends RuntimeException {

  public MatchAlreadyExistsException(Match match) {
    super("Match " + match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName()
        + " already exists");
  }

}
