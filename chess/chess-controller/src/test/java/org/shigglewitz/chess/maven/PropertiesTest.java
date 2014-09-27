package org.shigglewitz.chess.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;

public class PropertiesTest {
    @Test
    public void testNoPropertiesUnfiltered() throws IllegalArgumentException,
            IllegalAccessException {
        Field[] fields = Properties.class.getDeclaredFields();

        for (Field f : fields) {
            assertEquals("Property " + f.getName() + " is not a string!",
                    String.class, f.getType());
            assertNotNull("Property " + f.getName() + " is null", f.get(null));
            assertFalse(
                    "Property " + f.getName() + " was not filtered: "
                            + f.get(null),
                    f.get(null).toString().startsWith("${"));
        }
    }
}
