import java.io.*;
import java.util.*;
import java.text.*;

public class KModes
{
  // Amount of species desired
  private static int speciesCount;
  // Map where key is the cluster centroid, and string array is all members of cluster(species)
  public static Map<String, ArrayList<String>> speciesList;
  private int[][] comparisonMatrix;

  public KModes()
  {
    speciesCount = Parameters.species;
    speciesList = new HashMap<>();
    comparisonMatrix = new int[speciesCount][Search.member.length];

    initKModes();
  }

  // Initialize KModess
  private void initKModes()
  {
    // Pick cluster centroids
    boolean [] usedCentroids = new boolean [Search.member.length];
    String [] chosenCentroids = new String [speciesCount];

    for(int i = 0; i < speciesCount; i++)
    {
      int centroid = Search.r.nextInt(Search.member.length);
      while(usedCentroids[centroid]) // ensure centroid hasn't already been used
      {
        centroid = Search.r.nextInt(Search.member.length);
      }

      speciesList.put(Search.member[centroid].chromo, new ArrayList<String>());
    }
    // Sort clusters until no more changes need to be made
    while(!sortClusters())
    {
      adjustCentroids();
    }

    // Initialize done
    for(Map.Entry<String, ArrayList<String>> entry : speciesList.entrySet())
    {
      System.out.println(entry.getKey() + ":" + entry.getValue().toString());
    }
  }

  // Compute the hamming distance of two binary strings
  public static int hammingDistance(String str1, String str2)
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
  private static boolean sortClusters()
  {
    ArrayList<String>[] tempClusterList = new ArrayList[speciesCount];
    for (int i = 0; i < speciesCount; i++) {
            tempClusterList[i] = new ArrayList<String>();
        }

    boolean noChangeMade = true;
    int newClusterIndex;
    int cluster;
    String clusterKey = "";
    int previousDist;
    int newDist;
    for(int i = 0; i < Search.member.length; i++)
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
    int i = 0;
    for(String key : speciesList.keySet())
    {
      speciesList.put(key, tempClusterList[i]);
      i++;
    }

    return noChangeMade;
  }

  // Adjust centriod based on cluster
  private void adjustCentroids()
  {
    Map<String, ArrayList<String>> newSpeciesList = new HashMap<>();
    // Go through each species and build the new  centroid string
    String newCentroid = "";
    int oneSum;
    int zeroSum;
    for(String key : speciesList.keySet())
    {
      // Go through each gene
      for(int i = 0; i < Search.member[0].chromo.length(); i++)
      {
        oneSum = 0;
        zeroSum = 0;
        // Go through each member
        for(int j = 0; j < speciesList.get(key).get(0).length(); j++)
        {
          if(speciesList.get(key).get(j).charAt(i) == '1')
            oneSum++;
          else
            zeroSum++;
        }

        // If mode is 1 add 1 to the new centroid string, else add 0
        if(oneSum > zeroSum)
        {
          newCentroid += '1';
        }
        else
        {
          newCentroid += '0';
        }
      }

      // Now put new centroid in replacement map
      newSpeciesList.put(newCentroid, speciesList.get(key));
    }

    // Replace map with new map
    speciesList = newSpeciesList;
  }

  // Run KModes to speciate
  public void speciate()
  {
    // Sort clusters until no more changes need to be made
    while(sortClusters())
    {
      adjustCentroids();
    }
  }
}
