"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { UserPicker } from "@/components/users/UserPicker";
import { CreateUserForm } from "@/components/users/CreateUserForm";
import { listUsers, createUser } from "@/lib/api/users";
import { getStoredUser, storeUser } from "@/lib/user-store";
import type { User } from "@/lib/adapters";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function Home() {
  const router = useRouter();
  const [users, setUsers] = useState<User[]>([]);
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = getStoredUser();
    if (stored) {
      router.replace("/dashboard");
      return;
    }
    listUsers().then((u) => {
      setUsers(u);
      setLoading(false);
    });
  }, [router]);

  async function handleSelect(user: User) {
    storeUser(user);
    router.push("/dashboard");
  }

  async function handleCreate({ email, displayName }: { email: string; displayName: string }) {
    const { id } = await createUser(email, displayName);
    storeUser({ id, email, displayName });
    router.push("/dashboard");
  }

  if (loading) return <div className="p-8 text-muted-foreground">Loading…</div>;

  return (
    <main className="min-h-screen flex items-center justify-center p-4">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>Hexagonal Chess</CardTitle>
        </CardHeader>
        <CardContent>
          {showCreate ? (
            <>
              <CreateUserForm onSubmit={handleCreate} />
              <button className="mt-3 text-sm text-muted-foreground underline" onClick={() => setShowCreate(false)}>
                Back
              </button>
            </>
          ) : (
            <UserPicker users={users} onSelect={handleSelect} onCreateNew={() => setShowCreate(true)} />
          )}
        </CardContent>
      </Card>
    </main>
  );
}
