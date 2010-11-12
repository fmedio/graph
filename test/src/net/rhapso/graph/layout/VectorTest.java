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

import junit.framework.TestCase;

public class VectorTest extends TestCase {
    public void testAdd() {
        assertEquals("[x:1.0, y:1.0]", new Vector(0, 1).add(new Vector(1, 0)).toString());
        assertEquals("[x:1.0, y:1.0]", new Vector(0, 1).add(new Vector(1, 0)).toString());
    }

    public void testNormalize() {
        float oneOverSquareRootOfTwo = 1f / (float) Math.sqrt(2);
        Vector normalized = new Vector(1, 1).normalize();
        assertEquals(oneOverSquareRootOfTwo, normalized.getX());
        assertEquals(oneOverSquareRootOfTwo, normalized.getY());
        assertEquals(0.99999994f, normalized.length());
    }

    public void testLength() throws Exception {
        assertEquals((float) Math.sqrt(2), new Vector(1, 1).length());
    }
    public void testMultiply() throws Exception {
        assertEquals("[x:2.0, y:2.0]", new Vector(1, 1).multiply(2f).toString());
    }
}
