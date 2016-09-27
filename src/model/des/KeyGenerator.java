package model.des;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;
import util.UtilDES;

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
        
        key = keyStr;

        keys = new Key[17];
        
        // Get k0
        Key currentKey = getFirstKey(key);
        
        System.out.println("Keys generator ---START ---");
        
        BitSet ci, di;
        
        // Add k0
        keys[0] = currentKey;
        ci = currentKey.getC();
        di = currentKey.getD();
        
        
        // Add k1 ... k16
        for(int i = 1; i <= 16; i++){
            // C_i = LS_i(C_i-1)
            ci = applyLS(ci, i);
            
            // D_i = LS_i(D_i-1)
            di = applyLS(di, i);
            
            System.out.print("C" + String.format("%2d", i) + ": " +UtilDES.toStringBitSet(ci, DES.C_LENGTH, 64));
            System.out.println("\tD" + String.format("%2d", i) + ": " +UtilDES.toStringBitSet(di, DES.D_LENGTH, 64));
            
            // K_i = PC-2(C_i D_i)
            currentKey = new Key(ci, di);
            currentKey.setWord(UtilDES.permutation(currentKey.getWord(), DES.PC2));
            keys[i] = currentKey;
        }
        
        System.out.println("");
        for(int i = 1; i <= 16; i++)
            System.out.println("k" + String.format("%2d", i) + " = " + UtilDES.toStringBitSet(keys[i].getWord(), DES.KEY_BLOCK_LENGTH, 6));
        
        
        System.out.println("Keys generator ---END ---\n");
        
    }
 
    
    /**
     * Generate the first key [k0]
     * @return Key
     */
    private Key getFirstKey(String keyStr) throws Exception{
        Key theKey = new Key(keyStr);
        System.out.println(UtilDES.toStringBitSet(theKey.getWord(), DES.D_LENGTH * 2, 6));
        theKey.setWord(UtilDES.permutation(theKey.getWord(), DES.PC1));
        System.out.println(UtilDES.toStringBitSet(theKey.getWord(), DES.D_LENGTH * 2, 6));
        
        return theKey;
    }
    

    private BitSet applyLS(BitSet bitSet, int id){
        int shifts = 0;
        if(id == 1 || id == 2 || id == 9 || id == 16)
            shifts = 1;
        else
            shifts = 2;
        
        return UtilDES.leftShift(bitSet, DES.C_LENGTH, shifts);
    }

    public Key[] getKeys() {
        return keys;
    }

    public void setKeys(Key[] keys) {
        this.keys = keys;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
    
}
