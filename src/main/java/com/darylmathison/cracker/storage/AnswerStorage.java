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
