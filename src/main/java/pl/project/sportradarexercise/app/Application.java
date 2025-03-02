package pl.project.sportradarexercise.app;

import java.util.ArrayList;
import java.util.List;
import pl.project.sportradarexercise.model.error.match.MatchAlreadyExistsException;
import pl.project.sportradarexercise.model.error.match.NoScoreChangeException;
import pl.project.sportradarexercise.model.error.match.UpdateMatchResultException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardAlreadyExistsException;
import pl.project.sportradarexercise.model.error.scoreboard.ScoreboardDoesNotExistException;
import pl.project.sportradarexercise.model.match.Match;
import pl.project.sportradarexercise.model.match.Team;
import pl.project.sportradarexercise.model.scoreboard.Scoreboard;
import pl.project.sportradarexercise.repository.match.MatchRepositoryImpl;
import pl.project.sportradarexercise.repository.scoreboard.ScoreboardRepositoryImpl;
import pl.project.sportradarexercise.service.match.MatchServiceImpl;
import pl.project.sportradarexercise.service.scoreboard.ScoreboardServiceImpl;

public class Application {

  public static void main(String[] args)
      throws ScoreboardDoesNotExistException, UpdateMatchResultException, ScoreboardAlreadyExistsException {

    System.out.println("Started application");

    // Repositories responsible for managing match and scoreboard data
    MatchRepositoryImpl matchRepository = new MatchRepositoryImpl();
    ScoreboardRepositoryImpl scoreboardRepository = new ScoreboardRepositoryImpl();

    // Services that handle business logic related to matches and the scoreboard
    MatchServiceImpl matchService = new MatchServiceImpl(matchRepository);
    ScoreboardServiceImpl scoreboardService = new ScoreboardServiceImpl(scoreboardRepository,
        matchService);

    // Creating teams participating in matches
    Team poland = new Team(1, "Poland");
    Team germany = new Team(2, "Germany");
    Team mexico = new Team(3, "Mexico");
    Team brazil = new Team(4, "Brazil");
    Team argentina = new Team(5, "Argentina");
    Team france = new Team(6, "France");

    // Creating matches between teams
    Match polandVsGermany = new Match(1, poland, germany);
    Match mexicoVsBrazil = new Match(2, mexico, brazil);
    Match argentinaVsFrance = new Match(3, argentina, france);

    // Adding matches to the match service
    Match matchPolandVsGermany = matchService.createNewMatch(polandVsGermany);
    System.out.printf("Create match %s vs %s \n", matchPolandVsGermany.getHomeTeam().getName(), matchPolandVsGermany.getAwayTeam().getName());
    Match matchMexicoVsBrazil = matchService.createNewMatch(mexicoVsBrazil);
    System.out.printf("Create match %s vs %s \n", matchMexicoVsBrazil.getHomeTeam().getName(), matchMexicoVsBrazil.getAwayTeam().getName());
    Match matchArgentinaVsFrance = matchService.createNewMatch(argentinaVsFrance);
    System.out.printf("Create match %s vs %s \n", matchArgentinaVsFrance.getHomeTeam().getName(), matchArgentinaVsFrance.getAwayTeam().getName());

    try{
      matchService.createNewMatch(polandVsGermany);
    } catch (MatchAlreadyExistsException e) {
      System.out.println(e.getMessage());
    }

    // Creating an empty scoreboard and adding it to the scoreboard service
    List<Match> matchList = new ArrayList<>();
    Scoreboard mainScoreboard = new Scoreboard(1, matchList);
    scoreboardService.addScoreboard(mainScoreboard);

    // Printing scoreboard details
    System.out.println("Created Scoreboard with ID: " + mainScoreboard.getId());

    try {
      scoreboardService.addScoreboard(mainScoreboard);
    } catch (ScoreboardAlreadyExistsException e){
      System.out.println(e.getMessage());
    }

    // Adding matches to the scoreboard
    scoreboardService.addMatchToScoreboard(1, polandVsGermany);
    scoreboardService.addMatchToScoreboard(1, mexicoVsBrazil);
    scoreboardService.addMatchToScoreboard(1, argentinaVsFrance);

    try {
      scoreboardService.addMatchToScoreboard(2, argentinaVsFrance);
    } catch (ScoreboardDoesNotExistException e){
      System.out.println(e.getMessage());
    }

    System.out.println("Matches in the scoreboard:");
    for (Match match : mainScoreboard.getMatches()) {
      System.out.println("Match ID: " + match.getId() + " - " + match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName());
    }

    // Updating match scores for Poland vs Germany
    Match updatedPolandVsGermany = new Match(1, poland, germany);
    updatedPolandVsGermany.getResult().setHomeGoals(1); // Poland scores 1 goal
    Match polandScoreGoal = matchService.updateMatch(1, updatedPolandVsGermany);
    System.out.printf("%s %d vs %d %s\n", polandScoreGoal.getHomeTeam().getName(), polandScoreGoal.getResult().getHomeGoals(), polandScoreGoal.getResult().getAwayGoals(),polandScoreGoal.getAwayTeam().getName());

    Match finalPolandVsGermany = new Match(1, poland, germany);
    finalPolandVsGermany.getResult().setAwayGoals(1); // Germany scores 1 goal
    finalPolandVsGermany.getResult()
        .setHomeGoals(updatedPolandVsGermany.getResult().getHomeGoals());
    Match germanyEqualizes = matchService.updateMatch(1, finalPolandVsGermany);
    System.out.printf("%s %d vs %d %s\n", germanyEqualizes.getHomeTeam().getName(), germanyEqualizes.getResult().getHomeGoals(), germanyEqualizes.getResult().getAwayGoals(),germanyEqualizes.getAwayTeam().getName());

    try {
      matchService.updateMatch(1, finalPolandVsGermany);
    } catch (NoScoreChangeException e){
      System.out.println(e.getMessage());
    }

    // Updating match scores for Mexico vs Brazil
    Match mexicoScoresFirst = new Match(2, mexico, brazil);
    mexicoScoresFirst.getResult().setHomeGoals(1); // Mexico scores first goal
    Match mexicoScoreGoal = matchService.updateMatch(2, mexicoScoresFirst);
    System.out.printf("%s %d vs %d %s\n", mexicoScoreGoal.getHomeTeam().getName(), mexicoScoreGoal.getResult().getHomeGoals(), mexicoScoreGoal.getResult().getAwayGoals(),mexicoScoreGoal.getAwayTeam().getName());

    Match brazilEqualizes = new Match(2, mexico, brazil);
    brazilEqualizes.getResult().setAwayGoals(1); // Brazil scores equalizing goal
    brazilEqualizes.getResult().setHomeGoals(mexicoScoresFirst.getResult().getHomeGoals());
    Match brazilEqualizeMatch = matchService.updateMatch(2, brazilEqualizes);
    System.out.printf("%s %d vs %d %s\n", brazilEqualizeMatch.getHomeTeam().getName(), brazilEqualizeMatch.getResult().getHomeGoals(), brazilEqualizeMatch.getResult().getAwayGoals(),brazilEqualizeMatch.getAwayTeam().getName());

    Match argentinaScoresFirst = new Match(3, argentina, france);
    argentinaScoresFirst.getResult().setHomeGoals(1); // Argentina scores first goal
    Match argentinaFirstUpdate = matchService.updateMatch(3, argentinaScoresFirst);
    System.out.printf("%s %d vs %d %s\n", argentinaFirstUpdate.getHomeTeam().getName(), argentinaFirstUpdate.getResult().getHomeGoals(), argentinaFirstUpdate.getResult().getAwayGoals(), argentinaFirstUpdate.getAwayTeam().getName());

    Match argentinaScoresSecond = new Match(3, argentina, france);
    argentinaScoresSecond.getResult().setHomeGoals(2); // Argentina scores second goal
    argentinaScoresSecond.getResult().setAwayGoals(argentinaFirstUpdate.getResult().getAwayGoals());
    Match argentinaSecondUpdate = matchService.updateMatch(3, argentinaScoresSecond);
    System.out.printf("%s %d vs %d %s\n", argentinaSecondUpdate.getHomeTeam().getName(), argentinaSecondUpdate.getResult().getHomeGoals(), argentinaSecondUpdate.getResult().getAwayGoals(), argentinaSecondUpdate.getAwayTeam().getName());

    Match argentinaScoresThird = new Match(3, argentina, france);
    argentinaScoresThird.getResult().setHomeGoals(3); // Argentina scores third goal
    argentinaScoresThird.getResult().setAwayGoals(argentinaSecondUpdate.getResult().getAwayGoals());
    Match argentinaThirdUpdate = matchService.updateMatch(3, argentinaScoresThird);
    System.out.printf("%s %d vs %d %s\n", argentinaThirdUpdate.getHomeTeam().getName(), argentinaThirdUpdate.getResult().getHomeGoals(), argentinaThirdUpdate.getResult().getAwayGoals(), argentinaThirdUpdate.getAwayTeam().getName());

    Match endMatchArgentinaVsFrance = matchService.finishMatch(3);
    System.out.printf("Match end %s %d vs %d %s \n", endMatchArgentinaVsFrance.getHomeTeam().getName(), endMatchArgentinaVsFrance.getResult().getHomeGoals(), endMatchArgentinaVsFrance.getResult().getAwayGoals(), endMatchArgentinaVsFrance.getAwayTeam().getName());

    // Display the scoreboard summary
    System.out.println("Summary scoreboard:");
    System.out.println(scoreboardService.getSummary(1));

    System.out.println("End application");
  }
}
