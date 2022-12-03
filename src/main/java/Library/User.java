package Library;

abstract class User implements Search{
    private String userId;
    private String name;
    private Gender gender;
    private String password;

    ///////////////////
    // Constructors //
    /////////////////
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

    //////////////
    // Getters //
    ////////////
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

    //////////////
    // Setters //
    /////////////
    public void setuserId(String newUserId) {
        DBConnection db = new DBConnection();
        boolean result = db.setUserId(userId, newUserId);
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
        DBConnection db = new DBConnection();
        boolean result = db.setGender(userId, g);
    }

    public void setGender(Gender g) {
        this.gender = g;
        DBConnection db = new DBConnection();
        boolean result = db.setGender(userId, g.toString().charAt(0));
    }

    public String getInformationAsString() {
        return "userId: " + this.userId + "   " + "Name: " + this.name + " gender: " + this.gender;

    }

    public boolean checkPassword(String check) {
        if(check.equals(this.password)){
            return true;
        }else{
            return false;
        }
    }
}
