/*
 * The MIT License
 *
 * Copyright (c) 2010 Fabrice Medio <fmedio@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.rhapso.graph.layout;

import com.google.common.collect.Sets;
import junit.framework.TestCase;

import java.util.HashSet;

public class PointTest extends TestCase {
    public void testHashSetBehavior() throws Exception {
        Point<String> point = new Point<String>(0f, 0f, "foo");
        HashSet<Point<String>> set = Sets.newHashSet(point);
        assertTrue(set.contains(new Point<String>(1f, 1f, "foo")));
    }

    public void testEquals() throws Exception {
        Point<String> left = new Point<String>(0f, 0f, "foo");
        assertTrue(left.equals(new Point<String>(0f, 0f, "foo")));
        assertTrue(left.equals(new Point<String>(1f, 1f, "foo")));
        assertFalse(left.equals(new Point<String>(0f, 0f, "bar")));
    }

    public void testDistance() throws Exception {
        Point<String> point = new Point<String>(0, 0, "");
        assertEquals(1f, point.distance(new Point<String>(1, 0, "")));
        assertEquals((float)Math.sqrt(2), point.distance(new Point<String>(1, 1, "")));
        assertEquals((float)Math.sqrt(2), point.distance(new Point<String>(-1, -1, "")));
    }

    public void testMove() throws Exception {
        Point<String> point = new Point<String>(0, 0, "");
        point.move(new Vector(1, 1));
        assertEquals(1f, point.getX());
        assertEquals(1f, point.getY());
    }

    public void testDilate() throws Exception {
        Point<String> point = new Point<String>(3, 3, "");
        point.dilate(new Vector(3, 3));
        assertEquals(9f, point.getX());
        assertEquals(9f, point.getY());
    }

    public void testGetCoulombRepulsion() throws Exception {
        Point<String> point = new Point<String>(0, 0, "");
        assertEquals("[x:-0.49999997, y:-0.49999997]", point.coulombRepulsion(new Point<String>(1, 1, ""), 1f, 1f).toString());
        assertEquals("[x:-0.24999999, y:-0.24999999]", point.coulombRepulsion(new Point<String>(2, 2, ""), 1f, 1f).toString());
        assertEquals("[x:-0.16666669, y:-0.16666669]", point.coulombRepulsion(new Point<String>(3, 3, ""), 1f, 1f).toString());
        assertEquals("[x:-0.33333337, y:-0.33333337]", point.coulombRepulsion(new Point<String>(3, 3, ""), 2f, 1f).toString());
        assertEquals("[x:-0.21081853, y:-0.42163706]", point.coulombRepulsion(new Point<String>(3, 3, ""), 2f, 2f).toString());
    }

    public void testHookesAttraction() throws Exception {
        Point<String> point = new Point<String>(0, 0, "");
        assertEquals("[x:1.0, y:1.0]", point.hookesAttraction(new Point<String>(1, 1, "")).toString());
    }
}
