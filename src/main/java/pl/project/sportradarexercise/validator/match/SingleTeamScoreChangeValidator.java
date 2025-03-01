package pl.project.sportradarexercise.validator.match;

import pl.project.sportradarexercise.model.error.match.SimultaneousScoreChangeException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchResult;

public class SingleTeamScoreChangeValidator implements MatchUpdateValidator{

  @Override
  public void validate(Match match, Match updatedMatch) throws UpdateMatchResultException {
    MatchResult matchResult = match.getResult();
    MatchResult newMatchResult = updatedMatch.getResult();

    if (newMatchResult.getHomeGoals() != matchResult.getHomeGoals() &&
        newMatchResult.getAwayGoals() != matchResult.getAwayGoals()) {
      throw new SimultaneousScoreChangeException(match);
    }
  }
}
