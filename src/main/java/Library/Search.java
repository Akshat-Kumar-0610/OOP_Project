package Library;

import java.util.ArrayList;

public interface Search {

    public default ArrayList<Books> searchBookByAuthor(String author) {
        ArrayList<Books> BooksList = new ArrayList<>();
        DBConnection db = new DBConnection();
        BooksList = db.searchBookbyAuthor(author);
        return BooksList;
    }

    public default ArrayList<Books> searchBookByTitle(String title) {
        ArrayList<Books> BooksList = new ArrayList<>();
        DBConnection db = new DBConnection();
        BooksList = db.searchBookbyTitle(title);
        return BooksList;
    }
}
