package pl.project.sportradarexercise.service.scoreboard;

import java.util.List;
import java.util.Optional;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardAlreadyExistsException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;
import pl.project.sportradarexercise.repository.scoreboard.ScoreboardRepository;
import pl.project.sportradarexercise.service.match.MatchService;

public class ScoreboardServiceImpl implements ScoreboardService {

  private final ScoreboardRepository scoreboardRepository;
  private final MatchService matchService;

  public ScoreboardServiceImpl(ScoreboardRepository scoreboardRepository,
      MatchService matchService) {
    this.scoreboardRepository = scoreboardRepository;
    this.matchService = matchService;
  }

  @Override
  public Scoreboard addScoreboard(Scoreboard scoreboard) throws ScoreboardAlreadyExistsException {
    Optional<Scoreboard> maybeScoreboard = scoreboardRepository.findScoreboard(scoreboard.getId());
    if (maybeScoreboard.isPresent()) {
      throw new ScoreboardAlreadyExistsException(scoreboard);
    }
    return scoreboardRepository.addScoreboard(scoreboard);
  }

  @Override
  public Scoreboard updateScoreboard(int id, Scoreboard scoreboard)
      throws ScoreboardUpdateException {
    Optional<Scoreboard> maybeScoreboard = scoreboardRepository.findScoreboard(id);

    if (maybeScoreboard.isEmpty()) {
      throw new ScoreboardDoesNotExistException(id);
    }
    return scoreboardRepository.updateScoreboard(id, scoreboard);
  }

  @Override
  public String getSummary(int id) throws ScoreboardDoesNotExistException {

    Optional<Scoreboard> maybeScoreboard = scoreboardRepository.findScoreboard(id);

    if (maybeScoreboard.isEmpty()) {
      throw new ScoreboardDoesNotExistException(id);
    } else {
      Scoreboard scoreboard = maybeScoreboard.get();
      List<Match> matches = scoreboard.getMatches();

      List<Match> matchList = matchService.sortByTotalGoalsThenLatestKickoff(matches);
      List<String> summaryLines = matchList
          .stream()
          .map(match -> String.format("%s %d vs %s %d",
              match.getHomeTeam().getName(),
              match.getResult().getHomeGoals(),
              match.getAwayTeam().getName(),
              match.getResult().getAwayGoals())).toList();

      return String.join("\n", summaryLines);
    }
  }

  @Override
  public Scoreboard addMatchToScoreboard(int id, Match match)
      throws ScoreboardDoesNotExistException, MatchDoesNotExistException {
    Optional<Scoreboard> maybeScoreboard = scoreboardRepository.findScoreboard(id);
    if (maybeScoreboard.isEmpty()) {
      throw new ScoreboardDoesNotExistException(id);
    } else {
      Scoreboard scoreboard = maybeScoreboard.get();
      Optional<Match> maybeMatch = matchService.findMatch(match.getId());
      if (maybeMatch.isEmpty()) {
        throw new MatchDoesNotExistException(match.getId());
      } else {
        Match matchAddedToScoreboard = maybeMatch.get();
        scoreboard.getMatches().add(matchAddedToScoreboard);

        return scoreboard;
      }
    }
  }
}
