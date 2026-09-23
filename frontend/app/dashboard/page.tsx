"use client";

import { useEffect, useState, useCallback } from "react";
import { useRouter } from "next/navigation";
import { getStoredUser, clearUser } from "@/lib/user-store";
import { listUsers } from "@/lib/api/users";
import { listFriends, addFriend, removeFriend } from "@/lib/api/friends";
import { listInvitations, createInvitation, acceptInvitation, declineInvitation } from "@/lib/api/invitations";
import { listGames } from "@/lib/api/games";
import { FriendsList } from "@/components/friends/FriendsList";
import { InvitationsList } from "@/components/invitations/InvitationsList";
import { UserCombobox } from "@/components/users/UserCombobox";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import type { User, Friend, Invitation, Game } from "@/lib/adapters";

export default function Dashboard() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const [friends, setFriends] = useState<Friend[]>([]);
  const [receivedInvitations, setReceivedInvitations] = useState<Invitation[]>([]);
  const [sentInvitations, setSentInvitations] = useState<Invitation[]>([]);
  const [games, setGames] = useState<Game[]>([]);
  const [addFriendUser, setAddFriendUser] = useState<User | null>(null);
  const [inviteUser, setInviteUser] = useState<User | null>(null);
  const [addFriendKey, setAddFriendKey] = useState(0);
  const [inviteKey, setInviteKey] = useState(0);

  const load = useCallback(async (user: User) => {
    const [allU, fr, recv, sent, gs] = await Promise.all([
      listUsers(),
      listFriends(user.id),
      listInvitations(user.id, "received"),
      listInvitations(user.id, "sent"),
      listGames(),
    ]);
    setAllUsers(allU);
    setFriends(fr.filter((f, i, arr) => arr.findIndex((x) => x.friendId === f.friendId) === i));
    setReceivedInvitations(recv.filter((i) => i.status === "pending"));
    setSentInvitations(sent);
    setGames(gs);
  }, []);

  useEffect(() => {
    const user = getStoredUser();
    if (!user) { router.replace("/"); return; }
    setCurrentUser(user);
    load(user);
  }, [router, load]);

  async function handleAddFriend() {
    if (!currentUser || !addFriendUser) return;
    await addFriend(currentUser.id, addFriendUser.id);
    setAddFriendUser(null);
    setAddFriendKey((k) => k + 1);
    load(currentUser);
  }

  async function handleRemoveFriend(friendId: string) {
    if (!currentUser) return;
    await removeFriend(currentUser.id, friendId);
    load(currentUser);
  }

  async function handleInvite() {
    if (!currentUser || !inviteUser) return;
    await createInvitation(currentUser.id, inviteUser.id);
    setInviteUser(null);
    setInviteKey((k) => k + 1);
    load(currentUser);
  }

  async function handleAccept(id: string) {
    const { id: gameId } = await acceptInvitation(id);
    router.push(`/games/${gameId}`);
  }

  async function handleDecline(id: string) {
    await declineInvitation(id);
    if (currentUser) load(currentUser);
  }

  function handleSwitchUser() {
    clearUser();
    router.push("/");
  }

  if (!currentUser) return null;

  const myGameIds = new Set([
    ...games.filter((g) => g.whiteName === currentUser.displayName || g.blackName === currentUser.displayName).map((g) => g.id),
  ]);

  return (
    <main className="max-w-5xl mx-auto p-4 sm:p-6 flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <div className="flex items-center gap-3">
          <span className="text-sm text-muted-foreground">Playing as <strong>{currentUser.displayName}</strong></span>
          <Button size="sm" variant="outline" onClick={handleSwitchUser}>Switch user</Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <CardHeader><CardTitle>Received Invitations</CardTitle></CardHeader>
          <CardContent>
            <InvitationsList
              invitations={receivedInvitations}
              allUsers={allUsers}
              currentUserId={currentUser.id}
              direction="received"
              onAccept={handleAccept}
              onDecline={handleDecline}
            />
          </CardContent>
        </Card>

        <Card>
          <CardHeader><CardTitle>My Games</CardTitle></CardHeader>
          <CardContent>
            {myGameIds.size === 0 ? (
              <p className="text-sm text-muted-foreground">No active games.</p>
            ) : (
              <ul className="flex flex-col gap-2">
                {games.filter((g) => myGameIds.has(g.id)).map((g) => (
                  <li key={g.id} className="flex items-center justify-between">
                    <span>{g.whiteName} vs {g.blackName}</span>
                    <Button size="sm" onClick={() => router.push(`/games/${g.id}`)}>Open</Button>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>

        <Card className="md:col-span-2">
          <CardHeader><CardTitle>Friends</CardTitle></CardHeader>
          <CardContent className="flex flex-col gap-4">
            <FriendsList
              friendIds={friends.map((f) => f.friendId)}
              allUsers={allUsers}
              onRemove={handleRemoveFriend}
            />
            <Separator />
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="flex gap-2">
                <div className="flex-1">
                  <UserCombobox
                    key={addFriendKey}
                    users={allUsers.filter((u) => u.id !== currentUser.id)}
                    onSelect={setAddFriendUser}
                    placeholder="Search by email to add friend"
                  />
                </div>
                <Button onClick={handleAddFriend} disabled={!addFriendUser}>Add friend</Button>
              </div>
              <div className="flex gap-2">
                <div className="flex-1">
                  <UserCombobox
                    key={inviteKey}
                    users={allUsers.filter((u) => u.id !== currentUser.id)}
                    onSelect={setInviteUser}
                    placeholder="Search by email to invite"
                  />
                </div>
                <Button variant="outline" onClick={handleInvite} disabled={!inviteUser}>Invite</Button>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="md:col-span-2">
          <CardHeader><CardTitle>Sent Invitations</CardTitle></CardHeader>
          <CardContent>
            <InvitationsList
              invitations={sentInvitations}
              allUsers={allUsers}
              currentUserId={currentUser.id}
              direction="sent"
              onAccept={() => {}}
              onDecline={() => {}}
            />
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
