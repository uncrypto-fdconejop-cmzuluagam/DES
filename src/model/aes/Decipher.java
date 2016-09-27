/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.aes;

import util.UtilAES;

/**
 *
 * @author christian
 */
public class Decipher {
    
    private String message;
    private String key;
    private String plainText;
    private short[][][] blocks;

    public Decipher(String message, String key) throws Exception {
        this.message = message;
        plainText = "";
        this.key = key;
        System.out.println(message + " tamano " + message.length());
        blocks = UtilAES.stringToShortBlocks(message);
        for(int i = 0; i < blocks.length; i ++)
            blocks[i] = UtilAES.transpose(blocks[i]);
        
        System.out.println("numero de blocks " + blocks.length);
        KeyGenerator generator = new KeyGenerator(key);
        Key[] keys = generator.getKeys();
        
        System.out.println("\n\n-----------START DECIPHER-----------\n");
        
        for (short[][] block : blocks) {
            // Initial round
            //block = slidesExample(); // This is only for slides example
            
            // Transpose key only for the first round
            Key keyTranspose = new Key(UtilAES.transpose(keys[0].getWord()), true);
            
            System.out.println("Input");
            System.out.println(new Key(block, true));
            
            block = AES.addRoundKey(block, keys[10]);
            System.out.println("First ARK");
            System.out.println(new Key(block, true));
            
            block = AES.InvShiftRow(block);
            System.out.println("First InvSR");
            System.out.println(new Key(block, true));
            
            block = AES.InvSubBytes(block);
            System.out.println("First invSubB");
            System.out.println(new Key(block, true));
            
            for (int i = 9; i >= 1; i--) { 
                System.out.println("\nRound " + i);
                
                Key roundKey = keys[i];
                
                block = AES.addRoundKey(block, roundKey);
                System.out.println("ARk");
                System.out.println(new Key(block,true));
                
                block = AES.InvMixColumns(block);
                System.out.println("InvMC");
                System.out.println(new Key(block, true));
                
                block = AES.InvShiftRow(block);
                System.out.println("InvSR");
                System.out.println(new Key(block, true));
                
                block = AES.InvSubBytes(block);
                System.out.println("InvSB");
                System.out.println(new Key(block, true));
                
                
                System.out.println("RoundeKey");
                System.out.println(keys[i]);
                
            }
            
            System.out.println("Round 0");

            System.out.println("\nARK");
            System.out.println(new Key(block, true));
            
            block = AES.addRoundKey(block, keyTranspose);
            System.out.println("Output");
            System.out.println(new Key(block, true));

            System.out.println("M =");
            for (int i = 0; i < 4; i++) 
                for (int j = 0; j < 4; j++) {
                    if(i != 0 || j != 0) System.out.print(" ");
                    System.out.print(String.format("%02x", block[i][j]));
                }
            
            System.out.println("\nM original =");
            for (int i = 0; i < 4; i++) 
                for (int j = 0; j < 4; j++) {
                    if(i != 0 || j != 0) System.out.print(" ");
                    char cur = (char)block[i][j];
                    plainText += cur;
                    System.out.print(cur + " ");
                }
        }
        System.out.println("");
        System.out.println("mensaje original: " + plainText);
        System.out.println("\n-----------END DECIPHER-----------\n\n");
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

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }
  
    static short[][] slidesExample(){
        return new short[][]{
            {(short)0x000000e4,(short)0x000000a3,(short)0x000000c3,(short)0x0000009b},
            {(short)0x00000048,(short)0x00000074,(short)0x0000003c,(short)0x0000008e},
            {(short)0x000000e5,(short)0x000000d9,(short)0x00000022,(short)0x000000ab},
            {(short)0x00000074,(short)0x0000000c,(short)0x000000af,(short)0x0000007f}};
    }
    public static void main(String[] args) throws Exception{
        gui.aes.Main main = new gui.aes.Main();
        main.setVisible(true);
    }
    
}
