package pl.project.sportradarexercise.repository.match;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import pl.project.sportradarexercise.model.error.match.MatchDoesNotExistException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.MatchResult;
import pl.project.sportradarexercise.model.match.MatchStatus;
import pl.project.sportradarexercise.model.match.Team;

public class MatchRepositoryImplTest {

  private final MatchRepositoryImpl matchRepository = new MatchRepositoryImpl();

  @Test
  void testAddMatch() {
    //given
    Team homeTeam = new Team(1, "Poland");
    Team awayTeam = new Team(2, "Germany");
    Match match = new Match(1, homeTeam, awayTeam);
    //when
    matchRepository.addMatch(match);
    //then
    assertEquals(homeTeam, match.getHomeTeam());
    assertEquals(awayTeam, match.getAwayTeam());
    assertEquals(1, match.getId());
    assertEquals(MatchStatus.IN_PROGRESS, match.getStatus());
  }

  @Test
  void testUpdateMatch() throws MatchDoesNotExistException {
    //given
    Team homeTeam = new Team(1, "Poland");
    Team awayTeam = new Team(2, "Germany");
    Match match = new Match(1, homeTeam, awayTeam);
    Match newMatch = new Match(1, homeTeam, awayTeam);
    MatchResult matchResult = newMatch.getResult();
    matchResult.setHomeGoals(1);
    newMatch.setResult(matchResult);
    //when
    matchRepository.addMatch(match);
    matchRepository.updateMatch(1, newMatch);
    //then
    assertEquals(1, match.getResult().getHomeGoals());

  }

  @Test
  void testMatchNotExistUpdate() {
    Team homeTeam = new Team(1, "Poland");
    Team awayTeam = new Team(2, "Germany");
    Match newMatch = new Match(1, homeTeam, awayTeam);

    assertThrows(MatchDoesNotExistException.class, () -> matchRepository.updateMatch(1, newMatch));
  }

  @Test
  void testFinishMatch() {
    Team homeTeam = new Team(1, "Poland");
    Team awayTeam = new Team(2, "Germany");
    Match match = new Match(1, homeTeam, awayTeam);

    matchRepository.addMatch(match);
    matchRepository.finishMatch(1);

    assertEquals(MatchStatus.FINISHED, match.getStatus());
  }
}
