"use client";

import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import type { User } from "@/lib/adapters/types";

interface UserComboboxProps {
  users: User[];
  onSelect: (user: User) => void;
  placeholder?: string;
}

export function UserCombobox({ users, onSelect, placeholder }: UserComboboxProps) {
  return (
    <Command>
      <CommandInput placeholder={placeholder} />
      <CommandList>
        <CommandEmpty>No users found.</CommandEmpty>
        <CommandGroup>
          {users.map((user) => (
            <CommandItem
              key={user.id}
              value={`${user.email} ${user.displayName}`}
              onSelect={() => onSelect(user)}
            >
              <span>{user.displayName}</span>
              <span>{user.email}</span>
            </CommandItem>
          ))}
        </CommandGroup>
      </CommandList>
    </Command>
  );
}
