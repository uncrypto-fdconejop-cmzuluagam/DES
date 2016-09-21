package model.aes;

import java.util.Random;
import util.Util;

public class KeyGenerator {
    
    private Key[] keys;
    private String key;
    private int initialLength;
    
    public KeyGenerator(String keyStr) throws Exception{
        if(keyStr.length() > 16)
            throw new Exception("Key must have at maximum 8 characters");
        
        initialLength = keyStr.length();
        
        // Complete missing characters
        Random rnd = new Random();
        for(int i = initialLength; i < 16; i++)
            keyStr += (char)('a' + rnd.nextInt(26));
        
        key = keyStr;

        int rounds = AES._128_ROUNDS;
        
        keys = new Key[rounds];
        
        
        // Set first 4 rows
        
        //Key initialKey = new Key(keyStr);
        Key initialKey = new Key(true); // Only for slides example
        short[][] initialKeyWord = initialKey.getWord();
        
        short[][] w = new short[(rounds + 1) * 4][4];
        for (int i = 0; i < 4; i++) 
            for(int j = 0; j < 4; j++)
                w[i][j] = initialKeyWord[j][i];
        
        for(int round = 1, row = 4; round <= rounds; round++){
            for(int i = 0; i < 4; i++, row++){
                if(i == 0){
                    short[] rot = AES.rotWord(w[row - 1]);
                    short[] subWord = AES.subBytes(rot);
                    short[] xorSubRcon = xorSubRCon(subWord, row);
                    w[row] = Util.xorArrays(xorSubRcon, w[row - 4]);
                }else
                    w[row] = Util.xorArrays(w[row - 1], w[row - 4]);
            }
            keys[round - 1] = new Key(w[round * 4], w[round * 4 + 1], w[round * 4 + 2], w[round * 4 + 3]);
            System.out.println("Round " + round);
            System.out.println(keys[round - 1]);
        }
    }
    
    
    
    // Xor between SubWord and RCon
    public short[] xorSubRCon(short[] subWord, int index) throws Exception{
        int pos = index / 4;
        short[] rcon = new short[subWord.length];
        int r = AES.RCON[pos];
        for(int i = 0; i < subWord.length; i++, r <<= 8){
            rcon[i] = (short) (r & 0x000000ff);
        }
        return Util.xorArrays(subWord, rcon);
    }
    
    public static void main(String[] args) throws Exception{
        KeyGenerator generator = new KeyGenerator("abcdefghijklmnop");
        
    }
    
}
