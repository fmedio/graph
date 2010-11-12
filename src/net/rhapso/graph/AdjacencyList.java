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

import clutter.Bag;
import clutter.SetBag;
import com.google.common.collect.Sets;

import java.util.*;

public class AdjacencyList<T> implements Graph<T> {
    final private Bag<T, T> adjacentyList;
    final private Bag<T, T> doubleLinkedGraph;
    final private Set<T> nodes = new HashSet<T>();

    public AdjacencyList() {
        this.adjacentyList = new SetBag<T,T>();
        doubleLinkedGraph = new SetBag<T,T>();
    }

    public void addEdge(T from, T to) {
        adjacentyList.put(from, to);
        doubleLinkedGraph.put(from, to);
        doubleLinkedGraph.put(to, from);
        nodes.add(from);
        nodes.add(to);
    }

    public void addEdge(Edge<T> edge) {
        addEdge(edge.getFrom(), edge.getTo());
    }

    public int size() {
        return nodes.size();
    }

    public Iterable<T> children(T tee) {
        return adjacentyList.getValues(tee);
    }

    public int neighbors(T tee) {
        return doubleLinkedGraph.getValues(tee).size();
    }

    public Iterable<T> nodes() {
        return nodes;
    }

    public Iterable<Edge<T>> edges() {
        Iterable<Edge<T>> iterable = new Iterable<Edge<T>>() {
            public Iterator<Edge<T>> iterator() {
                return new Iterator<Edge<T>>() {
                    Iterator<Map.Entry<T, T>> iterator = adjacentyList.entries().iterator();

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Edge<T> next() {
                        Map.Entry<T, T> entry = iterator.next();
                        return new Edge<T>(entry.getKey(), entry.getValue());
                    }

                    public void remove() {}
                };
            }
        };
        return iterable;
    }

    public boolean hasEdge(T from, T to) {
        return adjacentyList.getValues(from).contains(to);
    }

    public boolean hasEdge(Edge<T> edge) {
        return hasEdge(edge.getFrom(), edge.getTo());
    }

    public List<Graph<T>> subgraphs() {
        Set<T> universe = Sets.newHashSet(nodes);
        List<Graph<T>> graphs = new ArrayList<Graph<T>>();

        while (universe.size() != 0) {
            T startingPoint = universe.iterator().next();
            AdjacencyList<T> graph = explore(startingPoint, new NodeFilter<T>() {
                public boolean accept(Edge<T> edge, int currentDepth, Graph<T> currentGraph) {
                    return true;
                }
            });
            graphs.add(graph);
            universe.removeAll(graph.nodes);
        }

        return graphs;
    }

    private void explore(AdjacencyList<T> graph, Edge<T> edge, Set<Edge<T>> visitedEdges, int currentDepth, NodeFilter<T> filter) {
        T startingPoint = edge.getTo();
        for (T neighbor : doubleLinkedGraph.getValues(startingPoint)) {
            Edge<T> forth = new Edge<T>(startingPoint, neighbor);
            Edge<T> back = new Edge<T>(neighbor, startingPoint);
            if (!filter.accept(forth, currentDepth + 1, this)) {
                continue;
            }
            if (visitedEdges.contains(forth) || visitedEdges.contains(back)) {
                continue;
            } else {
                if (hasEdge(forth)) {
                    graph.addEdge(forth);
                }
                if (hasEdge(back)) {
                    graph.addEdge(back);
                }
                visitedEdges.add(forth);
                visitedEdges.add(back);
                explore(graph, forth, visitedEdges, currentDepth + 1, filter);
            }
        }
    }

    public AdjacencyList<T> explore(T from, NodeFilter<T> nodeFilter) {
        Set<Edge<T>> visited = new HashSet<Edge<T>>();
        AdjacencyList<T> newGraph = new AdjacencyList<T>();
        explore(newGraph, new Edge<T>(null, from), visited, 0, nodeFilter);
        return newGraph;
    }

    public Graph<T> vicinity(T node, final int degrees) {
        return explore(node, new NodeFilter<T>() {
            public boolean accept(Edge<T> edge, int currentDepth, Graph<T> currentGraph) {
                return degrees >= currentDepth;
            }
        });
    }
}
