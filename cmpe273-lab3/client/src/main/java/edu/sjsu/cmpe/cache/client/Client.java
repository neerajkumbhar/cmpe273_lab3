package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.PrimitiveSink;
import edu.sjsu.cmpe.cache.methods.ConsistentHash;
import edu.sjsu.cmpe.cache.methods.RendezvousHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This Client class is used to demonstrate both Consistent Hashing and Rendezvous Hashing
//Please note that: for running Rendezvous Hashing technology you will have to uncomment the code for Consistent Hashing
public class Client {

    public static void main(String[] args) throws Exception {

        System.out.println("*********** Client started ***********");
        char[] Mappedvalues = {'X','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        List<CacheServiceInterface> AvailableServers = new ArrayList<CacheServiceInterface>();
        AvailableServers.add(new DistributedCacheService("http://localhost:3000"));
        AvailableServers.add(new DistributedCacheService("http://localhost:3001"));
        AvailableServers.add(new DistributedCacheService("http://localhost:3002"));

        //Start of Consistent hasing
        //Please uncomment below code for Consistent hashing
        ConsistentHash consistentHash=new ConsistentHash(com.google.common.hash.Hashing.md5(),3,AvailableServers);
        System.out.println("*********** Consistent Hashing ***********");
        for(int i=1; i<=10; i++)	{
            System.out.println("Input - Get value from key: "+i+ consistentHash.get(i));
            DistributedCacheService node=(DistributedCacheService)consistentHash.get(i);
            node.put(i,Character.toString(Mappedvalues[i]));
        }
        System.out.println("*********** Consistent Hashing ***********");
        for(int j=1; j<=10; j++)	{
            DistributedCacheService node=(DistributedCacheService)consistentHash.get(j);
            System.out.println("Output - Get("+j+") => value: "+node.get(j));
        }
       

        //RendezvousHash
       
/*
        RendezvousHash<String> RendezvousHash_consistentHash = new RendezvousHash(AvailableServers);
        for (int i = 1; i <= 10; i++) {
            addToCache(i, String.valueOf((char) (i + 96)), RendezvousHash_consistentHash);
        }
        System.out.println("Rendezvous Hash:-");
        for (int i = 1; i <= 10; i++) {
            CacheServiceInterface value = (DistributedCacheService) getFromCache(i, RendezvousHash_consistentHash);
            System.out.println("Output - Get (" + i + ") => "+ value.get(i));
        }

*/    
    }

    /*
    * Function used for Rendezvous Hashing
    * */
    public static void addToCache(int toAddKey, String toAddValue, RendezvousHash consistentHash) {
        CacheServiceInterface cache =(DistributedCacheService)consistentHash.getCache(toAddKey);
        cache.put(toAddKey, toAddValue);
        System.out.println("put(" + toAddKey + " => " + toAddValue + ")");
    }

    public static Object getFromCache(int key, RendezvousHash consistentHash) {
        CacheServiceInterface cache =  (DistributedCacheService)consistentHash.getCache(key);
        return cache;
    }
}
