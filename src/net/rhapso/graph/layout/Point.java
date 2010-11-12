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

public class Point<T> {
    private float x, y;
    final private T tee;

    public Point(float x, float y, T tee) {
        this.x = x;
        this.y = y;
        this.tee = tee;
    }

    public Point(Point<T> point) {
        this(point.getX(), point.getY(), point.getT());
    }

    public static <T> Point<T> random(T t) {
        return new Point<T>(new Double(Math.random()).floatValue(), new Double(Math.random()).floatValue(), t);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public T getT() {
        return tee;
    }

    public Vector hookesAttraction(Point<T> point) {
        return new Vector(point.x - x, point.y - y);
    }

    public float distance(Point<T> point) {
        float a = this.x - point.x;
        float b = this.y - point.y;
        return (float) Math.sqrt(a * a + b * b);
    }
    
    public Vector coulombRepulsion(Point<T> point, float charge, float yAdjust) {
        float inverseDistance = charge / distance(point);
        float a = this.x - point.x;
        float b = yAdjust * (this.y - point.y);
        return new Vector(a, b).normalize().multiply(inverseDistance);  
    }

    public void move(Vector vector) {
        x = this.x + vector.getX();
        y = y + vector.getY();
    }

    public void dilate(Vector vector) {
        x = this.x * vector.getX();
        y = y * vector.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (tee != null ? !tee.equals(point.tee) : point.tee != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (tee != null ? tee.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "[x: " + x + ", y:" + y + ", value:" + tee.toString() + "]";
    }
}
