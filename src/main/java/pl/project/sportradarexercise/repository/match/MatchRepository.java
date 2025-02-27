package pl.project.sportradarexercise.repository.match;

import java.util.Optional;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.match.Match;

public interface MatchRepository {
  Match addMatch(Match match);

  Optional<Match> findMatch(int id);

  Match updateMatch(int id, Match newMatch) throws MatchDoesNotExistException;

  Match finishMatch(int id);

}
