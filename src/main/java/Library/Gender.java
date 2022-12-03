package Library;

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
