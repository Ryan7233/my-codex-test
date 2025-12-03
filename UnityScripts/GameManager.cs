using System.Collections;
using UnityEngine;
using UnityEngine.UI;

public class GameManager : MonoBehaviour
{
    public static GameManager Instance { get; private set; }

    [Header("Economy")]
    public int startingMoney = 100;
    public Text moneyText;

    [Header("Waves")]
    public Text waveText;
    public Button nextWaveButton;
    public GameObject enemyPrefab;
    public WaypointPath path;
    public int baseEnemiesPerWave = 5;
    public float spawnInterval = 0.8f;

    [Header("Managers")]
    public TowerPlacer towerPlacer;

    private int currentWave = 0;
    private int money;
    private bool waveInProgress;
    private bool spawning;
    private int aliveEnemies;

    private void Awake()
    {
        if (Instance != null && Instance != this)
        {
            Destroy(gameObject);
            return;
        }

        Instance = this;
    }

    private void Start()
    {
        money = startingMoney;
        UpdateUI();
        if (nextWaveButton != null)
        {
            nextWaveButton.onClick.AddListener(StartNextWave);
            nextWaveButton.interactable = true;
        }
    }

    public void AddMoney(int amount)
    {
        money += amount;
        UpdateUI();
    }

    public bool CanAfford(float cost)
    {
        return money >= cost;
    }

    public void SpendMoney(float cost)
    {
        money -= Mathf.RoundToInt(cost);
        UpdateUI();
    }

    public void StartNextWave()
    {
        if (waveInProgress || enemyPrefab == null || path == null)
        {
            return;
        }

        currentWave++;
        UpdateUI();
        StartCoroutine(SpawnWave());
        if (nextWaveButton != null)
        {
            nextWaveButton.interactable = false;
        }
    }

    private IEnumerator SpawnWave()
    {
        waveInProgress = true;
        spawning = true;
        int enemyCount = baseEnemiesPerWave + (currentWave - 1) * 2;

        for (int i = 0; i < enemyCount; i++)
        {
            SpawnEnemy();
            yield return new WaitForSeconds(spawnInterval);
        }

        spawning = false;
        TryFinishWave();
    }

    private void SpawnEnemy()
    {
        Transform firstWaypoint = path.GetWaypoint(0);
        if (firstWaypoint == null)
        {
            return;
        }

        GameObject enemyObj = Instantiate(enemyPrefab, firstWaypoint.position, Quaternion.identity);
        EnemyMover mover = enemyObj.GetComponent<EnemyMover>();
        if (mover != null)
        {
            mover.path = path;
            mover.OnDeath += HandleEnemyDeath;
            aliveEnemies++;
        }
        else
        {
            Destroy(enemyObj);
        }
    }

    private void HandleEnemyDeath(EnemyMover mover)
    {
        aliveEnemies = Mathf.Max(0, aliveEnemies - 1);
        TryFinishWave();
    }

    private void TryFinishWave()
    {
        if (spawning || aliveEnemies > 0)
        {
            return;
        }

        waveInProgress = false;
        if (nextWaveButton != null)
        {
            nextWaveButton.interactable = true;
        }
    }

    private void UpdateUI()
    {
        if (moneyText != null)
        {
            moneyText.text = $"Money: {money}";
        }

        if (waveText != null)
        {
            waveText.text = $"Wave: {currentWave}";
        }
    }
}
