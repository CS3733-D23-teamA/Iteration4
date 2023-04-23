package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;

public enum CRRRSingleton implements IRequestSingleton<ConferenceRoomResRequest> {
  INSTANCE;
  // example of how attributes are added to the Enum
  ConferenceRoomResRequest crrr;

  @Override
  public ConferenceRoomResRequest getValue() {
    return crrr;
  }

  @Override
  public void setValue(ConferenceRoomResRequest obj) {
    this.crrr = obj;
  }
}
