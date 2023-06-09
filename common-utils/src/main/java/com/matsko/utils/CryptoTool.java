package com.matsko.utils;

import org.hashids.Hashids;

/**
 * Class that encrypts the generated file reference.
 */
public class CryptoTool {

    /**
     * Class object from the library {@link Hashids} is being set.
     */
    private final Hashids hashids;

    /**
     * Constructor that accepts a salt and limits the minimum length of the generated hash.
     *
     * @param salt random data that is used as an additional input to a one-way function that hashes data.
     */
    public CryptoTool(String salt) {
        var minHashLength = 10;
        this.hashids = new Hashids(salt, minHashLength);
    }

    /**
     * Method that creates the hash.
     *
     * @param value file id.
     * @return the hash.
     */
    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    /**
     * Method that decrypt back to a number.
     *
     * @param value hash file id.
     * @return id.
     */
    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}