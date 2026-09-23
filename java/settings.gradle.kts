rootProject.name = "hexagonal-chess"

include(
    "gameplay-policy",
    "gameplay-api-adapter",
    "gameplay-db-adapter",
    "organizing-policy",
    "organizing-api-adapter",
    "organizing-db-adapter",
    "cross-context-adapter",
    "user-management-policy",
    "user-management-db-adapter",
    "user-management-api-adapter",
    "user-management-organizing-adapter",
    "chess-app"
)
