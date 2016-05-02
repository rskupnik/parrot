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
        Parrot parrot = new Parrot();

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
    }

    @Test
    public void shouldLoadTestFromClasspathOnlyAllowedFiles() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "test";
        String invalidParam = "test2";

        // when
        Parrot parrot = new Parrot("test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadTestFromClasspathOnlyAllowedFilesWithFileEnding() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "test";
        String invalidParam = "test2";

        // when
        Parrot parrot = new Parrot("test.properties");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadFromUserDir() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";

        // when
        Parrot parrot = new Parrot();

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
    }

    @Test
    public void shouldLoadFromUserDirOnlyAllowedFiles() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";
        String invalidParam = "testUserDir2";

        // when
        Parrot parrot = new Parrot("testUserDir", "test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadFromUserDirOnlyAllowedFilesWithFileEnding() throws IOException {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";
        String invalidParam = "testUserDir2";

        // when
        Parrot parrot = new Parrot("testUserDir.properties", "test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldNotFindParamThatIsNotInFile() throws IOException {
        // given
        String param = "invalidParam";

        // when
        Parrot parrot = new Parrot();

        // then
        assertFalse(parrot.get(param).isPresent());
    }
}
