package pl.project.sportradarexercise.model.error.match;

public class MatchDoesNotExistException extends UpdateMatchResultException {

  public MatchDoesNotExistException(int id) {
    super("Cannot update non-existent match with id: " + id);
  }
}
