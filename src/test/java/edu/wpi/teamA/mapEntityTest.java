package edu.wpi.teamA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class mapEntityTest {
  private NodeDAOImp nodeDAO = new NodeDAOImp();
  MapEditorEntity MEEntity = new MapEditorEntity();
  HashMap<Integer, Node> testNodeMap = new HashMap<Integer, Node>();

  // L1
  Node node1 = new Node(100, 2265, 904, "L1", "45 Francis");

  Node node2 = new Node(110, 2360, 849, "L1", "45 Francis");
  Node node3 = new Node(115, 2130, 904, "L1", "45 Francis");

  // L2
  Node node4 = new Node(105, 2130, 849, "L2", "Tower");
  Node node5 = new Node(140, 2300, 854, "L2", "45 Francis");
  Node node6 = new Node(210, 2015, 1280, "L2", "45 Francis");
  Node node19 = new Node(1300, 1667, 2682, "L2", "BTM");

  // 1
  Node node7 = new Node(640, 2190, 910, "1", "45 Francis");
  Node node8 = new Node(645, 2240, 910, "1", "45 Francis");
  Node node9 = new Node(1160, 1638, 2553, "1", "BTM");
  Node node10 = new Node(2905, 1810, 1280, "1", "Tower");

  // 2
  Node node11 = new Node(215, 4550, 375, "2", "15 Francis");
  Node node12 = new Node(245, 3635, 830, "2", "15 Francis");
  Node node13 = new Node(420, 4390, 605, "2", "15 Francis");
  Node node14 = new Node(425, 4385, 585, "2", "15 Francis");
  Node node15 = new Node(1165, 1591, 2560, "2", "BTM");

  // 3
  Node node16 = new Node(1170, 1534, 2732, "3", "BTM");
  Node node17 = new Node(2265, 1580, 1858, "3", "Shapiro");
  Node node18 = new Node(2270, 1685, 2052, "3", "Shapiro");
  Node node20 = new Node(2815, 2715, 1075, "3", "45 Francis");

  @BeforeEach
  protected void setUp() throws Exception {
    System.out.println("Setting it up!");
    NodeDAOImp nodeDAO = new NodeDAOImp();
    testNodeMap.put(node1.getNodeID(), node1);
    testNodeMap.put(node2.getNodeID(), node2);
    testNodeMap.put(node3.getNodeID(), node3);
    testNodeMap.put(node4.getNodeID(), node4);
    testNodeMap.put(node5.getNodeID(), node5);
    testNodeMap.put(node6.getNodeID(), node6);
    testNodeMap.put(node7.getNodeID(), node7);
    testNodeMap.put(node8.getNodeID(), node8);
    testNodeMap.put(node9.getNodeID(), node9);
    testNodeMap.put(node10.getNodeID(), node10);
    testNodeMap.put(node11.getNodeID(), node11);
    testNodeMap.put(node12.getNodeID(), node12);
    testNodeMap.put(node13.getNodeID(), node13);
    testNodeMap.put(node14.getNodeID(), node14);
    testNodeMap.put(node15.getNodeID(), node15);
    testNodeMap.put(node16.getNodeID(), node16);
    testNodeMap.put(node17.getNodeID(), node17);
    testNodeMap.put(node18.getNodeID(), node18);
    testNodeMap.put(node19.getNodeID(), node19);
    testNodeMap.put(node20.getNodeID(), node20);
  }

  //  @Test
  //  public void testGetFloorNodes() {
  //    ArrayList<Node> L2FloorArrayExpected = new ArrayList<Node>();
  //    L2FloorArrayExpected.add(node4);
  //    L2FloorArrayExpected.add(node5);
  //    L2FloorArrayExpected.add(node6);
  //    L2FloorArrayExpected.add(node19);
  //    ArrayList<Node> L2FloorArrayActual = MEEntity.getFloorNodes(testNodeArray, "L2");
  //    assertEquals(L2FloorArrayExpected, L2FloorArrayActual);
  //
  //    ArrayList<Node> GFloorArrayExpected = new ArrayList<Node>();
  //    ArrayList<Node> GFloorArrayActual = MEEntity.getFloorNodes(testNodeArray, "G");
  //    assertEquals(GFloorArrayExpected, GFloorArrayActual);
  //  }
  //
  //  @Test
  //  public void testGetFloorEdges() {
  //    //
  //  }

  @Test
  public void testLoadFloorNodes() {
    HashMap<Integer, Node> L2FloorArrayExpected = new HashMap<Integer, Node>();
    L2FloorArrayExpected.put(node4.getNodeID(), node4);
    L2FloorArrayExpected.put(node5.getNodeID(), node5);
    L2FloorArrayExpected.put(node6.getNodeID(), node6);
    L2FloorArrayExpected.put(node19.getNodeID(), node19);

    MEEntity.loadFloorNodes();
    assertEquals(
        L2FloorArrayExpected.get(node4.getNodeID()),
        MEEntity.getLevelL2NodeMap().get(node4.getNodeID()));
    assertEquals(
        L2FloorArrayExpected.get(node5.getNodeID()),
        MEEntity.getLevelL2NodeMap().get(node5.getNodeID()));
    assertEquals(
        L2FloorArrayExpected.get(node6.getNodeID()),
        MEEntity.getLevelL2NodeMap().get(node6.getNodeID()));
    assertEquals(
        L2FloorArrayExpected.get(node19.getNodeID()),
        MEEntity.getLevelL2NodeMap().get(node19.getNodeID()));
  }

  @Test
  public void testLoadFloorEdges() {
    // TODO
  }

  //    @Test
  //    public void testDetermineArray() {
  //      assertArrayEquals(
  //          MEEntity.getLevelL1NodeArray().toArray(),
  //          MEEntity.determineNodeArray("Level L1").toArray());
  //      assertArrayEquals(
  //          MEEntity.getLevelL2NodeArray().toArray(),
  //          MEEntity.determineNodeArray("Level L2").toArray());
  //      assertArrayEquals(
  //          MEEntity.getLevel1NodeArray().toArray(), MEEntity.determineNodeArray("Level
  //   1").toArray());
  //      assertArrayEquals(
  //          MEEntity.getLevel2NodeArray().toArray(), MEEntity.determineNodeArray("Level
  //   2").toArray());
  //      assertArrayEquals(
  //          MEEntity.getLevel3NodeArray().toArray(), MEEntity.determineNodeArray("Level
  //   3").toArray());
  //    }

  @Test
  public void testAddCircle() {
    double x = 230.45;
    double y = 154.33;
    double r = 10;
    Circle expected_c = new Circle(x, y, r);
    expected_c.setFill(Color.web("0x012D5A"));
    Circle actual_c = MEEntity.addCircle(x, y);

    assertEquals(expected_c.getCenterX(), actual_c.getCenterX());
    assertEquals(expected_c.getCenterY(), actual_c.getCenterY());
    assertEquals(expected_c.getFill(), actual_c.getFill());
    assertEquals(expected_c.getRadius(), actual_c.getRadius());
  }

  @Test
  public void testGetNodeInfo() {
    int n1ID = node1.getNodeID();
    Node actual_node = MEEntity.getNodeInfo(n1ID);

    assertEquals(node1.getNodeID(), actual_node.getNodeID());
    assertEquals(node1.getXcoord(), actual_node.getXcoord());
    assertEquals(node1.getYcoord(), actual_node.getYcoord());
    assertEquals(node1.getBuilding(), actual_node.getBuilding());
    assertEquals(node1.getFloor(), actual_node.getFloor());
  }
  /*
  @Test
  public void testGetLocationName() {
    int nodeID = node1.getNodeID();
    String longName = "taxi stand";
    String shortName = "taxi";
    String nodeType = "HALL";
    LocalDate localDate = new LocalDate(2023, 1, 1);
    Move move = new Move(nodeID, longName, localDate);
    LocationName locName_expected = new LocationName(longName, shortName, nodeType);

    //LocationName(longName, shortName, nodeType)
  }*/
}
