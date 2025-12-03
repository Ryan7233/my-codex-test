using System.Collections;
using UnityEngine;

[RequireComponent(typeof(Collider))]
public class EnemyMover : MonoBehaviour
{
    [Header("Stats")]
    public float moveSpeed = 2f;
    public float maxHealth = 10f;
    public int reward = 10;

    [Header("Path")]
    public WaypointPath path;

    private float currentHealth;
    private int currentWaypointIndex;
    private bool isDead;
    private bool reachedEnd;

    public System.Action<EnemyMover> OnDeath;

    private void OnEnable()
    {
        currentHealth = maxHealth;
        currentWaypointIndex = 0;
        isDead = false;
        reachedEnd = false;
    }

    private void Update()
    {
        if (isDead || path == null || path.Count == 0)
        {
            return;
        }

        MoveAlongPath();
    }

    private void MoveAlongPath()
    {
        Transform waypoint = path.GetWaypoint(currentWaypointIndex);
        if (waypoint == null)
        {
            return;
        }

        Vector3 direction = waypoint.position - transform.position;
        float distanceThisFrame = moveSpeed * Time.deltaTime;

        if (direction.magnitude <= distanceThisFrame)
        {
            currentWaypointIndex++;
            if (currentWaypointIndex >= path.Count)
            {
                ReachDestination();
            }
        }
        else
        {
            transform.Translate(direction.normalized * distanceThisFrame, Space.World);
        }
    }

    private void ReachDestination()
    {
        if (reachedEnd)
        {
            return;
        }

        reachedEnd = true;
        NotifyDeath();
        Destroy(gameObject);
    }

    public void TakeDamage(float amount)
    {
        if (isDead)
        {
            return;
        }

        currentHealth -= amount;
        if (currentHealth <= 0)
        {
            Die();
        }
    }

    private void Die()
    {
        if (isDead)
        {
            return;
        }

        isDead = true;
        GameManager.Instance?.AddMoney(reward);
        NotifyDeath();
        Destroy(gameObject);
    }

    private void NotifyDeath()
    {
        OnDeath?.Invoke(this);
    }
}
