# 🌳 AVL Tree Phonebook: User & Installation Guide

## 1. Overview

The **AVL Tree Phonebook** is a Java Swing desktop application that manages your contacts while visually demonstrating how a self-balancing AVL Tree works in real-time. As you add, delete, or search for contacts, you can watch the tree automatically rotate and balance itself with smooth animations.

---

## 2. Installation & Setup

### Prerequisites

* **Java Development Kit (JDK):** Ensure you have Java 11 or higher installed on your computer. You can verify this by opening your terminal and typing `java -version`.

### Step-by-Step Installation

1. **Download the Source Code:** Clone this repository or download the ZIP file and extract it to your computer.
```bash
git clone https://github.com/your-username/avl-phonebook.git
cd avl-phonebook

```


2. **Folder Structure Check:**
Ensure your source code is located inside the `src1/phonebookavl/` directory.
3. **Compile the Application:**
Open your terminal (or PowerShell), navigate to the root folder of the project, and run the following command to compile the Java files into a new `bin` folder:
```bash
javac -d bin src1/phonebookavl/*.java

```


4. **Run the Application:**
Once compiled successfully, launch the app using:
```bash
java -cp bin phonebookavl.PhonebookApp

```



---

## 3. How to Use the Application

When you launch the app, you will see a sidebar on the left for controls and a large canvas on the right where the AVL Tree is visualized.

### 👤 Adding or Updating a Contact

1. Type a name into the **"Tên liên hệ"** (Contact Name) field.
2. Type a phone number into the **"Số điện thoại"** (Phone Number) field.
3. Click **"Thêm / Cập nhật"**.
* *Note: If the name already exists, the app will update their phone number instead of creating a duplicate.*
* Watch the canvas as the new node drops in and the tree re-balances itself!



### 🗑️ Deleting a Contact

1. Type the name of the contact you want to remove in the Name field.
2. Click **"Xóa theo tên"**.
3. The node will disappear, and the tree will smoothly glide to close the gap.

### 🔍 Searching for a Contact

1. Type a name into the **"Tìm kiếm tên"** (Search Name) field.
2. Click **"Search + Highlight Path"**.
3. The app will visually trace the exact path it took through the binary search tree to find your contact, turning those specific nodes **Green**.
4. To remove the search path colors, click **"Bỏ highlight"**.

### ⏪ Time Travel: Undo & Redo

Made a mistake? The app records a deep-history snapshot of your tree.

* Click **"Undo"** to instantly revert the tree back to its previous state and shape.
* Click **"Redo"** to step forward again.

### 💾 Saving and Loading (CSV)

* **Lưu CSV (Save):** Click this to save your current contacts to a local `phonebook.csv` file. *(Note: The app also auto-saves quietly in the background after major actions).*
* **Đọc CSV (Load):** Click this to import contacts from your `phonebook.csv` file and watch the tree build itself.

---

## 4. Understanding the Visualizer (Color Guide)

The right side of the screen isn't just a static picture—it's a live mathematical model. The nodes change color to teach you about their **Balance Factor (BF)**.

* 🟦 **Blue (Perfect Balance):** The node has a Balance Factor of `0`. Both its left and right branches are the exact same height.
* 🟨 **Yellow (Slight Imbalance):** The node has a Balance Factor of `1` or `-1`. One branch is slightly taller than the other, but it is still within safe AVL limits.
* 🟥 **Red (Critical Imbalance):** The node's Balance Factor has exceeded `±1`. When a node turns red, the tree will instantly perform a Left or Right Rotation to fix it.
* 🟩 **Green (Search Path):** This color highlights the exact route the search algorithm took to find a specific contact.
