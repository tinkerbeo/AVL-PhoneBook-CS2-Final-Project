package phonebookavl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TreePanel extends JPanel {
   private static final int NODE_W = 120;
   private static final int NODE_H = 46;
   private static final int LEVEL_GAP = 90;
   private static final int H_GAP = 34;
   private static final int MARGIN = 40;
   
   private final AVLTree tree;
   private Set<String> highlightedPath = new HashSet<>();
   private final Map<Node, Integer> subtreeWidth = new HashMap<>();

   // --- ANIMATION VARIABLES ---
   private final Map<String, Point.Double> currentPos = new HashMap<>();
   private final Map<String, Point> targetPos = new HashMap<>();
   private final Timer animTimer;

   public TreePanel(AVLTree tree) {
      this.tree = tree;
      this.setBackground(new Color(248, 250, 252));
      this.setPreferredSize(new Dimension(1000, 700));

      // Animation Loop running at ~60 FPS
      this.animTimer = new Timer(16, e -> updateAnimations());
      this.animTimer.start();
   }

   private void updateAnimations() {
      boolean needsRepaint = false;
      for (String name : targetPos.keySet()) {
         Point.Double curr = currentPos.get(name);
         Point target = targetPos.get(name);
         if (curr != null && target != null) {
            double dx = target.x - curr.x;
            double dy = target.y - curr.y;
            
            // If it's not at the target yet, move it closer (Easing)
            if (Math.abs(dx) > 0.5 || Math.abs(dy) > 0.5) {
               curr.x += dx * 0.01; // 0.15 is the animation speed
               curr.y += dy * 0.01;
               needsRepaint = true;
            } else {
               curr.x = target.x;
               curr.y = target.y;
            }
         }
      }
      if (needsRepaint) repaint();
   }

   public void setHighlightedPath(List<String> path) {
      this.highlightedPath = new HashSet<>(path);
      this.repaint();
   }

   public void clearHighlight() {
      this.highlightedPath.clear();
      this.repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      Node root = this.tree.getRoot();
      if (root == null) {
         this.drawEmpty(g2);
         this.targetPos.clear();
         g2.dispose();
         return;
      }

      this.subtreeWidth.clear();
      int totalWidth = this.computeSubtreeWidth(root);
      int startX = Math.max(MARGIN, (this.getWidth() - totalWidth) / 2);

      // 1. Calculate Target Positions
      this.targetPos.clear();
      this.calculateTargets(root, startX, 60);

      // 2. Initialize new nodes in Current Positions
      for (String name : targetPos.keySet()) {
         if (!currentPos.containsKey(name)) {
            Point target = targetPos.get(name);
            // Spawn new nodes slightly higher so they "drop" in
            currentPos.put(name, new Point.Double(target.x, target.y - 40)); 
         }
      }
      // Remove deleted nodes
      currentPos.keySet().retainAll(targetPos.keySet());

      // 3. Draw Edges based on CURRENT positions
      this.drawEdges(g2, root);

      // 4. Draw Nodes based on CURRENT positions
      this.drawNodes(g2, root);

      g2.dispose();
      updateAnimations(); // Trigger loop if things changed
   }

   private void calculateTargets(Node node, int x, int y) {
      if (node == null) return;

      int leftW = this.subtreeWidth.getOrDefault(node.getLeft(), 0);
      int nodeW = this.subtreeWidth.get(node);
      int rightW = this.subtreeWidth.getOrDefault(node.getRight(), 0);

      int nodeX = x + leftW + (nodeW - leftW - rightW) / 2 - NODE_W / 2;
      if (node.getLeft() == null && node.getRight() == null) {
         nodeX = x + nodeW / 2 - NODE_W / 2;
      }

      this.targetPos.put(node.getContact().getName(), new Point(nodeX, y));

      if (node.getLeft() != null) {
         calculateTargets(node.getLeft(), x, y + LEVEL_GAP);
      }
      if (node.getRight() != null) {
         calculateTargets(node.getRight(), x + leftW + H_GAP, y + LEVEL_GAP);
      }
   }

   private void drawEdges(Graphics2D g2, Node node) {
      if (node == null) return;
      String name = node.getContact().getName();
      Point.Double curr = currentPos.get(name);
      if (curr == null) return;

      int startX = (int) curr.x + NODE_W / 2;
      int startY = (int) curr.y + NODE_H;

      if (node.getLeft() != null) {
         Point.Double leftPos = currentPos.get(node.getLeft().getContact().getName());
         if (leftPos != null) {
            this.drawEdge(g2, startX, startY, (int) leftPos.x + NODE_W / 2, (int) leftPos.y);
         }
         drawEdges(g2, node.getLeft());
      }
      if (node.getRight() != null) {
         Point.Double rightPos = currentPos.get(node.getRight().getContact().getName());
         if (rightPos != null) {
            this.drawEdge(g2, startX, startY, (int) rightPos.x + NODE_W / 2, (int) rightPos.y);
         }
         drawEdges(g2, node.getRight());
      }
   }

   private void drawNodes(Graphics2D g2, Node node) {
      if (node == null) return;
      String name = node.getContact().getName();
      Point.Double pos = currentPos.get(name);
      
      if (pos != null) {
         int x = (int) pos.x;
         int y = (int) pos.y;
         this.drawNode(g2, node, x, y, x + NODE_W / 2, y + NODE_H / 2);
      }
      drawNodes(g2, node.getLeft());
      drawNodes(g2, node.getRight());
   }

   private void drawEmpty(Graphics2D g2) {
      g2.setColor(new Color(100, 116, 139));
      g2.setFont(new Font("SansSerif", 1, 22));
      String text = "Danh bạ đang trống - hãy thêm liên hệ";
      FontMetrics fm = g2.getFontMetrics();
      g2.drawString(text, (this.getWidth() - fm.stringWidth(text)) / 2, this.getHeight() / 2);
   }

   private int computeSubtreeWidth(Node node) {
      if (node == null) return 0;
      int leftW = this.computeSubtreeWidth(node.getLeft());
      int rightW = this.computeSubtreeWidth(node.getRight());
      int w = Math.max(NODE_W, leftW + rightW + H_GAP);
      this.subtreeWidth.put(node, w);
      return w;
   }

   private void drawEdge(Graphics2D g2, int x1, int y1, int x2, int y2) {
      g2.setStroke(new BasicStroke(2.2F));
      g2.setColor(new Color(148, 163, 184));
      g2.drawLine(x1, y1, x2, y2);
   }

   private void drawNode(Graphics2D g2, Node node, int x, int y, int cx, int cy) {
      boolean isHighlighted = this.highlightedPath.contains(node.getContact().getName());
      int bf = this.tree.checkBalance(node);
      boolean isUnbalanced = Math.abs(bf) > 1;

      // Shadow
      g2.setColor(new Color(15, 23, 42, 35));
      g2.fillRoundRect(x + 4, y + 5, NODE_W, NODE_H, 20, 20);

      // Color Logic
      if (isHighlighted) {
         g2.setColor(new Color(52, 211, 153)); // Green
      } else if (isUnbalanced) {
         g2.setColor(new Color(248, 113, 113)); // Red
      } else if (Math.abs(bf) == 1) {
         g2.setColor(new Color(250, 204, 21)); // Yellow
      } else {
         g2.setColor(new Color(96, 165, 250)); // Blue
      }

      g2.fillRoundRect(x, y, NODE_W, NODE_H, 20, 20);
      g2.setColor(new Color(30, 41, 59));
      g2.setStroke(new BasicStroke(1.5F));
      g2.drawRoundRect(x, y, NODE_W, NODE_H, 20, 20);
      
      g2.setFont(new Font("SansSerif", 1, 13));
      g2.setColor(Color.WHITE);
      this.drawCentered(g2, node.getContact().getName(), cx, y + 18);
      g2.setFont(new Font("SansSerif", 0, 11));
      this.drawCentered(g2, "h=" + node.getHeight() + ", bf=" + bf, cx, y + 35);
   }

   private void drawCentered(Graphics2D g2, String text, int cx, int y) {
      FontMetrics fm = g2.getFontMetrics();
      if (text.length() > 16) {
         text = text.substring(0, 13) + "...";
      }
      g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
   }
}