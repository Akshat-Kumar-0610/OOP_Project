/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lib.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Minahil Imtiaz
 */
public class Loan {

    private int loanId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private Student user;
    private Books issuedBook;
    private String fineStatus;
    private boolean returnedStatus;

    Loan() {
        loanId = -1;
        Calendar calendar = Calendar.getInstance();
        issueDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);

        dueDate = calendar.getTime();
        this.returnDate = dueDate;
        returnedStatus = false;
        fineStatus = "no fine";

    }

    Loan(int id) {
        loanId = id;
        Calendar calendar = Calendar.getInstance();
        issueDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 7);

        dueDate = calendar.getTime();
        this.returnDate = dueDate;
        returnedStatus = false;
        fineStatus = "no fine";

    }

    Loan(int id, String user_id, int borrowedbook_id, boolean returnedStatus, String fineStatus, Date issueDate, Date dueDate, Date returnDate) {
        dbConnectivity db = new dbConnectivity();
        this.loanId = id;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fineStatus = fineStatus;
        this.user = db.getStudentObjectByUserId(user_id);
        this.issuedBook = db.getBookById(borrowedbook_id);
        this.returnedStatus = returnedStatus;
    }

    Loan(int id, Student borrower, Books issuedBook, String finestatus, boolean returnedStatus, Date issueDate, Date dueDate, Date returnDate) {

        this.loanId = id;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fineStatus = finestatus;
        this.user = borrower;
        this.issuedBook = issuedBook;
        this.returnedStatus = returnedStatus;
    }

    public int getBookId() {
        return this.issuedBook.GetBookId();
//        dbConnectivity db = new dbConnectivity();
//        return (db.getLoanedBookId(this.loanId));

    }

    public String getFineStatus() {
        return fineStatus;
//        dbConnectivity db = new dbConnectivity();
//        return (db.getLoanFineStatus(this.loanId));
    }

    public int getLoanId() {
        return this.loanId;

    }

    public Date getReturnDate() {
        return this.returnDate;
//        dbConnectivity db = new dbConnectivity();
//        return (db.getReturnDate(this.loanId));
    }

    public Date getDueDate() {
        return this.dueDate;
//        dbConnectivity db = new dbConnectivity();
//        return (db.getDueDate(this.loanId));
    }

    public Date getIssueDate() {
        return this.issueDate;
//        dbConnectivity db = new dbConnectivity();
//        return (db.getIssueDate(this.loanId));
    }

    public Books getaBook() {

        //  return this.issuedBook;
        dbConnectivity db = new dbConnectivity();
        return (db.getLoanedBook(this.loanId));
    }

    public String getUserId() {
        return this.user.getUserId();

    }

    public User getUser() {
        return this.user;
    }

    public boolean getStatus() {
        return this.returnedStatus;
//        dbConnectivity db = new dbConnectivity();
//        return (db.getLoanReturnedStatus(this.loanId));

    }

    public void setReturnedDate(Date Ret_date) {

        this.returnDate = Ret_date;
        dbConnectivity db = new dbConnectivity();
        db.setLoanReturnedDate(this.loanId, Ret_date);

    }

    public void setBook(Books NewBook) {

        this.issuedBook = NewBook;
        dbConnectivity db = new dbConnectivity();
        db.setLoanedBook(this.loanId, this.issuedBook.GetBookId());

    }

    public void setUser(Student user) {

        this.user = user;
        dbConnectivity db = new dbConnectivity();
        db.setLoaneeObject(this.loanId, this.user.getUserId());

    }

    public void SetReturnStatus(boolean status) {

        this.returnedStatus = status;
        dbConnectivity db = new dbConnectivity();
        db.setReturnStatus(this.loanId, status);
    }

    public void setLoan(Loan Update) {
        this.issuedBook = Update.issuedBook;
        this.user = Update.user;
        this.dueDate = Update.dueDate;
        this.issueDate = Update.issueDate;
        this.fineStatus = Update.fineStatus;
        this.returnDate = Update.returnDate;
        this.returnedStatus = Update.returnedStatus;

        dbConnectivity db = new dbConnectivity();
        db.setLoan(this.loanId, Update);
    }

    public void SetFineStatus(String status) {

        this.fineStatus = status;
        dbConnectivity db = new dbConnectivity();
        db.setLoanFineStatus(loanId, status);
    }

    public boolean GetLoan(ArrayList<Books> BooksList, Student Loanee, ArrayList<User> Borrowers, Admin AdminBody, ArrayList<Loan> LoanList) {
        System.out.print("Search the book here \n Press 1. to search with title \n Press 2. to search by author name \n 3. to search by subject\n ");
        Scanner input = new Scanner(System.in);
        boolean status = false;
        int command = input.nextInt();
        if (command == 1) {
            System.out.print("Enter the title ");
            String Title = input.next();
            Loanee.SearchBookbyTitle(Title);
        } else if (command == 2) {
            System.out.print("Enter the author name ");
            String A_name = input.next();
            Loanee.SearchBookbyAuthor(A_name);
        } else {
            System.out.print("You pressed invalid key ! Now displaying all the books ");
            for (Books b : BooksList) {

                b.PrintInformation();
            }
        }
        System.out.print("Now provide the id of book you want ");
        int id = input.nextInt();
        boolean is_available = false;
        for (Books b : BooksList) {
            if (b.ChekcAvailability(id) == true) {
                is_available = true;
                // return status;
            }
        }

        if (is_available == true) {
            status = AdminBody.checkOutItem(id, Loanee, BooksList, this, LoanList);

        }

        return status;

    }

    public double CalculateFine() {

        if (returnDate.after(dueDate)) {

            long difference = (returnDate.getTime() - dueDate.getTime()) / 86400000;
            difference = Math.abs(difference);
            return 30.0 * difference;

        } else {
            return 0.0;

        }

    }

    String PrintLoanInfo() {
        String userId = this.user.getUserId();
        int bookid = issuedBook.GetBookId();
        String resultant = " ";
        resultant = resultant + "loanId:" + loanId + "\t" + "issue date:" + issueDate + "\t" + "due date" + dueDate + "\t"
                + "returnDate:" + returnDate + "\t" + "borrower id " + userId + "\t" + " issuedBook :"
                + bookid + "\t" + "fine status  :" + fineStatus + "\t" + "returned status" + returnedStatus + "\n";
        return resultant;

    }

}