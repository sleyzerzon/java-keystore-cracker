package com.darylmathison.cracker.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daryl
 */
public final class CombonationMakerImpl implements CombonationMaker {
    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890][?/<~#!@$%^&*()+=}|:;'>{ ".toCharArray();
    private static final Logger logger = LoggerFactory.getLogger(CombonationMakerImpl.class);

    private StringBuilder builder;
    private int[] charIndex;
    private int mostSignificantIndex;
    private int currentIndex;
    private final int minLength;
    private final int maxLength;
    private boolean empty;
    
    public CombonationMakerImpl(int minLen, int maxLen) {
        if(maxLen < minLen) {
            throw new IllegalArgumentException("minLen cannot be larger than maxLen");
        }
        minLength = minLen;
        maxLength = maxLen;
        charIndex = new int[maxLength];
        builder = new StringBuilder(maxLength);
        reset();
    }

    @Override
    public char[] nextCombonation() {
        
        if(empty) {
            return null;
        }
        if(mostSignificantIndex < maxLength) {
            logger.trace("mostSignificantIndex: ${1} currentIndex: ${2}", mostSignificantIndex, currentIndex);
            if(currentIndex < mostSignificantIndex) {
                if(charIndex[currentIndex] + 1 >= chars.length) {
                    while(currentIndex < mostSignificantIndex 
                            && charIndex[currentIndex] + 1 >= chars.length) {
                        currentIndex++;
                    }
                    logger.trace("mostSignificantIndex: ${1} currentIndex: ${2}", mostSignificantIndex, currentIndex);
                    if(currentIndex == mostSignificantIndex) {
                        logger.trace("mostSignificantIndex: ${1} currentIndex: ${2}", mostSignificantIndex, currentIndex);
                        mostSignificantIndex++;
                        charIndex[currentIndex]++;
                        currentIndex = (currentIndex > 0)? currentIndex - 1:0;
                        for(; currentIndex > 0; currentIndex--) {
                            charIndex[currentIndex] = 0;
                        }
                        charIndex[currentIndex] = 0;
                    } else {
                        charIndex[currentIndex]++;
                        currentIndex = (currentIndex > 0)? currentIndex - 1:0;
                        for(;currentIndex > 0; currentIndex--) {
                            charIndex[currentIndex] = 0;
                        }
                        charIndex[currentIndex] = 0;
                    }
                } else { // if(charIndex[currentIndex] + 1 < chars.length)
                    charIndex[currentIndex]++;
                }
            } 
        } else { //if(mostSignificantIndex >= maxLength)
            if(currentIndex < mostSignificantIndex) {
                logger.trace("mostSignificantIndex: ${1} currentIndex: ${2}", mostSignificantIndex, currentIndex);
                if(charIndex[currentIndex] + 1 >= chars.length) {
                    while(currentIndex < mostSignificantIndex 
                            && charIndex[currentIndex] + 1 >= chars.length) {
                        currentIndex++;
                    }
//                    System.out.printf("mostSignificantIndex: %d currentIndex: %d\n", mostSignificantIndex, currentIndex);
                    if(currentIndex == mostSignificantIndex) {
                        empty = true;
                    } else {
                        for(;currentIndex <= 0; currentIndex--) {
                            charIndex[currentIndex] = 0;
                       }
                    }
                } else { // if(charIndex[currentIndex] + 1 < chars.length)
                    charIndex[currentIndex]++;
                }
            } 
        }
        
        if(!empty) {
            builder.setLength(0);
            for(int i: charIndex) {
                if(i != -1) {
                    builder.insert(0, chars[i]);
                } 
            }
            logger.debug("builder in nextCombonation -> ${1}", builder.toString());
            char[] ret = builder.toString().toCharArray();
            return ret;
        } else {
            return null;
        }
    }
    
    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void reset() {
        for(int i = charIndex.length - 1; i >= minLength; i--) {
            charIndex[i] = -1;
        }
        for(int i = 0; i < minLength - 1; i++) {
            charIndex[i] = 0;
        }
        charIndex[0] = -1;
//        charIndex[0] = 0;
        currentIndex = 0;
        mostSignificantIndex = 1;
        empty = false;
    }
}
