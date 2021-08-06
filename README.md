# CloudPay Encoder

Interview test project by Marcus Downing.

## Assignment

Consider the following string:

> "AAAAANNNMMMMYYYYuuuuUUUUaaaarWWLLLLJ888DDDDDDDDD"

Write a program, in Java, that shortens the string so it can be stored in fewer bytes, and when required the shortened string can be restored to its original state. The exercise should include unit tests.

Any JDK 1.8 or later is permitted. The use of 3rd party libraries (apart from those needed for unit tests) should be avoided.

## Requirements

Given the imprecise project description, I made the following assumptions:

- Input string may be any length (but a length of 48 characters is representative).
- Input string consists of uppercase and lowercase letters, and numbers. No other characters will appear.
- Input string will often contain sequences of repeated single characters.
- Input string will NOT follow the distribution of letters found in the English language.
- Speed of encoding and decoding are equally important.

## Algorithm

I considered common compression methods, such as Huffman coding, but these did not match the requirements:

- In many compression algorithms, efficiency is only achieved when compressing larger inputs. For example, Zip compression has a large overhead containing its parameters and dictionary. When compressing a large enough file this overhead is worth it, but not for a short string.
- Compression algorithms need to accomodate cases such as repeated sequences ("AbAbAbAb").
- Sometimes you encounter an extended uncompressable section, for example when compressing images or already-compressed files.

With that in mind I kept to the simplest method: a count followed by a letter. I chose the following limits:

- 3 bits for the count, up to 7 copies of each letter; sequences of more than 7 require more than one entry.
- 6 bits for the letter, following the base64 pattern.

## Testing

The primary test is to encode the sample string, decode it and compare the results.

I added a number of other test cases: an empty string, a pangram, and a much longer sequence similar to the sample string.

## Results

I was able to compress the sample string down to 16 bytes, a saving of 67%. Other tests showed similar benefits.