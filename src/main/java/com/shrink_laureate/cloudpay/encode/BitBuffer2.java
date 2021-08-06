package com.shrink_laureate.cloudpay.encode;

import java.util.Arrays;

/**
 * A binary buffer that stores arbitrarily sized numbers
 */
public class BitBuffer2 {
    byte[] array;
    int position = 0;
    int subpos = 0;
    int size = 0;

    public BitBuffer2(int capacity) {
        array = new byte[capacity+8];
    }

    public BitBuffer2(byte[] data) {
        array = new byte[data.length+8];
        System.arraycopy(data, 0, array, 0, data.length);
        size = data.length;
    }

    public boolean has() {
        return position < size;
    }

    // because Java doesn't have unsigned bytes.
    private int b2i(byte b) {
        int i = (int) b;
        if (i < 0) {
            b += 128;
            i = (int) b + 128;
        }
        return i;
    }

    private int getFragment() {
        int high = b2i(array[position]);
        int low = b2i(array[position+1]) << 8;
        return high | low;
    }

    private void putFragment(int fragment) {
        byte high = (byte) ((fragment >>> 8) & 0xFF);
        byte low = (byte) (fragment & 0xFF);
        array[position] = low;
        array[position + 1] = high;
    }

    private int bitMask(int bits) {
        switch (bits) {
            case 1:
                return 0x01;
            case 2:
                return 0x03;
            case 3:
                return 0x07;
            case 4:
                return 0x0F;
            case 5:
                return 0x1F;
            case 6:
                return 0x3F;
            case 7:
                return 0x7F;
            case 8:
                return 0xFF;
        }
        return 0x00;
    }

    private void advance(int bits) {
        subpos += bits;
        while (subpos >= 8) {
            subpos -= 8;
            position++;
        }
    }

    public void put(byte b, int bits) {
        // put those bits into bytes at the current position
        int fragment = getFragment();
        int bval = b & bitMask(bits);
        fragment = fragment | (bval << subpos);
        putFragment(fragment);
        advance(bits);
    }

    public byte get(int bits) {
        int fragment = getFragment();
        int mask = bitMask(bits);
        int value = (fragment >>> subpos) & mask;

        advance(bits);
        return (byte) value;
    }
    
    public byte[] toArray() {
        return Arrays.copyOf(array, position + ((subpos > 0) ? 1 : 0));
    }
}
