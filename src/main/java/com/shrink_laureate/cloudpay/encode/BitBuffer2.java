package com.shrink_laureate.cloudpay.encode;

import java.util.Arrays;

public class BitBuffer2 {
    private static boolean DEBUG = false;
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

    private String bits(byte value) {
        StringBuffer buf = new StringBuffer(16);
        for (int i = 0; i < 8; i++) {
            int bit = (value >>> i) & 0x01;
            buf.append(bit != 0 ? '1' : '0');
        }
        return buf.toString();
    }

    private String bits(int value) {
        StringBuffer buf = new StringBuffer(16);
        for (int i = 0; i < 32; i++) {
            int bit = (value >>> i) & 0x01;
            buf.append(bit != 0 ? '1' : '0');
        }
        return buf.toString();
    }

    private String arrayBits(byte[] bytes) {
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
        if (DEBUG) System.out.println("     - BitBuffer: get fragment: "+bits(array[position])+", "+bits(array[position+1]));
        int high = b2i(array[position]);
        int low = b2i(array[position+1]) << 8;
        if (DEBUG) System.out.println("     - BitBuffer: get fragment: high = "+bits(high)+"/"+high+", low = "+bits(low)+"/"+low);
        int fragment = high | low;

        if (DEBUG) System.out.println("     - BitBuffer: get fragment: "+bits(fragment)+"/"+fragment+" at "+position);
        // return (((int) array[position]) << 8) + (int) array[position+1];
        return fragment;
    }

    private void putFragment(int fragment) {
        if (DEBUG) System.out.println("     - BitBuffer: set fragment: "+bits(fragment)+"/"+fragment+" at "+position);

        byte high = (byte) ((fragment >>> 8) & 0xFF);
        byte low = (byte) (fragment & 0xFF);
        if (DEBUG) System.out.println("     - BitBuffer: set high = "+bits(high)+"/"+high+", low = "+bits(low)+"/"+low);
        array[position] = low;
        array[position + 1] = high;

        // if (DEBUG) System.out.println("     - BitBuffer: buffer: "+arrayBits(array));
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
        if (DEBUG) System.out.println("     - BitBuffer: put("+bits(b)+"/"+b+", "+bits+")");
        int fragment = getFragment();
        int bval = b & bitMask(bits);
        if (DEBUG) System.out.println("     - BitBuffer: put("+bits(bval)+"/"+bval+", "+bits+")");
        fragment = fragment | (bval << subpos);
        putFragment(fragment);
        advance(bits);
    }

    public byte get(int bits) {
        int fragment = getFragment();
        int mask = bitMask(bits);
        if (DEBUG) System.out.println("     - BitBuffer: get("+bits+"): fragment = "+bits(fragment)+" sub "+subpos+", mask = "+bits(mask));
        int value = (fragment >>> subpos) & mask;
        if (DEBUG) System.out.println("     - BitBuffer: get("+bits+") => "+value);

        advance(bits);
        return (byte) value;
    }
    
    public byte[] toArray() {
        return Arrays.copyOf(array, position + ((subpos > 0) ? 1 : 0));
    }
}
