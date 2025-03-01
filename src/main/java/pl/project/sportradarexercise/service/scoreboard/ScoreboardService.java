package pl.project.sportradarexercise.service.scoreboard;

import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardAlreadyExistsException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;

public interface ScoreboardService {

  Scoreboard addScoreboard(Scoreboard scoreboard)
      throws ScoreboardDoesNotExistException, ScoreboardAlreadyExistsException;

  Scoreboard updateScoreboard(int id, Scoreboard scoreboard)
      throws ScoreboardUpdateException;

  String getSummary(int id) throws ScoreboardDoesNotExistException;

  Scoreboard addMatchToScoreboard(int id, Match match)
      throws ScoreboardDoesNotExistException, MatchDoesNotExistException;
}
