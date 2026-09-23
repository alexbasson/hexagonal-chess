#!/usr/bin/env node
import { select, input } from "@inquirer/prompts";
import { spawn } from "child_process";
import { writeFileSync } from "fs";
import { resolve, dirname } from "path";
import { fileURLToPath } from "url";

const __dirname = dirname(fileURLToPath(import.meta.url));

const BACKENDS = {
  java: {
    label: "Java (Spring Boot) — port 8080",
    url: "http://localhost:8080",
    cwd: resolve(__dirname, "../java"),
    cmd: "./gradlew",
    args: [":chess-app:bootRun"],
  },
  python: {
    label: "Python (FastAPI) — port 8000",
    url: "http://localhost:8000",
    cwd: resolve(__dirname, "../python"),
    cmd: "uv",
    args: ["run", "python", "-m", "chess_app.main"],
  },
  ruby: {
    label: "Ruby (Sinatra) — port 9292",
    url: "http://localhost:9292",
    cwd: resolve(__dirname, "../ruby"),
    cmd: "bundle",
    args: ["exec", "rackup", "chess_app/config.ru"],
  },
};

const choice = await select({
  message: "Which backend would you like to run?",
  choices: Object.entries(BACKENDS).map(([value, { label }]) => ({ value, name: label })),
});

const backend = BACKENDS[choice];
console.log(`\nStarting ${backend.label}...`);

writeFileSync(resolve(__dirname, ".env.local"), `BACKEND_URL=${backend.url}\n`);

const backendProcess = spawn(backend.cmd, backend.args, {
  cwd: backend.cwd,
  stdio: "inherit",
  shell: process.platform === "win32",
});

const frontendProcess = spawn("npm", ["run", "dev"], {
  cwd: __dirname,
  stdio: "inherit",
  shell: process.platform === "win32",
  env: { ...process.env, BACKEND_URL: backend.url },
});

for (const sig of ["SIGINT", "SIGTERM"]) {
  process.on(sig, () => {
    backendProcess.kill();
    frontendProcess.kill();
    process.exit(0);
  });
}

backendProcess.on("exit", (code) => {
  if (code !== 0 && code !== null) console.error(`Backend exited with code ${code}`);
  frontendProcess.kill();
  process.exit(code ?? 0);
});
