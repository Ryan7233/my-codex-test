using System.Collections;
using UnityEngine;

public class TowerController : MonoBehaviour
{
    [Header("Combat")]
    public float attackRange = 5f;
    public float fireRate = 1f;
    public float damage = 2f;
    public LayerMask enemyMask;

    [Header("Setup")]
    public Transform firePoint;
    public GameObject bulletPrefab;

    private float fireCooldown;

    private void Update()
    {
        fireCooldown -= Time.deltaTime;
        if (fireCooldown <= 0f)
        {
            TryShoot();
        }
    }

    private void TryShoot()
    {
        Collider[] hits = Physics.OverlapSphere(transform.position, attackRange, enemyMask);
        if (hits.Length == 0)
        {
            return;
        }

        Transform closest = null;
        float closestDistance = float.MaxValue;
        foreach (Collider hit in hits)
        {
            float dist = Vector3.Distance(transform.position, hit.transform.position);
            if (dist < closestDistance)
            {
                closestDistance = dist;
                closest = hit.transform;
            }
        }

        if (closest != null)
        {
            Shoot(closest);
            fireCooldown = 1f / fireRate;
        }
    }

    private void Shoot(Transform target)
    {
        if (bulletPrefab == null || firePoint == null)
        {
            return;
        }

        GameObject bulletObj = Instantiate(bulletPrefab, firePoint.position, firePoint.rotation);
        Bullet bullet = bulletObj.GetComponent<Bullet>();
        if (bullet != null)
        {
            bullet.Initialize(target, damage);
        }
    }

    private void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.yellow;
        Gizmos.DrawWireSphere(transform.position, attackRange);
    }
}
