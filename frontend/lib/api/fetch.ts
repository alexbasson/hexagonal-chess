export type Fetcher = typeof fetch;

export async function fetchJson<T>(url: string, init?: RequestInit, fetcher: Fetcher = fetch): Promise<T> {
  const res = await fetcher(url, init);
  if (!res.ok) throw new Error(`Request failed: ${res.status}`);
  return res.json();
}
