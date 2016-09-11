package model;

import java.io.FileInputStream;
import java.util.BitSet;
import java.util.Scanner;
import util.Util;

public class Cipher {

    private String message;
    private String cipher;
    private String key;

    private KeyGenerator keyGenerator;
    private BitSet[] pblocks, cblocks; // Plain and ciphered blocks

    private BitSet[][] states;

    public Cipher(String message, String key) throws Exception {
        this.message = message;
        this.key = key;

        keyGenerator = new KeyGenerator(key);

        pblocks = Util.splitStringInBlocks(message, DES.MESSAGE_BLOCK_LENGTH);
        cblocks = new BitSet[pblocks.length];

        states = new BitSet[pblocks.length][17];

        BitSet block, li, ri, li1, ri1;

        for (int i = 0; i < pblocks.length; i++) {
            block = pblocks[i];

            block = Util.permutation(block, DES.IP);

            states[i][0] = block;

            for (int j = 1; j <= 15; j++) {
                li = DES.getL(states[i][j - 1]);
                ri = DES.getR(states[i][j - 1]);

                li1 = ri;
                ri1 = DES.innerFunction(ri, keyGenerator.getKeys()[j]);
                ri1.xor(li);

                states[i][j] = Util.createBitSet(li1, DES.L_LENGTH, ri1, DES.R_LENGTH);
            }

            li = DES.getL(states[i][15]);
            ri = DES.getR(states[i][15]);

            li1 = ri;
            ri1 = DES.innerFunction(li, keyGenerator.getKeys()[16]);
            ri1.xor(li);

            states[i][16] = Util.createBitSet(li1, DES.L_LENGTH, ri1, DES.R_LENGTH);

            cblocks[i] = states[i][16];
            cblocks[i] = Util.permutation(cblocks[i], DES.INV_IP);
        }

        cipher = "";
        for (BitSet cblock : cblocks) {
            block = cblock;
            for (int i = 0; i < 64; i += 8)
                cipher += Util.getCharacterFromBitSet(block, 64, i, i + 8);
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public BitSet[] getPblocks() {
        return pblocks;
    }

    public void setPblocks(BitSet[] pblocks) {
        this.pblocks = pblocks;
    }

    public BitSet[] getCblocks() {
        return cblocks;
    }

    public void setCblocks(BitSet[] cblocks) {
        this.cblocks = cblocks;
    }

    public BitSet[][] getStates() {
        return states;
    }

    public void setStates(BitSet[][] states) {
        this.states = states;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new FileInputStream("in.txt"));

        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String message = in.nextLine();
            String key = in.nextLine();

            Cipher cipher = new Cipher(message, key);

            System.out.println(cipher.getCipher());
        }
    }

}
