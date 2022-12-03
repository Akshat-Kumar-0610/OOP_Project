package Library;

import java.util.ArrayList;

public class Student extends User implements Search{

    private boolean fineDefaulter;
    private double fine;
    private ArrayList<Loan> loans;

    ///////////////////
    // Constructors //
    /////////////////
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

    //////////////
    // Getters //
    ////////////
    public boolean getFineDefaulterStatus() {
//        return this.fine_defaulter;
        DBConnection db = new DBConnection();
        return (db.getFineStatus(this.getUserId()));
    }

    public double getFineAmount() {
//        return this.fine;
        DBConnection db = new DBConnection();
        return (db.getFineAmount(this.getUserId()));
    }

    public ArrayList<Loan> getLoans() {
        return this.loans;
    }

    public String getLoanInformationAsString() {
        String Resultant = "";
        if (loans.isEmpty() == false) {
            for (int i = 0; i < loans.size(); i++) {
                Loan L = loans.get(i);
                Resultant += L.getLoanInfoAsString();
                Resultant += "\n";
            }
        } else {

            Resultant += "NO LOANS TILL YET!";
        }

        return Resultant;

    }

    //////////////
    // Setters //
    /////////////
    public boolean setFineDefaulterStatus(boolean fineDefaulter) {
        this.fineDefaulter = fineDefaulter;
        DBConnection db = new DBConnection();
        boolean result = db.setFineStatus(this.getUserId(), fineDefaulter);
        return result;
    }

    public void setFineAmount(double user_fine) {
        this.fine = user_fine;
        DBConnection db = new DBConnection();
        boolean result = db.setFineAmount(this.getUserId(), user_fine);
    }

    public void setLoans(ArrayList<Loan> loans) {
        this.loans = loans;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        DBConnection db = new DBConnection();
        boolean result = db.setName(this.getUserId(), name);
    }

    public void loadAllLoansofUserFromDatabase() {
        DBConnection db = new DBConnection();
        this.loans = db.loadLoanListofSpecificUser(this.getUserId());
    }
}
