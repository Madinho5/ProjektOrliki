
# Projekt Orliki - API Documentation

A REST API application built with Spring Boot for simulating football tournaments played on local football fields. The system supports ADMIN and TRAINER roles, registration, team and player management, match and tournament bracket generation, and tournament list import from XML.

---

## Technologies

- Java 21+
- Spring Boot 4
- Spring Security (Basic Auth)
- Spring Data JPA
- Hibernate
- PostgreSQL + Docker Compose
- Spring Validation
- JAXB (XML import)
- Lombok

---

## Running the Application

1. The database is started with **docker-compose**.
2. Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Data Seeded on Startup

- **1 admin user**
  - login: `admin@gmail.com`
  - password: `admin`

- **8 trainers and 8 teams**
  - logins: `trainer1` ... `trainer8`
  - password: `Password123!`
  - Each trainer gets one team with 7 players (1 goalkeeper, the rest DEF).

---

## Main Entities

- User
- Team
- Player
- Tournament
- Match

---

## Authorization and Access

**Mechanism:** HTTP Basic Auth

### Role-Based Access

| Action | Roles |
|-------|------|
| Registration | TRAINER |
| Login | TRAINER, ADMIN |
| Manage own team | TRAINER |
| Manage players | TRAINER |
| Register team to tournament | TRAINER |
| Manage tournaments | ADMIN |
| Generate tournament rounds | ADMIN |
| View tournaments | TRAINER, ADMIN |

---

## Error Handling

Global exception handler covers:

- `UserAlreadyExistsException` - **409** - Attempt to register an existing user
- `IllegalArgumentException` - **400** - Invalid input data
- `MethodArgumentNotValidException` - **400** - DTO validation failed
- `IllegalStateException` - **400** - Operation not allowed in current state
- `HttpMessageNotReadableException` - **400** - Invalid JSON format
- `ConstraintViolationException` - **400** - Parameter validation / XML import validation
- `Exception` - **500** - Any other error

**Error messages are returned as plain text.**

---

# API Endpoints

---

# AUTH

### POST /auth/register
Registers a trainer.

**Validation:**
- first and last name - letters only
- password - min. 8 characters, one digit, uppercase and lowercase letter, special character
- unique username

### POST /auth/login
Verifies username and password.

---

# TRAINER

### GET /trainer/me
Returns trainer profile.

### PUT /trainer/me
Updates trainer data.

**Validation:**
- first name/last name -> letters only
- phone number -> 9 digits

---

# TEAM

### POST /teams
Creates a team.

**Validation:**
- a trainer can have only one team
- team name must be unique

### GET /teams/mine
Returns the currently logged-in trainer's team.

### PUT /teams/mine
Changes team name.

**Validation:** unique team name

### DELETE /teams/mine
Deletes a team.

**Validation:**
- team cannot be deleted if it participates in a tournament with status **IN_PROGRESS** or **FINISHED**

---

# PLAYER

**Condition:** each player must belong to the team of the trainer who creates them.

### POST /players
Creates a player.

**Validation:**
- first/last name - non-empty, letters only
- age - range **5-80**
- max **20** players per team
- position: **GK / DEF / MID / ST**

### PUT /players/{id}
Edits a player.

**Validation:**
- same rules as for create
- player must belong to trainer's team

### DELETE /players/{id}
Deletes a player.

**Validation:** player must belong to trainer's team

---

# TOURNAMENT

### POST /tournaments (ADMIN)
Creates a tournament.

**Validation:**
- unique name
- date cannot be in the past
- team count: **2 / 4 / 8 / 16**
- initial status: **CREATED**

### GET /tournaments/{id}
Returns:
- team list
- status
- winnerName and winnerId
- startDate
- teamCount

### PUT /tournaments/{id}
**Validation:**
- editing allowed only for statuses: **CREATED**, **REGISTRATION_OPENED**
- teamCount >= number of already registered teams
- date cannot be in the past

### DELETE /tournaments/{id} (ADMIN)
**Validation:**
- cannot delete when status is **IN_PROGRESS**
- deletes all matches of the tournament

### PATCH /tournaments/{id}/status
Changes tournament status:
CREATED / REGISTRATION_OPENED / REGISTRATION_CLOSED / IN_PROGRESS / FINISHED

### GET /tournaments?status=...
Filters tournaments by status.

### POST /tournaments/{id}/register
Registers a team for a tournament.

**Validation:**
- tournament must be in **REGISTRATION_OPENED** status
- trainer must have a team
- team must have **7-10** players
- at least **1 GK**
- team cannot register twice
- team limit cannot be exceeded
- when full, status changes to REGISTRATION_CLOSED

### POST /tournaments/import
Imports tournaments from XML.

---

# BRACKET

### GET /tournaments/{id}/matches
Returns tournament bracket in the following format:

```json
{
  "roundNumber": 1,
  "matches": [
    {
      "id": 1,
      "matchNumber": 1,
      "teamAName": "string",
      "teamBName": "string1",
      "scoreA": 1,
      "scoreB": 2,
      "winnerName": "string1"
    }
  ]
}
```

Data is sorted by:
- rounds ascending (1 -> 2 -> ... -> final)
- matches in each round by match number

Example:
8-team tournament:
Round 1 - Quarterfinals (4 matches)
    8 teams -> 4 matches -> 4 winners
Round 2 - Semifinals (2 matches)
    4 teams -> 2 matches -> 2 winners
Round 3 - Final (1 match)
    2 teams -> 1 winner -> tournament FINISHED

### POST /tournaments/{id}/next-round
Generates the next round of matches.

Generation rules:
- round 1 - random team pairings
- next rounds - pairings of winners from previous round
- match score is generated randomly (0-5)
- winner advances to next round
- when 1 team remains, tournament ends and status becomes FINISHED

---

# Tournament Flow

- ADMIN creates a tournament, status is CREATED
- ADMIN opens registration, sets REGISTRATION_OPENED
- Trainers register teams
- When registered teams reach the limit, system sets REGISTRATION_CLOSED
- ADMIN starts round 1, status becomes IN_PROGRESS
- ADMIN generates next rounds up to the final
- System automatically determines the winner and sets FINISHED

---

# Tournament XML Import

Supported format:

```xml
<tournaments>
    <tournament>
        <name>Tournament 1</name>
        <startDate>2025-06-10</startDate>
        <teamCount>8</teamCount>
    </tournament>
</tournaments>
```

Each record is validated the same way as REST requests.

Returned values:
- number imported
- number skipped
- list of errors

---

# Unit Tests

## BracketServiceTest checks:
- correct first-round generation
- blocking round generation when tournament is finished
- blocking round generation when tournament has invalid status
- correct match grouping when fetching bracket
- correct empty bracket response when no matches exist

## TournamentServiceTest checks:
- blocking creation of tournament with existing name
- validation of allowed team counts: 2, 4, 8, 16
- fetching tournament by ID
- handling non-existing tournament
- deleting tournament
- team registration validation

## TrainerServiceTest checks:
- fetching trainer profile with correct role
- blocking access for roles other than TRAINER
- updating trainer data
- updating fields
- verifying repository save operation

