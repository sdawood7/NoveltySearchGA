import java.io.*;
import java.util.*;
import java.text.*;

public class KModes
{
  // Amount of species desired
  private int speciesCount;
  // Map where key is the cluster centroid, and string array is all members of cluster(species)
  public Map<String, ArraysList<String>> speciesList;
  private int[][] comparisonMatrix;

  public KModes()
  {
    speciesCount = Parameters.species;
    speciesList = new Map<>();
    comparisonMatrix = new int[speciesCount][Search.member.length];
  }

  // Initialize KModess
  private void initKModes()
  {
    // Pick cluster centroids
    boolean [] usedCentroids = new boolean [speciesCount];
    String [] chosenCentroids = new String [speciesCount];

    for(int i = 0; i < speciesCount; i++)
    {
      int centroid = Search.r.nextInt();
      while(!usedCentroids[centroid]) // ensure centroid hasn't already been used
      {
        int centroid = Search.r.nextInt();
      }

      speciesList.put(Search.member[centroid], new ArrayList<String>());
    }

    // Sort clusters until no more changes need to be made
    While(sortClusters())
    {
      adjustCentroids();
    }

    // Initialize done
  }

  // Compute the hamming distance of two binary strings
  private static int hammingDistance(String str1, String str2)
  {
    int dist = 0;
    for(int i = 0; i < str1.length(); i++)
    {
      if(str1.charAt(i) != str2.charAt(i))
      {
        dist++;
      }
    }

    return dist;
  }

  private void setComparisonMatrix()
  {

  }

  // Sort pop members into their clusters, return false if changes were made, true otherwise
  private boolean sortClusters()
  {
    ArrayList<String>[] tempClusterList = new ArrayList[speciesCount];
    for (int i = 0; i < speciesCount; i++) {
            tempClusterList[i] = new ArrayList<String>();
        }

    boolean noChangeMade = true;
    int newClusterIndex;
    int cluster;
    String clusterKey;
    int previousDist;
    int newDist;
    for(i = 0; i < Search.member.length; i++)
    {
      previousDist = Integer.MAX_VALUE;
      newClusterIndex = 0;
      cluster = 0;
      for(String key : speciesList.keySet())
      {
        // Assign to a cluster if distance is now lower
        newDist = hammingDistance(Search.member[i].chromo, key);
        if(newDist < previousDist)
        {
          cluster = newClusterIndex;
          clusterKey = key;
          previousDist = newDist;
        }

        newClusterIndex++;
      }

      // Add member to temp arraylist for that cluster
      tempClusterList[cluster].add(Search.member[i].chromo);

      // Check if the member was moved to a different cluster
      if(speciesList.get(clusterKey).contains(Search.member[i].chromo))
        noChangeMade = false;
    }

    // Replace all cluster lists
    int i = 0
    for(String key : speciesList.keySet())
    {
      speciesList.put(key, tempClusterList[i]);
      i++
    }

    return noChangeMade;
  }

  // Adjust centriod based on cluster
  private void adjustCentroids()
  {

  }

  // Run KModes to speciate
  public void speciate()
  {
    // Sort clusters until no more changes need to be made
    While(sortClusters())
    {
      adjustCentroids();
    }
  }
}
