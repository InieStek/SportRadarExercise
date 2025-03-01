package pl.project.sportradarexercise.validator.match;

import pl.project.sportradarexercise.model.error.match.ExcessiveScoreChangeException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchResult;

public class SingleGoalChangeValidator implements MatchUpdateValidator{

  @Override
  public void validate(Match match, Match updatedMatch) throws UpdateMatchResultException {
    MatchResult oldMatchResult = match.getResult();
    MatchResult newMatchResult = updatedMatch.getResult();

    int homeGoalsDiff = Math.abs(newMatchResult.getHomeGoals() - oldMatchResult.getHomeGoals());
    int awayGoalsDiff = Math.abs(newMatchResult.getAwayGoals() - oldMatchResult.getAwayGoals());

    if (homeGoalsDiff > 1 || awayGoalsDiff > 1) {
      throw new ExcessiveScoreChangeException(match);
    }
  }
}
