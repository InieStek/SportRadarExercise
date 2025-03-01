package pl.project.sportradarexercise.validator.match;

import pl.project.sportradarexercise.model.error.match.FinishedMatchException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;

public class MatchNotFinishedValidator implements MatchUpdateValidator{

  @Override
  public void validate(Match match, Match updatedMatch) throws UpdateMatchResultException {
    if(!match.isInProgress()) {
      throw new FinishedMatchException(match);
    }

  }
}
