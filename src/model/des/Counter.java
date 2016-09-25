/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.des;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author christian
 */
public class Counter {
    public BitSet IV;
    public BitSet counter;
    public BigInteger maxCounter;
    public BitSet[] ctrBlocks;
    public static final int BLOCK_LENGTH = 64;
    
    private void init(){
        counter = new BitSet(BLOCK_LENGTH);
        maxCounter = new BigInteger("0");
        for(int i = 0; i < BLOCK_LENGTH; i ++){
            counter.set(i,false);
            maxCounter = maxCounter.setBit(i);
        }
    }
    private void generateIV(){
        Random rd = new Random();
        IV = new BitSet(BLOCK_LENGTH);
        counter = new BitSet(BLOCK_LENGTH);
        maxCounter = new BigInteger("0");
        for(int i = 0; i < BLOCK_LENGTH; i ++){
            IV.set(i, rd.nextBoolean());
            counter.set(i,false);
            maxCounter = maxCounter.setBit(i);
        }
    }
    
    public BitSet BigIntegerToBitSet(BigInteger current){
        BitSet b = new BitSet(current.bitLength());
        
        for(int i = 0; i < current.bitLength(); i ++){
            if(current.testBit(i))
                b.set(i);
        }
        return b;
    }
    private BitSet sumIVCounter(){
        BigInteger _IV = new BigInteger("0");
        BigInteger _counter = new BigInteger("0");
        BigInteger sum = new BigInteger("0");
        for(int i = 0; i < IV.length(); i ++){
            if(IV.get(i))
                _IV = _IV.setBit(i);
            if(counter.get(i))
                _counter = _counter.setBit(i);
        }
        sum = _IV.add(_counter);
        sum = sum.mod(maxCounter);
        return BigIntegerToBitSet(sum);
    }
    
    private void incrementCounter(){
        BigInteger _counter = new BigInteger("0");
        for(int i = 0; i < counter.length(); i ++)
            if(counter.get(i))
                _counter = _counter.setBit(i);
        
        _counter = _counter.add(BigInteger.ONE);
        
        counter = BigIntegerToBitSet(_counter);
        
    }
    
    public void printBitSet(BitSet current){
        for(int i = BLOCK_LENGTH - 1; i >= 0; i --)
            System.out.print(current.get(i)?1:0);
        System.out.println("");
    }
    
    private BitSet applyEk(BitSet Ij){
        BitSet b = new BitSet(Ij.length());
        for(int i = 0; i < DES.Ek.length; i ++){
            b.set(i, Ij.get(DES.Ek[i]));
        }
        return b;
    }
    
    public Counter(BitSet[] cblocks){
        ctrBlocks = new BitSet[cblocks.length];
        generateIV();
        for(int i = 0; i < cblocks.length; i ++){
            BitSet Ij = sumIVCounter();
            BitSet oj = applyEk(Ij);
            oj.xor(cblocks[i]);
            ctrBlocks[i] = oj;
            incrementCounter();
        }
    }
    
    public Counter(BitSet[] cblocks, BitSet _IV){
        ctrBlocks = new BitSet[cblocks.length];
        IV = _IV;
        init();
        
        System.out.println("iv es ");
        for(int i = 0; i < IV.length(); i ++)
            System.out.print(IV.get(i)?1:0);
        System.out.println("");
        
        for(int i = 0; i < cblocks.length; i ++){
            BitSet Ij = sumIVCounter();
            BitSet oj = applyEk(Ij);
            oj.xor(cblocks[i]);
            ctrBlocks[i] = oj;
            incrementCounter();
        }
    }
    
    public static void main(String args[]){
    }
}
