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

    @Test
    void testHashCode(){
        HiveLocation l1 = new HiveLocation(0,0);
        HiveLocation l2 = new HiveLocation(0,0);
        HiveLocation l3 = new HiveLocation(-1,0);
        HiveLocation l4 = new HiveLocation(-1,0);
        HiveLocation l5 = new HiveLocation(0,-1);
        HiveLocation l6 = new HiveLocation(0,-1);
        // Moet equal zijn
        assertTrue(l1.hashCode() == l2.hashCode());
        assertTrue(l3.hashCode() == l4.hashCode());
        assertTrue(l3.hashCode() == l4.hashCode());
        // Verwachten we niet equal
        assertTrue(l3.hashCode() != l1.hashCode());
        assertTrue(l1.hashCode() != l5.hashCode());
        assertTrue(l5.hashCode() != l3.hashCode());
        assertTrue(l5.hashCode() != l2.hashCode());
    }
}