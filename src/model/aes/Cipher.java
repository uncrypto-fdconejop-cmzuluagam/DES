package model.aes;

import util.UtilAES;

public class Cipher {
    
    private String message;
    private String key;
    private short[][][] blocks;

    public Cipher(String message, String key) throws Exception {
        this.message = message;
        this.key = key;
        
        blocks = UtilAES.stringToShortBlocks(message);
        
        KeyGenerator generator = new KeyGenerator(key);
        Key[] keys = generator.getKeys();
        for (short[][] block : blocks) {
            // Initial round
            block = AES.addRoundKey(block, keys[0]);
            
            for (int i = 1; i <= 9; i++) {
                Key roundKey = keys[i];
                block = AES.subBytes(block);
                block = AES.mixColumns(block);
                block = AES.addRoundKey(block, roundKey);
            }
            
            block = AES.subBytes(block);
            block = AES.shiftRow(block);
            block = AES.addRoundKey(block, keys[10]);
            System.out.println("Round");
            System.out.println(new Key(block));
        }
    }
  
    public static void main(String[] args) throws Exception{
        Cipher cipher = new Cipher("1234", "1234");
        
    }
}