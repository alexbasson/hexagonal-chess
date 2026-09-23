from dataclasses import dataclass


@dataclass(frozen=True)
class InvitationId:
    value: str
