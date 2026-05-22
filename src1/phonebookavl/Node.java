// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package phonebookavl;

public class Node {
   Contact contact;
   Node left;
   Node right;
   int height;

   public Node(Contact var1) {
      this.contact = var1;
      this.height = 1;
   }

   public Contact getContact() {
      return this.contact;
   }

   public Node getLeft() {
      return this.left;
   }

   public Node getRight() {
      return this.right;
   }

   public int getHeight() {
      return this.height;
   }
}
