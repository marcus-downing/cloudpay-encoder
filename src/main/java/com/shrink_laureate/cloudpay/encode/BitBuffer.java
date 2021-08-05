package com.shrink_laureate.cloudpay.encode;

import java.math.BigInteger;
import java.util.Arrays;

public class BitBuffer {
    BigInteger bitdata;
    int size = 0;
    int position = 0;

    public BitBuffer(int capacity) {
        bitdata = new BigInteger(new byte[capacity+4]);
    }

    public BitBuffer(byte[] data) {
        bitdata = new BigInteger(data);
        size = data.length * 8;
    }

    public boolean has() {
        return position < size;
    }

    public void put(byte b, int bits) {
        byte[] barr = {b};
        BigInteger bdata = new BigInteger(barr);

        for (int i = 0; i < bits; i++) {
            if (bdata.testBit(i)) {
                bitdata = bitdata.setBit(position + i);
            } else {
                bitdata = bitdata.clearBit(position + i);
            }
        }
        position += bits;
        size += bits;
    }

    public byte get(int bits) {
        BigInteger bdata = new BigInteger(new byte[4]);
        
        for (int i = 0; i < bits; i++) {
            if (bitdata.testBit(position + i)) {
                bdata = bdata.setBit(i);
            } else {
                bdata = bdata.clearBit(i);
            }
        }
        position += bits;
        return bdata.byteValue();
    }

    public byte[] toArray() {
        return bitdata.toByteArray();
    }
}
