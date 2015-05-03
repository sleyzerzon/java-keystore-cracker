package com.darylmathison.cracker.master;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertNull;

/**
 *
 * @author Daryl
 */
public class CombonationMakerImplTest {
    private static Logger logger = LoggerFactory.getLogger(CombonationMakerImplTest.class);

    private CombonationMakerImpl maker;
    
    public CombonationMakerImplTest() {
    }
    
    @Before
    public void setup() {
        maker = new CombonationMakerImpl(1,3);
    }
    /**
     * Test of nextCombonation method, of class CombonationMakerImpl.
     */
    @Test
    public void testNextCombonation() {
        logger.info("nextCombonation");
        while(!maker.isEmpty()) {
            System.out.println(Arrays.toString(maker.nextCombonation()));
        }
        
        assertNull(maker.nextCombonation());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMaxIsLessThanMin() {
        logger.info("testMaxIsLessThanMin");
        CombonationMakerImpl combo = new CombonationMakerImpl(2, 1);
    }
}
