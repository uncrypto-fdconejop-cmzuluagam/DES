package model.aes;

public class Key {
    private short[][] word;

    public Key(String keyStr) throws Exception{
        if(keyStr.length() != 16)
            throw new Exception("Key must have 16 characters.");
        word = new short[4][4];
        int index = 0;
        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 4; i++){
                word[i][j] = (short)keyStr.charAt(index++);
            }
        }
    }
    
    // Only for slides example
    public Key(boolean algo){
        word = new short[][]{
            {(short)0x0000002b,(short)0x0000007e,(short)0x00000015,(short)0x00000016},
            {(short)0x00000028,(short)0x000000ae,(short)0x000000d2,(short)0x000000a6},
            {(short)0x000000ab,(short)0x000000f7,(short)0x00000015,(short)0x00000088},
            {(short)0x00000009,(short)0x000000cf,(short)0x0000004f,(short)0x0000003c}};
        short[][] trans = new short[4][4];
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                trans[i][j] = word[j][i];
        word = trans;
        //System.out.println(this);
    }
    
    
    public Key(short[][] key) {
        word = new short[4][4];
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                word[i][j] = key[j][i];
    }

    public short[][] getWord() {
        return word;
    }

    public void setWord(short[][] word) {
        this.word = word;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < word.length; i++){
            for (int j = 0; j < word[i].length; j++) {
                if(j != 0) out.append(' ');
                short b = word[i][j];
                out.append(String.format("%02x", b));
            }
            out.append('\n');
        }
        return out.toString();
    }

}