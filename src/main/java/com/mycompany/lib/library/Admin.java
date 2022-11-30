package com.mycompany.lib.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin extends User {

    Admin(String userId, String password, String user_name, Gender g) {
        super(userId, password, user_name, g);
    }

    public boolean checkOutItem(int book_id, Student current_borrower, ArrayList<Books> BooksList, Loan Current_Loan, ArrayList<Loan> LoanList) {

        if (current_borrower.GetFineStatus() == true) {
            System.out.println("You are fine defaulter Can't get any more loans");
            return false;
        } else {
            Books loaned_book;
            for (Books b : BooksList) {
                if (b.GetBookId() == book_id) {
                    RenewItem(book_id, BooksList, 2);
                    Current_Loan.setBook(b);
                    Current_Loan.setUser(current_borrower);
                    current_borrower.AddLoanInfo(Current_Loan);
                    LoanList.add(Current_Loan);
                    dbConnectivity db = new dbConnectivity();
                    return (db.addNewLoan(Current_Loan));


                }
            }

            return false;

        }
    }

    public boolean checkInItem(String ret_date, int book_id, User current_borrower, ArrayList<Books> BooksList, List<Loan> LoanList) {

//        Iterator<Loan> itr = LoanList.listIterator(LoanList.size());
//        while (itr.hasPrevious()) {
        for (int counter = LoanList.size() - 1; counter >= 0; counter--) {
            Loan L = LoanList.get(counter);
            //  Loan L = itr.previous();

            if ((L.getBookId() == book_id) && (L.getUserId() == current_borrower.getUserId()) && L.getStatus() == false) {

                L.SetReturnStatus(true);
                String expectedPattern = "yyyy/MM/dd";
                SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);

                String return_date = ret_date;
                Date date = new Date();
                try {
                    date = formatter.parse(return_date);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                L.setReturnedDate(date);
                double user_fine = L.CalculateFine();
                if (user_fine > 0.0) {
                    L.SetFineStatus("Fined");
                    current_borrower.SetFineStatus(true);
                    current_borrower.SetFineAmount(user_fine);

                    RenewItem(book_id, BooksList, 1);
                    current_borrower.UpdateLoanInfo(L, book_id);
                    return true;

                }

            }

        }
        return false;
    }

    @Override
    public String viewInformation(ArrayList<Loan> LoanList, String userId) {
        String Result = "  ";
        for (int i = 0; i < LoanList.size(); i++) {
            Loan l = LoanList.get(i);
            String currentborrower = l.getUserId();
            if (/*(l.GetStatus() == false) &&*/ currentborrower == userId) {
                User U = l.getUser();
                Result = Result + U.PrintInformation();
                Books LoanedBook = l.getaBook();
                Result = Result + LoanedBook.PrintInformation();
            }
        }

        return Result;
    }

    public static boolean addStudent(ArrayList<Student> studentList, String studentName, Gender studentGender, String studentId, String password) {
        Student NewBorrower = new Student(studentId, password, studentName, studentGender);
        ;
        studentList.add(NewBorrower);

        dbConnectivity db = new dbConnectivity();
        boolean result = db.addStudent(studentId, password, studentName, studentGender);
        return result;

    }

    public boolean updatePerosnalInformation(ArrayList<Student> studentList, String Info, int command, String userId) {
        boolean result = false;
        for (int i = 0; i < studentList.size(); i++) {

            Student B = studentList.get(i);
            if (B.getUserId().equals(userId)) {

//                System.out.println("Press 1 to change name \n  Press 2 to change gender");
//                {

//                    int command = input.nextInt();
                if (command == 1) {

//                        System.out.println("Enter the new name of user");
//                        String borrowers_name = input.next();
                    B.SetName(Info);
                    result = true;
                } else if (command == 2) {
//                        System.out.println("Enter the gender of user");
//                        char borrowers_gender = input.next().charAt(0);
                    B.setGender(Info.charAt(0));
                    result = true;
                } else if (command == 3) {
//                        System.out.println("Enter the gender of user");
//                        char borrowers_gender = input.next().charAt(0);
                    B.setuserId(Info);
                    result = true;
//                    } else if (command == 3) {
////                        System.out.println("Enter the telephone");
////                        String TelNum = input.next();
//
//                        B.SetTelephone(Info);
//                    } else if (command == 4) {
////                        System.out.println("Enter the address");
////                        String Address = input.next();
//                        B.SetAddress(Info);
//                    } else {
                }
                else {
                    System.out.println("You have pressed an invalid command");
                    result = false;

                }
//                }
            }
        }
        return result;
    }

    public void RenewItem(int book_id, ArrayList<Books> BooksList, int type) {
        for (Books b : BooksList) {
            if (b.GetBookId() == book_id) {
                if (type == 1) {
                    b.IncreaseQuantity();
                } else {
                    b.DecreaseQuantity();
                }
                break;

            }

        }
    }

    void PayFine(String userId, ArrayList<User> studentList, ArrayList<Loan> loanList) {

        //   Iterator<Loan> itr = LoanList.Iterator(LoanList.size());
        //   while (itr.hasPrevious()) {
        for (int counter = loanList.size() - 1; counter >= 0; counter--) {
            Loan L = loanList.get(counter);
            String F = L.getFineStatus();
            if (L.getUserId() == userId && F.equals("Fined") == true) {
                L.SetFineStatus("Paid");
                for (User b : studentList) {
                    int book_id = L.getBookId();
                    if (b.getUserId().equals(userId)) {
                        b.SetFineAmount(0);
                        b.SetFineStatus(false);
                        b.UpdateLoanInfo(L, book_id);
                        break;
                    }
                }
            }

        }

    }

    public boolean AddBook(ArrayList<Books> BooksList, String NewAuthor, String NewTitle, int quantity) {

        boolean added = false;
        Books LastBook = BooksList.get(BooksList.size()-1);
        int book_id = LastBook.GetBookId();
        book_id += 1;

        Books NewBook = new Books(book_id, NewTitle, NewAuthor, quantity);
        dbConnectivity db = new dbConnectivity();
        boolean result = db.addNewBook(book_id, NewTitle, NewAuthor, quantity);
        if (result == true){
            added = true;
        }
        BooksList.add(NewBook);
        return added;


    }

    public boolean DeleteBook(ArrayList<Books> BooksList, int book_id) {

        boolean deleted = false;
        Books ToDelete = new Books();
        dbConnectivity db = new dbConnectivity();
        boolean result = db.deleteABook(book_id);
        if (result == true) {
            for (Books B : BooksList) {
                if (B.GetBookId() == book_id) {

                    ToDelete = B;
                    break;
                }
            }

            BooksList.remove(ToDelete);
            deleted = true;
        }


        return deleted;
    }

    public void ChangeInfo(ArrayList<Books> BooksList, int book_id, int command, String NewInfo) {

//        System.out.print("Enter the id of the book you want to update");
//        Scanner input = new Scanner(System.in);
//        int book_id = input.nextInt();
        for (Books b : BooksList) {
            if (b.GetBookId() == book_id) {

//                System.out.print("Press \n 1.to change title \n 2. to change author \n 3.to change subject \n 4.to change quantity");
//
//                int command = input.nextInt();
                if (command == 1) {
//                    System.out.print("Enter the  new title ");
//                    String Title = input.next();
                    b.SetTitle(NewInfo);
                } else if (command == 2) {
//                    System.out.print("Enter the new  author name ");
//                    String A_name = input.next();
                    b.SetAuthor(NewInfo);
                } else if (command == 3) {
//                    System.out.print("Enter the new  quantity  ");
//                    int quantity = input.nextInt();
                    b.SetQuantity(Integer.parseInt(NewInfo));

                } else {
                    System.out.print("You pressed invalid key ! Can't perform any operation ");

                }
            }

        }

    }
}
