 // Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package phonebookavl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PhonebookApp extends JFrame {
   private final AVLTree tree = new AVLTree();
   private final TreePanel treePanel;
   private final DefaultListModel<Contact> listModel;
   private final JList<Contact> contactList;
   private final JTextField nameField;
   private final JTextField phoneField;
   private final JTextField searchField;
   // private final Deque<List<Contact>> undoStack;
   // private final Deque<List<Contact>> redoStack;
   private final Deque<Node> undoStack;
   private final Deque<Node> redoStack;
   private final Path dataFile;

   public PhonebookApp() {
      super("AVL Tree Rotation Visualizer - Phonebook Application");
      this.treePanel = new TreePanel(this.tree);
      this.listModel = new DefaultListModel();
      this.contactList = new JList(this.listModel);
      this.nameField = new JTextField();
      this.phoneField = new JTextField();
      this.searchField = new JTextField();
   // To this:
      this.undoStack = new LinkedList<>();
      this.redoStack = new LinkedList<>();
      this.dataFile = Path.of("phonebook.csv");
      this.setDefaultCloseOperation(3);
      this.setLayout(new BorderLayout(12, 12));
      this.add(this.buildSidebar(), "West");
      this.add(new JScrollPane(this.treePanel), "Center");
      this.loadFromFileQuietly();
      this.refreshListAndTree();
      this.setSize(1280, 760);
      this.setLocationRelativeTo((Component)null);
   }

   private JPanel buildSidebar() {
      JPanel var1 = new JPanel(new GridBagLayout());
      var1.setPreferredSize(new Dimension(330, 720));
      var1.setBackground(new Color(241, 245, 249));
      var1.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      GridBagConstraints selectedContact = new GridBagConstraints();
      selectedContact.fill = 2;
      selectedContact.insets = new Insets(6, 4, 6, 4);
      selectedContact.gridx = 0;
      selectedContact.weightx = (double)1.0F;
      JLabel var3 = new JLabel("AVL Phonebook");
      var3.setFont(new Font("SansSerif", 1, 24));
      selectedContact.gridy = 0;
      var1.add(var3, selectedContact);
      ++selectedContact.gridy;
      var1.add(new JLabel("Tên liên hệ:"), selectedContact);
      ++selectedContact.gridy;
      var1.add(this.nameField, selectedContact);
      ++selectedContact.gridy;
      var1.add(new JLabel("Số điện thoại:"), selectedContact);
      ++selectedContact.gridy;
      var1.add(this.phoneField, selectedContact);
      JButton var4 = new JButton("Thêm / Cập nhật");
      var4.addActionListener((var1x) -> this.addOrUpdateContact());
      ++selectedContact.gridy;
      var1.add(var4, selectedContact);
      JButton var5 = new JButton("Xóa theo tên");
      var5.addActionListener((var1x) -> this.deleteContact());
      ++selectedContact.gridy;
      var1.add(var5, selectedContact);
      ++selectedContact.gridy;
      var1.add(new JLabel("Tìm kiếm tên:"), selectedContact);
      ++selectedContact.gridy;
      var1.add(this.searchField, selectedContact);
      JButton var6 = new JButton("Search + Highlight Path");
      var6.addActionListener((var1x) -> this.searchContact());
      ++selectedContact.gridy;
      var1.add(var6, selectedContact);
      JButton var7 = new JButton("Bỏ highlight");
      var7.addActionListener((var1x) -> this.treePanel.clearHighlight());
      ++selectedContact.gridy;
      var1.add(var7, selectedContact);
      JPanel var8 = new JPanel(new BorderLayout(6, 0));
      JButton var9 = new JButton("Undo");
      JButton var10 = new JButton("Redo");
      var9.addActionListener((var1x) -> this.undo());
      var10.addActionListener((var1x) -> this.redo());
      var8.add(var9, "West");
      var8.add(var10, "East");
      var8.setOpaque(false);
      ++selectedContact.gridy;
      var1.add(var8, selectedContact);
      JButton var11 = new JButton("Lưu CSV");
      var11.addActionListener((var1x) -> this.saveToFile());
      ++selectedContact.gridy;
      var1.add(var11, selectedContact);
      JButton var12 = new JButton("Đọc CSV");
      var12.addActionListener((var1x) -> this.loadFromFile());
      ++selectedContact.gridy;
      var1.add(var12, selectedContact);
      JLabel var13 = new JLabel("Danh bạ theo thứ tự alphabet:");
      ++selectedContact.gridy;
      var1.add(var13, selectedContact);
      this.contactList.setVisibleRowCount(12);
      this.contactList.addListSelectionListener((var1x) -> {
         Contact clickedContact = (Contact)this.contactList.getSelectedValue();
         if (!var1x.getValueIsAdjusting() && clickedContact != null) {
            this.nameField.setText(clickedContact.getName());
            this.phoneField.setText(clickedContact.getPhone());
         }
      });
      ++selectedContact.gridy;
      selectedContact.weighty = (double)1.0F;
      selectedContact.fill = 1;
      var1.add(new JScrollPane(this.contactList), selectedContact);
      return var1;
   }

   private void addOrUpdateContact() {
      String var1 = this.nameField.getText().trim();
      String selectedContact = this.phoneField.getText().trim();
      if (var1.isEmpty()) {
         this.showMessage("Tên không được để trống.");
      } else {
         this.saveStateForUndo();
         this.tree.insert(var1, selectedContact);
         this.redoStack.clear();
         this.refreshListAndTree();
         this.saveToFileQuietly();
      }
   }

   private void deleteContact() {
      String var1 = this.nameField.getText().trim();
      if (var1.isEmpty()) {
         var1 = this.searchField.getText().trim();
      }

      if (var1.isEmpty()) {
         this.showMessage("Nhập tên cần xóa.");
      } else if (this.tree.search(var1) == null) {
         this.showMessage("Không tìm thấy: " + var1);
      } else {
         this.saveStateForUndo();
         this.tree.delete(var1);
         this.redoStack.clear();
         this.refreshListAndTree();
         this.saveToFileQuietly();
      }
   }

   private void searchContact() {
      String var1 = this.searchField.getText().trim();
      if (var1.isEmpty()) {
         var1 = this.nameField.getText().trim();
      }

      if (var1.isEmpty()) {
         this.showMessage("Nhập tên cần tìm.");
      } else {
         this.treePanel.setHighlightedPath(this.tree.searchPath(var1));
         Contact selectedContact = this.tree.search(var1);
         if (selectedContact == null) {
            this.showMessage("Không tìm thấy. Đường đi tìm kiếm đã được highlight đến vị trí cuối.");
         } else {
            this.nameField.setText(selectedContact.getName());
            this.phoneField.setText(selectedContact.getPhone());
            this.showMessage("Tìm thấy: " + String.valueOf(selectedContact));
         }

      }
   }

   private void undo() {
      if (this.undoStack.isEmpty()) {
         this.showMessage("Không còn thao tác để Undo.");
      } else {
         this.redoStack.push(this.tree.cloneTree());
         this.restoreState(this.undoStack.pop());
      }
   }

   private void redo() {
      if (this.redoStack.isEmpty()) {
         this.showMessage("Không còn thao tác để Redo.");
      } else {
         this.undoStack.push(this.tree.cloneTree());
         this.restoreState(this.redoStack.pop());
      }
   }

   private void saveStateForUndo() {
      this.undoStack.push(this.tree.cloneTree());
      if (this.undoStack.size() > 30) {
         this.undoStack.removeLast();
      }
   }

   private void restoreState(Node savedRoot) {
      this.tree.restoreTree(savedRoot);
      this.refreshListAndTree();
      this.saveToFileQuietly();
   }

   private void refreshListAndTree() {
      this.listModel.clear();

      for(Contact selectedContact : this.tree.inOrder()) {
         this.listModel.addElement(selectedContact);
      }

      // ADD THIS LINE to clear old highlights when the tree changes!
      this.treePanel.clearHighlight();
      
      this.treePanel.revalidate();
      this.treePanel.repaint();
   }

   private void saveToFile() {
      try {
         PhonebookStorage.save(this.dataFile, this.tree.inOrder());
         this.showMessage("Đã lưu vào " + String.valueOf(this.dataFile.toAbsolutePath()));
      } catch (IOException selectedContact) {
         this.showMessage("Lỗi lưu file: " + selectedContact.getMessage());
      }

   }

   private void saveToFileQuietly() {
      try {
         PhonebookStorage.save(this.dataFile, this.tree.inOrder());
      } catch (IOException selectedContact) {
      }

   }

   private void loadFromFile() {
      try {
         this.saveStateForUndo();
         PhonebookStorage.load(this.dataFile, this.tree);
         this.redoStack.clear();
         this.refreshListAndTree();
         this.showMessage("Đã đọc từ " + String.valueOf(this.dataFile.toAbsolutePath()));
      } catch (IOException selectedContact) {
         this.showMessage("Lỗi đọc file: " + selectedContact.getMessage());
      }

   }

   private void loadFromFileQuietly() {
      try {
         PhonebookStorage.load(this.dataFile, this.tree);
      } catch (IOException selectedContact) {
      }

   }

   private void showMessage(String var1) {
      JOptionPane.showMessageDialog(this, var1);
   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(() -> {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var1) {
         }

         (new PhonebookApp()).setVisible(true);
      });
   }
}
