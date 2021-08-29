package bank;

import account.SavingAccount;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class SavingBank extends Bank {

    public void withdraw(SavingAccount account) throws Exception{
        // TODO: Account의 출금 메서드에서 잔액/목표 금액 체크하여 조금 다르게 구현
        // throws Exception 적금 계좌는 잔액이 목표 금액(%s원) 이상이어야 출금 가능합니다.
        BigDecimal balance = account.getBalance();
        BigDecimal goalAmount = account.getGoalAmount();
        if(balance.compareTo(goalAmount)>=0){
            System.out.println("\n출금할 금액을 입력하세요.");
            String amount = scanner.next();

            BigDecimal interest = interestCalculators.get("S").getInterest(balance);
            BigDecimal withdrawAmount = account.withdraw(new BigDecimal(amount)).add(interest);

            System.out.println("\n이자 "+ df.format(interest) + "원을 포함한 " + df.format(withdrawAmount) + "원이 출금되었습니다.");
        }
        else
            System.out.printf("\n적금 계좌는 잔액(%s)이 목표 금액(%s) 이상이어야 출금 가능합니다.\n",df.format(balance),df.format(goalAmount));
    }
    // TODO: 목표금액을 입력받아서 SavingAccount 객체 생성하도록 재정의
    @Override
    public SavingAccount createAccount() throws NoSuchElementException{
        try{
            return account;
        }catch (){
            //TODO: 오류 throw
        }
    }
}