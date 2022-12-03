package Library;

import java.util.ArrayList;

public class Admin extends User implements Search, SuperUserMethods{

    ///////////////////
    // Constructors //
    /////////////////
    Admin(String userId, String password, String user_name, Gender g) {
        super(userId, password, user_name, g);
    }

    public static boolean addStudent(ArrayList<Student> studentList, String studentName, Gender studentGender, String studentId, String password) {
        Student NewBorrower = new Student(studentId, password, studentName, studentGender);
        studentList.add(NewBorrower);

        DBConnection db = new DBConnection();
        boolean result = db.addStudent(studentId, password, studentName, studentGender);
        return result;
    }

    public boolean updateStudentInformation(ArrayList<Student> studentList, String Info, int command, String userId) {
        boolean result = false;
        for (int i = 0; i < studentList.size(); i++) {
            Student B = studentList.get(i);
            if (B.getUserId().equals(userId)) {
                if (command == 1) {
                    B.setName(Info);
                    result = true;
                } else if (command == 2) {
                    B.setGender(Info.charAt(0));
                    result = true;
                } else if (command == 3) {
                    B.setuserId(Info);
                    result = true;
                }
                else {
                    System.out.println("You have pressed an invalid command");
                    result = false;

                }
            }
        }
        return result;
    }

    public boolean addBook(ArrayList<Books> BooksList, String NewAuthor, String NewTitle, int quantity) {
        boolean added = false;
        Books LastBook = BooksList.get(BooksList.size()-1);
        int book_id = LastBook.getBookId();
        book_id += 1;

        Books NewBook = new Books(book_id, NewTitle, NewAuthor, quantity);
        DBConnection db = new DBConnection();
        boolean result = db.addNewBook(book_id, NewTitle, NewAuthor, quantity);
        if (result == true){
            added = true;
        }
        BooksList.add(NewBook);
        return added;
    }

    public boolean deleteBook(ArrayList<Books> BooksList, int book_id) {
        boolean deleted = false;
        Books ToDelete = new Books();
        DBConnection db = new DBConnection();
        boolean result = db.deleteABook(book_id);
        if (result == true) {
            for (Books B : BooksList) {
                if (B.getBookId() == book_id) {

                    ToDelete = B;
                    break;
                }
            }

            BooksList.remove(ToDelete);
            deleted = true;
        }
        return deleted;
    }

    public void updateBookInformation(ArrayList<Books> BooksList, int book_id, int command, String NewInfo) {
        for (Books b : BooksList) {
            if (b.getBookId() == book_id) {
                if (command == 1) {
                    b.setTitle(NewInfo);
                } else if (command == 2) {
                    b.setAuthor(NewInfo);
                } else if (command == 3) {
                    b.setQuantity(Integer.parseInt(NewInfo));
                } else {
                    System.out.print("You pressed invalid key ! Can't perform any operation ");

                }
            }

        }

    }
}
