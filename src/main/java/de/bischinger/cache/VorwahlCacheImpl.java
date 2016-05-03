package de.bischinger.cache;

import net.openhft.chronicle.map.ChronicleMap;

import java.io.File;
import java.io.IOException;

import static java.lang.Integer.valueOf;

/**
 * Created by bischofa on 02/05/16.
 */
class VorwahlCacheImpl implements VorwahlCache {

    private final ChronicleMap<Integer, CharSequence> vorwahlCache;

    public VorwahlCacheImpl(String fileName, long size) throws IOException {
        vorwahlCache = ChronicleMap
                .of(Integer.class, CharSequence.class)
                .entries(size)
                .averageValue("Amsterdam")
                .createPersistedTo(new File(fileName));
    }

    public VorwahlCacheImpl(String fileName) throws IOException {
        this(fileName, 50_000_000L);
    }

    @Override
    public CharSequence get(String phonenumber) {

        CharSequence payload;

        int number = valueOf(phonenumber);
        do {
            payload = vorwahlCache.get(number);
            number /= 10;
        } while (payload == null && number > 9999);

        return payload;
    }

    public CharSequence put(String key, CharSequence city) {
        return vorwahlCache.put(valueOf(key), city);
    }

    public int size() {
        return vorwahlCache.size();
    }

    @Override
    public void close() throws Exception {
        vorwahlCache.close();
    }
}
