/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Library;

public class Books {

    private int book_id;
    private String author;
    private String title;
    private int quantity; //a book may have more than one quantity

    ///////////////////
    // Constructors //
    /////////////////
    Books() {
        this.book_id = -1;
        this.author = "  ";
        this.title = "  ";
        this.quantity = 0;
    }

    Books(int book_id, String author, String title, int quantity) {
        this.book_id = book_id;
        this.author = author;
        this.title = title;
        this.quantity = quantity;
    }

    //////////////
    // Getters //
    ////////////
    public String getTitle() {
        DBConnection db = new DBConnection();
        String titleofbook = db.getTitleOfBook(this.book_id);
        return titleofbook;
    }

    public String getAuthor() {
        DBConnection db = new DBConnection();
        String authorofbook = db.getAuthorOfBook(this.book_id);
        return authorofbook;
    }

    public int getBookId() {
        return this.book_id;
    }

    public int getQuantity() {
        DBConnection db = new DBConnection();
        int quantity_available = db.getQuantityofBookRemaining(this.book_id);
        return quantity_available;

    }

    //////////////
    // Setters //
    /////////////
    public void setBookId(int book_id) {
        this.book_id = book_id;

    }

    public void setAuthor(String author) {
        this.author = author;
        DBConnection db = new DBConnection();
        db.changeBookInfo(book_id, author, 2);
    }

    public void setTitle(String title) {
        this.title = title;
        DBConnection db = new DBConnection();
        db.changeBookInfo(book_id, title, 1);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        DBConnection db = new DBConnection();
        db.updateBookQuantity(this.quantity, this.book_id);
    }

    public static  boolean checkAvailability(int book_id) {
        DBConnection db = new DBConnection();
        int quantityAvailable = db.getQuantityofBookRemaining(book_id);
        if (quantityAvailable <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getInformationAsString() {
        boolean available = checkAvailability(this.book_id);
        String status;
        if (available == true) {
            status = "available";
        } else {
            status = "not available";
        }
        String Resultant = " ";
        Resultant = Resultant + "    " + this.book_id + "\t" + this.title + "\t" + this.author + "\t" + "\t" + this.quantity + "\t" + status + "\n";
        return Resultant;
    }

}
