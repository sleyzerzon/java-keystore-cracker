/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.master;

/**
 *
 * @author Daryl
 */
public final class CombonationMakerImpl implements CombonationMaker {
    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890][?/<~#!@$%^&*()+=}|:;'>{ ".toCharArray();
    
    private StringBuilder builder;
    private int[] charIndex;
    private int mostSignificantIndex;
    private final int minLength;
    private final int maxLength;
    private boolean empty;
    
    public CombonationMakerImpl(int minLen, int maxLen) {
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
        int index = 0;
        boolean shouldLoop = true;
        while(shouldLoop) {
//            System.out.println("looping");
            if(index == mostSignificantIndex 
                    && mostSignificantIndex >= maxLength 
                    && charIndex[index - 1] + 1 >= chars.length) {
                shouldLoop = false;
                empty = true;
            } else if(index < mostSignificantIndex 
//                    && mostSignificantIndex < maxLength
                    && charIndex[index] < chars.length) {
                if(charIndex[index] + 1 >= chars.length) {
                    charIndex[index] = 0;
                    index++;
                } else {
                    charIndex[index]++;
                    shouldLoop = false;
                }
            } else if(index == mostSignificantIndex
                    && mostSignificantIndex < maxLength
                    && charIndex[index - 1] + 1 >= chars.length) {
                mostSignificantIndex++;
                charIndex[index] = 0;
                shouldLoop = false;
            } else {
                System.out.println("should not be here");
                shouldLoop = false;
            }
        }
        builder.setLength(0);
        for(int i: charIndex) {
            if(i != -1) {
                builder.insert(0, chars[i]);
            } 
        }
//        System.out.println("builder in nextCombonation -> " + builder.toString());
        char[] ret = builder.toString().toCharArray();
        return ret;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void reset() {
        for(int i = 0; i < maxLength; i++) {
            charIndex[i] = -1;
        }
        for(int i = 1; i < minLength; i++) {
            charIndex[i] = 0;
        }
        mostSignificantIndex = minLength;
        empty = false;
    }
}
