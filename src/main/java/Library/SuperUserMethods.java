package Library;

public interface SuperUserMethods {

    public default boolean addNewStudent(String borrower_name, char gender, String adminId, String userId, String password) {
        boolean result = false;
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin C = Library.getAdminsList().get(i);
            if (C.getUserId().equals(adminId)) {
                result = C.addStudent(Library.getStudentsList(), borrower_name, Gender.genderFromChar(gender), userId, password);
                break;
            }
        }
        return result;
    }

    public default Boolean updateStudentInformation(String Info, int choice, String adminId, String user_id) {
        boolean result = false;
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin C = Library.getAdminsList().get(i);
            if (C.getUserId().equals(adminId)) {
                result = C.updateStudentInformation(Library.getStudentsList(), Info, choice, user_id);
                break;
            }
        }
        return result;
    }

    public default boolean addNewStudent(String borrower_name, char gender, String tel_num, String address, String adminId, String userId, String password) {
        boolean result = false;
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin L = Library.getAdminsList().get(i);
            if (L.getUserId().equals(adminId)) {
                result = L.addStudent(Library.getStudentsList(), borrower_name, Gender.genderFromChar(gender), userId, password);
                result = true;
                break;
            }
        }
        return result;
    }

    public default boolean addNewBook(String NewAuthor, String NewTitle, int quantity, String adminId) {
        boolean result = false;
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin L = Library.getAdminsList().get(i);
            if (L.getUserId().equals(adminId)) {
                result = L.addBook(Library.getBooksList(), NewAuthor, NewTitle, quantity);
            }
        }
        return result;
    }

    public default boolean deleteBook(int book_id, String lib_id) {
        boolean result = false;
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin L = Library.getAdminsList().get(i);
            if (L.getUserId().equals(lib_id)) {
                result = L.deleteBook(Library.getBooksList(), book_id);
            }
        }
        return result;
    }

    public default void updateBookInformation(int book_id, String adminId, String NewInfo, int command) {
        for (int i = 0; i < Library.getAdminsList().size(); i++) {
            Admin L = Library.getAdminsList().get(i);
            if (L.getUserId().equals(adminId)) {
                L.updateBookInformation(Library.getBooksList(), book_id, command, NewInfo);
            }
        }
    }
}
