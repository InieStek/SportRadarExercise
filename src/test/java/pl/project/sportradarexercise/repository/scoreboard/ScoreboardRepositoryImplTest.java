package pl.project.sportradarexercise.repository.scoreboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.Team;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;

class ScoreboardRepositoryImplTest {

  private ScoreboardRepositoryImpl scoreboardRepository;
  private Team polandTeam;
  private Team germanyTeam;

  @BeforeEach
  void setUp() {
    scoreboardRepository = new ScoreboardRepositoryImpl();

    polandTeam = new Team(1, "Poland");
    germanyTeam = new Team(2, "Germany");
  }

  @Test
  void testCreateScoreboard() {
    ArrayList<Match> matches = new ArrayList<>();
    Scoreboard scoreboard = new Scoreboard(1, matches);

    scoreboardRepository.addScoreboard(scoreboard);

    assertNotNull(scoreboard);
    assertEquals(1, scoreboard.getId());
    assertEquals(matches, scoreboard.getMatches());
  }

  @Test
  void testUpdateScoreboard() throws ScoreboardUpdateException {
    ArrayList<Match> matches = new ArrayList<>();
    Scoreboard scoreboard = new Scoreboard(1, matches);

    scoreboardRepository.addScoreboard(scoreboard);

    ArrayList<Match> newMatches = new ArrayList<>();
    newMatches.add(new Match(1, polandTeam, germanyTeam));
    Scoreboard newScoreboard = new Scoreboard(1, newMatches);

    scoreboardRepository.updateScoreboard(1, newScoreboard);

    assertEquals(1, scoreboard.getMatches().size());
    assertEquals(polandTeam.getName(), scoreboard.getMatches().getFirst().getHomeTeam().getName());
    assertEquals(germanyTeam.getName(), scoreboard.getMatches().getFirst().getAwayTeam().getName());
  }

  @Test
  void testScoreboardDoesNotExistUpdate(){
    ArrayList<Match> matches = new ArrayList<>();
    Scoreboard scoreboard = new Scoreboard(1, matches);

    assertThrows(ScoreboardDoesNotExistException.class, () -> scoreboardRepository.updateScoreboard(1, scoreboard));
  }

  @Test
  void testFindScoreboard() {
    ArrayList<Match> matches = new ArrayList<>();
    Scoreboard scoreboard = new Scoreboard(1, matches);

    scoreboardRepository.addScoreboard(scoreboard);

    Optional<Scoreboard> maybeScoreboard = scoreboardRepository.findScoreboard(1);
    Scoreboard foundScoreboard = maybeScoreboard.get();

    assertEquals(1, foundScoreboard.getId());
    assertTrue(foundScoreboard.getMatches().isEmpty());
  }

}