package model.aes;

import java.util.Random;
import util.UtilAES;

public class KeyGenerator {
    
    private Key[] keys;
    private String key;
    private int initialLength;
    
    public KeyGenerator(String keyStr) throws Exception{
        if(keyStr.length() > 16)
            throw new Exception("Key must have at maximum 16 characters");
        
        initialLength = keyStr.length();
        
        // Complete missing characters
        Random rnd = new Random();
        for(int i = initialLength; i < 16; i++)
            keyStr += (char)('a' + rnd.nextInt(26));
        
        key = keyStr;

        int rounds = AES._128_ROUNDS;
        
        keys = new Key[rounds + 1];
        
        
        // Set first 4 rows
        
        Key initialKey = new Key(keyStr);
        //Key initialKey = new Key(true); // Only for slides example
        short[][] initialKeyWord = initialKey.getWord();
        
        keys[0] = new Key(initialKeyWord);
        
        short[][] w = new short[(rounds + 1) * 4][4];
        for (int i = 0; i < 4; i++) 
            for(int j = 0; j < 4; j++)
                w[i][j] = initialKeyWord[j][i];
        
        
        System.out.println("\n\n-----------START KEY GENERATOR-----------\n");
        
        for(int round = 1, row = 4; round <= rounds; round++){
            for(int i = 0; i < 4; i++, row++){
                if(i == 0){
                    short[] rot = AES.rotWord(w[row - 1]);
                    short[] subWord = AES.subBytesRow(rot);
                    short[] xorSubRcon = xorSubRCon(subWord, row);
                    w[row] = UtilAES.xorArrays(xorSubRcon, w[row - 4]);
                }else
                    w[row] = UtilAES.xorArrays(w[row - 1], w[row - 4]);
            }
            keys[round] = new Key(new short[][]{w[round * 4], w[round * 4 + 1], w[round * 4 + 2], w[round * 4 + 3]});
            System.out.println("Round key" + round);
            System.out.println(keys[round]);
        }
        System.out.println("\n-----------END KEY GENERATOR-----------\n\n");
    }
    
     // Xor between SubWord and RCon
    public short[] xorSubRCon(short[] subWord, int index) throws Exception{
        int pos = index / 4;
        short[] rcon = new short[subWord.length];
        int r = AES.RCON[pos];
        for(int i = 0; i < subWord.length; i++, r <<= 8){
            rcon[i] = (short) (r & 0x000000ff);
        }
        return UtilAES.xorArrays(subWord, rcon);
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
