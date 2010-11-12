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

import clutter.BaseTestCase;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.rhapso.graph.AdjacencyList;
import net.rhapso.graph.Edge;

import java.util.List;

public class LayoutWeaverTest extends BaseTestCase {
    public void testDistanceBetweenDisconnectedNodesIncreases() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        graph.addEdge("hello", "panda");
        LayoutWeaver<String> weaver = new LayoutWeaver<String>(graph);
        float worldPanda = weaver.distance("world", "panda");
        weaver.convergeOnce(.1f, .1f);
        assertTrue(weaver.distance("world", "panda") > worldPanda);
    }

    public void testMovePoint() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        graph.addEdge("hello", "panda");
        graph.addEdge("hello", "bear");
        LayoutWeaver<String> weaver = new LayoutWeaver<String>(graph);
        moveToCenter(weaver, weaver.get("hello"));
        moveToCenter(weaver, weaver.get("world"));
        for (Edge<Point<String>> edge : weaver.edges()) {
            System.out.println(edge);
        }        
    }

    private void moveToCenter(LayoutWeaver<String> weaver, Point<String> point) {
        point.move(new Vector(-point.getX(), -point.getY()));
    }

    public void testNeighborhood() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        LayoutWeaver<String> weaver = new LayoutWeaver<String>(graph);
        assertEquals(1, Iterables.size(weaver.children(weaver.get("hello"))));
        assertEquals(1, Iterables.size(weaver.children(weaver.get("world"))));
    }

    public void testInitializeWithRandomPoints() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        graph.addEdge("hello", "panda");
        LayoutWeaver<String> weaver = new LayoutWeaver<String>(graph);
        List<Point<String>> nodes = Lists.newArrayList(weaver.nodes());
        assertEquals(3, nodes.size());

        for (Edge<Point<String>> edge : weaver.edges()) {
            assertTrue(nodes.contains(edge.getFrom()));
            assertTrue(nodes.contains(edge.getTo()));            
        }
    }

    public void testNormalize() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "dolly");
        graph.addEdge("hello", "world");
        graph.addEdge("hello", "jane");
        graph.addEdge("hello", "charles");
        LayoutWeaver<String> layoutWeaver = new LayoutWeaver<String>(graph);
        for (Point<String> point : layoutWeaver.nodes()) {
            point.dilate(new Vector(200f, 200f));
        }

        assertTrue(layoutWeaver.upperBoundary().getX() > 1f);
        assertTrue(layoutWeaver.upperBoundary().getY() > 1f);
        layoutWeaver.normalize();
        assertFalse(layoutWeaver.upperBoundary().getX() > 1f);
        assertFalse(layoutWeaver.upperBoundary().getY() > 1f);
    }

    public void testDirectionalityIsPreserved() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        LayoutWeaver<String> layoutWeaver = new LayoutWeaver<String>(graph);
        assertEquals(1, Iterables.size(layoutWeaver.edges()));
    }
    
    public void testPointsStartInRandomPositions() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("hello", "world");
        Point<String> first = new LayoutWeaver<String>(graph).get("hello");
        Point<String> second = new LayoutWeaver<String>(graph).get("hello");
        assertFalse(first.getX() == second.getX());
        assertFalse(first.getY() == second.getY());
    }
}
