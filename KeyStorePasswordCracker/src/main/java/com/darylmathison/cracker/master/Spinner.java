/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.master;

import com.darylmathison.cracker.data.Answer;
import com.darylmathison.cracker.data.TargetKeyStore;
import com.darylmathison.cracker.worker.KeyStoreOpenner;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.spring.context.SpringAware;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Daryl
 */
@SpringAware
public class Spinner implements ApplicationContextAware, Runnable {

    private String answerQName;
    private String passwordQName;
    private String targetTopicName;
    private String registrationIdTargetTopic;

    private TargetKeyStore store;

    private CombonationMaker comboMaker;
    private HazelcastInstance instance;
    private ApplicationContext context;

    private IQueue<char[]> passwordQ;
    private IQueue<Answer> answerQ;
    private ITopic<byte[]> targetTopic;
    private IExecutorService pool;
    private AtomicBoolean keepRunning;
    private AtomicBoolean keepFeeding;

    @PostConstruct
    private void init() {
        keepRunning = new AtomicBoolean(true);
        keepFeeding = new AtomicBoolean(true);
        instance = context.getBean("instance", HazelcastInstance.class);
        pool = instance.getExecutorService("pool");
        passwordQ = instance.getQueue(passwordQName);
        answerQ = instance.getQueue(answerQName);
        answerQ.addItemListener(new ItemListener<Answer>() {

            @Override
            public void itemAdded(ItemEvent<Answer> ie) {
                //kill all guessing
//                keepFeeding.set(false);
                keepRunning.set(false);
                passwordQ.clear();
                comboMaker.reset();
                Answer ans = answerQ.poll();
                passwordQ.add(new char[0]);
                System.out.println(String.valueOf(ans.getPassword()));
            }

            @Override
            public void itemRemoved(ItemEvent<Answer> ie) {
                //don't care
            }
        }, false);
        targetTopic = instance.getTopic(targetTopicName);
        registrationIdTargetTopic = targetTopic.addMessageListener(new MessageListener<byte[]>() {

            @Override
            public void onMessage(Message<byte[]> msg) {
                store.setTarget(msg.getMessageObject());
                comboMaker.reset();
            }
        });
    }

    @PreDestroy
    private void destroy() {
        targetTopic.removeMessageListener(registrationIdTargetTopic);
        keepRunning.set(false);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }

    public void setComboMaker(CombonationMaker maker) {
        comboMaker = maker;
    }

    public void setAnswerQName(String name) {
        answerQName = name;
    }

    public void setPasswordQName(String name) {
        passwordQName = name;
    }

    public void setTargetTopicName(String name) {
        targetTopicName = name;
    }

    public void setTarget(TargetKeyStore store) {
        this.store = store;
    }

    @Override
    public void run() {
        System.out.println("Spinner running");
        KeyStoreOpenner worker = context.getBean("worker", KeyStoreOpenner.class);
        worker.setInitialTarget(store);
        worker.setName("I don't know yet");
        pool.executeOnAllMembers(worker);
        
        instance.getCluster().addMembershipListener(new MembershipListener() {

            @Override
            public void memberAdded(MembershipEvent me) {
                launchWorker(me.getMember());
            }

            @Override
            public void memberRemoved(MembershipEvent me) {
                //don't care
            }

            @Override
            public void memberAttributeChanged(MemberAttributeEvent mae) {
                //don't care
            }

        });

        try {
            while (keepRunning.get()) {
                while (keepFeeding.get() && !comboMaker.isEmpty()) {
                    try {
                        char[] pass = comboMaker.nextCombonation();
                        if(pass == null) {
                            System.out.println("help the password is null");
                        }
                    passwordQ.offer(pass, 1, TimeUnit.SECONDS);
                    //get possible passwords and put them on the Q
                    // When the maker is empty, turn the keepFeeding off.
                    System.out.println("passwordQ is " + passwordQ.size());
                    } catch(NullPointerException npe) {
                        System.out.println("hit npe");
                        npe.printStackTrace();
                        passwordQ = instance.getQueue(passwordQName);
                    }
                }
                                //put a wait here till another target comes up.
            }
        } catch(InterruptedException ie) {
            System.out.println("leaving the scene");
        }
    }

    public void stop() {
        keepRunning.set(false);
    }

    private void launchWorker(Member member) {
        System.out.println("launching worker");
        KeyStoreOpenner worker = context.getBean("worker", KeyStoreOpenner.class);
        worker.setInitialTarget(store);
        worker.setName("I don't know yet");

        pool.executeOnMember(worker, member);
    }
}
