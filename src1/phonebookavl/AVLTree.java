// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package phonebookavl;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {
   private Node root;

   public AVLTree() {
   }

   public Node getRoot() {
      return this.root;
   }

   public int height(Node var1) {
      return var1 == null ? 0 : var1.height;
   }

   private int balanceFactor(Node var1) {
      return var1 == null ? 0 : this.height(var1.left) - this.height(var1.right);
   }

   private void updateHeight(Node var1) {
      if (var1 != null) {
         var1.height = 1 + Math.max(this.height(var1.left), this.height(var1.right));
      }

   }

   public int checkBalance(Node var1) {
      return this.balanceFactor(var1);
   }

   public Node rotateRight(Node var1) {
      Node var2 = var1.left;
      Node var3 = var2.right;
      var2.right = var1;
      var1.left = var3;
      this.updateHeight(var1);
      this.updateHeight(var2);
      return var2;
   }

   public Node rotateLeft(Node var1) {
      Node var2 = var1.right;
      Node var3 = var2.left;
      var2.left = var1;
      var1.right = var3;
      this.updateHeight(var1);
      this.updateHeight(var2);
      return var2;
   }

   private Node rebalance(Node var1) {
      this.updateHeight(var1);
      int var2 = this.balanceFactor(var1);
      if (var2 > 1) {
         if (this.balanceFactor(var1.left) < 0) {
            var1.left = this.rotateLeft(var1.left);
         }

         return this.rotateRight(var1);
      } else if (var2 < -1) {
         if (this.balanceFactor(var1.right) > 0) {
            var1.right = this.rotateRight(var1.right);
         }

         return this.rotateLeft(var1);
      } else {
         return var1;
      }
   }

   public void insert(String var1, String var2) {
      if (var1 != null && !var1.trim().isEmpty()) {
         this.root = this.insert(this.root, new Contact(var1, var2));
      }
   }

   private Node insert(Node var1, Contact var2) {
      if (var1 == null) {
         return new Node(var2);
      } else {
         int var3 = this.compare(var2.getName(), var1.contact.getName());
         if (var3 < 0) {
            var1.left = this.insert(var1.left, var2);
         } else {
            if (var3 <= 0) {
               var1.contact.setPhone(var2.getPhone());
               return var1;
            }

            var1.right = this.insert(var1.right, var2);
         }

         return this.rebalance(var1);
      }
   }

   public void delete(String var1) {
      if (var1 != null && !var1.trim().isEmpty()) {
         this.root = this.delete(this.root, var1.trim());
      }
   }

   private Node delete(Node var1, String var2) {
      if (var1 == null) {
         return null;
      } else {
         int var3 = this.compare(var2, var1.contact.getName());
         if (var3 < 0) {
            var1.left = this.delete(var1.left, var2);
         } else if (var3 > 0) {
            var1.right = this.delete(var1.right, var2);
         } else if (var1.left != null && var1.right != null) {
            Node var4 = this.minValueNode(var1.right);
            var1.contact = var4.contact;
            var1.right = this.delete(var1.right, var4.contact.getName());
         } else {
            var1 = var1.left != null ? var1.left : var1.right;
         }

         return var1 == null ? null : this.rebalance(var1);
      }
   }

   private Node minValueNode(Node var1) {
      Node var2;
      for(var2 = var1; var2.left != null; var2 = var2.left) {
      }

      return var2;
   }

   public Contact search(String var1) {
      Node var2 = this.searchNode(var1);
      return var2 == null ? null : var2.contact;
   }

   public Node searchNode(String var1) {
      if (var1 == null) {
         return null;
      } else {
         int var3;
         for(Node var2 = this.root; var2 != null; var2 = var3 < 0 ? var2.left : var2.right) {
            var3 = this.compare(var1.trim(), var2.contact.getName());
            if (var3 == 0) {
               return var2;
            }
         }

         return null;
      }
   }

   public List<String> searchPath(String var1) {
      ArrayList var2 = new ArrayList();
      if (var1 == null) {
         return var2;
      } else {
         int var4;
         for(Node var3 = this.root; var3 != null; var3 = var4 < 0 ? var3.left : var3.right) {
            var2.add(var3.contact.getName());
            var4 = this.compare(var1.trim(), var3.contact.getName());
            if (var4 == 0) {
               break;
            }
         }

         return var2;
      }
   }

   public List<Contact> inOrder() {
      ArrayList var1 = new ArrayList();
      this.inOrder(this.root, var1);
      return var1;
   }

   private void inOrder(Node var1, List<Contact> var2) {
      if (var1 != null) {
         this.inOrder(var1.left, var2);
         var2.add(var1.contact);
         this.inOrder(var1.right, var2);
      }
   }

   public void clear() {
      this.root = null;
   }

   private int compare(String var1, String var2) {
      return var1.compareToIgnoreCase(var2);
   }


   public Node cloneTree() {
      return this.copyNode(this.root);
   }

   private Node copyNode(Node node) {
      if (node == null) return null;
      // Deep copy the contact so history doesn't mutate!
      Contact clonedContact = new Contact(node.contact.getName(), node.contact.getPhone());
      Node copy = new Node(clonedContact);
      copy.height = node.height;
      copy.left = this.copyNode(node.left);
      copy.right = this.copyNode(node.right);
      return copy;
   }

   public void restoreTree(Node savedRoot) {
      this.root = this.copyNode(savedRoot);
   }
}
