package util;

import java.math.BigInteger;
import java.util.BitSet;

public class Util {

    public static BitSet permutation(BitSet word, int[] permutation) {
        BitSet p = new BitSet(permutation.length);
        for (int i = 0; i < permutation.length; i++)
            p.set(i, word.get(permutation[i]));
        return p;
    }

    public static BitSet expantion(BitSet word, int[] expantion) {
        BitSet p = new BitSet(expantion.length);
        for (int i = 0; i < expantion.length; i++)
            p.set(i, word.get(expantion[i]));
        return p;
    }

    public static BitSet leftShift(BitSet word, int lenBS, int shifts) {
        BitSet p = new BitSet(lenBS);
        for (int i = 0; i < lenBS; i++)
            //p.set(i, word.get((i - shifts + lenBS) % lenBS));
            p.set(i, word.get((i + shifts) % lenBS));
        return p;
    }

    public static String stringToHex(String word) {
        return String.format("%x", new BigInteger(1, word.getBytes()));
    }

    public static BitSet[] splitStringInBlocks(String word, int lengthBlock) throws Exception {
        if (lengthBlock <= 0)
            throw new Exception("The block's length must be greater than 0");
        word = stringToHex(word);
        int blocks = (int) Math.ceil((4.0 * word.length()) / lengthBlock);
        BitSet[] b = new BitSet[blocks];
        int index = 0;
        for (int i = 0; i < blocks; i++) {
            b[i] = new BitSet(lengthBlock);
            for (int j = 0; j < lengthBlock && index < word.length(); j++) {
                int l = Integer.parseInt(word.charAt(index++) + "", 16);
                for (int k = 0; k < 4; k++, l /= 2)
                    b[i].set(j * 4 + (3 - k), l % 2 == 1);
            }
        }
        return b;
    }

    public static BitSet[] splitStringInBlocksMessage(String word, int lengthBlock) throws Exception {
        if (lengthBlock <= 0)
            throw new Exception("The block's length must be greater than 0");
        word = stringToHex(word);
        word = "0123456789ABCDEF";
        int blocks = (int) Math.ceil((4.0 * word.length()) / lengthBlock);
        BitSet[] b = new BitSet[blocks];
        int index = 0;
        for (int i = 0; i < blocks; i++) {
            b[i] = new BitSet(lengthBlock);
            for (int j = 0; j < lengthBlock && index < word.length(); j++) {
                int l = Integer.parseInt(word.charAt(index++) + "", 16);
                for (int k = 0; k < 4; k++, l /= 2)
                    b[i].set(j * 4 + (3 - k), l % 2 == 1);
            }
        }
        return b;
    }

    public static BitSet[] splitStringInBlocksKey(String word, int lengthBlock) throws Exception {
        if (lengthBlock <= 0)
            throw new Exception("The block's length must be greater than 0");
        word = stringToHex(word);
        word = "133457799BBCDFF1";
        int blocks = (int) Math.ceil((4.0 * word.length()) / lengthBlock);
        BitSet[] b = new BitSet[blocks];
        int index = 0;
        for (int i = 0; i < blocks; i++) {
            b[i] = new BitSet(lengthBlock);
            for (int j = 0; j < lengthBlock && index < word.length(); j++) {
                int l = Integer.parseInt(word.charAt(index++) + "", 16);
                for (int k = 0; k < 4; k++, l /= 2)
                    b[i].set(j * 4 + (3 - k), l % 2 == 1);
            }
        }
        return b;
    }

    public static BitSet[] splitBitSetInBlocks(BitSet word, int lenWord, int blocks, int lengthBlock) throws Exception {
        if (lenWord < blocks * lengthBlock)
            throw new Exception("The aren't enough bits in word to split in blocks.");
        if (blocks <= 0)
            throw new Exception("The block's number must be greater than 0");
        if (lengthBlock <= 0)
            throw new Exception("The block's length must be greater than 0");

        BitSet[] b = new BitSet[blocks];
        int index = 0;
        for (int i = 0; i < blocks; i++) {
            b[i] = new BitSet(lengthBlock);
            for (int j = 0; j < lengthBlock; j++)
                b[i].set((lengthBlock - j - 1), word.get(index++));
        }
        return b;
    }

    /**
     * Creates bitset AB
     *
     * @param a
     * @param lengthA
     * @param b
     * @param lengthB
     * @return
     */
    public static BitSet createBitSet(BitSet a, int lengthA, BitSet b, int lengthB) {
        BitSet c = new BitSet(lengthA + lengthB);

        for (int i = 0; i < lengthB; i++)
            c.set(i, b.get(i));

        for (int i = 0; i < lengthA; i++)
            c.set(lengthA + i, a.get(i));

        return c;
    }

    public static int getNumberFromBitSet(BitSet word, int from, int to) {
        int number = 0;

        for (int i = from, p = 1; i < to; i++, p *= 2)
            number += (word.get(i) ? 1 : 0) * p;

        return number;
    }

    public static int getNumberFromBitSet(BitSet word, int to) {
        return getNumberFromBitSet(word, 0, to);
    }

    public static char getCharacterFromBitSet(BitSet bs, int lenBS, int from, int to) {
        int number = getNumberFromBitSet(bs, from, to);
        return (char) number;
    }

    public static String toStringBitSet(BitSet bs, int lenBS, int splitter) {
        StringBuilder out = new StringBuilder();
        for (int i = 0, cnt = 0; i < lenBS; i++, cnt = (cnt + 1) % splitter) {
            if (cnt == 0)
                out.append(' ');
            out.append(bs.get(i) ? '1' : '0');
        }
        return out.toString();
    }

}
