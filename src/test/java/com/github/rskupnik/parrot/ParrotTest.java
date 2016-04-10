package com.github.rskupnik.parrot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParrotTest {

    @Test
    public void shouldLoadTestFromClasspath() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "test";

        // when
        Parrot.init();

        // then
        assertTrue(Parrot.get(param).isPresent());
        assertEquals(properOutcome, Parrot.get(param).get());
    }

    @Test
    public void shouldLoadFromUserDir() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";

        // when
        Parrot.init();

        // then
        assertTrue(Parrot.get(param).isPresent());
        assertEquals(properOutcome, Parrot.get(param).get());
    }

    @Test
    public void shouldNotFindParamThatIsNotInFile() throws IOException {
        // given
        String param = "invalidParam";

        // when
        Parrot.init();

        // then
        assertFalse(Parrot.get(param).isPresent());
    }
}
