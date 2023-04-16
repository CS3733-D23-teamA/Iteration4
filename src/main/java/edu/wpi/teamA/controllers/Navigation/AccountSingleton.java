package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.ORMclasses.User;

public enum AccountSingleton {
    INSTANCE1;
    // example of how attributes are added to the Enum
    User user1;
    public User getValue() {
        return user1;
    }
    public void setValue(User user1) {
        this.user1 = user1;
    }
}
