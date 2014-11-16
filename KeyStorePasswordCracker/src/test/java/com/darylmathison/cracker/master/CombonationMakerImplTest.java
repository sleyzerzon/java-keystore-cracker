/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darylmathison.cracker.master;

import java.util.Arrays;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Daryl
 */
public class CombonationMakerImplTest {
    private CombonationMakerImpl maker;
    
    public CombonationMakerImplTest() {
    }
    
    @Before
    public void setup() {
        maker = new CombonationMakerImpl(3, 4);
    }
    /**
     * Test of nextCombonation method, of class CombonationMakerImpl.
     */
    @Test
    public void testNextCombonation() {
        System.out.println("nextCombonation");
        while(!maker.isEmpty()) {
            System.out.println(Arrays.toString(maker.nextCombonation()));
        }
        
        assertNull(maker.nextCombonation());
    }
    
}
