package pl.project.sportradarexercise.validator.match;

import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;

public interface MatchUpdateValidator {
  void validate(Match match, Match updatedMatch) throws UpdateMatchResultException;
}
