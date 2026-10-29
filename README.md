# EvalForge AI

[![CI](https://github.com/thillainirmal-tech/EvalForge-AI/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/EvalForge-AI/actions/workflows/ci.yml)

A reproducible RAG evaluation and regression-gating workbench. It scores groundedness, context relevance, citation precision, expected-answer completeness and latency without paid APIs.

## Capabilities

- Inspectable deterministic metrics instead of a hidden evaluator prompt
- Invalid/duplicate citation detection
- Golden-answer term coverage
- Baseline-versus-candidate comparison with a 3% regression budget
- PASS, WARN and FAIL quality gates with actionable flags
- Browser dashboard, Actuator health, tests, CI and Docker

## Run

```bash
mvn spring-boot:run
```

Open `http://localhost:8089` or call `GET /api/evaluations/demo`.

## API

- `POST /api/evaluations` — score one RAG case
- `POST /api/evaluations/compare` — compare a candidate against a baseline
- `GET /api/evaluations/demo` — run a built-in golden case

## Verify

```bash
mvn verify
docker build -t evalforge-ai .
```

Deterministic lexical metrics are intentionally transparent and fast. They complement—not replace—human review and model-based semantic evaluation for production systems.

## License

MIT

