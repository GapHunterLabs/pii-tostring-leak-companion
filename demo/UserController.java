class User {
    String email;
    String name;

    @Override
    public String toString() {
        return "User{name=" + name + ", email=" + email + "}";
    }

    String getEmail() {
        return email;
    }
}

class UserController {

    private static final Logger log = null;

    // Flagged: user's toString() exposes email, which never appears
    // textually at this call site.
    void handleUnsafe(User user) {
        log.info(user);
    }

    // Not flagged: explicit field selection.
    void handleSafe(User user) {
        log.info(user.getEmail());
    }
}

interface Logger {
    void info(Object o);
}
