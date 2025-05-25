package ui;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class BankUI extends JFrame {
    private AccountDAO dao;

    public BankUI() {
        dao = new AccountDAO();
        setTitle("Bank Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Create Account", createAccountPanel());
        tabbedPane.add("View Accounts", viewAccountsPanel());
        tabbedPane.add("Deposit", depositPanel());
        tabbedPane.add("Withdraw", withdrawPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField tfAcno = new JTextField();
        JTextField tfName = new JTextField();
        JTextField tfBal = new JTextField();
        JButton btnCreate = new JButton("Create");

        panel.add(new JLabel("Account Number:")); panel.add(tfAcno);
        panel.add(new JLabel("Name:")); panel.add(tfName);
        panel.add(new JLabel("Balance:")); panel.add(tfBal);
        panel.add(new JLabel()); panel.add(btnCreate);

        btnCreate.addActionListener(e -> {
            int acno = Integer.parseInt(tfAcno.getText());
            String name = tfName.getText();
            int bal = Integer.parseInt(tfBal.getText());
            dao.createAccount(new Account(acno, name, bal));
            JOptionPane.showMessageDialog(this, "Account Created Successfully");
        });

        return panel;
    }

    private JPanel viewAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.addActionListener(e -> {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Acno");
            model.addColumn("Name");
            model.addColumn("Balance");
            Vector<Vector<Object>> data = dao.getAllAccounts();
            for (Vector<Object> row : data) model.addRow(row);
            table.setModel(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel depositPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField tfAcno = new JTextField();
        JTextField tfAmt = new JTextField();
        JButton btnDeposit = new JButton("Deposit");

        panel.add(new JLabel("Account Number:")); panel.add(tfAcno);
        panel.add(new JLabel("Amount:")); panel.add(tfAmt);
        panel.add(new JLabel()); panel.add(btnDeposit);

        btnDeposit.addActionListener(e -> {
            int acno = Integer.parseInt(tfAcno.getText());
            int amt = Integer.parseInt(tfAmt.getText());
            dao.deposit(acno, amt);
            JOptionPane.showMessageDialog(this, "Amount Deposited Successfully");
        });

        return panel;
    }

    private JPanel withdrawPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField tfAcno = new JTextField();
        JTextField tfAmt = new JTextField();
        JButton btnWithdraw = new JButton("Withdraw");

        panel.add(new JLabel("Account Number:")); panel.add(tfAcno);
        panel.add(new JLabel("Amount:")); panel.add(tfAmt);
        panel.add(new JLabel()); panel.add(btnWithdraw);

        btnWithdraw.addActionListener(e -> {
            int acno = Integer.parseInt(tfAcno.getText());
            int amt = Integer.parseInt(tfAmt.getText());
            dao.withdraw(acno, amt);
            JOptionPane.showMessageDialog(this, "Amount Withdrawn Successfully");
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankUI::new);
    }
}
