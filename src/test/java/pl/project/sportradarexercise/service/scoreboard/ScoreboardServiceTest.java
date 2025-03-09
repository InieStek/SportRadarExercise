package pl.project.sportradarexercise.service.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardAlreadyExistsException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardUpdateException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchStatus;
import pl.project.sportradarexercise.model.match.Team;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;
import pl.project.sportradarexercise.repository.scoreboard.ScoreboardRepository;
import pl.project.sportradarexercise.service.match.MatchService;

public class ScoreboardServiceTest {

  @Mock
  private ScoreboardRepository scoreboardRepository;
  @Mock
  private MatchService matchService;
  @InjectMocks
  private ScoreboardServiceImpl scoreboardService;

  private final ArrayList<Match> matches = new ArrayList<>();
  private Team polandTeam;
  private Team germanyTeam;
  private Team spainTeam;
  private Team finlandTeam;
  private Team mexicoTeam;
  private Team canadaTeam;

  @BeforeEach
  void setUp() {
    polandTeam = new Team(1, "Poland");
    germanyTeam = new Team(2, "Germany");
    spainTeam = new Team(3, "Spain");
    finlandTeam = new Team(4, "Finland");
    mexicoTeam = new Team(5, "Mexico");
    canadaTeam = new Team(6, "Canada");
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddScoreboard() throws ScoreboardAlreadyExistsException {
    Scoreboard scoreboard = new Scoreboard(1, matches);
    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);

    Scoreboard newScoreboard = scoreboardService.addScoreboard(scoreboard);

    assertEquals(newScoreboard, scoreboard);
  }

  @Test
  void testAddExistScoreboard() {
    Scoreboard scoreboard = new Scoreboard(1, matches);

    when(scoreboardRepository.findScoreboard(scoreboard.getId()))
        .thenReturn(Optional.of(scoreboard));

    assertThrows(ScoreboardAlreadyExistsException.class,
        () -> scoreboardService.addScoreboard(scoreboard));
  }


  @Test
  void testUpdateScoreboard()
      throws ScoreboardUpdateException {
    Scoreboard scoreboard = new Scoreboard(1, matches);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);
    scoreboardService.addScoreboard(scoreboard);

    ArrayList<Match> newMatches = new ArrayList<>();
    newMatches.add(new Match(1, polandTeam, germanyTeam));
    Scoreboard newScoreboard = new Scoreboard(1, newMatches);

    when(scoreboardRepository.updateScoreboard(1, newScoreboard)).thenReturn(newScoreboard);
    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));

    scoreboardService.updateScoreboard(1, newScoreboard);

    assertEquals(polandTeam.getName(),
        newScoreboard.getMatches().getFirst().getHomeTeam().getName());
    assertEquals(germanyTeam.getName(),
        newScoreboard.getMatches().getFirst().getAwayTeam().getName());
  }

  @Test
  void testUpdateDoesNotExistScoreboard() throws ScoreboardUpdateException {
    Scoreboard scoreboard = new Scoreboard(1, matches);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);

    ArrayList<Match> newMatches = new ArrayList<>();
    newMatches.add(new Match(1, polandTeam, germanyTeam));
    Scoreboard newScoreboard = new Scoreboard(1, newMatches);
    when(scoreboardRepository.updateScoreboard(2, newScoreboard)).thenReturn(newScoreboard);
    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));

    assertThrows(ScoreboardDoesNotExistException.class,
        () -> scoreboardService.updateScoreboard(2, newScoreboard));
  }

  @Test
  void testAddingMatchToScoreboard()
      throws ScoreboardDoesNotExistException, MatchDoesNotExistException, ScoreboardAlreadyExistsException {
    Scoreboard scoreboard = new Scoreboard(1, matches);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);

    scoreboardService.addScoreboard(scoreboard);

    Match newMatch = new Match(1, polandTeam, germanyTeam);

    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));

    when(matchService.findMatch(newMatch.getId())).thenReturn(Optional.of(newMatch));

    scoreboardService.addMatchToScoreboard(1, newMatch);

    assertEquals(newMatch.getId(), scoreboard.getMatches().getFirst().getId());
  }

  @Test
  void testAddingMatchToDoesNotExistScoreboard() {
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    Scoreboard scoreboard = new Scoreboard(1, matches);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);

    assertThrows(ScoreboardDoesNotExistException.class,
        () -> scoreboardService.addMatchToScoreboard(2, newMatch));
  }

  @Test
  void testAddingNotExistMatchToScoreboard() {
    Scoreboard scoreboard = new Scoreboard(1, matches);
    Match newMatch = new Match(1, polandTeam, germanyTeam);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);
    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));

    assertThrows(MatchDoesNotExistException.class,
        () -> scoreboardService.addMatchToScoreboard(1, newMatch));
  }

  @Test
  void testGetSummary() throws ScoreboardDoesNotExistException, ScoreboardAlreadyExistsException {
    Scoreboard scoreboard = new Scoreboard(1, matches);
    Match match1 = new Match(1, polandTeam, germanyTeam);
    Match match2 = new Match(2, spainTeam, finlandTeam);
    Match match3 = new Match(3, mexicoTeam, canadaTeam);

    match1.getResult().setHomeGoals(1);
    match1.getResult().setAwayGoals(1);
    match2.getResult().setHomeGoals(4);
    match2.getResult().setAwayGoals(1);
    match3.getResult().setHomeGoals(4);
    match3.getResult().setAwayGoals(0);
    Collections.addAll(matches, match1, match2, match3);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);
    scoreboardService.addScoreboard(scoreboard);

    String expectedSummary = "Spain 4 vs Finland 1\nMexico 4 vs Canada 0\nPoland 1 vs Germany 1";

    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));
    when(matchService.sortByTotalGoalsThenLatestKickoff(matches)).thenAnswer(
        invocation -> matches.stream()
            .filter(match -> !match.isFinished())
            .sorted(Comparator.comparing((Match match) -> match.getResult().getTotalGoals())
                .thenComparing(Match::getKickOffTimestamp)
                .reversed())
            .toList());

    String actualSummary = scoreboardService.getSummary(1);

    System.out.println(actualSummary);

    assertEquals(expectedSummary, actualSummary.trim());
  }

  @Test
  void testGetEmptyScoreboard() {
    assertThrows(ScoreboardDoesNotExistException.class, () -> scoreboardService.getSummary(1));
  }

  @Test
  void testGetOneFinishedMatchScoreboard()
      throws ScoreboardDoesNotExistException, ScoreboardAlreadyExistsException {
    Scoreboard scoreboard = new Scoreboard(1, matches);
    Match match1 = new Match(1, polandTeam, germanyTeam);
    Match match2 = new Match(2, spainTeam, finlandTeam);
    Match match3 = new Match(3, mexicoTeam, canadaTeam);

    match1.getResult().setHomeGoals(1);
    match1.getResult().setAwayGoals(1);
    match2.getResult().setHomeGoals(3);
    match2.getResult().setAwayGoals(0);
    match3.getResult().setHomeGoals(4);
    match3.getResult().setAwayGoals(0);

    match2.setStatus(MatchStatus.FINISHED);

    Collections.addAll(matches, match1, match2, match3);

    when(scoreboardRepository.addScoreboard(scoreboard)).thenReturn(scoreboard);
    scoreboardService.addScoreboard(scoreboard);

    when(scoreboardRepository.findScoreboard(1)).thenReturn(Optional.of(scoreboard));
    when(matchService.sortByTotalGoalsThenLatestKickoff(matches)).thenAnswer(
        invocation -> matches.stream()
            .filter(match -> !match.isFinished())
            .sorted(Comparator.comparing((Match match) -> match.getResult().getTotalGoals())
                .thenComparing(Match::getKickOffTimestamp)
                .reversed())
            .toList());

    String summary = scoreboardService.getSummary(1);
    String expectedSummary = "Mexico 4 vs Canada 0\nPoland 1 vs Germany 1";

    assertEquals(expectedSummary, summary.trim());
  }


}
