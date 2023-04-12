package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import java.util.List;

public interface ICRRRDAO {
  public List<ConferenceRoomResRequest> getAllCRRR();

  public ConferenceRoomResRequest getCRRR(String name);

  public void updateCRRR(ConferenceRoomResRequest room);

  public void addCRRR(ConferenceRoomResRequest room);

  public void deleteCRRR(ConferenceRoomResRequest room);
}
