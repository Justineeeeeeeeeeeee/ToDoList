import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ToDoListApp extends JFrame {
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;
    private JTextField inputField;
    private JComboBox<String> daySelector;
    private HashMap<String, String> dateMap;

    public ToDoListApp() {
        setTitle("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 600);
        setMinimumSize(new Dimension(400, 600));

        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        inputField = new JTextField();
        dateMap = new HashMap<>();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(inputField, BorderLayout.CENTER);

        JPanel dayPanel = new JPanel(new BorderLayout());
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        daySelector = new JComboBox<>(days);
        dayPanel.add(daySelector, BorderLayout.CENTER);
        inputPanel.add(dayPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String todo = inputField.getText();
                String day = (String) daySelector.getSelectedItem();
                if (!todo.isEmpty() && day != null) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String item = day + ": " + todo + " (Added on " + timestamp + ")";
                    todoListModel.addElement(item);
                    dateMap.put(item, timestamp);
                    inputField.setText("");
                }
            }
        });
        inputPanel.add(addButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoList.setVisibleRowCount(10);
        JScrollPane todoScrollPane = new JScrollPane(todoList);
        mainPanel.add(todoScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = todoList.getSelectedIndex();
                if (selectedIndex != -1) {
                    todoListModel.remove(selectedIndex);
                }
            }
        });
        buttonPanel.add(removeButton);

        JButton markAsDoneButton = new JButton("Mark as Done");
        markAsDoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = todoList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String item = todoListModel.remove(selectedIndex);
                    historyListModel.addElement(item + " (Done on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
                }
            }
        });
        buttonPanel.add(markAsDoneButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel historyLabel = new JLabel("History");
        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.setVisibleRowCount(10);
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("To-Do", mainPanel);
        tabbedPane.addTab("History", historyPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ToDoListApp().setVisible(true);
            }
        });
    }
}
