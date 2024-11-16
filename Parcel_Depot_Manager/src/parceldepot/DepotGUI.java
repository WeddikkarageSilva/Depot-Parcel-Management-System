package parceldepot;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepotGUI extends JFrame {
    private Manager manager;
    private JTextArea logArea;
    private JButton processButton, addCustomerButton, addParcelButton, searchButton;
    private JLabel collectionResultLabel;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public DepotGUI(Manager manager) {
        this.manager = manager;
        setupComponents();
        loadCustomerTable();
    }

    private void setupComponents() {
        setTitle("Parcel Depot Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        logArea = new JTextArea();
        logArea.setEditable(false);
        mainPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        processButton = new JButton("Process Selected Customer");
        processButton.addActionListener(e -> processSelectedCustomer());

        addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(e -> addCustomer());

        addParcelButton = new JButton("Add Parcel");
        addParcelButton.addActionListener(e -> addParcel());

        searchButton = new JButton("Search Parcel");
        searchButton.addActionListener(e -> searchParcel());

        controlPanel.add(processButton);
        controlPanel.add(addCustomerButton);
        controlPanel.add(addParcelButton);
        controlPanel.add(searchButton);

        collectionResultLabel = new JLabel("Last collection: None");
        mainPanel.add(collectionResultLabel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"Seq No", "Customer Name", "Parcel ID", "Days in Depot", "Weight", "Collected"}, 0);
        customerTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadCustomerTable() {
        // Load data from Manager's customer queue and populate the JTable
        tableModel.setRowCount(0);  // Clear the table before adding new data
        for (QueueOfCustomers.Customer customer : manager.getQueueOfCustomers().getAllCustomers()) {
            Parcel parcel = manager.findParcelByID(customer.getParcelID());
            if (parcel != null && !parcel.isCollected()) {
                tableModel.addRow(new Object[]{
                        customer.getSeqNo(),
                        customer.getName(),
                        parcel.getParcelID(),
                        parcel.getDaysInDepot(),
                        parcel.getWeight(),
                        parcel.isCollected() ? "Yes" : "No"
                });
            }
        }
    }

    private void processSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            String parcelID = (String) customerTable.getValueAt(selectedRow, 2);
            String customerName = (String) customerTable.getValueAt(selectedRow, 1);
            manager.processCustomer(parcelID, customerName);
            JOptionPane.showMessageDialog(this, "Processed Customer: " + customerName + ", Parcel: " + parcelID);
            updateCustomerTable();
            collectionResultLabel.setText("Last collection: " + manager.getLastCollectedParcelInfo());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to process.");
        }
    }

    private void updateCustomerTable() {
        // Update the customer table after processing a record
        tableModel.setRowCount(0);  // Clear the table
        loadCustomerTable();  // Reload the updated data
    }

    private void searchParcel() {
        String parcelID = JOptionPane.showInputDialog("Enter Parcel ID to search:");
        if (parcelID != null && !parcelID.trim().isEmpty()) {
            Parcel parcel = manager.findParcelByID(parcelID);
            if (parcel != null) {
                JOptionPane.showMessageDialog(this, "Parcel Found: " + parcel.getParcelID() + ", Collected: " + (parcel.isCollected() ? "Yes" : "No"));
            } else {
                JOptionPane.showMessageDialog(this, "Parcel not found.");
            }
        }
    }

    private void addCustomer() {
        String name = JOptionPane.showInputDialog("Enter Customer Name:");
        String parcelID = JOptionPane.showInputDialog("Enter Parcel ID:");
        int seqNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Sequence Number:"));
        manager.addNewCustomer(seqNo, name, parcelID);
        updateCustomerTable();
    }

    private void addParcel() {
        String parcelID = JOptionPane.showInputDialog("Enter Parcel ID:");
        int daysInDepot = Integer.parseInt(JOptionPane.showInputDialog("Enter Days in Depot:"));
        double weight = Double.parseDouble(JOptionPane.showInputDialog("Enter Parcel Weight:"));
        boolean isCollected = false;
        manager.addNewParcel(parcelID, daysInDepot, weight, isCollected);
        updateCustomerTable();
    }

    public static void main(String[] args) {
        Manager manager = new Manager();
        DepotGUI gui = new DepotGUI(manager);
        gui.setVisible(true);
    }

    // The auto-generated initComponents method will remain here, but it is unused.

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
