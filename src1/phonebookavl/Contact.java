// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package phonebookavl;

public class Contact {
   private final String name;
   private String phone;

   public Contact(String var1, String var2) {
      this.name = var1 == null ? "" : var1.trim();
      this.phone = var2 == null ? "" : var2.trim();
   }

   public String getName() {
      return this.name;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String var1) {
      this.phone = var1 == null ? "" : var1.trim();
   }

   public String toString() {
      return this.name + " - " + this.phone;
   }
}
