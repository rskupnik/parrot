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
        Parrot parrot = Parrot.newInstance();

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
        Parrot parrot = Parrot.newInstance("test");

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
        Parrot parrot = Parrot.newInstance("test.properties");

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
        Parrot parrot = Parrot.newInstance();

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
        Parrot parrot = Parrot.newInstance("testUserDir", "test");

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
        Parrot parrot = Parrot.newInstance("testUserDir.properties", "test");

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
        Parrot parrot = Parrot.newInstance();

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
        Parrot parrot1 = Parrot.newInstance("test");
        Parrot parrot2 = Parrot.newInstance("test2");

        // then
        assertTrue(parrot1.get(param1).isPresent());
        assertEquals(properOutcome, parrot1.get(param1).get());
        assertFalse(parrot1.get(param2).isPresent());
        assertTrue(parrot2.get(param2).isPresent());
        assertEquals(properOutcome, parrot2.get(param2).get());
        assertFalse(parrot2.get(param1).isPresent());
    }

    @Test
    public void shouldInitiateStaticParrot() {
        // given
        String param1 = "test";
        String param2 = "test2";
        String properOutcome = "passed";

        // when
        Parrot parrot1 = Parrot.newInstance("test");
        Parrot.newInstance("test2");

        // then
        assertTrue(parrot1.get(param1).isPresent());
        assertEquals(properOutcome, parrot1.get(param1).get());
        assertFalse(parrot1.get(param2).isPresent());
        assertTrue(Parrot.getInstance().get(param2).isPresent());
        assertEquals(properOutcome, Parrot.getInstance().get(param2).get());
        assertFalse(Parrot.getInstance().get(param1).isPresent());
    }
}
