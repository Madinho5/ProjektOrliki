
# Projekt Orliki – Dokumentacja API

Aplikacja REST API stworzona w Spring Boot, umożliwiająca symulowanie turniejów piłkarskich rozgrywanych na orlikach. System wspiera role ADMIN oraz TRAINER, rejestrację, zarządzanie drużynami, zawodnikami, tworzy mecze i drabinkę turnieju, umożliwia import listy turniejów z XML.

---

## Technologie

- Java 21+
- Spring Boot 4
- Spring Security (Basic Auth)
- Spring Data JPA
- Hibernate
- PostgreSQL + Docker Compose
- Spring Validation
- JAXB (import XML)
- Lombok

---

## Uruchomienie

1. Baza danych tworzona za pomocą **docker-compose-support**
2. Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Przy starcie aplikacji tworzone są:

- **1 użytkownik o roli admin**
  - login: `admin@gmail.com`
  - hasło: `admin`

- **8 trenerów i 8 drużyn**
  - loginy: `trainer1` … `trainer8`
  - hasło: `Password123!`
  - Każdy trener dostaje drużynę z 7 zawodnikami (1 bramkarz, reszta DEF).

---

## Główne encje

- User  
- Team  
- Player  
- Tournament  
- Match  

---

## Autoryzacja i dostęp

**Mechanizm:** HTTP Basic Auth

### Dostęp według ról

| Akcja | Role |
|-------|------|
| Rejestracja | TRAINER |
| Logowanie | TRAINER, ADMIN |
| Zarządzanie swoją drużyną | TRAINER |
| Zarządzanie zawodnikami | TRAINER |
| Rejestracja drużyny do turnieju | TRAINER |
| Zarządzanie turniejami | ADMIN |
| Generowanie rund turnieju | ADMIN |
| Podgląd turniejów | TRAINER, ADMIN |

---

## Obsługa błędów

Globalny handler obsługuje:

- `UserAlreadyExistsException` — **409** — Próba rejestracji istniejącego użytkownika  
- `IllegalArgumentException` — **400** — Nieprawidłowe dane wejściowe  
- `MethodArgumentNotValidException` — **400** — Walidacja DTO nie przeszła  
- `IllegalStateException` — **400** — Operacja niedozwolona w danym stanie  
- `HttpMessageNotReadableException` — **400** — Zły format JSON  
- `ConstraintViolationException` — **400** — Walidacja parametrów / XML import  
- `Exception` — **500** — Każdy inny błąd  

**Komunikaty błędów zwracane są jako prosty tekst.**

---

# Endpointy API

---

# AUTH

### POST /auth/register  
Rejestracja trenera.

**Walidacja:**
- imię i nazwisko — tylko litery
- hasło — min. 8 znaków, cyfra, wielka i mała litera, znak specjalny
- unikalny username

### POST /auth/login  
Sprawdza poprawność loginu i hasła.

---

# TRAINER

### GET /trainer/me  
Zwraca profil trenera.

### PUT /trainer/me  
Aktualizacja danych.

**Walidacja:**
- imię/nazwisko → tylko litery  
- numer telefonu → 9 cyfr

---

# TEAM

### POST /teams  
Tworzenie drużyny.

**Walidacja:**
- trener może mieć tylko jedną drużynę  
- nazwa drużyny musi być unikalna  

### GET /teams/mine  
Zwraca drużynę aktualnie zalogowanego trenera.

### PUT /teams/mine  
Zmiana nazwy drużyny.

**Walidacja:** unikalność nazwy

### DELETE /teams/mine  
Usunięcie drużyny.

**Walidacja:**  
- nie można usunąć drużyny, jeśli bierze udział w turnieju w statusie **IN_PROGRESS** lub **FINISHED**

---

# PLAYER

**Warunek:** każdy zawodnik należy do drużyny trenera, który go tworzy.

### POST /players  
Tworzenie zawodnika.

**Walidacja:**
- imię/nazwisko — niepuste, tylko litery  
- wiek — zakres **5–80** lat  
- max **20** zawodników w drużynie  
- pozycja: **GK / DEF / MID / ST**

### PUT /players/{id}  
Edycja zawodnika.

**Walidacja:**
- takie same zasady jak przy tworzeniu  
- zawodnik musi należeć do drużyny trenera  

### DELETE /players/{id}  
Usunięcie zawodnika.

**Walidacja:** zawodnik musi należeć do drużyny trenera  

---

# TOURNAMENT

### POST /tournaments (ADMIN)  
Tworzenie turnieju.

**Walidacja:**
- unikalna nazwa  
- data nie może być przeszła  
- liczba drużyn: **2 / 4 / 8 / 16**  
- status początkowy: **CREATED**

### GET /tournaments/{id}  
Zwraca:
- listę drużyn  
- status  
- winnerName i winnerId  
- startDate  
- teamCount  

### PUT /tournaments/{id}  
**Walidacja:**
- edycja tylko przy statusach: **CREATED**, **REGISTRATION_OPENED**  
- teamCount ≥ liczba zapisanych drużyn  
- data nie może być przeszła  

### DELETE /tournaments/{id} (ADMIN)  
**Walidacja:**
- nie można usuwać w trakcie **IN_PROGRESS**  
- usuwa wszystkie mecze danego turnieju  

### PATCH /tournaments/{id}/status  
Zmienia status turnieju:  
CREATED / REGISTRATION_OPENED / REGISTRATION_CLOSED / IN_PROGRESS / FINISHED

### GET /tournaments?status=...  
Filtrowanie turniejów po statusie.

### POST /tournaments/{id}/register  
Rejestracja drużyny do turnieju.

**Walidacja:**
- turniej w statusie **REGISTRATION_OPENED**  
- trener ma drużynę  
- drużyna: **7–10** zawodników  
- co najmniej **1 GK**  
- drużyna nie może rejestrować się dwa razy  
- limit drużyn nie może być przekroczony  
- po zapełnieniu — status zmienia się na REGISTRATION_CLOSED  

### POST /tournaments/import  
Import turniejów z XML.

---

# BRACKET

### GET /tournaments/{id}/matches  
Zwraca drabinkę turnieju w formie:

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

Dane są posortowane:
- rundy rosnąco (1 -> 2 -> … -> finał)
- mecze w rundzie po numerze meczu

Przykład:
Turniej 8 drużyn:
Runda 1 — Ćwierćfinały (4 mecze)
    8 drużyn -> 4 mecze - 4 zwycięzców
Runda 2 — Półfinały (2 mecze)
    4 drużyny -> 2 mecze - 2 zwycięzców
Runda 3 — Finał (1 mecz)
    2 drużyny -> 1 zwycięzca - turniej FINISHED

### POST /tournaments/{id}/next-round  
Generuje kolejną rundę meczów.

Zasady generowania:
- runda 1 - losowe pary drużyn  
- następne rundy - pary zwycięzców poprzedniej rundy  
- wynik meczu generowany losowo 0–5  
- zwycięzca awansuje dalej  
- kiedy zostaje 1 drużyna - turniej się kończy, ustawiany status FINISHED  

---

# Logika turniejów

- ADMIN tworzy turniej, ustawiony CREATED  
- ADMIN otwiera rejestrację, ustawia REGISTRATION_OPENED  
- Trenerzy zgłaszają drużyny  
- Gdy limit drużyn zrówna się z liczbą zgłoszonych, system ustawia REGISTRATION_CLOSED  
- ADMIN uruchamia 1 rundę, ustawiany status IN_PROGRESS  
- ADMIN generuje kolejne rundy aż do finału  
- System automatycznie ustala zwycięzcę i ustawia status FINISHED  

---

# Import turniejów z XML

Obsługiwany format:

```xml
<tournaments>
    <tournament>
        <name>Turniej 1</name>
        <startDate>2025-06-10</startDate>
        <teamCount>8</teamCount>
    </tournament>
</tournaments>
```

Każdy rekord jest walidowany tak samo jak żądania REST.

Zwracane są:
- liczba zaimportowanych
- liczba pominiętych
- lista błędów

---

# Testy jednostkowe

## BracketServiceTest sprawdza:
- poprawne generowanie pierwszej rundy  
- blokadę generowania rundy w przypadku turnieju zakończonego  
- blokadę generowania rundy, gdy turniej jest w niewłaściwym statusie  
- poprawne grupowanie meczów podczas pobierania drabinki  
- poprawne zwracanie pustej drabinki, gdy brak meczów  

## TournamentServiceTest sprawdza:
- blokadę tworzenia turnieju o istniejącej nazwie  
- walidację liczby drużyn 2, 4, 8, 16  
- pobieranie turnieju po ID  
- obsługę przypadku, gdy turniej nie istnieje  
- usuwanie turnieju  
- walidację rejestracji drużyny  

## TrainerServiceTest sprawdza:
- pobieranie profilu trenera z poprawną rolą  
- blokowanie dostępu dla roli innej niż TRAINER  
- aktualizację danych trenera  
- aktualizację pól  
- weryfikację zapisu do repozytorium  

## TrainerServiceTest 
- pobieranie profilu trenera z poprawną rolą  
- blokowanie dostępu dla roli innej niż TRAINER  
- aktualizację danych trenera  
- aktualizację pól  
- weryfikację zapisu do repozytorium  

