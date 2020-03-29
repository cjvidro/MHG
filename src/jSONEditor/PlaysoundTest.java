package jSONEditor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaysoundTest {
    Playsound playsound;

    @Before
    public void before() {
        playsound = new Playsound();
    }

    @Test
    public void initial() {
        assertEquals(0, playsound.numberSounds());
        assertNull(playsound.getCategory());
        assertNull(playsound.getMin());
        assertNull(playsound.getMax());
        assertNull(playsound.getName());
    }

    @Test
    public void addSound() {
        playsound.addSound("/animals/cow", true, null, 0.0, false);
        assertEquals(1, playsound.numberSounds());

        Sound sound = playsound.getSound(0);
        assertEquals("/animals/cow", sound.getDirectory());
        assertTrue(sound.getStream());
        assertNull(sound.getPitch());
        assertEquals(new Double(0.0), sound.getVolume());
        assertFalse(sound.getLOLM());
    }

    @Test
    public void removeSound() {
        playsound.addSound("/animals/cow", true, null, 0.0, false);
        assertEquals(1, playsound.numberSounds());
        playsound.removeSound(playsound.getSound(0));
        assertEquals(0, playsound.numberSounds());
    }

    @Test
    public void numberSounds() {
        assertEquals(0, playsound.numberSounds());
        for (int i = 1; i < 11; i++) {
            playsound.addSound("/animals/cow" + i, true, null, 0.0, false);
            assertEquals(i, playsound.numberSounds());
        }
    }

    @Test
    public void category() {
        playsound.setCategory(Category.master);
        assertEquals(Category.master, playsound.getCategory());
    }
    @Test
    public void min() {
        playsound.setMin(5.1);
        assertEquals(new Double(5.1), playsound.getMin());
    }

    @Test
    public void max() {
        playsound.setMax(5.1);
        assertEquals(new Double(5.1), playsound.getMax());
    }

    @Test
    public void name() {
        playsound.setName("cat");
        assertEquals("cat", playsound.getName());
    }

    @Test
    public void group() {
        PlaysoundGroup group = new PlaysoundGroup();
        group.setName("myGroup");
        assertEquals("myGroup", group.getName());

        playsound.setGroup(group);
        assertEquals(group, playsound.getGroup());

    }
}