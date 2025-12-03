using UnityEngine;

public class TowerPlacer : MonoBehaviour
{
    public GameObject towerPrefab;
    public float towerCost = 20f;
    public LayerMask placementMask;
    public float placementYOffset = 0f;

    private Camera mainCamera;

    private void Start()
    {
        mainCamera = Camera.main;
    }

    private void Update()
    {
        if (Input.GetMouseButtonDown(0))
        {
            TryPlaceTower();
        }
    }

    private void TryPlaceTower()
    {
        if (towerPrefab == null || GameManager.Instance == null)
        {
            return;
        }

        if (!GameManager.Instance.CanAfford(towerCost))
        {
            return;
        }

        Ray ray = mainCamera.ScreenPointToRay(Input.mousePosition);
        if (Physics.Raycast(ray, out RaycastHit hit, 1000f, placementMask))
        {
            Vector3 position = hit.point + Vector3.up * placementYOffset;
            Instantiate(towerPrefab, position, Quaternion.identity);
            GameManager.Instance.SpendMoney(towerCost);
        }
    }
}
