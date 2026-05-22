// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package phonebookavl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PhonebookStorage {
   public PhonebookStorage() {
   }

   public static void save(Path var0, List<Contact> var1) throws IOException {
      BufferedWriter var2 = Files.newBufferedWriter(var0, StandardCharsets.UTF_8);

      try {
         var2.write("name,phone");
         var2.newLine();

         for(Contact var4 : var1) {
            String var10001 = escape(var4.getName());
            var2.write(var10001 + "," + escape(var4.getPhone()));
            var2.newLine();
         }
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

   }

   public static void load(Path var0, AVLTree var1) throws IOException {
      if (Files.exists(var0, new LinkOption[0])) {
         var1.clear();
         BufferedReader var2 = Files.newBufferedReader(var0, StandardCharsets.UTF_8);

         try {
            boolean var4 = true;

            String var3;
            while((var3 = var2.readLine()) != null) {
               if (var4 && var3.toLowerCase().startsWith("name,")) {
                  var4 = false;
               } else {
                  var4 = false;
                  String[] var5 = parseCsvLine(var3);
                  if (var5.length >= 2 && !var5[0].isBlank()) {
                     var1.insert(var5[0], var5[1]);
                  }
               }
            }
         } catch (Throwable var7) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (var2 != null) {
            var2.close();
         }

      }
   }

   private static String escape(String var0) {
      String var1 = var0 == null ? "" : var0;
      return !var1.contains(",") && !var1.contains("\"") && !var1.contains("\n") ? var1 : "\"" + var1.replace("\"", "\"\"") + "\"";
   }

   private static String[] parseCsvLine(String var0) {
      StringBuilder var1 = new StringBuilder();
      ArrayList var2 = new ArrayList();
      boolean var3 = false;

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == '"') {
            if (var3 && var4 + 1 < var0.length() && var0.charAt(var4 + 1) == '"') {
               var1.append('"');
               ++var4;
            } else {
               var3 = !var3;
            }
         } else if (var5 == ',' && !var3) {
            var2.add(var1.toString());
            var1.setLength(0);
         } else {
            var1.append(var5);
         }
      }

      var2.add(var1.toString());
      return (String[])var2.toArray(new String[0]);
   }
}
