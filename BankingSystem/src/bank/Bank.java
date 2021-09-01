package bank;

import account.Account;
import account.SavingAccount;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {
    //TODO: Bank 클래스는 출금, 입금, 송금, 계좌 생성, 계좌 검색 기능들을 갖고 있습니다.
    protected static Scanner scanner = new Scanner(System.in);
    protected static int seq = 0;
    public static DecimalFormat df = new DecimalFormat("#,###");
    protected HashMap<String,InterestCalculator> interestCalculators;

    public Bank(){
        interestCalculators = new HashMap<>();
        InterestCalculator basic = new BasicInterestCalculator();
        InterestCalculator saving = new SavingInterestCalculator();
        interestCalculators.put("N",basic);
        interestCalculators.put("S",saving);
    }
    // 뱅킹 시스템의 기능들
    public void withdraw() throws Exception {
        //TODO: 출금 메서드 구현
        //TODO: key, value 형태의 HashMap을 이용하여 interestCalculators 구현
        //여기서 key: category, value: 각 category의 InterestCalculator 인스턴스

        // 계좌번호 입력
        Account account;
        while(true){
            System.out.println("\n출금하시려는 계좌번호를 입력하세요.");
            String accNo = scanner.next();
            // TODO: 검색 -> 적금 계좌이면 적금 계좌의 출금 메소드 호출 -> 완료시 break
            account = findAccount(accNo);
            if(account != null){
                if(account.getCategory().equals("S")){
                    ((SavingBank) this).withdraw((SavingAccount) account);
                    return;
                }
                break;
            }
            System.out.println("\n찾으시는 계좌번호가 존재하지 않습니다. 다시 입력해주세요.");

        }
        // 출금처리
        System.out.printf("\n출금할 금액을 입력하세요.(잔액: %s원)\n",df.format(account.getBalance()));
        String amount = scanner.next();
        // TODO: interestCalculators 이용하 이자 조회 및 출금
        try {
            BigDecimal balance = account.getBalance();
            BigDecimal interest = interestCalculators.get("N").getInterest(balance);
            BigDecimal withdrawAmount = account.withdraw(new BigDecimal(amount)).add(interest);

            System.out.println("\n이자 "+ df.format(interest) +"원을 포함한 " + df.format(withdrawAmount) + "원이 출금되었습니다.");
            System.out.printf("잔액: %s원\n",df.format(account.getBalance()));
        }catch (Exception e){
            System.out.println("출금 과정 중 오류가 발생하여 재시작합니다.");
            System.out.println(e);
            withdraw();
        }
    }

    public void deposit(){
        //TODO: 입금 메서드 구현
        // 존재하지 않는 계좌이면 다시 물어보기
        Account account;
        while(true){
            System.out.println("\n입금하시려는 계좌번호를 입력해주세요.");
            String accNo = scanner.next();
            account = findAccount(accNo);
            if(account != null)
                break;
            System.out.println("\n찾으시는 계좌번호가 존재하지 않습니다. 다시 입력해주세요.");
        }
        // TODO: 입금 처리
        System.out.println("\n입금할 금액을 입력하세요.");
        BigDecimal amount = new BigDecimal(scanner.next());
        account.deposit(amount);
        System.out.println(df.format(amount) + "원 입금완료");
    }

    public Account createAccount() throws InputMismatchException {
        //TODO: 계좌 생성하는 메서드 구현
        try {
            // 계좌번호 채번
            // 계좌번호는 "0000"+증가한 seq 포맷을 가진 번호입니다.
            System.out.println("\n고객님의 이름을 입력해주세요.");
            String owner = scanner.next();
            String accNo = String.format("%04d",++seq);
            Account account = new Account(accNo, owner, new BigDecimal("0"));
            //TODO
            System.out.printf("\n%s님 계좌가 발급되었습니다.\n", owner);
            return account;
        }catch (Exception e){
            //TODO: 오류 throw
            System.out.println(e);
            return null;
        }
    }

    public Account findAccount(String accNo){
        //TODO: 계좌리스트에서 찾아서 반환하는 메서드 구현
        Account account;
        for(Account chk : CentralBank.getInstance().getAccountList()){
            if(chk.getAccNo().equals(accNo)){
                account = chk;
                return account;
            }
        }
        return null;
    }

    public void transfer() {
        //TODO: 송금 메서드 구현
        // 잘못 입력하거나 예외처리시 다시 입력가능하도록
        try {
            Account sendAccount;
            Account recAccount;
            while (true) {
                System.out.println("\n송금하시려는 계좌번호를 입력해주세요.");
                //TODO
                String sendAccNo = scanner.next();
                sendAccount = findAccount(sendAccNo);
                if (sendAccount != null) {
                    if (sendAccount.getCategory().equals("S")) {
                        System.out.println("\n적금 계좌로는 송금이 불가합니다. 다시 입력해주세요.");
                        continue;
                    }
                    break;
                }
                System.out.println("\n찾으시는 계좌번호가 존재하지 않습니다. 다시 입력해주세요.");
            }
            while (true) {
                System.out.println("\n어느 계좌번호로 보내시려나요?");
                //TODO
                String recAccNo = scanner.next();
                recAccount = findAccount(recAccNo);
                if (recAccount != null) {
                    if (recAccount.getAccNo().equals(sendAccount.getAccNo())) {
                        System.out.println("\n본인 계좌로의 송금은 입금을 이용해주세요. 다시 입력해주세요.");
                        continue;
                    }
                    break;
                }
                System.out.println("\n찾으시는 계좌번호가 존재하지 않습니다. 다시 입력해주세요.");
            }
            System.out.printf("\n송금할 금액을 입력하세요.(잔액: %s원)\n", df.format(sendAccount.getBalance()));
            String amount = scanner.next();
            BigDecimal transfer = new BigDecimal(amount);
            sendAccount.withdraw(transfer);
            recAccount.deposit(transfer);
            System.out.printf("\n계좌번호 %s로 %s원 송금완료\n", recAccount.getAccNo(), df.format(transfer));
        } catch (Exception e){
            System.out.println("송금 과정 중 오류가 발생하여 재시작합니다.");
            System.out.println(e);
            transfer();
        }
        }
    }
