package com.shrink_laureate.cloudpay.encode;

public interface CloudPayEncoder {
    public byte[] encode(String input);
    public String decode(byte[] input);
}
