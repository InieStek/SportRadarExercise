package pl.project.sportradarexercise.model.error.scoreboard;

import pl.project.sportradarexercise.model.scoreboard.Scoreboard;

public class ScoreboardAlreadyExistsException extends ScoreboardUpdateException {

  public ScoreboardAlreadyExistsException(Scoreboard scoreboard) {
    super("Scoreboard: " + scoreboard.getId() + " is already exists");
  }
}
