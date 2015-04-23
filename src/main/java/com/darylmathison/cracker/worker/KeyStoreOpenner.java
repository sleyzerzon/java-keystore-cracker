/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.worker;

import com.darylmathison.cracker.data.TargetKeyStore;

/**
 *
 * @author Daryl
 */
public interface KeyStoreOpenner extends Runnable {
    public void setPasswordQName(String qName);
    public void setAnswerQName(String qName);
    public void setName(String name);
    public String getName();
    public void stop();
    public void setTargetTopicName(String name);
    public void setInitialTarget(TargetKeyStore target);
}
