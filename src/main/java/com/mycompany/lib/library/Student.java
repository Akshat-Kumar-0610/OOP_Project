package com.mycompany.lib.library;

import java.util.ArrayList;

public class Student extends User {

    private boolean fineDefaulter;
    private double fine;
    private ArrayList<Loan> loans;

    Student() {
        super();
        fineDefaulter = false;
        loans = new ArrayList<>();
        fine = 0.0;
    }

    Student(String userId, String passsword, String name, Gender gender) {
        super(userId, passsword, name, gender);
        this.fineDefaulter = false;
        this.fine = 0.0;
        loans = new ArrayList<>();

    }

    @Override
    public boolean GetFineStatus() {
//        return this.fine_defaulter;
        dbConnectivity db = new dbConnectivity();
        return (db.GetFineStatus(this.getUserId()));
    }

    public double GetFineAmount() {
//        return this.fine;
        dbConnectivity db = new dbConnectivity();
        return (db.GetFineAmount(this.getUserId()));

    }

    public ArrayList<Loan> getLoans() {
        return this.loans;
    }

    @Override
    public boolean SetFineStatus(boolean fineDefaulter) {
        this.fineDefaulter = fineDefaulter;
        dbConnectivity db = new dbConnectivity();
        boolean result = db.setFineStatus(this.getUserId(), fineDefaulter);
        return result;
    }


    @Override
    public void SetFineAmount(double user_fine) {
        this.fine = user_fine;
        dbConnectivity db = new dbConnectivity();
        boolean result = db.SetFineAmount(this.getUserId(), user_fine);


    }

    public void SetName(String name) {
        super.setName(name);
        dbConnectivity db = new dbConnectivity();
        boolean result = db.SetName(this.getUserId(), name);

    }


    @Override
    public boolean AddLoanInfo(Loan LoanInfo) {

        loans.add(LoanInfo);
        return true;
//        dbConnectivity db = new dbConnectivity ();
//        boolean result=db.AddLoanInfo(this.GetId(), LoanInfo);
//        return result;

    }

    public void AllLoansofUser(ArrayList<Loan> LoansofUser) {

        // this.BookLoans=LoansofUser;
        dbConnectivity db = new dbConnectivity();
        this.loans = db.loadLoanListofSpecificUser(this.getUserId());
    }

    //helping function to update loan info
    @Override
    public void UpdateLoanInfo(Loan Update, int bookId) {

//        Iterator<Loan> itr = BookLoans.Iterator(BookLoans.size());
//        while (itr.hasPrevious()) {
//
//            Loan L = itr.previous();
        for (int i = loans.size() - 1; i >= 0; i--) {
            Loan L = loans.get(i);

            if ((L.getBookId() == bookId)) {
                L.setLoan(Update);
                break;
            }
        }

    }

    //needed to be checked
    @Override
    public String viewInformation(ArrayList<Loan> LoanList, String userId) {


        String Resultant = "";
        Resultant = Resultant + super.PrintInformation();
        Resultant += "\n Loan Info \n";

        if (loans.isEmpty() == false) {
            for (int i = 0; i < loans.size(); i++) {
                Loan L = loans.get(i);
                Resultant += L.PrintLoanInfo();
                Resultant += "\n";
            }
        } else {

            Resultant += "NO LOANS TILL YET!";
        }

        return Resultant;

    }

}
