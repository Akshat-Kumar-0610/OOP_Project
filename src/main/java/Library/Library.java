package Library;

import java.util.ArrayList;

public class Library implements SuperUserMethods, Search{

    private static int issuePeriod;
    private static int maxIssues;

    private static double finePerDayPerBook;
    private static ArrayList<Student> studentsList;
    private static ArrayList<Admin> adminsList;
    private static ArrayList<Books> booksList;
    private static ArrayList<Loan> allLoansList;

    ///////////////////
    // Constructors //
    /////////////////
    Library() {
        DBConnection db = new DBConnection();
        studentsList = db.loadAllStudents();
        booksList = db.loadAllBooks();
        adminsList = db.loadAllAdmin();
        allLoansList = db.loadLoanList();
        finePerDayPerBook = 2.00;
        maxIssues = 3;
        issuePeriod = 15;
    }

    /////////////
    // Getters//
    ////////////
    public static int getIssuePeriod(){
        return issuePeriod;
    }

    public static int getMaxIssues(){
        return maxIssues;
    }

    public static double getFinePerDayPerBook(){
        return finePerDayPerBook;
    }

    public static ArrayList<Loan> getAllLoansList(){
        return allLoansList;
    }

    public static ArrayList<Student> getStudentsList() {
        return studentsList;
    }

    public static ArrayList<Admin> getAdminsList() {
        return adminsList;
    }

    public static ArrayList<Books> getBooksList() {
        return booksList;
    }


    boolean isstudentpresent(String id) {
        for (int i = 0; i < studentsList.size(); i++) {
            Student U = studentsList.get(i);
            if (U.getUserId().equals(id)) {
                return true;
            }

        }
        return false;
    }

    boolean isAdminPresent(String id) {
        for (int i = 0; i < adminsList.size(); i++) {
            Admin L = adminsList.get(i);
            if (L.getUserId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String getLoansOfUserAsString(String userId) {
        String Str = "";
        for (int i = 0; i < studentsList.size(); i++) {
            Student S = studentsList.get(i);
            if (S.getUserId().equals(userId)) {
                Str+=S.getLoanInformationAsString();
            }
        }
        return Str;
    }

    public static void addLoan(Loan L){
        allLoansList.add(L);
    }

    boolean authenticate(String id, String password, String userType){
        boolean authenticated = false;

        switch (userType){
            case "ADMIN": {
                for (int i = 0; i< this.adminsList.size(); i++){
                    if (adminsList.get(i).getUserId().equals(id) && adminsList.get(i).getPassword().equals(password)) {
                        authenticated = true;
                    }
                }
                break;
            }
            case "STUDENT": {
                for (int i = 0; i< this.studentsList.size(); i++){
                    if (studentsList.get(i).getUserId().equals(id) && studentsList.get(i).getPassword().equals(password)) {
                        authenticated = true;
                    }
                }
                break;
            }
        }
        return authenticated;
    }

    boolean register(String borrower_name, char gender, String userId, String password) {
        boolean result = false;
        result = Admin.addStudent(studentsList, borrower_name, Gender.genderFromChar(gender), userId, password);

        return result;

    }
}
