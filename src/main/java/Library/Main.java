package Library;

import java.text.ParseException;
import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("lol");
        File inputFile = new File("resources\\input.txt");
        File outputFile = new File("resources\\output.txt");

        BufferedReader brInput = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bwOutput = new BufferedWriter(new FileWriter(outputFile));

        DBConnection db = new DBConnection();
        Library lib = new Library();
        int t = Integer.parseInt(brInput.readLine());
        while(t-->0){
            String name = brInput.readLine();
            String studentId = brInput.readLine();
            int bookId = Integer.parseInt(brInput.readLine());
            String date = brInput.readLine();
            int operation = Integer.parseInt(brInput.readLine());
            Date dateObj = new SimpleDateFormat("ddmmyyyy").parse(date);
            boolean result = Library.register(name, studentId);
            lib.addNewBook("bookAuthor"+bookId, "bookTitle"+bookId, 1, Library.getAdminsList().get(0).getUserId());
            Student studObject = new Student();
            if(!result){
                bwOutput.write("Error: Error occured while registering user to the database.");
                bwOutput.close();
                brInput.close();
                return;
            } else {
                studObject = Library.getStudentObjectFromUserID(studentId);
                bwOutput.write("Student registered with following details: " +studObject.getInformationAsString()+'\n');
            }
            switch (operation){
                case 1: {
                    String res = Loan.issueBook(bookId, studentId);
                    bwOutput.write(res);
                    bwOutput.write('\n');
                    break;
                }
                case 2: {
                    String res = Loan.returnBook(bookId, studentId, dateObj);
                    bwOutput.write(res);
                    bwOutput.write('\n');
                    break;
                }
                case 3: {
                    String res = Loan.renewBook(bookId, studentId, dateObj);
                    bwOutput.write(res);
                    bwOutput.write('\n');
                    break;
                } default: bwOutput.write("Invalid input provided.\n");
            }


        }
        bwOutput.close();
    }
}
