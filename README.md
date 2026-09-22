# Hexagonal Chess

Sample implementations of [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) in Java, Python, and Ruby, using an online chess game as the domain.

The goal is to show how the same architectural principles apply across languages, and how strict module boundaries make the structure legible regardless of the ecosystem.

## Domain

The application has two bounded contexts:

- **Gameplay** — the rules of chess: setting up boards, validating and recording moves.
- **Organizing Games** — arranging games between players: starting games, listing them, looking them up.

The two contexts are joined by a cross-context adapter (`GameplayGameInitializer`) that bridges them at the boundary, without either context depending on the other.

## Architecture

Each implementation follows the same structure:

```
<bc>_policy/          Domain: value objects, use cases, port interfaces
<bc>_api_adapter/     Primary adapter: HTTP routes that call into the domain
<bc>_db_adapter/      Secondary adapter: in-memory repository implementations
cross_context_adapter/ Bridges the two bounded contexts
chess_app/            Wires everything together into a single deployable
```

The rules enforced in every implementation:

- Domain modules have no dependencies outside themselves.
- Adapter modules may depend on domain modules, but not on the deployable.
- The deployable may depend on everything.

## Running the apps

### Java

Requires Java 21. Uses Gradle (wrapper included).

```bash
cd java
./gradlew :chess-app:bootRun
```

Runs on `http://localhost:8080`.

To run the tests:

```bash
./gradlew test
```

### Ruby

Requires Ruby 4.0.5 and Bundler.

```bash
cd ruby
bundle install
bundle exec rackup chess_app/config.ru
```

Runs on `http://localhost:9292`.

To run the tests:

```bash
bundle exec rake
```

### Python

Requires Python 3.14 and [uv](https://docs.astral.sh/uv/).

```bash
cd python
uv sync
uv run python -m chess_app.main
```

Runs on `http://localhost:8000`.

To run the tests:

```bash
uv run pytest
```

## API

All three apps expose the same HTTP API:

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/games` | Start a new game |
| `GET` | `/games` | List all games |
| `GET` | `/games/:id` | Get a game |
| `GET` | `/games/:id/board` | Get the current board state |
| `POST` | `/games/:id/moves` | Make a move |

**Start a game:**
```bash
curl -X POST http://localhost:8080/games \
  -H 'Content-Type: application/json' \
  -d '{"whiteName": "Alice", "blackName": "Bob"}'
```

**Make a move** (using the `gameId` from the response above):
```bash
curl -X POST http://localhost:8080/games/<gameId>/moves \
  -H 'Content-Type: application/json' \
  -d '{"from": "e2", "to": "e4"}'
```

> The Java app uses `whiteName`/`blackName` and `from`/`to` in request bodies. The Python and Ruby apps use `white_name`/`black_name` and `from_square`/`to_square`.
