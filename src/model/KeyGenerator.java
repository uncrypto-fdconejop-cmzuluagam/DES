package model;

import java.util.BitSet;
import java.util.Random;
import util.Util;

public class KeyGenerator {

    private Key[] keys;
    private String key;
    private int initialLength;

    public KeyGenerator() {
    }

    public KeyGenerator(String keyStr) throws Exception {
        if (keyStr.length() > 8)
            throw new Exception("Key must have at maximum 8 characters");
        
        initialLength = keyStr.length();
        
        // Complete missing characters
        Random rnd = new Random();
        for(int i = initialLength; i < 8; i++)
            keyStr += (char)('a' + rnd.nextInt(26));
        
        this.key = keyStr;

        keys = new Key[17];
        
        // Get k0
        Key currentKey = getFirstKey(key);
        
        System.out.println("Keys generator ---START ---");
        
        // Add k0
        keys[0] = currentKey;
        System.out.println(Util.toStringBitSet(currentKey.getWord(), DES.D_LENGTH * 2));
        
        // Add k1 ... k16
        for(int i = 1; i <= 16; i++){
            // C_i = LS_i(C_i-1)
            BitSet ci = currentKey.getC();
            //System.out.println(Util.toStringBitSet(ci, DES.C_LENGTH));
            ci = applyLS(ci, i);
            System.out.println(Util.toStringBitSet(ci, DES.C_LENGTH));
            
            // D_i = LS_i(D_i-1)
            BitSet di = currentKey.getD();
            //System.out.println(Util.toStringBitSet(di, DES.D_LENGTH));
            di = applyLS(di, i);
            System.out.println(Util.toStringBitSet(di, DES.D_LENGTH));
            
            // K_i = PC-2(C_i D_i)
            currentKey = new Key(ci, di);
            //System.out.println(Util.toStringBitSet(currentKey.getWord(), DES.D_LENGTH * 2));
            currentKey.setWord(Util.permutation(currentKey.getWord(), DES.PC2));
            System.out.println(Util.toStringBitSet(currentKey.getWord(), DES.D_LENGTH * 2));
            keys[i] = currentKey;
        }
        
        System.out.println("Keys generator ---END ---");
        
    }
 
    
    /**
     * Generate the first key [k0]
     * @return Key
     */
    private Key getFirstKey(String keyStr) throws Exception{
        Key theKey = new Key(keyStr);
        System.out.println(Util.toStringBitSet(theKey.getWord(), DES.D_LENGTH * 2));
        theKey.setWord(Util.permutation(theKey.getWord(), DES.PC1));
        System.out.println(Util.toStringBitSet(theKey.getWord(), DES.D_LENGTH * 2));
        
        return theKey;
    }
    

    private BitSet applyLS(BitSet bitSet, int id){
        int shifts = 0;
        if(id == 1 || id == 2 || id == 9 || id == 16)
            shifts = 1;
        else
            shifts = 2;
        
        return Util.leftShift(bitSet, DES.C_LENGTH, shifts);
    }

    public Key[] getKeys() {
        return keys;
    }

    public void setKeys(Key[] keys) {
        this.keys = keys;
    }
    
}
