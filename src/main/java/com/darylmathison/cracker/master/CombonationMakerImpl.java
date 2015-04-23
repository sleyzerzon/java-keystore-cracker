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
    
//    @Override
//    public char[] nextCombonation() {
//        if(empty) { 
//            return null; 
//        }
//        int index = 0;
//        boolean shouldLoop = true;
//        while(shouldLoop) {
////            System.out.println("looping charIndex.length = " + charIndex.length);
//            if(index == mostSignificantIndex 
//                    && mostSignificantIndex >= maxLength 
//                    && charIndex[index - 1] + 1 >= chars.length) {
//                shouldLoop = false;
//                empty = true;
//                return null;
//            } else if(index == mostSignificantIndex
//                    && mostSignificantIndex < maxLength
//                    && charIndex[index - 1] + 1 >= chars.length) {
//                mostSignificantIndex++;
//                charIndex[index] = 0;
//                for(int i = 0; i < index; i++) {
//                    charIndex[i] = 0;
//                } 
////                charIndex[index - 1] = 0;
//                shouldLoop = false;
//            } else if(charIndex[index] < chars.length) {//else if(index < mostSignificantIndex 
//////                    && mostSignificantIndex != maxLength
////                    && charIndex[index] < chars.length) {
//                if(charIndex[index] + 1 >= chars.length) {
////                    charIndex[index] = 0;
//                    index++;
//                } else {
//                    charIndex[index]++;
//                    shouldLoop = false;
//                }
//            }  else {
//                System.out.println("should not be here");
//                shouldLoop = false;
//            }
//        }
//        builder.setLength(0);
//        for(int i: charIndex) {
//            if(i != -1) {
//                builder.insert(0, chars[i]);
//            } 
//        }
//        System.out.println("builder in nextCombonation -> " + builder.toString());
//        char[] ret = builder.toString().toCharArray();
//        return ret;
//    }

    @Override
    public char[] nextCombonation() {
        
        if(empty) {
            return null;
        }
        if(mostSignificantIndex < maxLength) {
//            System.out.printf("mostSignificantIndex: %d currentIndex: %d\n", mostSignificantIndex, currentIndex);
            if(currentIndex < mostSignificantIndex) {
                if(charIndex[currentIndex] + 1 >= chars.length) {
                    while(currentIndex < mostSignificantIndex 
                            && charIndex[currentIndex] + 1 >= chars.length) {
                        currentIndex++;
                    }
//                    System.out.printf("mostSignificantIndex: %d currentIndex: %d\n", mostSignificantIndex, currentIndex);
                    if(currentIndex == mostSignificantIndex) {
//                        System.out.printf("mostSignificantIndex: %d currentIndex: %d\n", mostSignificantIndex, currentIndex);
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
//                System.out.printf("mostSignificantIndex: %d currentIndex: %d\n", mostSignificantIndex, currentIndex);
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
//            System.out.println("builder in nextCombonation -> " + builder.toString());
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
