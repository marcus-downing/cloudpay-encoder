package com.shrink_laureate.cloudpay.encode;

/**
 * Encoder that uses a BitBuffer2 to store a bit-dense representation
 * of the string.
 * 
 * Characters are represented as 6-bit base64.
 */
public class CloudPayEncoder3 implements CloudPayEncoder {
    private static final int COUNT_BITS = 3;
    private static final int CHAR_BITS = 6;

    /**
     * Encodes a character in 6 bits.
     * This only encodes uppercase and lowercase characters and numbers.
     * 
     * @param  c  a character in the right range
     * @return    a byte (actually only 6 bits) representing that character
     * @throws IllegalArgumentException if the character isn't in the valid range
     */
    private byte encodeChar(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (byte) (c - 'A');
        }
        if (c >= 'a' && c <= 'z') {
            return (byte) (26 + c - 'a');
        }
        if (c >= '0' && c <= '9') {
            return (byte) (52 + c - '0');
        }
        // if (c == '+') {
        //     return 62;
        // }
        // if (c == '/') {
        //     return 63;
        // }
        // return 63;
        throw new IllegalArgumentException("Unknown character: '"+c+"'");
    }

    /**
     * Decode a 6-bit character.
     * 
     * @param  b  a byte (actually only 6 bits) encoding a character
     * @return    the character represented by that number
     * @throws IllegalArgumentException if the number isn't in the valid range
     */
    private char decodeChar(byte b) {
        if (b < 26) {
            return (char) (b + 'A');
        }
        if (b < 52) {
            return (char) (b - 26 + 'a');
        }
        if (b < 62) {
            return (char) (b - 52 + '0');
        }
        // if (b == 62) {
        //     return '+';
        // }
        // if (b == 63) {
        //     return '/';
        // }
        // return ' ';
        throw new IllegalArgumentException("Unknown encoded character: '"+((int)b)+"'");
    }
    
    /**
     * Compress a string in the right format as a byte array.
     * 
     * @param   input  A string containing letters and numbers only
     * @return         The compressed representation of that string
     * @throws IllegalArgumentException if any character in the string isn't in the valid range
     */
    public byte[] encode(String input) {
        // window: the longest sequence that can be represented in one block
        int window = (int) Math.pow(2, COUNT_BITS) - 1;

        char[] inputChars = input.toCharArray();
        BitBuffer2 buffer = new BitBuffer2(input.length());
        
        // find repeating sequences of characters
        main_loop:
        for (int i = 0; i < inputChars.length; ) {
            for (int j = i + 1; j <= inputChars.length; j++) {
                if (j >= inputChars.length || inputChars[j] != inputChars[i] || j >= i + window) {
                    int count = j - i;
                    char c = inputChars[i];
                    buffer.put((byte) count, COUNT_BITS);
                    buffer.put(encodeChar(c), CHAR_BITS);
                    i = j;
                    continue main_loop;
                }
            }
        }

        // copy bytes to a buffer
        return buffer.toArray();
    }

    /**
     * Decompress a string as produced by the 'encode' method.
     * 
     * @param  input  A compressed string
     * @return        The decompressed string
     * @throws IllegalArgumentException if any encoded character isn't in the valid range
     */
    public String decode(byte[] input) {
        BitBuffer2 bytes = new BitBuffer2(input);
        StringBuffer buffer = new StringBuffer(input.length * 4);

        // add repeating sequences to a buffer
        while (bytes.has()) {
            int count = (int) bytes.get(COUNT_BITS);
            if (count == 0) {
                break;
            }
            char c = decodeChar(bytes.get(CHAR_BITS));
            for (int i = 0; i < count; i++) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}
