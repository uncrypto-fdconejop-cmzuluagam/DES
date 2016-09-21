package model.des;

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

        pblocks = Util.splitStringInBlocksMessage(message, DES.MESSAGE_BLOCK_LENGTH);
        for (int i = 0; i < pblocks.length; i++)
            System.out.println("m" + String.format("%2d", i) + "  = " + Util.toStringBitSet(pblocks[i], 64, 64));

        cblocks = new BitSet[pblocks.length];

        states = new BitSet[pblocks.length][17];

        BitSet block, li, ri, li1, ri1;

        for (int i = 0; i < pblocks.length; i++) {
            block = pblocks[i];

            block = Util.permutation(block, DES.IP);
            states[i][0] = block;
            li = DES.getL(states[i][0]);
            ri = DES.getR(states[i][0]);
            System.out.println("m" + String.format("%2d", i) + "' = " + Util.toStringBitSet(block, 64, 64));
            System.out.print("\tL" + String.format("%2d", 0) + ": " + Util.toStringBitSet(li, DES.L_LENGTH, 64));
            System.out.println("\tR" + String.format("%2d", 0) + ": " + Util.toStringBitSet(ri, DES.R_LENGTH, 64));

            for (int j = 1; j <= 15; j++) {

                li1 = (BitSet) ri.clone();
                ri1 = DES.innerFunction(ri, keyGenerator.getKeys()[j]);
                ri1.xor(li);

                System.out.print("\tL" + String.format("%2d", j) + ": " + Util.toStringBitSet(li1, DES.L_LENGTH, 64));
                System.out.println("\tR" + String.format("%2d", j) + ": " + Util.toStringBitSet(ri1, DES.R_LENGTH, 64));

                states[i][j] = Util.createBitSet(li1, DES.L_LENGTH, ri1, DES.R_LENGTH);

                li = li1;
                ri = ri1;
            }

            li1 = (BitSet) ri.clone();
            ri1 = DES.innerFunction(ri, keyGenerator.getKeys()[16]);
            ri1.xor(li);

            System.out.print("\tL" + String.format("%2d", 16) + "= " + Util.toStringBitSet(li1, DES.L_LENGTH, 64));
            System.out.println("\tR" + String.format("%2d", 16) + "= " + Util.toStringBitSet(ri1, DES.R_LENGTH, 64));

            // states[i][16] = Util.createBitSet(li1, DES.R_LENGTH, ri1, DES.L_LENGTH); Is it a mistake in the slides?
            states[i][16] = Util.createBitSet(li1, DES.R_LENGTH, ri1, DES.L_LENGTH);

            cblocks[i] = states[i][16];

            cblocks[i] = Util.permutation(cblocks[i], DES.INV_IP);
            System.out.println("c" + String.format("%2d", i) + " = " + Util.toStringBitSet(cblocks[i], 64, 8));
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
