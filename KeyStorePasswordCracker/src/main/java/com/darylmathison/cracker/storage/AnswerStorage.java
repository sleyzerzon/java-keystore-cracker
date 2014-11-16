/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.storage;

import com.darylmathison.cracker.data.Answer;
import com.darylmathison.cracker.data.TargetKeyStore;

/**
 *
 * @author Daryl
 */
public interface AnswerStorage {
    Answer findAnswer(TargetKeyStore target);
    void storeAnswer(Answer ans);
}
