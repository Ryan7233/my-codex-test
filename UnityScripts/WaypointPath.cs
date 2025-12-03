using System.Collections.Generic;
using UnityEngine;

public class WaypointPath : MonoBehaviour
{
    [Tooltip("Waypoints that define the enemy path.")]
    public List<Transform> waypoints = new List<Transform>();

    private void Awake()
    {
        // Auto-populate from children if none assigned
        if (waypoints.Count == 0)
        {
            foreach (Transform child in transform)
            {
                waypoints.Add(child);
            }
        }
    }

    public Transform GetWaypoint(int index)
    {
        if (index < 0 || index >= waypoints.Count)
        {
            return null;
        }

        return waypoints[index];
    }

    public int Count => waypoints.Count;
}
