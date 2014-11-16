/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.worker;

/**
 *
 * @author Daryl
 */
public interface KeyStoreOpenner extends Runnable {
    public void setOpennerQName(String qName);
    public void setTopicName(String topicName);
    public void setName(String name);
    public String getName();
    public void stop();
    public void setKeyStoreTopicName(String name);
}
