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
