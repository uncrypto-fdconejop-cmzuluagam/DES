package model.des;

import java.io.FileInputStream;
import java.util.BitSet;
import java.util.Scanner;
import util.UtilDES;

public class Cipher {

    private String message;
    private String cipher;
    private String key;
    private BitSet IV;

    private KeyGenerator keyGenerator;
    private BitSet[] pblocks, cblocks; // Plain and ciphered blocks

    private BitSet[][] states;

    public BitSet getIV(){
        return this.IV;
    }
    public Cipher(String message, String key) throws Exception {
        this.message = message;
        this.key = key;

        keyGenerator = new KeyGenerator(key);

        pblocks = UtilDES.splitStringInBlocks(message, DES.MESSAGE_BLOCK_LENGTH);
        for (int i = 0; i < pblocks.length; i++)
            System.out.println("m" + String.format("%2d", i) + "  = " + UtilDES.toStringBitSet(pblocks[i], 64, 64));

        cblocks = new BitSet[pblocks.length];

        states = new BitSet[pblocks.length][17];

        BitSet block, li, ri, li1, ri1;

        for (int i = 0; i < pblocks.length; i++) {
            block = pblocks[i];

            block = UtilDES.permutation(block, DES.IP);
            states[i][0] = block;
            li = DES.getL(states[i][0]);
            ri = DES.getR(states[i][0]);
            System.out.println("m" + String.format("%2d", i) + "' = " + UtilDES.toStringBitSet(block, 64, 64));
            System.out.print("\tL" + String.format("%2d", 0) + ": " + UtilDES.toStringBitSet(li, DES.L_LENGTH, 64));
            System.out.println("\tR" + String.format("%2d", 0) + ": " + UtilDES.toStringBitSet(ri, DES.R_LENGTH, 64));

            for (int j = 1; j <= 15; j++) {

                li1 = (BitSet) ri.clone();
                ri1 = DES.innerFunction(ri, keyGenerator.getKeys()[j]);
                ri1.xor(li);

                System.out.print("\tL" + String.format("%2d", j) + ": " + UtilDES.toStringBitSet(li1, DES.L_LENGTH, 64));
                System.out.println("\tR" + String.format("%2d", j) + ": " + UtilDES.toStringBitSet(ri1, DES.R_LENGTH, 64));

                states[i][j] = UtilDES.createBitSet(li1, DES.L_LENGTH, ri1, DES.R_LENGTH);

                li = li1;
                ri = ri1;
            }

            li1 = (BitSet) ri.clone();
            ri1 = DES.innerFunction(ri, keyGenerator.getKeys()[16]);
            ri1.xor(li);

            System.out.print("\tL" + String.format("%2d", 16) + "= " + UtilDES.toStringBitSet(li1, DES.L_LENGTH, 64));
            System.out.println("\tR" + String.format("%2d", 16) + "= " + UtilDES.toStringBitSet(ri1, DES.R_LENGTH, 64));

            // states[i][16] = UtilDES.createBitSet(li1, DES.R_LENGTH, ri1, DES.L_LENGTH); Is it a mistake in the slides?
            states[i][16] = UtilDES.createBitSet(li1, DES.R_LENGTH, ri1, DES.L_LENGTH);

            cblocks[i] = states[i][16];

            cblocks[i] = UtilDES.permutation(cblocks[i], DES.INV_IP);
            System.out.println("c " + String.format("%2d", i) + " = " + UtilDES.toStringBitSet(cblocks[i], 64, 8));
        }
        Counter counter = new Counter(cblocks);
        cblocks = counter.ctrBlocks;
        this.IV = counter.IV;
        
        cipher = "";
        for (BitSet cblock : cblocks) {
            block = cblock;
            for (int i = 0; i < 64; i += 8)
                cipher += UtilDES.getCharacterFromBitSetOrder(block, 64, i, i + 8);
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
        gui.des.Main main = new gui.des.Main();
        main.setVisible(true);
    }

}
