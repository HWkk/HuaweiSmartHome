package test;

import homeassistant.Caller;
import homeassistant.EntityName;
import stateautomaton.graph.OuterGraph;

public class Test {

//    public static void main(String[] args) {
//
//        String dir = "/Users/kk/Downloads/";
//        String specificationFile = "specification.txt";
//        String logFile = "log.txt";
//
//        InputUtils.readSpecificationFile(dir + specificationFile);
//        List<Data> data = InputUtils.readLogFile(dir + logFile);
//
//        OuterGraph graph = new OuterGraph();
//        graph.initStates();
//        graph.buildGraph(data);
//        graph.print();
//    }

    public static void main(String[] args) throws InterruptedException {

//        String deviceName = EntityName.AIR_HUMIDIFIER_NAME;
        String deviceName = EntityName.AIR_PURIFIER_NAME;
        int getAttrTimeGap = 5;
        int callServiceTimeGap = 90;
        int getAttrAfterCallingTimeGap = 60;

        OuterGraph graph = Caller.init(deviceName, getAttrTimeGap, callServiceTimeGap, getAttrAfterCallingTimeGap);
        Thread buildGraphThread = new Thread(new BuildGraphPhase(deviceName, graph));
        buildGraphThread.start();
        buildGraphThread.join();
//        graph = FileUtils.readFromFile(Constants.GRAPH_DIR + deviceName + "/model");
//        graph.print();

        new Thread(new CheckDataPhase(deviceName)).start();

//        new Thread(new CallServiceThread(deviceName)).start();
//        new Thread(new GetAttributeThread(deviceName, graph)).start();

//        HAScript.getAllServices();
//        HAScript.getAllAttributes(deviceName);
    }
}
