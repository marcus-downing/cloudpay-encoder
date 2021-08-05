package com.shrink_laureate.cloudpay.encode;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for basic encoder.
 */
public class AppTest {

    private String encodeAndDecode(String input) {
        CloudPayEncoder2 encoder = new CloudPayEncoder2();
        byte[] encoded = encoder.encode(input);
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
