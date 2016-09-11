package model;

import java.util.BitSet;
import util.Util;

public class Key {

    private BitSet word;

    public Key() {
        word = new BitSet(64);
    }

    public Key(BitSet keyBitSet) {
        this.word = keyBitSet;
    }

    public Key(String keyStr) throws Exception {
        if (keyStr.length() != 8)
            throw new Exception("Key must have 8 characters.");
        word = new BitSet(DES.KEY_BLOCK_LENGTH);
        word = Util.splitStringInBlocksKey(keyStr, 64)[0];

    }

    public Key(BitSet c, BitSet d) {
        word = new BitSet(DES.C_LENGTH + DES.D_LENGTH);

        // Set c part
        for (int i = 0; i < DES.C_LENGTH; i++)
            word.set(i, c.get(i));

        // Set d part
        for (int i = 0; i < DES.D_LENGTH; i++)
            word.set(i + DES.C_LENGTH, d.get(i));
    }

    public BitSet getWord() {
        return word;
    }

    public void setWord(BitSet word) {
        this.word = word;
    }

    public BitSet getC() {
        int from = 0, to = DES.C_LENGTH;
        return word.get(from, to);
    }

    public BitSet getD() {
        int from = DES.C_LENGTH, to = DES.C_LENGTH + DES.D_LENGTH;
        return word.get(from, to);
    }

}
