package com.shrink_laureate.cloudpay.encode;

import java.nio.ByteBuffer;

public class CloudPayEncoder {

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
        char[] inputChars = input.toCharArray();
        System.out.print("   = [");
        for (char c : inputChars) {
            System.out.print("'"+c+"',");
        }
        System.out.println("]");
        ByteBuffer buffer = ByteBuffer.allocateDirect(input.length() * 2);
        
        // find repeating sequences of characters
        main_loop:
        for (int i = 0; i < inputChars.length; ) {
            for (int j = i + 1; j <= inputChars.length; j++) {
                if (j >= inputChars.length || inputChars[j] != inputChars[i]) {
                    int count = j - i;
                    char c = inputChars[i];
                    System.out.println("   - Buffering "+count+" * '"+c+"'");
                    buffer.put((byte) count);
                    buffer.put(encodeChar(c));
                    i = j;
                    continue main_loop;
                }
            }
        }

        // copy bytes to a buffer
        byte[] bytes = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(bytes);
        return bytes;
    }

    public String decode(byte[] input) {
        System.out.print("   = [");
        for (byte b : input) {
            System.out.print("'"+b+"',");
        }
        System.out.println("]");
        ByteBuffer bytes = ByteBuffer.allocateDirect(input.length);
        bytes.put(input);
        bytes.rewind();
        bytes.limit(input.length);

        StringBuffer buffer = new StringBuffer(input.length * 4);

        // add repeating sequences to a buffer
        while (bytes.hasRemaining()) {
            int count = (int) bytes.get();
            char c = decodeChar(bytes.get());
            System.out.println("   - Writing "+count+" * '"+c+"'");
            for (int i = 0; i < count; i++) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}
