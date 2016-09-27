/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.des;
import java.io.FileInputStream;
import java.util.BitSet;
import java.util.Scanner;
import util.Util;


/**
 *
 * @author christian
 */
public class Decipher {
    private String message;
    private String cipher;
    private String key;

    private KeyGenerator keyGenerator;
    private BitSet[] pblocks, cblocks; // Plain and ciphered blocks

    private BitSet[][] states;
    public Decipher(String cipher, String key, BitSet IV)throws Exception{
        this.cipher = cipher;
        this.key = key;
        System.out.println("recibio " + cipher);
        
        keyGenerator = new KeyGenerator(key);
        
        cblocks = Util.splitStringInBlocks(cipher, DES.MESSAGE_BLOCK_LENGTH);
        
        for (int i = 0; i < cblocks.length; i++)
            System.out.println("c here" + String.format("%2d", i) + "  = " + Util.toStringBitSet(cblocks[i], 64, 64));

        System.out.println("key acabo bien");
        Counter counter = new Counter(cblocks, IV);
        cblocks = counter.ctrBlocks;
        
        pblocks = new BitSet[cblocks.length];

        states = new BitSet[cblocks.length][17];

        BitSet block, li, ri, li1, ri1;

        for (int i = 0; i < cblocks.length; i++) {
            block = cblocks[i];

            block = Util.permutation(block, DES.IP);
            
            li = DES.getR(block);
            ri = DES.getL(block);
            states[i][16] = Util.createBitSet(li, DES.L_LENGTH, ri, DES.R_LENGTH);
            System.out.println("c" + String.format("%2d", i) + "' = " + Util.toStringBitSet(block, 64, 64));
            System.out.print("\tR" + String.format("%2d", 16) + ": " + Util.toStringBitSet(ri, DES.R_LENGTH, 64));
            System.out.println("\tL" + String.format("%2d", 16) + ": " + Util.toStringBitSet(li, DES.L_LENGTH, 64));
            
            for (int j = 16; j > 1; j--) {

                ri1 = (BitSet) li.clone();
                li1 = DES.innerFunction(li, keyGenerator.getKeys()[j]);
                li1.xor(ri);
                System.out.print("\tR" + String.format("%2d", j) + ": " + Util.toStringBitSet(ri1, DES.R_LENGTH, 64));
                System.out.println("\tL" + String.format("%2d", j) + ": " + Util.toStringBitSet(li1, DES.L_LENGTH, 64));
                

                states[i][j] = Util.createBitSet(ri1, DES.L_LENGTH, li1, DES.R_LENGTH);

                li = li1;
                ri = ri1;
            }
            ri1 = (BitSet) li.clone();
            li1 = DES.innerFunction(li, keyGenerator.getKeys()[1]);
            li1.xor(ri);

            System.out.print("\tL" + String.format("%2d", 16) + "= " + Util.toStringBitSet(li1, DES.L_LENGTH, 64));
            System.out.println("\tR" + String.format("%2d", 16) + "= " + Util.toStringBitSet(ri1, DES.R_LENGTH, 64));
            
            // states[i][16] = Util.createBitSet(li1, DES.R_LENGTH, ri1, DES.L_LENGTH); Is it a mistake in the slides?
            states[i][0] = Util.createBitSet(ri1, DES.R_LENGTH, li1, DES.L_LENGTH);

            pblocks[i] = states[i][0];

            pblocks[i] = Util.permutation(pblocks[i], DES.INV_IP);
            System.out.println("p" + String.format("%2d", i) + " = " + Util.toStringBitSet(pblocks[i], 64, 8));
        }

        message = "";
        for (BitSet pblock : pblocks) {
            block = pblock;
            for (int i = 0; i < 64; i += 8)
                message += Util.getCharacterFromBitSetOrder(block, 64, i, i + 8);
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
