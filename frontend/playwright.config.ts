import { defineConfig, devices } from "@playwright/test";

// E2E tests expect the Java backend running on port 8080.
// Start it with: cd ../java && ./gradlew :chess-app:bootRun
// Then run tests with: npm run test:e2e

export default defineConfig({
  testDir: "./e2e",
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: "html",
  use: {
    baseURL: "http://localhost:3000",
    trace: "on-first-retry",
    video: "on",
  },
  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] },
    },
  ],
  webServer: {
    command: "npm run dev",
    url: "http://localhost:3000",
    reuseExistingServer: !process.env.CI,
    env: { BACKEND_URL: "http://localhost:8080" },
  },
});
