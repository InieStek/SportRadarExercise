package pl.project.sportradarexercise.repository.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchStatus;

public class MatchRepositoryImpl implements MatchRepository {

  private final List<Match> matches = new ArrayList<>();

  @Override
  public Match addMatch(final Match match) {
    matches.add(match);
    return match;
  }

  @Override
  public Optional<Match> findMatch(final int id) {
    return matches.stream()
        .filter(match -> match.getId() == id)
        .findFirst();
  }

  @Override
  public Match updateMatch(final int id, final Match newMatch) throws MatchDoesNotExistException {
    return findMatch(id).map(match -> {
      match.setResult(newMatch.getResult());
      return match;
    }).orElseThrow(() -> new MatchDoesNotExistException(id));
  }

  @Override
  public Match finishMatch(int id) {
    Optional<Match> maybeMatch = findMatch(id);

    Match match = maybeMatch.get();

    match.setStatus(MatchStatus.FINISHED);
    matches.remove(match);
    return match;
  }

  @Override
  public Optional<Match> findMatchByTeam(String teamName) {
    return matches.stream()
        .filter(match -> match.getHomeTeam().getName().equals(teamName) || match.getAwayTeam().getName().equals(teamName))
        .findFirst();
  }
}
