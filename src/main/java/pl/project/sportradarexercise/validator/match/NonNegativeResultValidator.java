package pl.project.sportradarexercise.validator.match;

import pl.project.sportradarexercise.model.error.match.NegativeResultException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchResult;

public class NonNegativeResultValidator implements MatchUpdateValidator{

  @Override
  public void validate(Match match, Match updatedMatch) throws UpdateMatchResultException {
    MatchResult newMatchResult = updatedMatch.getResult();

    if (newMatchResult.getHomeGoals() < 0 || newMatchResult.getAwayGoals() < 0) {
      throw new NegativeResultException(match);
    }
  }
}
