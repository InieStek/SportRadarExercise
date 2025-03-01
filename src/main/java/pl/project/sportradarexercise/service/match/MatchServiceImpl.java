package pl.project.sportradarexercise.service.match;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import pl.project.sportradarexercise.model.error.match.FinishedMatchException;
import pl.project.sportradarexercise.model.error.match.MatchAlreadyExistsException;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.repository.match.MatchRepository;
import pl.project.sportradarexercise.validator.match.MatchNotFinishedValidator;
import pl.project.sportradarexercise.validator.match.MatchResultHasChangedValidator;
import pl.project.sportradarexercise.validator.match.NonNegativeResultValidator;
import pl.project.sportradarexercise.validator.match.SingleGoalChangeValidator;
import pl.project.sportradarexercise.validator.match.SingleTeamScoreChangeValidator;

public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;

  public MatchServiceImpl(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  @Override
  public Match createNewMatch(Match match) {
    Optional<Match> maybeMatch = findMatch(match.getId());
    if (maybeMatch.isPresent()) {
      throw new MatchAlreadyExistsException(match);
    }
    return matchRepository.addMatch(match);
  }

  @Override
  public Match finishMatch(int id) throws MatchDoesNotExistException, FinishedMatchException {
    Optional<Match> maybeMatch = findMatch(id);
    if (maybeMatch.isEmpty()) {
      throw new MatchDoesNotExistException(id);
    } else {
      Match match = maybeMatch.get();
      if (match.isFinished()) {
        throw new FinishedMatchException(match);
      }
    }
    return matchRepository.finishMatch(id);
  }

  @Override
  public Match updateMatch(int id, Match newMatch) throws UpdateMatchResultException {
    Optional<Match> maybeMatch = findMatch(id);
    if (maybeMatch.isEmpty()) {
      throw new MatchDoesNotExistException(id);
    }
    Match match = maybeMatch.get();

    new MatchNotFinishedValidator().validate(match, newMatch);
    new NonNegativeResultValidator().validate(match, newMatch);
    new MatchResultHasChangedValidator().validate(match, newMatch);
    new SingleTeamScoreChangeValidator().validate(match, newMatch);
    new SingleGoalChangeValidator().validate(match, newMatch);

    return matchRepository.updateMatch(id, newMatch);
  }

  @Override
  public Optional<Match> findMatch(int id) {
    return matchRepository.findMatch(id);
  }

  @Override
  public List<Match> sortByTotalGoalsThenLatestKickoff(List<Match> matches) {
    return matches.stream()
        .filter(match -> !match.isFinished())
        .sorted(Comparator.comparing((Match match) -> match.getResult().getTotalGoals())
            .thenComparing(Match::getKickOffTimestamp)
            .reversed())
        .toList();
  }
}
