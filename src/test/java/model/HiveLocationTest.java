package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HiveLocationTest {
    @Test
    void testHiveLocationEqualsTrueWhenTwoDifferentObjectsButSameLocation(){
        HiveLocation l1 = new HiveLocation(0,0);
        HiveLocation l2 = new HiveLocation(0, 0);
        // Nieuw object maar we willen dat dit equal is zelfde waardes
        assertEquals(l1, l2);
    }

    @Test
    void testHiveLocationsInArrayListContainsTwoDifferentObjectsButFindsBoth(){
        HiveLocation l1 = new HiveLocation(1,0);
        HiveLocation l2 = new HiveLocation(1, 0);
        ArrayList<HiveLocation> test = new ArrayList<>();
        test.add(l1);
        // alleen l1 in arraylist maar we zeggen dat l2 equals is aan l1 omdat de coorindaten het zelfde zijn
        // we verwachten dus dat l2 in test zit
        assertTrue(test.contains(l2));
    }
}