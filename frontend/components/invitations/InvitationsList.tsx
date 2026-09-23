"use client";

import type { Invitation, User } from "@/lib/adapters";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";

type Props = {
  invitations: Invitation[];
  allUsers: User[];
  currentUserId: string;
  direction: "sent" | "received";
  onAccept: (id: string) => void;
  onDecline: (id: string) => void;
};

export function InvitationsList({ invitations, allUsers, direction, onAccept, onDecline }: Props) {
  if (invitations.length === 0) {
    return <p className="text-sm text-muted-foreground">No invitations.</p>;
  }

  return (
    <ul className="flex flex-col gap-3">
      {invitations.map((inv) => {
        const otherId = direction === "received" ? inv.invitingUserId : inv.invitedUserId;
        const other = allUsers.find((u) => u.id === otherId);
        return (
          <li key={inv.id} className="flex items-center justify-between gap-2">
            <span>{other?.displayName ?? otherId}</span>
            <div className="flex items-center gap-2">
              <Badge variant={inv.status === "pending" ? "outline" : "secondary"}>{inv.status}</Badge>
              {direction === "received" && inv.status === "pending" && (
                <>
                  <Button size="sm" onClick={() => onAccept(inv.id)}>Accept</Button>
                  <Button size="sm" variant="outline" onClick={() => onDecline(inv.id)}>Decline</Button>
                </>
              )}
            </div>
          </li>
        );
      })}
    </ul>
  );
}
