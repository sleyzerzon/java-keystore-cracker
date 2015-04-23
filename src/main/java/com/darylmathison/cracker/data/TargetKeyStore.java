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

/**
 *
 * @author Daryl
 */
public class TargetKeyStore implements DataSerializable {
    private byte[] target;
    
    public void setTarget(byte[] targetArray) {
        target = (byte[])targetArray.clone();
    }
    
    public byte[] getTargetCopy() {
        return (byte[])target.clone();
    }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
        odo.writeInt(target.length);
        odo.write(target);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
        int len = odi.readInt();
        target = new byte[len];
        odi.readFully(target);
    }
}
