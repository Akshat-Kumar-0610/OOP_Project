package com.mycompany.lib.library;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Library {

    private int issuePeriod;
    private int maxIssues;
    public static ArrayList<Student> studentsList;
    public static ArrayList<Admin> adminsList;
    public static ArrayList<Books> booksList;

    public static ArrayList<Loan> allLoansList;

    Library() {

        dbConnectivity db = new dbConnectivity();
        studentsList = db.loadAllStudents();

        booksList = db.LoadAllBooks();
        adminsList = db.loadAllAdmin();
        allLoansList = db.loadLoanList();

        for (int i = 0; i < studentsList.size(); i++) {
            Student B = studentsList.get(i);
            B.viewInformation(B.getLoans(), B.getUserId());
            System.out.println("\n");

        }


        for (int i = 0; i < booksList.size(); i++) {
            Books B = booksList.get(i);
            B.PrintInformation();
            System.out.println("\n");

        }


        for (int i = 0; i < adminsList.size(); i++) {
            Admin L = adminsList.get(i);
            L.PrintInformation();
            System.out.println("\n");

        }

    }

    boolean isStudentPresent(String id) {
        for (int i = 0; i < studentsList.size(); i++) {
            Student U = studentsList.get(i);
            if (U.getUserId().equals(id)) {
                return true;
            }

        }
        return false;

    }


    boolean IsAdminPresent(String id) {


        for (int i = 0; i < adminsList.size(); i++) {
            Admin L = adminsList.get(i);

            if (L.getUserId().equals(id)) {
                return true;
            }

        }
        return false;

    }

//    Student getStudentFromId(String Id){
//        for (int i =){
//    }

    public String CheckLoanofUser(String userId) {
        String Str = "";
        for (int i = 0; i < studentsList.size(); i++) {
            Student S = studentsList.get(i);
            if (S.getUserId().equals(userId)) {
                Str+=S.viewLoanInformation();
            }
        }
        return Str;


    }

    public ArrayList<Loan> getAllLoansList(){
        return allLoansList;
    }

    public static void addLoan(Loan L){
        allLoansList.add(L);
    }

    ArrayList<Books> AdminSearchBookbyTitle(String title, String adminId) {
        ArrayList<Books> SelectedBooks = new ArrayList<>();

        for (int i = 0; i < adminsList.size(); i++) {

            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                SelectedBooks = C.SearchBookbyTitle(title);
            }


        }

        return SelectedBooks;

    }


    ArrayList<Books> AdminSearchBookbyAuthor(String author, String adminId) {
        ArrayList<Books> SelectedBooks = new ArrayList<>();

        for (int i = 0; i < adminsList.size(); i++) {

            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                SelectedBooks = C.SearchBookbyAuthor(author);
            }


        }

        return SelectedBooks;

    }

//    ArrayList<Books> AdminSearchBookbySubject(String subject, String adminId) {
//        ArrayList<Books> SelectedBooks = new ArrayList<>();
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin C = adminsList.get(i);
//            if (C.getUserId().equals(adminId)) {
//                SelectedBooks = C.SearchBookbySubject(subject);
//            }
//
//
//        }
//
//        return SelectedBooks;
//
//    }

    boolean authenticate(String id, String password, String userType){
        boolean authenticated = false;

        if(userType.equals("ADMIN")){
            for (int i = 0; i< this.adminsList.size(); i++){
                if (adminsList.get(i).getUserId().equals(id) && adminsList.get(i).getPassword().equals(password)) {
                    authenticated = true;
                }
            }
        } else {
            for (int i = 0; i< this.studentsList.size(); i++){
                if (studentsList.get(i).getUserId().equals(id) && studentsList.get(i).getPassword().equals(password)) {
                    authenticated = true;
                }
            }
        }

        return authenticated;
    }

    boolean addNewStudent(String borrower_name, char gender, String adminId, String userId, String password) {
        boolean result = false;
        for (int i = 0; i < adminsList.size(); i++) {
            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                result = C.addStudent(studentsList, borrower_name, Gender.genderFromChar(gender), userId, password);
                break;

            }


        }
        return result;

    }

    boolean register(String borrower_name, char gender, String userId, String password) {
        boolean result = false;
        result = Admin.addStudent(studentsList, borrower_name, Gender.genderFromChar(gender), userId, password);

        return result;

    }

    Boolean AdminUpdatingInfo(String Info, int choice, String adminId, String user_id) {


        boolean result = false;
        for (int i = 0; i < adminsList.size(); i++) {
            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                result = C.updatePerosnalInformation(studentsList, Info, choice, user_id);

                break;

            }


        }
        return result;


    }


//    void AdminRecordFine(String user_id, String adminId) {
//
//        for (int i = 0; i < adminsList.size(); i++) {
//            Admin C = adminsList.get(i);
//            if (C.getUserId().equals(adminId)) {
//                C.PayFine(user_id, studentsList, U.getLoans());
//            }
//
//
//        }
//
//
//    }


    void AdminCheckInItem(String ret_date, int book_id, String borrower_id, String adminId) {

        for (int i = 0; i < adminsList.size(); i++) {

            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                for (int j = 0; j < studentsList.size(); j++) {
                    Student U = studentsList.get(j);
                    if (borrower_id == U.getUserId()) {
                        C.checkInItem(ret_date, book_id, U, booksList, U.getLoans());

                    }

                }

            }

        }
    }


    void AdminCheckOutItem(int book_id, String borrower_id, String adminId) {

        for (int i = 0; i < adminsList.size(); i++) {

            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                for (int j = 0; j < studentsList.size(); j++) {
                    Student U = studentsList.get(j);
                    if (borrower_id == U.getUserId()) {
                        int size = U.getLoans().size();
                        Loan L = U.getLoans().get(size - 1);
                        int index = L.getLoanId();
                        index = index + 1;
                        Loan LoanObj = new Loan(index);
                        C.checkOutItem(book_id, U, booksList, LoanObj, U.getLoans());

                    }

                }

            }

        }
    }


    void AdminRenewItem(int book_id, int option, String adminId) {

        for (int i = 0; i < adminsList.size(); i++) {

            Admin C = adminsList.get(i);
            if (C.getUserId().equals(adminId)) {
                C.RenewItem(book_id, booksList, option);
            }

        }

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    String AdminCheckLoanofUser(String user_id, String adminId) {
//        String Str = "";
//        for (int i = 0; i < adminsList.size(); i++) {
//            Admin L = adminsList.get(i);
//            if (adminId == L.getUserId()) {
//
//                Str = Str + L.viewInformation(Admin.getLoans(), user_id);
//                break;
//
//            }
//
//
//        }
//
//
//        return Str;
//
//
//    }


//    ArrayList<Books> AdminSearchBookbyTitle(String title, String adminId) {
//        ArrayList<Books> SelectedBooks = new ArrayList<>();
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L = adminsList.get(i);
//            if (L.getUserId() == adminId) {
//                SelectedBooks = L.searchBookbyTitle(title);
//            }
//
//
//        }
//
//        return SelectedBooks;
//
//    }


//    ArrayList<Books> AdminSearchBookbyAuthor(String author, String adminId) {
//        ArrayList<Books> SelectedBooks = new ArrayList<>();
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L = adminsList.get(i);
//            if (L.getUserId() == adminId) {
//                SelectedBooks = L.searchBookbyAuthor(author);
//            }
//
//
//        }
//
//        return SelectedBooks;
//
//    }

//    ArrayList<Books> AdminSearchBookbySubject(String subject, String adminId) {
//        ArrayList<Books> SelectedBooks = new ArrayList<>();
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L = adminsList.get(i);
//            if (L.getUserId().equals(adminId)) {
//                SelectedBooks = L.SearchBookbySubject(subject);
//            }
//
//
//        }
//
//        return SelectedBooks;
//
//    }


    boolean AddNewBorrowerAdmin(String borrower_name, char gender, String tel_num, String address, String adminId, String userId, String password) {
        boolean result = false;
        for (int i = 0; i < adminsList.size(); i++) {
            Admin L = adminsList.get(i);
            if (L.getUserId().equals(adminId)) {
                result = L.addStudent(studentsList, borrower_name, Gender.genderFromChar(gender), userId, password);
                result = true;
                break;

            }


        }
        return result;

    }

//
//    Boolean AdminUpdatingInfo(String Info, int choice, String adminId, String user_id) {
//
//
//        boolean result = false;
//        for (int i = 0; i < adminsList.size(); i++) {
//            Admin L = adminsList.get(i);
//            if (L.getUserId().equals(adminId)) {
//                result = L.updatePerosnalInformation(studentsList, Info, choice, user_id);
//
//                break;
//
//            }
//
//
//        }
//        return result;
//
//
//    }

//    void AdminRecordFine(String user_id, String adminId) {
//
//        for (int i = 0; i < adminsList.size(); i++) {
//            Admin L = adminsList.get(i);
//            if (L.getUserId().equals(adminId)) {
//                L.PayFine(user_id, studentsList, U.getLoans());
//            }
//
//
//        }
//
//
//    }

//    void AdminCheckInItem(String ret_date, int book_id, String borrower_id, String lib_id) {
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L = adminsList.get(i);
//            if (L.getUserId() == lib_id) {
//                for (int j = 0; j < studentsList.size(); j++) {
//                    Student U = studentsList.get(j);
//                    if (borrower_id == U.getUserId()) {
//                        L.checkInItem(ret_date, book_id, U, booksList, U.getLoans());
//
//                    }
//
//                }
//
//            }
//
//        }
//    }
//

//    void AdminCheckOutItem(int book_id, String borrower_id, String lib_id) {
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L1 = adminsList.get(i);
//            if (L1.getUserId() == lib_id) {
//                for (int j = 0; j < studentsList.size(); j++) {
//                    Student U = studentsList.get(j);
//                    if (borrower_id == U.getUserId()) {
//                        int size = U.getLoans().size();
//                        Loan L = U.getLoans().get(size - 1);
//                        int index = L.getLoanId();
//                        index = index + 1;
//                        Loan LoanObj = new Loan(index);
//                        L1.checkOutItem(book_id, U, booksList, LoanObj, U.getLoans());
//
//                    }
//
//                }
//
//            }
//
//        }
//    }


//    void AdminRenewItem(int book_id, int option, String lib_id) {
//
//        for (int i = 0; i < adminsList.size(); i++) {
//
//            Admin L = adminsList.get(i);
//            if (L.getUserId().equals(lib_id)) {
//                L.RenewItem(book_id, booksList, option);
//            }
//
//        }
//
//    }

    boolean AdminAddNewBook(String NewAuthor, String NewTitle, int quantity, String adminId) {
        boolean result = false;

        for (int i = 0; i < adminsList.size(); i++) {

            Admin L = adminsList.get(i);
            if (L.getUserId().equals(adminId)) {
                result = L.AddBook(booksList, NewAuthor, NewTitle, quantity);
            }

        }

        return result;


    }


    boolean AdminDeleteBook(int book_id, String lib_id) {
        boolean result = false;

        for (int i = 0; i < adminsList.size(); i++) {

            Admin L = adminsList.get(i);
            if (L.getUserId().equals(lib_id)) {
                result = L.DeleteBook(booksList, book_id);
            }

        }

        return result;
    }


    void AdminUpdateBookInfo(int book_id, String adminId, String NewInfo, int command) {


        for (int i = 0; i < adminsList.size(); i++) {

            Admin L = adminsList.get(i);
            if (L.getUserId().equals(adminId)) {
                L.ChangeInfo(booksList, book_id, command, NewInfo);
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        dbConnectivity db = new dbConnectivity();
        Loan L = new Loan(6);


    }

}
