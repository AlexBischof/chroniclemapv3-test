package de.bischinger.cache;

import java.util.Random;

/**
 * Created by bischofa on 03/05/16.
 */
class Importer {
    public static void main(String[] args) throws Exception {
        // fillDb();
        try (VorwahlCacheImpl vorwahlCacheImpl = new VorwahlCacheImpl("vorwahl.dat")) {
            System.out.println(vorwahlCacheImpl.get("498912312"));
            System.out.println(vorwahlCacheImpl.get("498912312"));
            System.out.println(vorwahlCacheImpl.get("4989123"));
            System.out.println(vorwahlCacheImpl.get("49891231"));

        }
    }

    private static void fillDb() throws Exception {
        int maxSize = 50_000_000;
        try (VorwahlCacheImpl vorwahlCacheImpl = new VorwahlCacheImpl("vorwahl.dat", maxSize)) {

            new Random().ints().boxed().filter(n -> n > 0).limit(maxSize).forEach(n -> vorwahlCacheImpl.put(n + "", "Amsterdam"));

            System.out.println(vorwahlCacheImpl.get("498912238"));
        }
        System.out.println("Ende");
    }
}
