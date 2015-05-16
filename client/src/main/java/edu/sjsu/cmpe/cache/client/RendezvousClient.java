package edu.sjsu.cmpe.cache.client;

import edu.sjsu.cmpe.cache.client.rendezvous.RendezvousHash;

import java.util.ArrayList;

/**
 * Created by kaustubh on 04/05/15.
 */
public class RendezvousClient {
    public static void main(String[] args) {

        System.out.println("---------------------------------------------------------------");
        System.out.println("-------------Starting Rendezvous Cache Client------------------");


        String cache1 = "http://localhost:3000";
        String cache2 = "http://localhost:3001";
        String cache3 = "http://localhost:3002";


        ArrayList collection = new ArrayList();
        collection.add(cache1);
        collection.add(cache2);
        collection.add(cache3);


        System.out.println("---------------------------------------------------------------");
        System.out.println("------------------Add to the distributed caches----------------");
        System.out.println("---------------------------------------------------------------");


        RendezvousHash<String> consistentHash = new RendezvousHash(collection);

        for (int i = 1; i <= 10; i++) {

            addToCache(i, String.valueOf((char) (i + 96)), consistentHash);
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("-----------Retrieve from the distributed caches----------------");
        System.out.println("---------------------------------------------------------------");
        for (int i = 1; i <= 10; i++) {
            String value = (String) getFromCache(i, consistentHash);
            //System.out.println("got (" + i + ") => " + value + " from " + cache.toString());
        }

        System.out.println("Exiting Cache Client...");
    }

    public static void addToCache(int toAddKey, String toAddValue, RendezvousHash consistentHash) {
        String cacheUrl = (String) consistentHash.getCache(toAddKey);
        CacheServiceInterface cache = new DistributedCacheService(cacheUrl);
        cache.put(toAddKey, toAddValue);

        System.out.println("put(" + toAddKey + " => " + toAddValue + ")" + " to "+cacheUrl);
        //System.out.println("added (" + toAddKey + " => " + toAddValue + ")" + " On " + cacheUrl);
    }

    public static Object getFromCache(int key, RendezvousHash consistentHash) {
        String cacheUrl = (String) consistentHash.getCache(key);
        CacheServiceInterface cache = new DistributedCacheService(cacheUrl);

        String value = cache.get(key);
        System.out.println("get(" + key + ") => " + value + " from "+cacheUrl);
//        System.out.println("got (" + key + ") => " + cache.get(key) + " from " + cacheUrl);
        return value;
    }
}
