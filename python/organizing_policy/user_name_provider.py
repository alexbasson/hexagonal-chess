from abc import ABC, abstractmethod


class UserNameProvider(ABC):
    @abstractmethod
    def get_display_name(self, user_id: str) -> str: ...
