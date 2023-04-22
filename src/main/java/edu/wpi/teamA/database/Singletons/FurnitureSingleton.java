package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;

public enum FurnitureSingleton  implements IRequestSingleton<FurnitureRequest>{
    INSTANCE;
    // example of how attributes are added to the Enum
    FurnitureRequest f;

    @Override
    public FurnitureRequest getValue() {
        return f;
    }

    @Override
    public void setValue(FurnitureRequest obj) {
        this.f = obj;
    }
}
