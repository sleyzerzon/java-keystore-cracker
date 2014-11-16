/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Daryl
 */
public class Answer implements DataSerializable {
    private byte[] keyStore;
    private char[] password;

    public byte[] getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(byte[] keyStore) {
        this.keyStore = keyStore;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Arrays.hashCode(this.keyStore);
        hash = 23 * hash + Arrays.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Answer other = (Answer) obj;
        if (!Arrays.equals(this.keyStore, other.keyStore)) {
            return false;
        }
        if (!Arrays.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
        odo.writeCharArray(password);
        odo.writeInt(keyStore.length);
        odo.write(keyStore);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
        password = odi.readCharArray();
        int len = odi.readInt();
        keyStore = new byte[len];
        odi.readFully(keyStore);
    }
    
}
