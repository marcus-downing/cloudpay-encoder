package com.shrink_laureate.cloudpay.encode;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for basic encoder.
 */
public class CloudPayEncoderTest {

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

    private String encodeAndDecode(String input, String testName) {
        CloudPayEncoder encoder = new CloudPayEncoder3();
        byte[] encoded = encoder.encode(input);

        int percent = 100 - (int) Math.round(100.0 * (float) encoded.length / (float) input.length());
        // System.out.println(" * Encoded: "+hex(encoded));
        // System.out.println(" * Encoded: "+binary(encoded));
        System.out.printf(" * Compressing %-20s %3d => %3d (%2d%% saving)\n", testName+":", input.length(), encoded.length, percent);
        // System.out.println(" * Compressing "+testName+": "+encoded.length+" / "+input.length()+" ("+percent+"% saving)");
        return encoder.decode(encoded);
    }

    @Test
    public void shouldEncodeTestString() {
        String input = "AAAAANNNMMMMYYYYuuuuUUUUaaaarWWLLLLJ888DDDDDDDDD";
        String decoded = encodeAndDecode(input, "test string");
        assertEquals(input, decoded);
    }
    
    @Test
    public void shouldEncodeSimpleString() {
        String input = "GGG";
        String decoded = encodeAndDecode(input, "simple string");
        assertEquals(input, decoded);
    }

    @Test
    public void shouldEncodePangram() {
        String input = "SphinxOfBlackQuartzJudgeMyVow";
        String decoded = encodeAndDecode(input, "pangram");
        assertEquals(input, decoded);
    }

    @Test
    public void shouldEncodeEmptyString() {
        String input = "";
        String decoded = encodeAndDecode(input, "empty string");
        assertEquals(input, decoded);
    }

    @Test
    public void shouldEncodeRepetitiveString() {
        String input = "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP";
        String decoded = encodeAndDecode(input, "repetitive string");
        assertEquals(input, decoded);
    }
    
    @Test
    public void shouldEncodeLongString() {
        String input = "TTTTGGG999999yyZWWWWWW000gggggggggaaaaXXXXBBBzzzzzzddddWWWW0000ggZGGGGGGGGGGGGGGGGGGGG999999ssssbbbbbb3IIIIIgggccccccc222l000000IIIIIGGGGGFFtttttZZZZZZXXXXQQQQsssIIIIGGNNNNNNvvvvbbnnnnnnNNNllllYY333333RRRRllllddHHHHHVVVyyyIIIIIIGGGFFFFkkkkaaaXXXXXXBBpppcccccc2222NNppppbbbbmmmccccccggZWWWWWxxxppppddCCCC4444444444gggTWWWFFFF1111cccmmmmmllzzzzzIIIIGGGGNNNvvvvppppbbbbmmmmddd1111ZZZZSSSSBB222222YYYYXXXJJJJppppdddXXXXXXXXXXMMMMMMMMMMgggggggggddddGGGGVVVVtttccccHHHHVVVzzzzLLLiiiiBBBVVVVVddddCCCBBBBBsssYYWWWWWWNNNpppbbbbmmmmlllhhIIIIIIIIIIGGGG555ppppccc2222wwwgggggddddXXXXQgggccccGGGG999yydGEgggggggggdddnnnnVVVsssccccHHHHHHVV00YYYYXXXRRRRlLi"; // BOdW5jIHRpbmNpZHVudCBlc3QgZWdldCBuaXNsIHVsbGFtY29ycGVyLCBxdWlzIGhlbmRyZXJpdCBvZGlvIGFsaXF1ZXQuIEV0aWFtIG5vbiBkdWkgY3Vyc3VzLCB2b2x1dHBhdCB0b3J0b3IgYSwgYmliZW5kdW0gdG9ydG9yLiBOdWxsYW0gZXQgZW5pbSB1bGxhbWNvcnBlciBpcHN1bSBzdXNjaXBpdCBwb3J0YSBxdWlzIGFjIGlwc3VtLiBDdXJhYml0dXIgdGluY2lkdW50IG1vbGxpcyBuaXNsLCBxdWlzIGN1cnN1cyBhbnRlIHNlbXBlciB2ZWwuIE1hZWNlbmFzIGxvYm9ydGlzIHRlbXBvciBsaWd1bGEsIGFjIGNvbnZhbGxpcyBuaXNpIGdyYXZpZGEgdml0YWUuIFBoYXNlbGx1cyB2ZWwgb3JjaSBxdWFtLiBQaGFzZWxsdXMgZGljdHVtIGFjIGlwc3VtIGVnZXQgZWZmaWNpdHVyLiBOdW5jIHRpbmNpZHVudCBhYyB1cm5hIHF1aXMgYWNjdW1zYW4uIEV0aWFtIG5lYyBlcm9zIHZlbCBtYXVyaXMgcG9ydHRpdG9yIGdyYXZpZGEuIFNlZCBiaWJlbmR1bSBlbmltIGV0IG9kaW8gc3VzY2lwaXQgbWF4aW11cy4gQWxpcXVhbSBlcmF0IHZvbHV0cGF0LiBNYWVjZW5hcyBhdWd1ZSB0dXJwaXMsIHRlbXB1cyB2aXRhZSBjb25zZXF1YXQgZXUsIGRpY3R1bSBzZWQgZHVpLiBNYXVyaXMgZXUgcHVydXMganVzdG8uCgpDdXJhYml0dXIgY29uc2VjdGV0dXIgbGliZXJvIGxhY2luaWEgdWx0cmljZXMgbWFsZXN1YWRhLiBWZXN0aWJ1bHVtIHZvbHV0cGF0IHJ1dHJ1bSBwbGFjZXJhdC4gTW9yYmkgdmVoaWN1bGEgdHVycGlzIGxpYmVybywgcG9ydGEgZWxlbWVudHVtIGlwc3VtIHZlaGljdWxhIGlkLiBBZW5lYW4gdGluY2lkdW50IG5lcXVlIHNpdCBhbWV0IGRvbG9yIHJob25jdXMgZWxlaWZlbmQgdmVsIGV0IHNhcGllbi4gVmVzdGlidWx1bSBub24ganVzdG8gYXQgbWF1cmlzIHBsYWNlcmF0IHVsdHJpY2VzIGV1IHV0IGFyY3UuIEFlbmVhbiBpbnRlcmR1bSBtYXVyaXMgbmVjIGFudGUgY29uc2VxdWF0LCBldCB0cmlzdGlxdWUgbGlndWxhIGNvbnNlY3RldHVyLiBQcmFlc2VudCBtb2xsaXMsIG1hc3NhIGEgdGluY2lkdW50IHByZXRpdW0sIGV4IGFudGUgY29uZGltZW50dW0gdmVsaXQsIHBvcnR0aXRvciBldWlzbW9kIGVyYXQgZXN0IHV0IG51bGxhLiBNYWVjZW5hcyBhdCB0b3J0b3IgdWx0cmljZXMsIHRlbXBvciBtYWduYSBhYywgZmV1Z2lhdCByaXN1cy4gTWF1cmlzIHBvcnRhIG5pc2kgYXVjdG9yLCBwbGFjZXJhdCBsb3JlbSB1dCwgdGluY2lkdW50IGF1Z3VlLiBWaXZhbXVzIG5pc2wgZHVpLCBwb3N1ZXJlIGlkIGxhY3VzIHNpdCBhbWV0LCBpbnRlcmR1bSB1bGxhbWNvcnBlciBsZW8uIFN1c3BlbmRpc3NlIHNlZCBhY2N1bXNhbiBtYWduYSwgc2VkIGJpYmVuZHVtIG5pYmguIFN1c3BlbmRpc3NlIGRhcGlidXMgZXggaWQgYXVjdG9yIGF1Y3Rvci4KClF1aXNxdWUgb2RpbyBtaSwgY29uc2VxdWF0IHNpdCBhbWV0IGZlcm1lbnR1bSBwb3N1ZXJlLCBpbnRlcmR1bSBldSBvcmNpLiBJbnRlZ2VyIG1vbGxpcyBtb2xsaXMgZXggbmVjIGFsaXF1YW0uIFN1c3BlbmRpc3NlIHNvZGFsZXMgYWNjdW1zYW4gcG9zdWVyZS4gUGhhc2VsbHVzIGZldWdpYXQgZW5pbSB0dXJwaXMsIGV1IHJ1dHJ1bSBsb3JlbSBhdWN0b3Igc2VkLiBQaGFzZWxsdXMgcGVsbGVudGVzcXVlIGp1c3RvIHF1aXMgbmlzaSBjb25zZXF1YXQsIG9ybmFyZSB1bHRyaWNpZXMgbG9yZW0gc3VzY2lwaXQuIER1aXMgYWNjdW1zYW4gY29udmFsbGlzIHR1cnBpcywgYWMgcHJldGl1bSB0ZWxsdXMgc2VtcGVyIHZpdGFlLiBOdW5jIGluIGVsZW1lbnR1bSB0b3J0b3IsIGFjIHZhcml1cyB0ZWxsdXMuIE5hbSByaG9uY3VzLCBzZW0gdXQgZWxlaWZlbmQgYXVjdG9yLCBvcmNpIGxlY3R1cyBpYWN1bGlzIG51bmMsIGZhdWNpYnVzIHZvbHV0cGF0IG5pc2wgZG9sb3IgZWdldCB0ZWxsdXMuIFByYWVzZW50IG5lYyBsdWN0dXMgbGVjdHVzLCB1dCBkYXBpYnVzIG5pc2kuIFV0IG5vbiBzb2xsaWNpdHVkaW4gc2FwaWVuLiBWaXZhbXVzIHRpbmNpZHVudCBzYWdpdHRpcyBwdXJ1cyB2aXRhZSB2dWxwdXRhdGUuIENyYXMgc2l0IGFtZXQgYW50ZSBlbmltLgoKRHVpcyBldCBzYXBpZW4gaW4gZXN0IGVnZXN0YXMgZWZmaWNpdHVyIG5lYyBhYyBlc3QuIFN1c3BlbmRpc3NlIGF0IGRvbG9yIHV0IG1hZ25hIGFsaXF1YW0gZmFjaWxpc2lzIHF1aXMgcXVpcyBzZW0uIE1hdXJpcyBub24gZXggbGVvLiBQaGFzZWxsdXMgdXQgdml2ZXJyYSB2ZWxpdC4gQ3VyYWJpdHVyIGludGVyZHVtIG1hdXJpcyBzaXQgYW1ldCBmYXVjaWJ1cyBlZ2VzdGFzLiBFdGlhbSB2dWxwdXRhdGUgbWF1cmlzIGZyaW5naWxsYSBzYXBpZW4gcGVsbGVudGVzcXVlIHBoYXJldHJhLiBGdXNjZSBuZWMgaW1wZXJkaWV0IGFyY3UuIEFsaXF1YW0gcmhvbmN1cyBqdXN0byBvZGlvLCBldCBzYWdpdHRpcyB2ZWxpdCBjb25zZXF1YXQgdml0YWUuCgpTZWQgbm9uIGR1aSBzZW1wZXIsIG1heGltdXMgbGliZXJvIG5lYywgdmVuZW5hdGlzIG9kaW8uIENyYXMgY29uc2VjdGV0dXIgYW50ZSBzZWQgYXVndWUgc29kYWxlcywgbmVjIGFsaXF1ZXQgb2RpbyBjb25kaW1lbnR1bS4gRnVzY2UgZWdldCB2ZW5lbmF0aXMgYW50ZS4gUHJvaW4gcXVpcyBtaSBhIGxpYmVybyBlZ2VzdGFzIHNhZ2l0dGlzIHZlbCBsdWN0dXMgZG9sb3IuIE51bmMgY29uZ3VlIHVybmEgYWMgbWV0dXMgYWxpcXVldCBvcm5hcmUuIFZlc3RpYnVsdW0gaWQgYW50ZSBtb2xlc3RpZSB0b3J0b3IgcGhhcmV0cmEgcmhvbmN1cyBtYXR0aXMgaW4gbWFnbmEuIEN1cmFiaXR1ciBleCBlcm9zLCBkaWN0dW0gcXVpcyBmYWNpbGlzaXMgaW4sIHBvcnR0aXRvciBuZWMgaXBzdW0uIFF1aXNxdWUgZXggbmlzaSwgdmFyaXVzIGluIHRpbmNpZHVudCBhLCBmYWNpbGlzaXMgZXQgaXBzdW0uIFF1aXNxdWUgcHVsdmluYXIgbG9yZW0gZW5pbSwgcXVpcyBiaWJlbmR1bSBuaWJoIHZlc3RpYnVsdW0gbmVjLiBOdWxsYW0gc2l0IGFtZXQgbGVvIHRlbGx1cy4";
        String decoded = encodeAndDecode(input, "long string");
        assertEquals(input, decoded);
    }
    
}
