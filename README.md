# SportRadarExercise

## **Project Description**

SportRadarExercise is a Java-based application for managing sports matches and scoreboards. It allows users to create, update, and finish matches while maintaining a structured scoreboard system. The application follows a service-oriented architecture and ensures data integrity through validation mechanisms.

### Key Features

* Manage sports matches (creation, updating scores, finishing matches)

* Maintain scoreboards with multiple matches

* Data validation to prevent incorrect score updates

* Repository and service layers for efficient data handling

* Unit tests to ensure application stability
## **Installation and Setup**

### Clone the repository

```
git clone https://github.com/InieStek/SportRadarExercise.git
cd SportRadarExercise
```

### Build the project using Maven

``mvn clean install``

## ⚙️ Usage

### Creating a Match

```
Match match = new Match(1, polandTeam, germanyTeam);
matchService.createNewMatch(match);
```

### Finishing a Match

```
matchService.finishMatch(1);
```

### Retrieving a Match
```
Match match = matchService.getMatch(1);
```

## 🏆 Scoreboard Management

### Creating a Scoreboard
```
List<Match> matchList = new ArrayList<>();
Scoreboard scoreboard = new Scoreboard(1, matchList);
scoreboardService.addScoreboard(scoreboard);
```

### Adding Matches to the Scoreboard
```
scoreboardService.addMatchToScoreboard(1, match);
scoreboardService.addMatchToScoreboard(1, match2);
scoreboardService.addMatchToScoreboard(1, match3);
```
### Updating Match Results
```
Match updatedMatch = new Match(1, polandTeam, germanyTeam);
updatedMatch.getResult().setHomeGoals(1);
matchService.updateMatch(1, updatedMatch);
```
### Retrieving the Scoreboard Summary
```
scoreboardService.getSummary(1);
```


## ✅ Testing

#### To run tests, execute:

``
mvn test
``

### Example Test
```
@Test
  void testFinishMatch() throws MatchDoesNotExistException, FinishedMatchException {
    Match match = new Match(1, polandTeam, germanyTeam);

    when(matchRepository.addMatch(match)).thenReturn(match);
    matchService.createNewMatch(match);

    when(matchRepository.findMatch(1)).thenReturn(Optional.of(match));
    when(matchRepository.endMatch(1)).thenAnswer(invocation -> {
      match.setStatus(MatchStatus.FINISHED);
      return match;
    });
```

## 🛠 Technologies

Java 21

Spring Boot

JUnit 5

Mockito

Maven