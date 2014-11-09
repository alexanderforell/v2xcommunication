/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;

import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 *
 * @author alexander
 */
public abstract class Decode{
    protected CipherInputStream cin;
    protected Cipher c;
    
    
 
    public int read() throws IOException{
        return cin.read();
    }
    

    public int read(byte[] bytes) throws IOException{
        return cin.read(bytes);
    }
    public int read(byte[] bytes, int i, int i1) throws IOException{
        return cin.read(bytes,i,i1);
    }
     public int readMessage(byte[] bytes, int i, int i1) throws IOException{
        int end;
        int a = i1/8;
        int b = i1%8;
        end = a*8;
        if (b>0) end = end + 8;
        return cin.read(bytes,i,end);
    }
    
    public long skip(long n) throws IOException{
        return cin.skip(n);
    }
    public int available() throws IOException{
        return cin.available();
    }
    public void close() throws IOException{
        cin.close();
    }
    
    public synchronized void mark(int readlimit) {
        cin.mark(readlimit);
    }
    
    
}
