package com.wipro.bank.service;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.InsufficientFundsException;
import com.wipro.bank.dao.BankDAO;

public class BankService {

    BankDAO dao = new BankDAO();

    public String checkBalance(String accountNumber) {
        if (dao.validateAccount(accountNumber)) {
            float balance = dao.findBalance(accountNumber);
            return "BALANCE:" + balance;
        }
        return "ACCOUNT NUMBER INVALID";
    }

    public String transfer(TransferBean transferBean) throws InsufficientFundsException {
        if (transferBean == null) return "INVALID";

        String fromACC = transferBean.getFromAccountNumber();
        String toACC = transferBean.getToAccountNumber();

        if (!dao.validateAccount(fromACC) || !dao.validateAccount(toACC))
            return "INVALID ACCOUNT";

        float fromBalance = dao.findBalance(fromACC);

        if (fromBalance >= transferBean.getAmount()) {
            float newFromBalance = fromBalance - transferBean.getAmount();
            float newToBalance = dao.findBalance(toACC) + transferBean.getAmount();

            dao.updateBalance(fromACC, newFromBalance);
            dao.updateBalance(toACC, newToBalance);

            if (dao.transferMoney(transferBean))
                return "SUCCESS";
            else
                return "TRANSFER FAILED";

        } else {
            throw new InsufficientFundsException();
        }
    }
}
