package banking.ajith.app.entity;

public class Transaction {

    private int accountnumber;

    @Override
    public String toString() {
        return "Transaction{" +
                "accountnumber=" + accountnumber +
                ", payment_type='" + payment_type + '\'' +
                ", debitedamount=" + debitedamount +
                ", branchname='" + branchname + '\'' +
                ", transactionid='" + transactionid + '\'' +
                '}';
    }

    private String payment_type;
    private int debitedamount;
    private String branchname;
    private String transactionid;

    public Transaction(int accountnumber, String payment_type, int debitedamount, String branchname, String transactionid) {
        this.accountnumber = accountnumber;
        this.payment_type = payment_type;
        this.debitedamount = debitedamount;
        this.branchname = branchname;
        this.transactionid = transactionid;
    }



    public int getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(int accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getDebitedamount() {
        return debitedamount;
    }

    public void setDebitedamount(int debitedamount) {
        this.debitedamount = debitedamount;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }



    public Transaction() {
    }


}
