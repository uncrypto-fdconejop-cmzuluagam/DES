package model.aes;

import util.UtilAES;

public class Cipher {
    
    private String message;
    private String key;
    private String cipherMessage;
    private KeyGenerator keyGenerator;
    private short[][][] blocks;

    public Cipher(String message, String key) throws Exception {
        this.message = message;
        this.key = key;
        cipherMessage = "";
        blocks = UtilAES.stringToShortBlocks(message);
        System.out.println("numero de bloques " + blocks.length);
        keyGenerator = new KeyGenerator(key);
        Key[] keys = keyGenerator.getKeys();
        
        System.out.println("\n\n-----------START CIPHER-----------\n");
        
        for (short[][] block : blocks) {
            // Initial round
            // block = slidesExample(); // This is only for slides example
            
            // Transpose key only for the first round
            Key keyTranspose = new Key(UtilAES.transpose(keys[0].getWord()), true);
            
            System.out.println("Input");
            System.out.println(new Key(block, true));
            
            block = AES.addRoundKey(block, keyTranspose);
            System.out.println("ROUND KEY");
            System.out.println(new Key(keyTranspose.getWord(), true));
            for (int i = 1; i <= 9; i++) { 
                System.out.println("\nRound " + i);
                
                System.out.println("\nARK");
                System.out.println(new Key(block, true));
                
                Key roundKey = keys[i];
                
                block = AES.subBytes(block);
                System.out.println("SB");
                System.out.println(new Key(block, true));
                
                block = AES.shiftRow(block);
                System.out.println("SR");
                System.out.println(new Key(block, true));
                
                block = AES.mixColumns(block);
                System.out.println("MC");
                System.out.println(new Key(block, true));
                
                block = AES.addRoundKey(block, roundKey);
                System.out.println("ROUND KEY");
                System.out.println(keys[i]);
            }
            
            System.out.println("Round 10");

            System.out.println("\nARK");
            System.out.println(new Key(block, true));
            
            block = AES.subBytes(block);
            System.out.println("SB");
            System.out.println(new Key(block, true));
            
            block = AES.shiftRow(block);
            System.out.println("SR");
            System.out.println(new Key(block, true));
            
            block = AES.addRoundKey(block, keys[10]);
            System.out.println("Output");
            System.out.println(new Key(block, true));

            System.out.println("c =");
            for (int i = 0; i < 4; i++) 
                for (int j = 0; j < 4; j++) {
                    if(i != 0 || j != 0) System.out.print(" ");
                    System.out.print(String.format("%02x", block[j][i]));
                    
                }
            System.out.println("\nc String es =");
            for (int i = 0; i < 4; i++) 
                for (int j = 0; j < 4; j++) {
                    if(i != 0 || j != 0) System.out.print(" ");
                    char cur = (char)block[j][i];
                    cipherMessage += cur;
                    System.out.print(cur);
                }
            
        }
        System.out.println("");
        System.out.println("Mensaje cifrado: " + cipherMessage);
        System.out.println("\n-----------END CIPHER-----------\n\n");
    }
  
    public static void main(String[] args) throws Exception{
        gui.aes.Main main = new gui.aes.Main();
        main.setVisible(true);
        
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCipherMessage() {
        return cipherMessage;
    }

    public void setCipherMessage(String cipherMessage) {
        this.cipherMessage = cipherMessage;
    }
    
   
    
    static short[][] slidesExample(){
        return new short[][]{
                            {65, 101, 117, 97},
                            {69, 115, 121, 99},
                            {83, 32,  32,  105},
                            {32, 109, 102, 108}
                            };
    }
}