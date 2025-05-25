package model;

public class Account {
    private int acno;
    private String name;
    private int balance;

    public Account(int acno, String name, int balance) {
        this.acno = acno;
        this.name = name;
        this.balance = balance;
    }

    public int getAcno() {
        return acno;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
