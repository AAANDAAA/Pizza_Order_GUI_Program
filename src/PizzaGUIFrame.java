import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea orderArea;
    private JButton orderButton, clearButton, quitButton;

    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Choose Crust"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Choose Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeBox = new JComboBox<>(sizes);
        sizePanel.add(sizeBox);

        JPanel toppingPanel = new JPanel();
        toppingPanel.setBorder(new TitledBorder("Choose Toppings"));
        toppingPanel.setLayout(new GridLayout(3, 2));
        String[] toppingNames = {"Donkey", "Vimp", "Blood", "teeth", "Human", "Cheese"};
        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingPanel.add(toppings[i]);
        }

        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BorderLayout());
        orderPanel.setBorder(new TitledBorder("Your Order"));
        orderArea = new JTextArea(10, 40);
        orderArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderArea);
        orderPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingPanel);

        add(topPanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private class OrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String crust = null;
            if (thinCrust.isSelected()) crust = "Thin";
            else if (regularCrust.isSelected()) crust = "Regular";
            else if (deepDishCrust.isSelected()) crust = "Deep-dish";

            if (crust == null) {
                JOptionPane.showMessageDialog(null, "Please select a crust.");
                return;
            }

            int sizeIndex = sizeBox.getSelectedIndex();
            String size = (String) sizeBox.getSelectedItem();
            double basePrice = sizePrices[sizeIndex];

            StringBuilder toppingsList = new StringBuilder();
            int toppingCount = 0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    toppingsList.append(" - ").append(topping.getText()).append("\n");
                    toppingCount++;
                }
            }

            if (toppingCount == 0) {
                JOptionPane.showMessageDialog(null, "Please select at least one topping.");
                return;
            }

            double toppingCost = toppingCount * toppingPrice;
            double subtotal = basePrice + toppingCost;
            double tax = subtotal * taxRate;
            double total = subtotal + tax;


            StringBuilder receipt = new StringBuilder();
            receipt.append("=========================================\n");
            receipt.append("** Type of Crust & Size: ").append(crust).append(" / ").append(size).append(" **\n");
            receipt.append(String.format("Base Price: $%.2f\n", basePrice));
            receipt.append("** Toppings:\n").append(toppingsList);
            receipt.append(String.format("Toppings Price: $%.2f\n", toppingCost));
            receipt.append("=========================================\n");
            receipt.append(String.format("Sub-total: $%.2f\n", subtotal));
            receipt.append(String.format("Tax: $%.2f\n", tax));
            receipt.append("-----------------------------------------\n");
            receipt.append(String.format("** Total: $%.2f **\n", total));
            receipt.append("=========================================\n");

            orderArea.setText(receipt.toString());
        }
    }

    private void clearForm() {
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustGroup.clearSelection();
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderArea.setText("");
    }
}
