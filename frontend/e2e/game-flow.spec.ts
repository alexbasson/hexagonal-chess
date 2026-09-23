import { test, expect, type Page } from "@playwright/test";

// Each run uses unique credentials to avoid conflicts with stale backend state.
// The backend must be running and reachable via BACKEND_URL (default: http://localhost:8080).

// react-chessboard v5 uses dnd-kit (pointer events). dragTo does not reliably trigger it,
// so we use the mouse API with intermediate steps and a brief hold.
async function dragPiece(page: Page, from: string, to: string) {
  const src = page.locator(`[data-square="${from}"]`).first();
  const tgt = page.locator(`[data-square="${to}"]`).first();
  const srcBox = await src.boundingBox();
  const tgtBox = await tgt.boundingBox();
  if (!srcBox || !tgtBox) throw new Error(`Square not found: ${from} or ${to}`);
  const sx = srcBox.x + srcBox.width / 2;
  const sy = srcBox.y + srcBox.height / 2;
  const tx = tgtBox.x + tgtBox.width / 2;
  const ty = tgtBox.y + tgtBox.height / 2;
  await page.mouse.move(sx, sy);
  await page.mouse.down();
  await page.mouse.move(sx + 1, sy + 1);          // small nudge to activate dnd-kit sensor
  await page.mouse.move(tx, ty, { steps: 15 });
  await page.waitForTimeout(50);
  await page.mouse.up();
}

test("full game flow", async ({ page }) => {
  const ts = Date.now();
  const aliceEmail = `alice.${ts}@example.com`;
  const aliceName  = `Alice${ts}`;
  const bobEmail   = `bob.${ts}@example.com`;
  const bobName    = `Bob${ts}`;

  // --- Create Alice ---
  await page.goto("/");
  await page.getByRole("button", { name: "Create new user" }).click();
  await page.getByLabel("Email").fill(aliceEmail);
  await page.getByLabel("Display Name").fill(aliceName);
  await page.getByRole("button", { name: "Create" }).click();
  await expect(page.getByText(`Playing as ${aliceName}`)).toBeVisible();

  // --- Switch to Bob and create account ---
  await page.getByRole("button", { name: "Switch user" }).click();
  await page.getByRole("button", { name: "Create new user" }).click();
  await page.getByLabel("Email").fill(bobEmail);
  await page.getByLabel("Display Name").fill(bobName);
  await page.getByRole("button", { name: "Create" }).click();
  await expect(page.getByText(`Playing as ${bobName}`)).toBeVisible();

  // --- Bob adds Alice as a friend ---
  const addFriendCmd = () => page.locator("[cmdk-root]").filter({ has: page.getByPlaceholder("Search by email to add friend") });
  const inviteCmd    = () => page.locator("[cmdk-root]").filter({ has: page.getByPlaceholder("Search by email to invite") });

  await page.getByPlaceholder("Search by email to add friend").fill(aliceEmail);
  await addFriendCmd().getByRole("option", { name: new RegExp(aliceName) }).first().click();
  await page.getByRole("button", { name: "Add friend" }).click();
  await expect(page.locator("li").filter({ hasText: aliceName }).filter({ hasText: "Remove" })).toBeVisible();

  // --- Switch to Alice ---
  await page.getByRole("button", { name: "Switch user" }).click();
  await page.getByRole("button", { name: aliceName }).first().click();
  await expect(page.getByText(`Playing as ${aliceName}`)).toBeVisible();

  // --- Alice adds Bob as a friend ---
  await page.getByPlaceholder("Search by email to add friend").fill(bobEmail);
  await addFriendCmd().getByRole("option", { name: new RegExp(bobName) }).first().click();
  await page.getByRole("button", { name: "Add friend" }).click();
  await expect(page.locator("li").filter({ hasText: bobName }).filter({ hasText: "Remove" })).toBeVisible();

  // --- Alice invites Bob to play ---
  await page.getByPlaceholder("Search by email to invite").fill(bobEmail);
  await inviteCmd().getByRole("option", { name: new RegExp(bobName) }).first().click();
  await page.getByRole("button", { name: "Invite" }).click();
  await expect(page.locator("li").filter({ hasText: bobName }).filter({ hasText: "pending" })).toBeVisible();

  // --- Switch to Bob ---
  await page.getByRole("button", { name: "Switch user" }).click();
  await page.getByRole("button", { name: bobName }).first().click();
  await expect(page.getByText(`Playing as ${bobName}`)).toBeVisible();

  // --- Bob accepts Alice's invitation ---
  await expect(page.locator("li").filter({ hasText: aliceName }).filter({ hasText: "Accept" })).toBeVisible();
  await page.getByRole("button", { name: "Accept" }).click();

  // --- Bob sees the game board ---
  await expect(page.getByText(`White: ${aliceName}`)).toBeVisible();
  await expect(page.getByText(`Black: ${bobName}`)).toBeVisible();
  await expect(page.getByText(new RegExp(`Waiting for ${aliceName}`))).toBeVisible();

  // --- Bob goes to dashboard ---
  await page.getByRole("button", { name: /Dashboard/ }).click();
  await expect(page.getByText(`Playing as ${bobName}`)).toBeVisible();

  // --- Switch to Alice ---
  await page.getByRole("button", { name: "Switch user" }).click();
  await page.getByRole("button", { name: aliceName }).first().click();
  await expect(page.getByText(`Playing as ${aliceName}`)).toBeVisible();

  // --- Alice opens the game ---
  const gameLabel = `${aliceName} vs ${bobName}`;
  await expect(page.locator("li").filter({ hasText: gameLabel })).toBeVisible();
  await page.locator("li").filter({ hasText: gameLabel }).getByRole("button", { name: "Open" }).click();
  await expect(page.getByText(/White's turn.*your move/)).toBeVisible();

  // --- Alice moves e2 to e4 ---
  await dragPiece(page, "e2", "e4");
  await expect(page.getByText(new RegExp(`Waiting for ${bobName}`))).toBeVisible();

  // --- Alice goes back to dashboard ---
  await page.getByRole("button", { name: /Dashboard/ }).click();

  // --- Switch to Bob ---
  await page.getByRole("button", { name: "Switch user" }).click();
  await page.getByRole("button", { name: bobName }).first().click();
  await expect(page.getByText(`Playing as ${bobName}`)).toBeVisible();

  // --- Bob opens the game ---
  await expect(page.locator("li").filter({ hasText: gameLabel })).toBeVisible();
  await page.locator("li").filter({ hasText: gameLabel }).getByRole("button", { name: "Open" }).click();
  await expect(page.getByText(/Black's turn.*your move/)).toBeVisible();

  // --- Bob tries an illegal move: g8 to g6 ---
  await dragPiece(page, "g8", "g6");
  await expect(page.getByText("Illegal move.")).toBeVisible();

  // --- Bob makes a legal move: g8 to f6 ---
  await dragPiece(page, "g8", "f6");
  await expect(page.getByText(new RegExp(`Waiting for ${aliceName}`))).toBeVisible();
});
