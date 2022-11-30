/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lib.library;

/**
 * @author Minahil Imtiaz
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Minahil
 */
public class dbConnectivity {

    Connection con;
    Statement stmt;

    dbConnectivity() //cons
    {
        try {

            String s = "jdbc:mysql://localhost:3306/Library";
            con = DriverManager.getConnection(s, "root", "root");

            stmt = con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void closeConnection() {

        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }


    Books getBookById(int bookId) {

        Books books = new Books();
        try {
            ResultSet rs = stmt.executeQuery("select * from book where idBook='" + bookId + "'");
            while (rs.next()) {
                int quantity;
                String title, author;
                bookId = rs.getInt(1);
                author = rs.getString(2);
                title = rs.getString(3);
                quantity = rs.getInt(4);
                Books NewBook = new Books(bookId, title, author, quantity);
                books = NewBook;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return books;

    }

    ArrayList<Books> LoadAllBooks() {
        ArrayList<Books> allBooks = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select * from book");
            while (rs.next()) {
                int bookId, quantity;
                String title, author, subject;
                bookId = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3);
                quantity = rs.getInt(4);
                Books NewBook = new Books(bookId, title, author, quantity);
                allBooks.add(NewBook);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return allBooks;
    }

    String getAuthorOfBook(int bookId) {
        String author = "";
        try {
            ResultSet rs = stmt.executeQuery("select author from book where idBook='" + bookId + "'");
            while (rs.next()) {
                author = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return author;
    }

    String GetTitleofBook(int bookId) {
        String title = " ";
        try {
            ResultSet rs = stmt.executeQuery("select title from book where idBook='" + bookId + "'");
            while (rs.next()) {
                title = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return title;

    }

    int getQuantityofBook(int bookId) {
        int quantity = -1;
        try {
            ResultSet rs = stmt.executeQuery("select quantity from book where idBook='" + bookId + "'");
            while (rs.next()) {
                quantity = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return quantity;
    }

    void updateBookQuantity(int newQuantity, int id) {
        try {
            int i = stmt.executeUpdate("UPDATE book  SET quantity='" + newQuantity + "'Where idBook='" + id + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void changeBookInfo(int bookId, String UpdatedInfo, int type) {
        try {
            if (type == 1) {
                int i = stmt.executeUpdate("UPDATE book  SET title='" + UpdatedInfo + "'Where idBook='" + bookId + "'");
            } else if (type == 2) {
                int i = stmt.executeUpdate("UPDATE book  SET author='" + UpdatedInfo + "'Where idBook='" + bookId + "'");
            } else if (type == 3) {
                int i = stmt.executeUpdate("UPDATE book  SET quantity='" + UpdatedInfo + "'Where idBook='" + bookId + "'");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Functions Related to Borrower are implemented below
    Student getStudentObjectByUserId(String userId) {
        Student student = new Student();
        try {
            ResultSet rs = stmt.executeQuery("SELECT user.userLoginId, user.password, user.name, user.gender, student.fine, student.fineDefaulter \n" +
                    "FROM student \n" +
                    "INNER JOIN user on student.idUser=user.userLoginId \n" +
                    "WHERE user.userLoginId='" + userId + "';" );
            while (rs.next()) {
                String id;
                String studentName;
                String password;
                Gender studentGender;
                boolean fineDefaulter;
                float fine;

                id = rs.getString(1);
                password = rs.getString(2);
                studentName = rs.getString(3);
                studentGender = Gender.genderFromChar(rs.getString(4).charAt(0));
                fineDefaulter = rs.getBoolean(5);
                fine = rs.getFloat(6);

                Student TempBorrowerObject = new Student(id, password, studentName, studentGender);
                student = TempBorrowerObject;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return student;

    }

    ArrayList<Student> loadAllStudents() {

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Student> CurrentUsers = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT user.userLoginId, user.password, user.name, user.gender, student.fine, student.fineDefaulter \n" +
                    "FROM student \n" +
                    "INNER JOIN user on student.idUser=user.userLoginId;");

            while (rs.next()) {

                String id;
                String password;
                String studentName;
                Gender studentGender;
                boolean fineDefaulter;
                float fine;

                id = rs.getString(1);
                password = rs.getString(2);
                studentName = rs.getString(3);
                studentGender = Gender.genderFromChar(rs.getString(4).charAt(0));
                fine = rs.getFloat(5);
                fineDefaulter = rs.getBoolean(6);

                Student TempBorrowerObj = new Student(id, password, studentName, studentGender);

                students.add(TempBorrowerObj);
                // System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getInt(5));
            }

            for (Student B : students) {
                String id = B.getUserId();
                ArrayList<Loan> UserLoanArray = loadLoanListofSpecificUser(id);
                B.AllLoansofUser(UserLoanArray);

            }

            for (Student B : students) {
                CurrentUsers.add(B);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return CurrentUsers;

    }

    boolean setFineStatus(String id, boolean fineDefaulter) {
        try {
            stmt.executeUpdate("Update student Set fineDefaulter='" + fineDefaulter + "' Where idUser='" + id + "'");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean SetName(String id, String name) {

        try {
            stmt.executeUpdate("Update user Set name='" + name + "' Where userLoginId='" + id + "'");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    boolean SetUserId(String id, String newId) {

        try {
            Student stud = getStudentObjectByUserId(id);
            stmt.executeUpdate("DELETE FROM user WHERE userLoginId='" + id + "'");
            addStudent(newId, stud.getPassword(), stud.getName(), stud.getGender());
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    boolean SetGender(String id, char g) {
        try {
            stmt.executeUpdate("Update user Set gender='" + g + "' Where userLoginId='" + id + "'");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean SetFineAmount(String id, double fine_amount) {

        try {
            stmt.executeUpdate("Update student Set fine='" + fine_amount + "' Where idUser='" + id + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    boolean GetFineStatus(String id) {
        boolean result = false;
        try {
            ResultSet rs = stmt.executeQuery("select fineDefaulter from student where idUser='" + id + "'");
            while (rs.next()) {
                result = rs.getBoolean(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    double GetFineAmount(String id) {
        double amount = 0.0;
        try {
            ResultSet rs = stmt.executeQuery("select fine from student where idUser='" + id + "'");
            while (rs.next()) {
                amount = rs.getDouble(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return amount;
    }

    //Admin Functions implemented here
    ArrayList<Admin> loadAllAdmin() {
        ArrayList<Admin> admins = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT user.userLoginId, user.password, user.name, user.gender \n" +
                    "FROM library.admin ad\n" +
                    "INNER JOIN user on ad.idUser=user.userLoginId;");
            while (rs.next()) {
                String id;
                String password;
                String name;
                Gender gender;

                id = rs.getString(1);
                password = rs.getString(2);
                name = rs.getString(3);
                gender = Gender.genderFromChar(rs.getString(4).charAt(0));

                Admin TempObj = new Admin(id,password, name, gender);
                admins.add(TempObj);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return admins;

    }

    boolean addStudent(String studentId, String password, String studentName, Gender studentGender) {

        try {

            boolean fine = false;
            double fineamount = 0.0;

            // Creating new user
            stmt.executeUpdate("Insert into user(userLoginId, password, name, gender) values ('" + studentId + "', '"+password + "','" + studentName + "','" + studentGender.toString().charAt(0) + "')");
            stmt.executeUpdate("Insert into student(idUser) values ('" + studentId + "');");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    ArrayList<Loan> loadLoanList() {

        ArrayList<Loan> LoanList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT user.userLoginId, user.password, user.name, user.gender, student.fine, student.fineDefaulter,loan.idLoan, loan.issueDate, loan.dueDate, loan.returnDate, loan.fineStatus, loan.returnedStatus, book.idBook, book.author, book.title, book.quantity\n" +
                    "FROM loan\n" +
                    "INNER JOIN student on loan.idStudent=student.idStudent\n" +
                    "INNER JOIN book on loan.issuedBookId=book.idBook\n" +
                    "INNER JOIN user on student.idUser=user.userLoginId;");
            while (rs.next()) {
                String id;
                String studentName;
                String password;
                Gender studentGender;
                double fine;
                boolean fineDefaulter;
                int loanId;
                Date issueDate, dueDate, returnedDate;
                String fineStatus;
                boolean returnedStatus;
                int bookId, quantity;
                String title, author, subject;

                id = rs.getString(1);
                password = rs.getString(2);
                studentName = rs.getString(3);
                studentGender = Gender.genderFromChar(rs.getString(4).charAt(0));
                fine = rs.getFloat(5);
                fineDefaulter = rs.getBoolean(6);
                loanId = rs.getInt(7);
                issueDate = rs.getDate(8);
                dueDate = rs.getDate(9);
                returnedDate = rs.getDate(10);
                fineStatus = rs.getString(11);
                returnedStatus = rs.getBoolean(12);
                bookId = rs.getInt(13);
                author = rs.getString(14);
                title = rs.getString(15);
                quantity = rs.getInt(16);

                Student student = new Student(id,password, studentName, studentGender);
                Books LoanedBook = new Books(bookId, author, title, quantity);
                Loan LoanTempObj = new Loan(loanId, student, LoanedBook, fineStatus, returnedStatus, issueDate, dueDate, returnedDate);
                LoanList.add(LoanTempObj);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return LoanList;

    }

    ArrayList<Loan> loadLoanListofSpecificUser(String userId) {

        ArrayList<Loan> LoanListofUser = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT user.userLoginId, user.password, user.name, user.gender, student.fine, student.fineDefaulter,loan.idLoan, loan.issueDate, loan.dueDate, loan.returnDate, loan.fineStatus, loan.returnedStatus, book.idBook, book.author, book.title, book.quantity\n" +
                    "FROM loan\n" +
                    "INNER JOIN student on loan.idStudent=student.idStudent\n" +
                    "INNER JOIN book on loan.issuedBookId=book.idBook\n" +
                    "INNER JOIN user on student.idUser=user.userLoginId;");

            while (rs.next()) {
                String id;
                String password;
                String studentName;
                Gender studentGender;
                boolean fineDefaulter;
                double fine;
                int loanId;
                Date issueDate, dueDate, returnedDate;
                String fineStatus;
                boolean returnedStatus;
                int bookId, quantity;
                String title, author, subject;

                id = rs.getString(1);
                password = rs.getString(2);
                studentName = rs.getString(3);
                studentGender = Gender.genderFromChar(rs.getString(4).charAt(0));
                fine = rs.getFloat(5);
                fineDefaulter = rs.getBoolean(6);
                loanId = rs.getInt(7);
                issueDate = rs.getDate(8);
                dueDate = rs.getDate(9);
                returnedDate = rs.getDate(10);
                fineStatus = rs.getString(11);
                returnedStatus = rs.getBoolean(12);
                bookId = rs.getInt(13);
                author = rs.getString(14);
                title = rs.getString(15);
                quantity = rs.getInt(16);

                Student Loanee = new Student(id,password, studentName, studentGender);
                Books LoanedBook = new Books(bookId, author, title, quantity);
                Loan LoanTempObj = new Loan(loanId, Loanee, LoanedBook, fineStatus, returnedStatus, issueDate, dueDate, returnedDate);
                LoanListofUser.add(LoanTempObj);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return LoanListofUser;

    }

//        LinkedList <Users>  LoadLoanInfo ( LinkedList <Borrower> BasicInfoList)
//        {
//            LinkedList<Users> CurrentUsers= new LinkedList <> ();
//              for( Borrower B: BasicInfoList  )
//              {
//
//                 int user_id=B.GetId();
//                 LinkedList<Loan> CurrentLoans=loadLoanListofSpecificUser(user_id);
//                 B.AllLoansofUser(CurrentLoans);
//                 CurrentUsers.add(B);
//
//              }
//
//              return CurrentUsers;
//
//        }
    //Implementing LoanFunction here

    boolean addNewLoan(Loan LoanObj) {

        try {
            int loanid = LoanObj.getLoanId();
            Date issueDate = LoanObj.getIssueDate();
            Date dueDate = LoanObj.getDueDate();
            Date return_date = LoanObj.getReturnDate();
            String fineStatus = LoanObj.getFineStatus();
            boolean returnedStatus = LoanObj.getStatus();
            String id = LoanObj.getUserId();
            int bookId = LoanObj.getBookId();

            java.sql.Date issueDate1 = new java.sql.Date(issueDate.getTime());
            java.sql.Date dueDate1 = new java.sql.Date(dueDate.getTime());
            java.sql.Date return_date1 = new java.sql.Date(return_date.getTime());

            stmt.executeUpdate("Insert into loan (idLoan , issueDate ,dueDate ,  returnDate , idStudent, issuedBookId fineStatus , returnedStatus ) values('" + loanid + "','" + issueDate1 + "','" + dueDate1 + "','" + return_date1 + "','" + id + "','" + bookId + "','" + fineStatus + "','" + returnedStatus + "')");

        } catch (Exception e) {
            System.out.println(e);
        }
        return true;

    }

    int getLoanedBookId(int loanId) {
        int bookId = -1;
        try {
            ResultSet rs = stmt.executeQuery("select issuedBookId from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                bookId = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return bookId;

    }

    String getLoanFineStatus(int loanId) {
        String LoanFineStatus = "";
        try {
            ResultSet rs = stmt.executeQuery("select fineStatus from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                LoanFineStatus = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return LoanFineStatus;
    }

    Date getReturnDate(int loanId) {

        Date ReturnDate = new Date();
        try {
            ResultSet rs = stmt.executeQuery("select returnDate from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                ReturnDate = rs.getDate(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ReturnDate;
    }

    Date getIssueDate(int loanId) {

        Date IssueDate = new Date();
        try {
            ResultSet rs = stmt.executeQuery("select issueDate from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                IssueDate = rs.getDate(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return IssueDate;
    }

    Date getDueDate(int loanId) {
        Date DueDate = new Date();
        try {
            ResultSet rs = stmt.executeQuery("select dueDate from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                DueDate = rs.getDate(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return DueDate;
    }

    Books getLoanedBook(int loanId) {
        Books LoanedBook = new Books();
        try {
            ResultSet rs = stmt.executeQuery("select issuedBookId from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                int bookId = rs.getInt(1);
                LoanedBook = getBookById(bookId);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return LoanedBook;
    }

    int getLoaneeId(int loanId) {

        int LoaneeId = -1;
        try {
            ResultSet rs = stmt.executeQuery("select idStudent from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                LoaneeId = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return LoaneeId;
    }

    boolean getLoanReturnedStatus(int loanId) {
        boolean result = false;
        try {
            ResultSet rs = stmt.executeQuery("select returnedStatus from loan where idLoan='" + loanId + "'");
            while (rs.next()) {
                result = rs.getBoolean(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    void setLoanReturnedDate(int loanId, Date Ret_date) {
        int i = 0;
        try {
            java.sql.Date retdate = new java.sql.Date(Ret_date.getTime());
            i = stmt.executeUpdate("UPDATE loan SET returnDate='" + retdate + "'Where idLoan='" + loanId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void setLoanedBook(int loanId, int bookId) {
        int i = 0;
        try {
            i = stmt.executeUpdate("UPDATE loan SET issuedBookId='" + bookId + "'Where idLoan='" + loanId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void setLoaneeObject(int loanId, String borrower) {
        int i = 0;
        try {
            i = stmt.executeUpdate("UPDATE loan SET id='" + borrower + "'Where idLoan='" + loanId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void setReturnStatus(int loanId, boolean status) {
        int i = 0;
        try {
            i = stmt.executeUpdate("UPDATE loan SET returnedStatus='" + status + "'Where idLoan='" + loanId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void setLoanFineStatus(int loanId, String status) {

        int i = 0;
        try {

            i = stmt.executeUpdate("UPDATE loan SET fineStatus='" + status + "'Where idLoan='" + loanId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void setLoan(int loanID, Loan LoanObj) {

        try {
            int loanid = LoanObj.getLoanId();
            Date issueDate = LoanObj.getIssueDate();
            Date dueDate = LoanObj.getDueDate();
            Date return_date = LoanObj.getReturnDate();
            String fineStatus = LoanObj.getFineStatus();
            boolean returnedStatus = LoanObj.getStatus();
            String id = LoanObj.getUserId();
            int bookId = LoanObj.getBookId();

            java.sql.Date issueDate1 = new java.sql.Date(issueDate.getTime());
            java.sql.Date dueDate1 = new java.sql.Date(dueDate.getTime());
            java.sql.Date return_date1 = new java.sql.Date(return_date.getTime());

            stmt.executeUpdate("Update Loan SET issueDate='" + issueDate1 + "',dueDate='" + dueDate1 + "' ,returnDate='" + return_date1 + "' , fineStatus='" + fineStatus + "', returnedStatus= '" + returnedStatus + "',idStudent='" + id + "' ,issuedBookId='" + bookId + "'  Where idLoan=' " + loanid + "'");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    boolean addNewBook(int bookId, String title, String author, int quantity) {

        boolean flag = false;
        try {

            stmt.executeUpdate("Insert into book (idBook, author ,title , quantity) values("+bookId +", '" + author + "','" + title + "','" + quantity + "')");
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

    boolean deleteABook(int bookId) {

        boolean flag = false;
        try {

            stmt.executeUpdate("Delete From book  Where idBook='" + bookId + "'");
            flag = true;

        } catch (Exception e) {
            System.out.println(e);

        }


        return flag;
    }

    ArrayList<Books> searchBookbyTitle(String input) {

        ArrayList<Books> BooksList = new ArrayList<>();

        try {

            ResultSet rs = stmt.executeQuery("select idBook from book where title='" + input + "'");
            while (rs.next()) {
                int bookId = rs.getInt(1);
                Books NewBook = getBookById(bookId);
                BooksList.add(NewBook);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return BooksList;
    }

    ArrayList<Books> searchBookbyAuthor(String input) {

        ArrayList<Books> BooksList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select idBook from book where author='" + input + "'");
            while (rs.next()) {
                int bookId = rs.getInt(1);
                Books NewBook = getBookById(bookId);
                BooksList.add(NewBook);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return BooksList;
    }

    boolean checkStudentId(int ID) {
        boolean flag = false;

        try {
            ResultSet rs = stmt.executeQuery("select * from student where idStudent='" + ID + "'");
            if (rs.next()) {

                flag = true;
            }


        } catch (Exception e) {
            System.out.println(e);
        }

        return flag;

    }

    boolean checkIsBookLoaned(int bookId) {

        boolean flag = false;
        try {
            ResultSet rs = stmt.executeQuery("select * from loan where IssuedBookId='" + bookId + " 'and returnedStatus ='" + 0 + "'");
            if (rs.next()) {
                flag = true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

}
