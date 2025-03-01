package pl.project.sportradarexercise.service.match;

import java.util.List;
import java.util.Optional;
import pl.project.sportradarexercise.model.error.match.FinishedMatchException;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;

public interface MatchService {

  Match createNewMatch(Match match);

  Match finishMatch(int id) throws MatchDoesNotExistException, FinishedMatchException;

  Match updateMatch(int id, Match newMatch) throws UpdateMatchResultException;

  Optional<Match> findMatch(int id);

  List<Match> sortByTotalGoalsThenLatestKickoff(List<Match> matches);
}
