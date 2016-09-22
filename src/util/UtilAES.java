package util;

public class UtilAES {
    public static short[] xorArrays(short[] a, short[] b) throws Exception{
        if(a.length != b.length)
            throw new Exception("Array's length must be the same.");
        int size = a.length;
        short[] xor = new short[size];
        for(int i = 0; i < size; i++)
            xor[i] = (short) (a[i] ^ b[i]);
        return xor;
    }
    
    public static String shortArrayToString(short[] row){
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if(i != 0) out.append(' ');
            short b = row[i];
            out.append(String.format("%02x", b));
        }
        return out.toString();
    }
    
    public static short[][][] stringToShortBlocks(String message){
        int length = message.length();
        int blocks = (int)Math.ceil(length / 16.0);
        short[][][] b = new short[blocks][4][4];
        int index = 0;
        for (int i = 0; i < blocks && index < length; i++) 
            for (int r = 0; r < 4 && index < length; r++) 
                for (int c = 0; c < 4 && index < length; c++) 
                    b[i][r][c] = (short) message.charAt(index++);
        return b;
    }
}
