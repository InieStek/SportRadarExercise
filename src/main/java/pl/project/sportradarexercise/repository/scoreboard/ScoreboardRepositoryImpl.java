package pl.project.sportradarexercise.repository.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;

public class ScoreboardRepositoryImpl implements ScoreboardRepository {

  private final List<Scoreboard> scoreboardList = new ArrayList<>();

  @Override
  public Scoreboard addScoreboard(Scoreboard scoreboard) {
    scoreboardList.add(scoreboard);
    return scoreboard;
  }

  @Override
  public Scoreboard updateScoreboard(int id, Scoreboard updatedScoreboard) throws ScoreboardUpdateException {
    return findScoreboard(id).map(scoreboard -> {
      scoreboard.setMatches(updatedScoreboard.getMatches());
      return scoreboard;
    }).orElseThrow(() -> new ScoreboardDoesNotExistException(id));
  }

  @Override
  public Optional<Scoreboard> findScoreboard(int id) {
    return scoreboardList.stream()
        .filter(scoreboard -> scoreboard.getId() == id)
        .findFirst();
  }
}
