/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.des;
import java.io.FileInputStream;
import java.util.BitSet;
import java.util.Scanner;
import util.Util;


/**
 *
 * @author christian
 */
public class Decipher {
    private String message;
    private String cipher;
    private String key;

    private KeyGenerator keyGenerator;
    private BitSet[] pblocks, cblocks; // Plain and ciphered blocks

    private BitSet[][] states;
    public Decipher(String cipher, String key)throws Exception{
        this.cipher = cipher;
        this.key = key;
    }
}
