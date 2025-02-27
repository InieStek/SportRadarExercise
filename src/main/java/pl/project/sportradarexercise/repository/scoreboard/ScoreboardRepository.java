package pl.project.sportradarexercise.repository.scoreboard;

import java.util.Optional;

import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;

public interface ScoreboardRepository {

  Scoreboard addScoreboard(Scoreboard scoreboard);

  Scoreboard updateScoreboard(int id, Scoreboard newScoreboard) throws ScoreboardUpdateException;

  Optional<Scoreboard> findScoreboard(int id);
}