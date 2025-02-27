package pl.project.sportradarexercise.model.error.scoreboard;

public class ScoreboardDoesNotExistException extends ScoreboardUpdateException {

  public ScoreboardDoesNotExistException(int id) {
    super("Cannot update non-existent scoreboard with id: " + id);
  }
}
