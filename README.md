# my-codex-test

This repository contains a small Unity tower-defense demo implemented in C# (Unity 2022+). The Unity scripts live under `UnityScripts/` and cover waypoint-driven enemies, towers that acquire targets via `Physics.OverlapSphere`, bullets that apply damage, basic wave management, and tower placement with raycasts.

## Unity setup quickstart
1. Open the project in Unity and load `MainScene`.
2. Ensure the prefabs `Enemy`, `Tower`, and `Bullet` exist and match the script expectations.
3. Create an empty `GameObject` named **Waypoints** with child transforms describing the enemy path; attach `WaypointPath` to it.
4. Attach scripts to prefabs/scene objects:
   - `EnemyMover` on the `Enemy` prefab; assign `path` to the `Waypoints` object.
   - `TowerController` on the `Tower` prefab; set `firePoint`, `bulletPrefab`, `attackRange`, `fireRate`, `damage`, and `enemyMask`.
   - `Bullet` on the `Bullet` prefab with a collider set as trigger; tune `speed`/`lifetime`.
   - `GameManager` on a scene controller object; assign UI text fields, `nextWaveButton`, `enemyPrefab`, and `path`. Optionally assign `towerPlacer`.
   - `TowerPlacer` on a scene controller object if you want runtime placement; set `towerPrefab`, `towerCost`, `placementMask`, and optional `placementYOffset`.
5. Wire the UI button to call `GameManager.StartNextWave` if not assigned automatically.
6. Enter Play mode to start spawning waves and placing towers.

## Notes
- Enemy movement uses `Update` to progress along waypoints.
- Towers search for targets inside an overlap sphere each frame and fire bullets at the nearest enemy within range.
- Waves are tracked by `GameManager`; the next wave button is disabled while enemies are spawning or alive.

## Git remote
If you cloned the project without a configured remote, add one before pushing changes:

```bash
git remote add origin https://github.com/Ryan7233/my-codex-test.git
git push -u origin work
```

## Copying the Unity scripts to a local directory
If you simply need the Unity C# files on a local machine (for example `/Users/yanran/PycharmProjects`), run the helper script from the repo root:

```bash
./copy_unity_scripts.sh               # copies into /Users/yanran/PycharmProjects by default
./copy_unity_scripts.sh /custom/path  # optionally choose another target directory
```

The script copies every `*.cs` file from `UnityScripts/` into the chosen folder so you can drop them into your Unity project.
