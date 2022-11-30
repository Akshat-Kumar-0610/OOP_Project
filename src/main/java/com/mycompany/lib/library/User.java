package com.mycompany.lib.library;

import java.util.ArrayList;

enum Gender {
    MALE,
    FEMALE;

    public static Gender genderFromChar(char c) {
        if (c == 'f' || c == 'F') {
            return Gender.FEMALE;
        } else {
            return Gender.MALE;
        }
    }
}

abstract class User {
    private String userId;
    private String name;
    private Gender gender;
    private String password;

    User() {
        this.userId = "";
        this.name = " ";
        this.password = " ";
        this.gender = Gender.MALE;
    }

    User(String userId, String password, String name, Gender gender) {
        this.password = password;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
    }

    public ArrayList<Books> SearchBookbyTitle(String title) {
//        for (Books b : BooksList) {
//            String titleofcurrentbook = b.GetTitle();
//            if (titleofcurrentbook.contains(title) == true) {
//                b.PrintInformation();
//            } else {
//                System.out.println("Sorry ! No record found \n");
//
//            }
//        }
        ArrayList<Books> BooksList = new ArrayList<>();
        dbConnectivity db = new dbConnectivity();
        BooksList = db.searchBookbyTitle(title);
//        if (BooksList.isEmpty() == false) {
//            for (Books b : BooksList) {
//
//                b.PrintInformation();
//            }
//        } else {
//            System.out.println("Sorry ! No record found \n");
//
//        }
        return BooksList;
    }

    public ArrayList<Books> SearchBookbyAuthor(String author) {

        ArrayList<Books> BooksList = new ArrayList<>();
        dbConnectivity db = new dbConnectivity();
        BooksList = db.searchBookbyAuthor(author);

        return BooksList;
    }

    public String getUserId() {
        return this.userId;

    }

    public String getName() {
        return this.name;

    }

    public String getPassword(){
        return  this.password;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setuserId(String newUserId) {

        dbConnectivity db = new dbConnectivity();
        boolean result = db.SetUserId(userId, newUserId);
        this.userId = newUserId;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(char g) {
        if (g == 'M') {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.FEMALE;
        }
        dbConnectivity db = new dbConnectivity();
        boolean result = db.SetGender(userId, g);
    }

    public void setGender(Gender g) {
        this.gender = g;
        dbConnectivity db = new dbConnectivity();
        boolean result = db.SetGender(userId, g.toString().charAt(0));
    }

    public String PrintInformation() {
        return "userId: " + this.userId + "   " + "Name: " + this.name + " gender: " + this.gender;

    }

    public boolean checkPassword(String check) {
        if(check.equals(this.password)){
            return true;
        }else{
            return false;
        }
    }

    void SetFineAmount(double fine) {
        dbConnectivity db = new dbConnectivity();
        db.SetFineAmount(userId, fine);
    }

    boolean SetFineStatus(boolean status) {
        dbConnectivity db = new dbConnectivity();
        boolean result = db.setFineStatus(userId, status);
        return result;
    }

    void UpdateLoanInfo(Loan L, int i) {
    }

    boolean GetFineStatus() {
        return true;
    }

    boolean AddLoanInfo(Loan Current_Loan) {
        return true;
    }

    abstract String viewInformation(ArrayList<Loan> LoanLoanList, String userId);

}
