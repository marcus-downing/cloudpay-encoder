package com.shrink_laureate.cloudpay.encode;

/**
 * CloudPayEncoder - interview test
 * 
 * Compress a string in the right format, and decompress it.
 * Strings must only contain upper- and lowercase letters and numbers.
 * No space, punctuation or other characters allowed.
 */
public interface CloudPayEncoder {
    /**
     * Compress a string in the right format as a byte array.
     * 
     * @param   input  A string containing letters and numbers only
     * @return         The compressed representation of that string
     * @throws IllegalArgumentException if any character in the string isn't in the valid range
     */
    public byte[] encode(String input);

    /**
     * Decompress a string as produced by the 'encode' method.
     * 
     * @param  input  A compressed string
     * @return        The decompressed string
     * @throws IllegalArgumentException if any encoded character isn't in the valid range
     */
    public String decode(byte[] input);
}
