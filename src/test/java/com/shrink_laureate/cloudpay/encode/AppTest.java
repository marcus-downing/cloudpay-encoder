package com.shrink_laureate.cloudpay.encode;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for basic encoder.
 */
public class AppTest {

    private String hex(byte[] bytes) {
        char[] display = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int high = (bytes[i] >>> 4) & 0x0F;
            int low = bytes[i] & 0x0F;

            buffer.append(display[low]);
            buffer.append(display[high]);
        }
        return buffer.toString();
    }

    private String binary(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 10);
        buffer.append("\n     ");
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                int bit = (bytes[i] >>> j) & 0x01;
                // buffer.append(bit != 0 ? '1' : '0');
                buffer.append(Integer.toString(bit));
            }
            buffer.append("\n     ");
        }
        return buffer.toString();
    }

    private String encodeAndDecode(String input) {
        CloudPayEncoder encoder = new CloudPayEncoder3();
        byte[] encoded = encoder.encode(input);
        // System.out.println(" * Encoded: "+hex(encoded));
        // System.out.println(" * Encoded: "+binary(encoded));
        System.out.println(" * Encoded length: "+encoded.length);
        return encoder.decode(encoded);
    }

    @Test
    public void shouldEncodeTestString() {
        String input = "AAAAANNNMMMMYYYYuuuuUUUUaaaarWWLLLLJ888DDDDDDDDD";
        String decoded = encodeAndDecode(input);
        assertEquals(input, decoded);
    }
    
    // @Test
    // public void shouldEncodeSimpleString() {
    //     String input = "GGG";
    //     String decoded = encodeAndDecode(input);
    //     assertEquals(input, decoded);
    // }

    // @Test
    // public void shouldEncodePangram() {
    //     String input = "SphinxOfBlackQuartzJudgeMyVow";
    //     String decoded = encodeAndDecode(input);
    //     assertEquals(input, decoded);
    // }

    // @Test
    // public void shouldEncodeEmptyString() {
    //     String input = "";
    //     String decoded = encodeAndDecode(input);
    //     assertEquals(input, decoded);
    // }
}
