"use client";

import type { User } from "@/lib/adapters";
import { Button } from "@/components/ui/button";

type Props = {
  friendIds: string[];
  allUsers: User[];
  onRemove: (friendId: string) => void;
};

export function FriendsList({ friendIds, allUsers, onRemove }: Props) {
  if (friendIds.length === 0) {
    return <p className="text-sm text-muted-foreground">No friends yet.</p>;
  }

  return (
    <ul className="flex flex-col gap-2">
      {friendIds.map((id) => {
        const user = allUsers.find((u) => u.id === id);
        return (
          <li key={id} className="flex items-center justify-between">
            <span>{user?.displayName ?? id}</span>
            <Button size="sm" variant="destructive" onClick={() => onRemove(id)}>
              Remove
            </Button>
          </li>
        );
      })}
    </ul>
  );
}
