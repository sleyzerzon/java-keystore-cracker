/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.master;

import com.darylmathison.cracker.data.Answer;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Daryl
 */
public class Spinner implements ApplicationContextAware, Runnable {
    
    private CombonationMaker comboMaker;

    private ApplicationContext context;
    
    private IQueue<char[]> comboQ;
    
    private ITopic<Answer> answerTopic;
    private ITopic<byte[]> targetTopic;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static final void main(String[] args) {
        
    }
}
