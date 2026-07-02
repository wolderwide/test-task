import org.example.EffectivityHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EffectivityHelperTest {

    @Test
    public void checkUnion() {
        var effectivity1 = "1,17-UP (MC-21)";
        var effectivity2 = "2-13 (MC-21)";

        assertEquals("1-13,17-UP (MC-21)", EffectivityHelper.union(effectivity1, effectivity2).toString());
    }

    @Test
    public void checkSubtraction() {
        var effectivity1 = "1,2,4-7 (MC-21)";
        var effectivity2 = "2,6,7 (MC-21)";

        assertEquals("1,4-5 (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());

        effectivity1 = "5-10 (MC-21)";
        effectivity2 = "7-8 (MC-21)";

        assertEquals("5-6,9-10 (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());

        effectivity1 = "1-10 (MC-21)";
        effectivity2 = "1-4 (MC-21)";

        assertEquals("5-10 (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());

        effectivity1 = "5-10 (MC-21)";
        effectivity2 = "1-15 (MC-21)";

        assertNull(EffectivityHelper.subtract(effectivity1, effectivity2));

        effectivity1 = "1-20 (MC-21)";
        effectivity2 = "5,10,15 (MC-21)";

        assertEquals("1-4,6-9,11-14,16-20 (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());

        effectivity1 = "5-UP (MC-21)";
        effectivity2 = "7-9 (MC-21)";

        assertEquals("5-6,10-UP (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());

        effectivity1 = "5-UP (MC-21)";
        effectivity2 = "10-UP (MC-21)";

        assertEquals("5-9 (MC-21)", EffectivityHelper.subtract(effectivity1, effectivity2).toString());
    }

    @Test
    public void checkIntersect() {
        var effectivity1 = "1,3-5,7-9 (MC-21)";
        var effectivity2 = "1-4,8-UP (MC-21)";

        assertEquals("1,3-4,8-9 (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());

        effectivity1 = "1-10 (MC-21)";
        effectivity2 = "5-15 (MC-21)";

        assertEquals("5-10 (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());

        effectivity1 = "1-20 (MC-21)";
        effectivity2 = "5-10 (MC-21)";

        assertEquals("5-10 (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());

        effectivity1 = "1-5 (MC-21)";
        effectivity2 = "10-15 (MC-21)";

        assertNull(EffectivityHelper.intersect(effectivity1, effectivity2));

        effectivity1 = "1-10,20-30 (MC-21)";
        effectivity2 = "8-22 (MC-21)";

        assertEquals("8-10,20-22 (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());

        effectivity1 = "1-20 (MC-21)";
        effectivity2 = "15-UP (MC-21)";

        assertEquals("15-20 (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());

        effectivity1 = "5-UP (MC-21)";
        effectivity2 = "15-UP (MC-21)";

        assertEquals("15-UP (MC-21)", EffectivityHelper.intersect(effectivity1, effectivity2).toString());
    }

    @Test
    public void checkInclude() {
        var effectivity1 = "1-9 (MC-21)";
        var effectivity2 = "2-4,6-8 (MC-21)";

        assertTrue(EffectivityHelper.include(effectivity1, effectivity2));

        effectivity1 = "1-20 (MC-21)";
        effectivity2 = "5-10 (MC-21)";

        assertTrue(EffectivityHelper.include(effectivity1, effectivity2));

        effectivity1 = "5-10 (MC-21)";
        effectivity2 = "1-20 (MC-21)";

        assertFalse(EffectivityHelper.include(effectivity1, effectivity2));

        effectivity1 = "1-10 (MC-21)";
        effectivity2 = "3,5,8 (MC-21)";

        assertTrue(EffectivityHelper.include(effectivity1, effectivity2));

        effectivity1 = "1-20 (MC-21)";
        effectivity2 = "15-UP (MC-21)";

        assertFalse(EffectivityHelper.include(effectivity1, effectivity2));

        effectivity1 = "10-UP (MC-21)";
        effectivity2 = "15-20 (MC-21)";

        assertTrue(EffectivityHelper.include(effectivity1, effectivity2));
    }

    @Test
    public void checkIncludePart() {
        var effectivity1 = "1-9 (MC-21)";
        var effectivity2 = "2-4,6-12 (MC-21)";

        assertTrue(EffectivityHelper.includePart(effectivity1, effectivity2));

        effectivity1 = "1-5 (MC-21)";
        effectivity2 = "10-15 (MC-21)";

        assertFalse(EffectivityHelper.includePart(effectivity1, effectivity2));

        effectivity1 = "10-UP (MC-21)";
        effectivity2 = "1-5 (MC-21)";

        assertFalse(EffectivityHelper.includePart(effectivity1, effectivity2));

        effectivity1 = "10-UP (MC-21)";
        effectivity2 = "5-15 (MC-21)";

        assertTrue(EffectivityHelper.includePart(effectivity1, effectivity2));
    }
}
