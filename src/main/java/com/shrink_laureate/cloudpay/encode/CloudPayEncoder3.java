package com.shrink_laureate.cloudpay.encode;

public class CloudPayEncoder3 implements CloudPayEncoder {
    private static boolean DEBUG = false;

    private static final int COUNT_BITS = 4;
    private static final int CHAR_BITS = 6;

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
        return 63;
    }

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
        return ' ';
    }
    
    public byte[] encode(String input) {
        if (DEBUG) System.out.println(" * Encoding "+input+" ("+input.length()+")");
        char[] inputChars = input.toCharArray();
        BitBuffer2 buffer = new BitBuffer2(input.length());
        
        // find repeating sequences of characters
        main_loop:
        for (int i = 0; i < inputChars.length; ) {
            for (int j = i + 1; j <= inputChars.length; j++) {
                if (j >= inputChars.length || inputChars[j] != inputChars[i]) {
                    int count = j - i;
                    char c = inputChars[i];
                    if (DEBUG) System.out.println("   - Buffering "+count+" * '"+c+"'");
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

    public String decode(byte[] input) {
        if (DEBUG) {
            System.out.print("   = [");
            for (byte b : input) {
                int i = b & 0xFF;
                System.out.print("'"+i+"',");
            }
            System.out.println("]");
        }

        BitBuffer2 bytes = new BitBuffer2(input);

        StringBuffer buffer = new StringBuffer(input.length * 4);

        // add repeating sequences to a buffer
        while (bytes.has()) {
            int count = (int) bytes.get(COUNT_BITS);
            if (count == 0) {
                break;
            }
            char c = decodeChar(bytes.get(CHAR_BITS));
            if (DEBUG) System.out.println("   - Writing "+count+" * '"+c+"'");
            for (int i = 0; i < count; i++) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}