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

package net.rhapso.graph;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AdjacencyListTest extends TestCase {
    public void testSubgraphs() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("2004", "2008");
        graph.addEdge("2005", "2008");
        graph.addEdge("2008", "2009");
        graph.addEdge("2008", "2010");
        graph.addEdge("2010", "2011");

        graph.addEdge("1000", "1001");
        graph.addEdge("1001", "1002");
        List<Graph<String>> subgraphs = graph.subgraphs();
        assertEquals(2, subgraphs.size());

        Graph first = subgraphs.get(0);
        assertEquals(6, Iterables.size(first.nodes()));
        assertEquals(5, Iterables.size(first.edges()));
    }

    public void testRecursionProblem() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("1000", "1001");
        graph.addEdge("1001", "1002");
        List<Graph<String>> subgraphs = graph.subgraphs();
        assertEquals(1, subgraphs.size());
        assertEquals(3, Iterables.size(subgraphs.get(0).nodes()));        
    }
    public void testGraph() throws Exception {
        Graph<String> graph = makeGraph();
        assertEquals(6, graph.size());
        assertEquals("bafoon bar baz foo panda silly", toString(graph.nodes()));
    }

    public void testEdges() throws Exception {
        Graph<String> graph = makeGraph();
        HashSet<Edge<String>> edges = Sets.newHashSet(graph.edges());
        assertEquals(4, edges.size());
        assertTrue(edges.contains(new Edge<String>("foo", "bar")));
        assertTrue(edges.contains(new Edge<String>("foo", "baz")));
        assertTrue(edges.contains(new Edge<String>("foo", "panda")));
        assertTrue(edges.contains(new Edge<String>("silly", "bafoon")));
    }

    public void testSillyBug() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("foo", "bar");
        graph.addEdge("foo", "hello");
        graph.addEdge("foo", "panda");
        ArrayList<Edge<String>> list = Lists.newArrayList(graph.edges());
        assertEquals(3, list.size());
    }
    
    public void testChildren() throws Exception {
        assertEquals("bar baz panda", toString(makeGraph().children("foo")));
    }

    public void testNeighbors() throws Exception {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("foo", "center");
        graph.addEdge("bar", "center");
        graph.addEdge("center", "bafoon");
        assertEquals(3, graph.neighbors("center"));
    }

    private String toString(Iterable<String> nodes) {
        List<String> allNodes = Lists.newArrayList(nodes);
        Collections.sort(allNodes);
        return Joiner.on(" ").join(allNodes);
    }

    private AdjacencyList<String> makeGraph() {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("foo", "bar");
        graph.addEdge("foo", "baz");
        graph.addEdge("foo", "panda");
        graph.addEdge("silly", "bafoon");
        return graph;
    }
}
