import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EffectivityHelperTest {

    @Test
    public void checkUnion() {
        var effectivity1 = "1,17-UP";
        var effectivity2 = "2-13";

        assertEquals("1-13,17-UP", EffectivityHelper.union(effectivity1, effectivity2));
    }

    @Test
    public void checkSubtraction() {
        var effectivity1 = "1,2,4-7";
        var effectivity2 = "2,6,7";

        assertEquals("1,4-5", EffectivityHelper.subtract(effectivity1, effectivity2));
    }

    @Test
    public void checkCross() {
        var effectivity1 = "1,3-5,7-9";
        var effectivity2 = "1-4,8-UP";

        assertEquals("1,3-4,8-9", EffectivityHelper.cross(effectivity1, effectivity2));
    }

    @Test
    public void checkInclude() {
        var effectivity1 = "1-9";
        var effectivity2 = "2-4,6-8";

        assertEquals(true, EffectivityHelper.include(effectivity1, effectivity2));
    }

    @Test
    public void checkIncludePart() {
        var effectivity1 = "1-9";
        var effectivity2 = "2-4,6-12";

        assertEquals(true, EffectivityHelper.includePart(effectivity1, effectivity2));
    }
}
