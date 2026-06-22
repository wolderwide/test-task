import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EffectivityTest {

    @Test
    public void checkToString() {
        var range = "1,2,4-6";
        var model = "MC-21";
        var effectivity = new Effectivity(range, model);

        assertEquals("1,2,3-6 (MC-21)", effectivity.toString());
    }

    @Test
    public void ifCorrectEffectivity_thenSuccess() {
        var range = "1,2,4-6";
        var model = "MC-21";

        assertDoesNotThrow(() -> new Effectivity(range, model));
    }

    @Test
    public void ifIncorrectEffectivity_thenThrowError() {
        var range = "1,$,&";
        var model = "MC*-21";

        assertThrows(EffectivityIncorrectException.class, () -> new Effectivity(range, model));
    }


}
