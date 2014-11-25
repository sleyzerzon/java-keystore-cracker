/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.master;

import com.darylmathison.cracker.data.TargetKeyStore;
import com.hazelcast.core.HazelcastInstance;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Daryl
 */
public class Main {

    public static final void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cracker.xml");
        Spinner master = context.getBean("spinner", Spinner.class);
        HazelcastInstance instance = context.getBean("instance", HazelcastInstance.class);
        byte[] targetStore = null;
        File targetFile = new File(args[0]);
        try(InputStream input = new FileInputStream(targetFile);
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[(int)targetFile.length()];
            int len = input.read(buffer);
            while(len != -1) {
                output.write(buffer, 0, len);
                len = input.read(buffer);
            }
            targetStore = output.toByteArray();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        TargetKeyStore targetKeyStore = new TargetKeyStore();
        targetKeyStore.setTarget(targetStore);
        master.setTarget(targetKeyStore);
        Thread thread = new Thread(master);
        thread.start();
        try {
            thread.join();
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            instance.shutdown();
        }
    }
}
