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

package net.rhapso.graph.ui;

import net.rhapso.graph.AdjacencyList;
import net.rhapso.graph.Edge;
import net.rhapso.graph.Graph;
import net.rhapso.graph.layout.LayoutWeaver;
import net.rhapso.graph.layout.Point;
import net.rhapso.graph.layout.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Panel {
    private LayoutWeaver<String> layoutWeaver;
    private Graph<String> graph;
    private Canvas canvas;
    private JTextField springField, dampingField, targetEnergyField;

    public static void main(String[] args) {
        AdjacencyList<String> graph = new AdjacencyList<String>();
        graph.addEdge("center", "hello");
        graph.addEdge("center", "panda");
        graph.addEdge("center", "poop");
        graph.addEdge("center", "flup");
        graph.addEdge("center", "crap");
        graph.addEdge("crap", "a");
        graph.addEdge("crap", "b");
        graph.addEdge("crap", "c");
        graph.addEdge("crap", "d");
        graph.addEdge("d", "e");
        graph.addEdge("d", "f");
        graph.addEdge("d", "g");
        graph.addEdge("d", "h");
        graph.addEdge("d", "i");
        graph.addEdge("crap", "j");
        graph.addEdge("j", "k");
        graph.addEdge("j", "l");
        graph.addEdge("j", "m");
        graph.addEdge("j", "n");
        graph.addEdge("j", "o");

        Panel panel = new Panel(graph);
        panel.execute();
    }

    public Panel(Graph<String> graph) {
        this.graph = graph;
        layoutWeaver = new LayoutWeaver<String>(graph);
        canvas = makeCanvas();
        springField = new JTextField(".2", 5);
        dampingField = new JTextField(".15", 5);
        targetEnergyField = new JTextField(".003", 5);
    }

    public void execute() {
        final JFrame frame  = new JFrame("Foo");
        frame.setPreferredSize(new Dimension(900, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.add(canvas, BorderLayout.CENTER);

        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(makeButton("reset", new ButtonHandler() {
            public void onClick() {
                layoutWeaver = new LayoutWeaver<String>(graph);
                canvas.repaint();
            }
        }));

        box.add(makeButton("converge", new ButtonHandler() {
            public void onClick() {
                float spring = Float.parseFloat(springField.getText());
                float damping = Float.parseFloat(dampingField.getText());
                float targetEnergy = Float.parseFloat(targetEnergyField.getText());

                System.out.println("spring = " + spring);
                System.out.println("damping = " + damping);
                int count = 0;
                for (double energy = layoutWeaver.convergeOnce(damping,  spring); energy > targetEnergy; energy = layoutWeaver.convergeOnce(damping,  spring)) {
                    count++;
                }
                System.out.println("count = " + count);
                canvas.repaint();
            }
        }));

        box.add(makeLabeledInput("Spring: ", springField));
        box.add(makeLabeledInput("Damping: ", dampingField));
        box.add(makeLabeledInput("Target energy: ", targetEnergyField));
        container.add(box, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
    }

    private Box makeLabeledInput(String text, JTextField textField) {
        Box springBox = new Box(BoxLayout.X_AXIS);
        springBox.add(new JLabel(text));
        springBox.add(textField);
        return springBox;
    }

    private Canvas makeCanvas() {
        return new Canvas() {
            @Override
            public void paint(Graphics graphics) {

                ((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(Color.white);
                graphics.fillRect(50, 50, 400, 400);
                layoutWeaver.normalize();

                for (Point<String> point : layoutWeaver.nodes()) {
                    graphics.setColor(Color.pink);
                    point = translate(point);
                    int x = (int) point.getX();
                    int y = (int) point.getY();
                    graphics.fillOval(x, y, 10, 10);
                    graphics.setColor(Color.black);
                    graphics.drawString(point.getT(), x, y);
                }

                for (Edge<Point<String>> edge : layoutWeaver.edges()) {
                    Point<String> from = translate(edge.getFrom());
                    Point<String> to = translate(edge.getTo());
                    graphics.setColor(Color.black);
                    graphics.drawLine((int)from.getX(), (int)from.getY(), (int)to.getX(), (int)to.getY());
                }
            }
        };
    }


    private JButton makeButton(String text, final ButtonHandler behavior) {
        JButton jButton = new JButton(text);
        jButton.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                behavior.onClick();

            }

            public void mousePressed(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mouseReleased(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mouseEntered(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mouseExited(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return jButton;
    }
    private static interface ButtonHandler {
        public void onClick();
    }
    private <T> Point<T> translate(Point<T> point) {
        Point<T> newPoint = new Point<T>(point);
        newPoint.dilate(new Vector(300f, 300f));
        newPoint.move(new Vector(75f, 75f));
        return newPoint;
    }
}
