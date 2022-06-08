# TourneyJourney

An easy way to host and manage tournaments

## Introduction

TJ is a tool that will allow the user to easily manage a single elimination tournament with however many players they
need!

## Inspiration

As a person who enjoys video games and competing against other people, I feel like this tool would be great for those
who wish to host their own tournaments, no matter the scale of the event.

## User Stories

As a user, I want to be able to:

- insert players into a bracket
- create a single elimination bracket
- update the status of a match (i.e the winner of a match/tournament)
- view the bracket

Phase 2 user stories:  
As a user, in addition to above, I want to be able to:

- save a tournament bracket
- load a specific tournament bracket

## Phase 4: Task 3

If I had more time to refactor, I would

- consider **combining** TourneyJourney and TourneyApp back together (TJ creates panels for the frame, but the frame
  itself (TourneyApp) doesn't do much)
    - If I were to add more functionality to the frame, maybe they could stay separate.
- **reduce coupling** by somehow removing the arrow from Match to Player
    - The best way to reduce coupling would be to just remove Match all together and have Bracket hold an array of an
      array of players.

## Phase 4: Task 2

Thu Nov 25 12:33:42 PST 2021 Tournament named test2 has been created

Thu Nov 25 12:33:42 PST 2021 Created player named e

Thu Nov 25 12:33:42 PST 2021 Created player named b

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named e and player named b

Thu Nov 25 12:33:42 PST 2021 Created player named e

Thu Nov 25 12:33:42 PST 2021 Created player named Bye

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named e and player named Bye

Thu Nov 25 12:33:42 PST 2021 Created player named c

Thu Nov 25 12:33:42 PST 2021 Created player named b

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named c and player named b

Thu Nov 25 12:33:42 PST 2021 Created player named Bye

Thu Nov 25 12:33:42 PST 2021 Created player named Bye

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named Bye and player named Bye

Thu Nov 25 12:33:42 PST 2021 Created player named e

Thu Nov 25 12:33:42 PST 2021 Created player named f

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named e and player named f

Thu Nov 25 12:33:42 PST 2021 Created player named c

Thu Nov 25 12:33:42 PST 2021 Created player named d

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named c and player named d

Thu Nov 25 12:33:42 PST 2021 Created player named a

Thu Nov 25 12:33:42 PST 2021 Created player named b

Thu Nov 25 12:33:42 PST 2021 Created a match object, containing a player named a and player named b

Thu Nov 25 12:33:51 PST 2021 Tournament named cooltournament has been created

Thu Nov 25 12:33:53 PST 2021 Created player named a

Thu Nov 25 12:33:53 PST 2021 Added a to the tournament.

Thu Nov 25 12:33:53 PST 2021 Created player named b

Thu Nov 25 12:33:53 PST 2021 Added b to the tournament.

Thu Nov 25 12:33:54 PST 2021 Created player named c

Thu Nov 25 12:33:54 PST 2021 Added c to the tournament.

Thu Nov 25 12:33:55 PST 2021 Created player named d

Thu Nov 25 12:33:55 PST 2021 Added d to the tournament.

Thu Nov 25 12:33:56 PST 2021 Added match to the bracket, containing players a and b

Thu Nov 25 12:33:56 PST 2021 Added match to the bracket, containing players c and d

Thu Nov 25 12:33:56 PST 2021 Tournament named cooltournament has created its bracket.

Thu Nov 25 12:33:59 PST 2021 Added d to their next match

Thu Nov 25 12:34:00 PST 2021 Added a to their next match

Thu Nov 25 12:34:02 PST 2021 d has won the tournament!
