/*
    Copyright 2016 Radosław Skupnik

    This file is part of Parrot.

    Parrot is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Parrot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Parrot; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.rskupnik.parrot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParrotTest {

    @Test
    public void shouldLoadTestFromClasspath() {
        // given
        String properOutcome = "passed";
        String param = "test";

        // when
        Parrot parrot = Parrot.load();

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
    }

    @Test
    public void shouldLoadTestFromClasspathOnlyAllowedFiles() {
        // given
        String properOutcome = "passed";
        String param = "test";
        String invalidParam = "test2";

        // when
        Parrot parrot = Parrot.load("test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadTestFromClasspathOnlyAllowedFilesWithFileEnding() {
        // given
        String properOutcome = "passed";
        String param = "test";
        String invalidParam = "test2";

        // when
        Parrot parrot = Parrot.load("test.properties");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadFromUserDir() {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";

        // when
        Parrot parrot = Parrot.load();

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
    }

    @Test
    public void shouldLoadFromUserDirOnlyAllowedFiles() {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";
        String invalidParam = "testUserDir2";

        // when
        Parrot parrot = Parrot.load("testUserDir", "test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldLoadFromUserDirOnlyAllowedFilesWithFileEnding() {
        // given
        String properOutcome = "passed";
        String param = "testUserDir";
        String invalidParam = "testUserDir2";

        // when
        Parrot parrot = Parrot.load("testUserDir.properties", "test");

        // then
        assertTrue(parrot.get(param).isPresent());
        assertEquals(properOutcome, parrot.get(param).get());
        assertFalse(parrot.get(invalidParam).isPresent());
    }

    @Test
    public void shouldNotFindParamThatIsNotInFile() {
        // given
        String param = "invalidParam";

        // when
        Parrot parrot = Parrot.load();

        // then
        assertFalse(parrot.get(param).isPresent());
    }

    @Test
    public void shouldInitiateTwoSeparateParrots() {
        // given
        String param1 = "test";
        String param2 = "test2";
        String properOutcome = "passed";

        // when
        Parrot parrot1 = Parrot.load("test");
        Parrot parrot2 = Parrot.load("test2");

        // then
        assertTrue(parrot1.get(param1).isPresent());
        assertEquals(properOutcome, parrot1.get(param1).get());
        assertFalse(parrot1.get(param2).isPresent());
        assertTrue(parrot2.get(param2).isPresent());
        assertEquals(properOutcome, parrot2.get(param2).get());
        assertFalse(parrot2.get(param1).isPresent());
    }

    @Test
    public void shouldMockParrot() {
        // given
        String param = "test";
        String anticipatedOutcome = "substituted";
        final Parrot mockedParrot = mock(Parrot.class);
        when(mockedParrot.get(param)).thenReturn(Optional.of(anticipatedOutcome));

        // when
        Optional<String> output = mockedParrot.get(param);

        // then
        assertTrue(output.isPresent());
        assertEquals(anticipatedOutcome, output.get());
    }
}
