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

import net.rhapso.graph.AdjacencyList;
import net.rhapso.graph.Edge;
import net.rhapso.graph.Graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LayoutWeaver<T> implements Graph<Point<T>> {
    private final AdjacencyList<Point<T>> positions;
    private final Map<T, Point<T>> nodes;
    private final Map<T, Vector> velocities;
    private Graph<T> originalGraph;

    public LayoutWeaver(Graph<T> graph) {
        originalGraph = graph;
        positions = new AdjacencyList<Point<T>>();
        nodes = new HashMap<T, Point<T>>();
        velocities = new HashMap<T, Vector>();

        for (Edge<T> edge : graph.edges()) {
            Point<T> from = registerPoint(edge.getFrom());
            Point<T> to = registerPoint(edge.getTo());
            positions.addEdge(from, to);
            positions.addEdge(to, from);
        }
    }

    private Point<T> registerPoint(T value) {
        if (nodes.containsKey(value)) {
            return nodes.get(value);
        } else {
            velocities.put(value, new Vector(0, 0));
            Point<T> point = Point.random(value);
            nodes.put(value, point);
            return point;
        }
    }

    public Point<T> get(T tee) {
        return nodes.get(tee);
    }

    public float distance(T from, T to) {
        Point<T> fromPoint = nodes.get(from);
        Point<T> toPoint = nodes.get(to);
        return fromPoint.distance(toPoint);
    }

    public double convergeOnce(float damping, float springConstant) {
        double totalEnergy = 0d;

        for (Point<T> point : nodes()) {
            Vector momentum = new Vector(0, 0);


            for (Point<T> node : nodes()) {
                if (!point.equals(node)) {
                    momentum = momentum.add(point.coulombRepulsion(node, neighbors(node) * neighbors(point), 1.00f));
                    //momentum = momentum.add(point.coulombRepulsion(node, 1f));
                }
            }

            for (Point<T> node : children(point)) {
                momentum = momentum.add(point.hookesAttraction(node).multiply(springConstant));
            }

            Vector velocity = velocities.get(point.getT());
            velocity = velocity.add(momentum);
            velocity = velocity.multiply(damping);
            totalEnergy += (velocity.length() * velocity.length());
            point.move(velocity);
            velocities.put(point.getT(), velocity);
        }

        return totalEnergy;
    }


    public void normalize() {
        float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE, minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

        for (Point<T> point : nodes()) {
            minX = Math.min(minX, point.getX());
            maxX = Math.max(maxX, point.getX());
            minY = Math.min(minY, point.getY());
            maxY = Math.max(maxY, point.getY());
        }

        float xScale = 1f / (maxX - minX);
        float yScale = 1f / (maxY - minY);
        Vector centering = new Vector(-minX, -minY);
        Vector scaling = new Vector(xScale, yScale);

        for (Point<T> point : nodes()) {
            point.move(centering);
            point.dilate(scaling);
        }
    }
    
    public int size() {
        return positions.size();
    }

    public Iterable<Point<T>> children(Point<T> tee) {
        return positions.children(tee);
    }

    public int neighbors(Point<T> tee) {
        return positions.neighbors(tee);
    }

    public Iterable<Point<T>> nodes() {
        return nodes.values();
    }

    public Iterable<Edge<Point<T>>> edges() {
        return new Iterable<Edge<Point<T>>>() {
            public Iterator<Edge<Point<T>>> iterator() {
                return new Iterator<Edge<Point<T>>>() {
                    Iterator<Edge<T>> iterator = originalGraph.edges().iterator();

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Edge<Point<T>> next() {
                        Edge<T> edge = iterator.next();
                        return new Edge<Point<T>>(get(edge.getFrom()), get(edge.getTo()));
                    }

                    public void remove() {}
                };
            }
        };
    }

    public boolean hasEdge(Point<T> from, Point<T> to) {
        return this.positions.hasEdge(from, to);
    }

    public Graph<Point<T>> explore(Point<T> from, NodeFilter<Point<T>> filter) {
        return this.positions.explore(from, filter);
    }

    public Point<T> lowerBoundary() {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;

        for (Point<T> point : nodes()) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
        }

        return new Point<T>(minX, minY, null);
    }

    public Point<T> upperBoundary() {
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Point<T> point : nodes()) {
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        return new Point<T>(maxX, maxY, null);
    }

    public List<Graph<Point<T>>> subgraphs() {
        return positions.subgraphs();
    }
}
