package pl.project.sportradarexercise.model.error.scoreboard;

public abstract class ScoreboardUpdateException extends Throwable {

    public ScoreboardUpdateException(String message) {
        super(message);
    }
}