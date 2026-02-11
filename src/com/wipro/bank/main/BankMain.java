package com.wipro.bank.main;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.service.BankService;
import com.wipro.bank.util.InsufficientFundsException;

public class BankMain {

    public static void main(String[] args) {

        try {
            BankService bankService = new BankService();

            System.out.println(bankService.checkBalance("717823331"));

            TransferBean bean = new TransferBean();
            bean.setFromAccountNumber("717823331");
            bean.setToAccountNumber("123345676");
            bean.setAmount(41500);
            bean.setDateOfTransaction(new java.util.Date());

            System.out.println(bankService.transfer(bean));

        } catch (InsufficientFundsException e) {
            System.out.println(e);
        }
    }
}
