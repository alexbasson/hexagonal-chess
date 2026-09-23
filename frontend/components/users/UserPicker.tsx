"use client";

import type { User } from "@/lib/adapters";
import { Button } from "@/components/ui/button";

type Props = {
  users: User[];
  onSelect: (user: User) => void;
  onCreateNew?: () => void;
};

export function UserPicker({ users, onSelect, onCreateNew }: Props) {
  return (
    <div className="flex flex-col gap-2">
      <p className="text-sm text-muted-foreground mb-2">Who are you?</p>
      {users.map((user) => (
        <Button key={user.id} variant="outline" onClick={() => onSelect(user)} className="justify-start">
          {user.displayName}
        </Button>
      ))}
      <Button variant="ghost" onClick={onCreateNew}>
        Create new user
      </Button>
    </div>
  );
}
