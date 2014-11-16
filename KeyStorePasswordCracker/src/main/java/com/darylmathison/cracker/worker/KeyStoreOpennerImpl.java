/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.worker;

import com.darylmathison.cracker.data.Answer;
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
    private String topicName;
    private String targetName;
    private String name;
    private IQueue<char[]> openningQ;
    private ITopic<Answer> answerTopic;
    private ITopic<byte[]> targetTopic;
    private ApplicationContext context;
    private KeyStore tester;
    private AtomicBoolean running;
    private byte[] store;
    private boolean solved = false;
    
    @PostConstruct
    private void init() throws KeyStoreException {
        HazelcastInstance instance = context.getBean("instance", HazelcastInstance.class);
        openningQ = instance.getQueue(qName);
        answerTopic = instance.getTopic(topicName);
        targetTopic = instance.getTopic(targetName);
        targetTopic.addMessageListener(this);
        tester = KeyStore.getInstance(KeyStore.getDefaultType());
        running = new AtomicBoolean(true);
    }
    
    @Override
    public void setOpennerQName(String qName) {
        this.qName = qName;
    }

    @Override
    public void setTopicName(String topicName) {
        this.topicName = topicName;
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
        solved = false;
    }

    @Override
    public void setKeyStoreTopicName(String name) {
        topicName = name;
    }
    
    @Override
    public void run() {
        char[] guess;
        try {
            while(running.get()) {
                guess = openningQ.poll(2, TimeUnit.SECONDS);
                if(guess != null  && !solved) {
                    InputStream in = new ByteArrayInputStream(store);
                    try {
                        tester.load(in, guess);
                        Answer ans = new Answer();
                        ans.setKeyStore(store);
                        ans.setPassword(guess);
                        answerTopic.publish(ans);
                        solved = true;
                    } catch(CertificateException|IOException e) {
                        //do nothing, didn't work
                    } finally {
                        try {
                            in.close();
                        } catch(Exception e) {
                            //do nothing
                        }
                    }
                }
            }
        } catch( NoSuchAlgorithmException | InterruptedException e ){
            running.set(false);
        }
    }

    @Override
    public void stop() {
        running.set(false);
    }
    
    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
        odo.writeUTF(qName);
        odo.writeUTF(topicName);
        odo.writeUTF(name);
        odo.writeUTF(targetName);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
        qName = odi.readUTF();
        topicName = odi.readUTF();
        name = odi.readUTF();
        targetName = odi.readUTF();
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }
}
