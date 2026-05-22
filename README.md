# 🌳 AVL Tree Phonebook Visualizer

A Java Swing desktop application that functions as a phonebook while visually demonstrating the mechanics of a self-balancing AVL Tree in real-time! 

## ✨ Features

* **Self-Balancing Logic:** Automatically performs Left, Right, Left-Right, and Right-Left rotations to maintain optimal $O(\log n)$ search times.
* **Smooth Animations:** 60fps visual easing when nodes move, insert, or rotate.
* **Color-Coded Balance Factors (BF):**
    * 🟦 **Blue:** Perfect balance ($BF = 0$).
    * 🟨 **Yellow:** Slight imbalance ($BF = \pm 1$).
    * 🟥 **Red:** Critical imbalance ($BF > 1$ or $BF < -1$, triggers rotation).
    * 🟩 **Green:** Highlighted search path.
* **Time Travel (Undo/Redo):** Complete deep-state cloning allows you to undo/redo operations flawlessly without breaking the visual structure.
* **Persistent Storage:** Automatically reads and writes contacts to a local `phonebook.csv` file.

## 🛠️ Tech Stack

* **Language:** Java
* **GUI Framework:** Java Swing / AWT
* **Core Concepts:** Binary Search Trees, AVL Rotations, Recursion, Graphics2D Animations, Stacks/Queues (Undo/Redo History).

## 🚀 How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/tinkerbeo/AVL-PhoneBook-CS2-Final-Project.git
    cd avl-phonebook
    ```
2.  **Compile the code:**
    ```bash
    javac -d bin src1/phonebookavl/*.java
    ```
3.  **Run the application:**
    ```bash
    java -cp bin phonebookavl.PhonebookApp
    ```

## 📂 Project Structure

* `PhonebookApp.java`: Main window, layout setup, and UI controller.
* `TreePanel.java`: The visual canvas handling Graphics2D drawing, math targeting, and 60fps easing animations.
* `AVLTree.java`: The algorithmic brain managing tree balancing, heights, and rotations.
* `Node.java`: The basic building block representing a tree node.
* `Contact.java`: The data payload storing a name and phone number.
* `PhonebookStorage.java`: The static file-handler parsing and saving `phonebook.csv`.

## 🤝 Contributing
Feel free to open issues or submit pull requests for additional features (like zoom/pan controls or editing contact details in place)!
