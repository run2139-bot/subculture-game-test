# Subculture Game Preference Test Project

## Tech Stack
- Frontend: React (Vite), Tailwind CSS, Lucide React
- Backend: Java 17, Spring Boot 3.2, Spring Data JPA, MySQL 8.0
- Tools: VS Code + Claude Code / Cursor

## Architecture Rules
- Backend follows Layered Architecture: Controller -> Service -> Repository -> Entity
- DTO must be used for all Request/Response objects (No raw Entity return)
- Frontend uses functional components with hooks, formatted with Tailwind CSS
- REST API endpoint prefix: `/api/v1`