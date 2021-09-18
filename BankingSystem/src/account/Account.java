package account;

import bank.Bank;

import java.math.BigDecimal;

public class Account {
    protected String category;
    protected String accNo;
    protected String owner;
    protected BigDecimal balance;
    protected boolean isActive;

    public Account() {
        this.category = "N";
        this.isActive = true;
    }
    public Account(String accNo, String owner, BigDecimal balance) {
        //TODO
        this.category = "N";
        this.accNo = accNo;
        this.owner = owner;
        this.balance = balance;
        this.isActive = true;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void getAccountInfo(Account account){
        System.out.printf("\n계좌종류: %s | 계좌번호: %s | 계좌주명: %s | 잔액: %s원",category,accNo,owner,Bank.df.format(balance));
    }

    public BigDecimal withdraw(BigDecimal amount) throws Exception{
        if(!this.isActive){
            throw new Exception("비활성된 계좌입니다.");
        }
        if(balance.compareTo(amount) < 0){
            throw new Exception(this.getAccNo()+"계좌의 잔액이 부족합니다.");
        }
        this.balance = this.balance.subtract(amount);
        return amount;
    }
    public BigDecimal deposit(BigDecimal amount){
        this.balance = this.balance.add(amount);
        return amount;
    }
}

