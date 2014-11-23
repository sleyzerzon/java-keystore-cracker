/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.worker;

import com.darylmathison.cracker.data.Answer;
import com.darylmathison.cracker.data.TargetKeyStore;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.spring.context.SpringAware;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Daryl
 */
@SpringAware
public class KeyStoreOpennerImpl implements KeyStoreOpenner, ApplicationContextAware, 
        DataSerializable, MessageListener<byte[]> {

    private String qName;
    private String answerQName;
    private String targetName;
    private String name;
    private IQueue<char[]> passwordQ;
    private IQueue<Answer> answerQ;
    private ITopic<byte[]> targetTopic;
    private ApplicationContext context;
    private KeyStore tester;
    private AtomicBoolean running;
    private byte[] store;
    
    @PostConstruct
    private void init() throws KeyStoreException {
        HazelcastInstance instance = context.getBean("instance", HazelcastInstance.class);
        passwordQ = instance.getQueue(qName);
        answerQ = instance.getQueue(answerQName);
        targetTopic = instance.getTopic(targetName);
        targetTopic.addMessageListener(this);
        tester = KeyStore.getInstance(KeyStore.getDefaultType());
        running = new AtomicBoolean(true);
    }
    
    @Override
    public void setPasswordQName(String qName) {
        this.qName = qName;
    }

    @Override
    public void setAnswerQName(String name) {
        this.answerQName = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onMessage(Message<byte[]> message) {
        store = message.getMessageObject();
    }

    @Override
    public void setTargetTopicName(String name) {
        targetName = name;
    }
    
    @Override
    public void run() {
        char[] guess;
        try {
            System.out.println("Openner thread running");
            while(running.get()) {
                guess = passwordQ.poll(2, TimeUnit.SECONDS);
                if(guess != null) {
                    if(guess.length == 0) {
                        running.set(false);
                        passwordQ.add(guess);
                        continue;
                    }
//                    InputStream in = new ByteArrayInputStream(store);
                    try(InputStream in = new ByteArrayInputStream(store);) {
//                        System.out.println("openning keystore with password " + String.valueOf(guess));
                        tester.load(in, guess);
                        Answer ans = new Answer();
                        ans.setKeyStore(store);
                        ans.setPassword(guess);
                        answerQ.add(ans);
                    } catch(CertificateException|IOException e) {
                        //do nothing, didn't work
                    }
                }
            }
        } catch( NoSuchAlgorithmException | InterruptedException e ){
            running.set(false);
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        running.set(false);
    }
    
    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
        odo.writeUTF(qName);
        odo.writeUTF(answerQName);
        odo.writeUTF(name);
        odo.writeUTF(targetName);
        odo.writeInt(store.length);
        odo.write(store);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
        qName = odi.readUTF();
        answerQName = odi.readUTF();
        name = odi.readUTF();
        targetName = odi.readUTF();
        int len = odi.readInt();
        store = new byte[len];
        odi.readFully(store);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }

    @Override
    public void setInitialTarget(TargetKeyStore target) {
        store = target.getTargetCopy();
    }
    
}
