/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Library;

import java.util.Calendar;
import java.util.Date;

public class Loan {

    private int loanId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private Student user;
    private Books issuedBook;
    private String fineStatus;
    private boolean returnedStatus;

    ///////////////////
    // Constructors //
    /////////////////
    Loan() {
        loanId = -1;
        Calendar calendar = Calendar.getInstance();
        issueDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, Library.getIssuePeriod());
        dueDate = calendar.getTime();
        returnedStatus = false;
        fineStatus = "no fine";
    }

    Loan(int id) {
        loanId = id;
        Calendar calendar = Calendar.getInstance();
        issueDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, Library.getIssuePeriod());
        dueDate = calendar.getTime();
        returnedStatus = false;
        fineStatus = "no fine";
    }

    Loan(int id, String studentId, int borrowedbook_id, boolean returnedStatus, String fineStatus, Date issueDate, Date dueDate, Date returnDate) {
        DBConnection db = new DBConnection();
        this.loanId = id;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fineStatus = fineStatus;
        this.user = Library.getStudentObjectFromUserID(studentId);
        this.issuedBook = Library.getBookObjectFromBookID(borrowedbook_id);
        this.returnedStatus = returnedStatus;
    }

    Loan(int id, Student student, Books issuedBook, String finestatus, boolean returnedStatus, Date issueDate, Date dueDate, Date returnDate) {
        this.loanId = id;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fineStatus = finestatus;
        this.user = student;
        this.issuedBook = issuedBook;
        this.returnedStatus = returnedStatus;
    }

    /////////////
    // Getters //
    /////////////
    public int getBookId() {
        return this.issuedBook.getBookId();
    }

    public String getFineStatus() {
        return fineStatus;
    }

    public int getLoanId() {
        return this.loanId;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }

    public Books getBook() {
        return this.issuedBook;
    }

    public String getUserId() {
        return this.user.getUserId();
    }

    public User getUser() {
        return this.user;
    }

    public boolean getReturnedStatus() {
        return this.returnedStatus;
    }

    //////////////
    // Setters //
    /////////////
    public void setReturnedDate(Date Ret_date) {
        this.returnDate = Ret_date;
        DBConnection db = new DBConnection();
        db.setLoanReturnedDate(this.loanId, this.returnDate);
    }

    public void setBook(Books NewBook) {
        this.issuedBook = NewBook;
        DBConnection db = new DBConnection();
        db.setLoanedBook(this.loanId, this.issuedBook.getBookId());
    }

    public void setUser(Student user) {
        this.user = user;
        DBConnection db = new DBConnection();
        db.setLoaneeObject(this.loanId, this.user.getUserId());
    }

    public void setReturnedStatus(boolean status) {
        this.returnedStatus = status;
        DBConnection db = new DBConnection();
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
        DBConnection db = new DBConnection();
        db.setLoan(this.loanId, Update);
    }

    public void setFineStatus(String status) {
        this.fineStatus = status;
        DBConnection db = new DBConnection();
        db.setLoanFineStatus(loanId, status);
    }

    public synchronized  static String issueBook(int bookId, String studentId) throws Exception {
        String result = "Unable to issue Book";
        Library lib = new Library();
        DBConnection db = new DBConnection();
        Student s = Library.getStudentObjectFromUserID(studentId);
        Books b = db.getBookById(bookId);

        int countIssued = 0;
        for(Loan l: s.getLoans()){
            if (l.getReturnedStatus() == false){
                countIssued++;
            }
        }
        if (countIssued>= Library.getMaxIssues()){
            return "You have already issued "+Library.getMaxIssues()+" book and cannot issue more";
        }
        for(Loan l: s.getLoans()){
            if (l.getBookId() == bookId && l.getReturnedStatus() == false){
                return "You have already issued the book with bookID "+bookId;
            }
        }

        int index =  lib.getAllLoansList().size();
        Loan LoanObj = new Loan(index+1);
        LoanObj.setBook(b);
        LoanObj.setUser(s);
        lib.addLoan(LoanObj);
        s.addLoan(LoanObj);
        boolean status = db.addNewLoan(LoanObj);
        if(!status){
            throw new Exception("Some error occured while issueing the book.");
        }
        result = "Book with bookId "+bookId+" issued successfully";
        return result;
    }

    public static String returnBook(int bookId,String studentId, Date returnDate) {
        DBConnection db  = new DBConnection();
        Student s1 = Library.getStudentObjectFromUserID(studentId);
        String result = "You have not issued this book.";
        Loan l = new Loan();
        for(int i=0; i < s1.getLoans().size(); i++){
            l = s1.getLoans().get(i);
            if(bookId == l.getBookId() && l.getReturnedStatus() == false){
                double fine = l.calculateFine();
                l.setReturnedStatus(true);
                l.setReturnedDate(returnDate);
                s1.setFineAmount(s1.getFineAmount() + fine);
                s1.removeLoan(l);
                result = "Book successfully returned.";
                break;
            }
        }
        s1.getLoans().remove(l);
        return result;
    }

    public static String renewBook(int bookId, String studentId, Date renewDate) throws Exception {
        String result = "You have not issued this book";
        DBConnection db = new DBConnection();
        Student studentObject = Library.getStudentObjectFromUserID(studentId);
        for(Loan l : studentObject.getLoans()) {
            if (bookId == l.getBookId() && l.getReturnedStatus() == false) {
                returnBook(bookId, studentId, renewDate);
                issueBook(bookId, studentId);
                result = "Book successfully renewed.";
                break;
            }
        }
        return result;
    }

    public double calculateFine() {
        if (returnDate!=null) {
            if (returnDate.after(dueDate)) {
                long difference = (returnDate.getTime() - dueDate.getTime()) / 86400000;
                difference = Math.abs(difference);
                return Library.getFinePerDayPerBook() * difference;
            } else {
                return 0.0;
            }
        }else {
            return 0.0;
        }
    }

    public String getLoanInfoAsString() {
        String userId = this.user.getUserId();
        int bookid = issuedBook.getBookId();
        String resultant = " ";
        resultant = resultant + "loanId:" + loanId + "\t" + "issue date:" + issueDate + "\t" + "due date" + dueDate + "\t"
                + "returnDate:" + returnDate + "\t" + "borrower id " + userId + "\t" + " issuedBook :"
                + bookid + "\t" + "fine status  :" + fineStatus + "\t" + "returned status" + returnedStatus + "\n";
        return resultant;

    }

}
