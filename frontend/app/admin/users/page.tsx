"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { listUsers, createUser, deleteUser } from "@/lib/api/users";
import { CreateUserForm } from "@/components/users/CreateUserForm";
import { getStoredUser } from "@/lib/user-store";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import type { User } from "@/lib/adapters";

export default function AdminUsersPage() {
  const router = useRouter();
  const [users, setUsers] = useState<User[]>([]);
  const [showCreate, setShowCreate] = useState(false);

  useEffect(() => {
    const current = getStoredUser();
    if (!current) { router.replace("/"); return; }
    listUsers().then(setUsers);
  }, [router]);

  async function handleCreate({ email, displayName }: { email: string; displayName: string }) {
    await createUser(email, displayName);
    setShowCreate(false);
    listUsers().then(setUsers);
  }

  async function handleDelete(id: string) {
    await deleteUser(id);
    listUsers().then(setUsers);
  }

  return (
    <main className="max-w-2xl mx-auto p-4 flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Users</h1>
        <div className="flex gap-2">
          <Button size="sm" variant="outline" onClick={() => router.push("/dashboard")}>← Dashboard</Button>
          <Button size="sm" onClick={() => setShowCreate((v) => !v)}>
            {showCreate ? "Cancel" : "New user"}
          </Button>
        </div>
      </div>

      {showCreate && (
        <Card>
          <CardContent className="pt-6">
            <CreateUserForm onSubmit={handleCreate} />
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader><CardTitle>All users</CardTitle></CardHeader>
        <CardContent>
          {users.length === 0 ? (
            <p className="text-sm text-muted-foreground">No users yet.</p>
          ) : (
            <ul className="flex flex-col gap-2">
              {users.map((user, i) => (
                <li key={user.id}>
                  {i > 0 && <Separator className="mb-2" />}
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">{user.displayName}</p>
                      <p className="text-sm text-muted-foreground">{user.email}</p>
                      <p className="text-xs text-muted-foreground font-mono">{user.id}</p>
                    </div>
                    <Button size="sm" variant="destructive" onClick={() => handleDelete(user.id)}>Delete</Button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </CardContent>
      </Card>
    </main>
  );
}
