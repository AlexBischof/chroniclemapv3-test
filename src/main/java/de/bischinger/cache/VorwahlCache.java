package de.bischinger.cache;

import java.io.IOException;

/**
 * Created by bischofa on 03/05/16.
 */
public interface VorwahlCache extends AutoCloseable {
    CharSequence get(String phonenumber);

    static VorwahlCache create(String fileName) throws IOException {
        return new VorwahlCacheImpl(fileName);
    }
}
