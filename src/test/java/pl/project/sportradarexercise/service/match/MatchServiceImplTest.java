package pl.project.sportradarexercise.service.match;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import pl.project.sportradarexercise.model.error.match.ExcessiveScoreChangeException;
import pl.project.sportradarexercise.model.error.match.FinishedMatchException;
import pl.project.sportradarexercise.model.error.match.MatchAlreadyExistsException;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.error.match.NegativeResultException;
import pl.project.sportradarexercise.model.error.match.NoScoreChangeException;
import pl.project.sportradarexercise.model.error.match.SimultaneousScoreChangeException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchStatus;
import pl.project.sportradarexercise.model.match.Team;
import pl.project.sportradarexercise.repository.match.MatchRepository;

public class MatchServiceImplTest {

  @Mock
  private MatchRepository matchRepository;
  @InjectMocks
  private MatchServiceImpl matchService;

  private Team polandTeam;
  private Team germanyTeam;
  private AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    polandTeam = new Team(1, "Poland");
    germanyTeam = new Team(2, "Germany");
    closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void cleanup() throws Exception {
    closeable.close();
  }

  @Test
  void testCreateMatch() {
    Match match = new Match(1, polandTeam, germanyTeam);

    when(matchRepository.addMatch(match)).thenReturn(match);

    Match newMatch = matchService.createNewMatch(match);
    assertEquals(polandTeam.getName(), newMatch.getHomeTeam().getName());
    assertEquals(germanyTeam.getName(), newMatch.getAwayTeam().getName());
    assertEquals(1, newMatch.getId());
  }

  @Test
  void testCreateExistMatch() {
    Match match = new Match(1, polandTeam, germanyTeam);

    when(matchRepository.addMatch(match)).thenThrow(new MatchAlreadyExistsException(match));

    assertThrows(MatchAlreadyExistsException.class, () -> matchService.createNewMatch(match));
  }

  @Test
  void testFinishMatch() throws MatchDoesNotExistException, FinishedMatchException {
    Match match = new Match(1, polandTeam, germanyTeam);

    when(matchRepository.addMatch(match)).thenReturn(match);
    matchService.createNewMatch(match);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));
    when(matchRepository.finishMatch(1)).thenAnswer(invocation -> {
      match.setStatus(MatchStatus.FINISHED);
      return match;
    });

    Match finishedMatch = matchService.finishMatch(1);

    assertEquals(MatchStatus.FINISHED, finishedMatch.getStatus());
  }

  @Test
  void testFinishDoesNotExistMatch() {
    Match match = new Match(1, polandTeam, germanyTeam);
    match.setStatus(MatchStatus.FINISHED);

    when(matchRepository.findMatch(1)).thenReturn(Optional.empty());
    assertThrows(MatchDoesNotExistException.class, () -> matchService.finishMatch(1));

  }

  @Test
  void testFinishEndedMatch() throws MatchDoesNotExistException, FinishedMatchException {
    Match match = new Match(1, polandTeam, germanyTeam);
    match.setStatus(MatchStatus.FINISHED);

    when(matchRepository.addMatch(match)).thenReturn(match);
    matchService.createNewMatch(match);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));
    when(matchRepository.finishMatch(1)).thenReturn(match);
    assertThrows(FinishedMatchException.class, () -> matchService.finishMatch(1));
  }

  @Test
  void testUpdateMatch() throws UpdateMatchResultException {
    Match match = new Match(1, polandTeam, germanyTeam);

    when(matchRepository.addMatch(match)).thenReturn(match);
    matchService.createNewMatch(match);

    Match newMatch = new Match(1, polandTeam, germanyTeam);
    newMatch.getResult().setHomeGoals(1);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));
    when(matchRepository.updateMatch(1, newMatch)).thenReturn(newMatch);
    Match updatedMatch = matchService.updateMatch(1, newMatch);

    assertEquals(1, updatedMatch.getResult().getHomeGoals());
    assertEquals(0, updatedMatch.getResult().getAwayGoals());
  }

  @Test
  void testDoesNotExistMatchUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    when(matchRepository.findMatch(1)).thenReturn(Optional.empty());
    assertThrows(MatchDoesNotExistException.class, () -> matchService.updateMatch(1, match));
  }

  @Test
  void testFinishedMatchUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    match.setStatus(MatchStatus.FINISHED);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));
    assertThrows(FinishedMatchException.class, () -> matchService.updateMatch(1, newMatch));
  }


  @Test
  void testNegativeResultUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    newMatch.getResult().setAwayGoals(-1);
    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));

    assertThrows(NegativeResultException.class, () -> matchService.updateMatch(1, newMatch));
  }

  @Test
  void testNoScoreChangeMatchUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));

    assertThrows(NoScoreChangeException.class, () -> matchService.updateMatch(1, newMatch));
  }

  @Test
  void testSimultaneousScoreChangeUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    newMatch.getResult().setAwayGoals(1);
    newMatch.getResult().setHomeGoals(1);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));

    assertThrows(SimultaneousScoreChangeException.class,
        () -> matchService.updateMatch(1, newMatch));
  }

  @Test
  void testExcessiveScoreChangeUpdate() {
    Match match = new Match(1, polandTeam, germanyTeam);
    Match newMatch = new Match(1, polandTeam, germanyTeam);
    newMatch.getResult().setHomeGoals(2);
    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));

    assertThrows(ExcessiveScoreChangeException.class, () -> matchService.updateMatch(1, newMatch));
  }
}
