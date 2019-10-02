package graphviz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GraphViz{
    private String dir = "";
    private String date = "";
    private String bash = "";
    private StringBuilder graph = new StringBuilder();

    private Runtime runtime = Runtime.getRuntime();

    public GraphViz(String dir, String date) {
        this.dir = dir;
        this.date = date;
    }

    public void run() {
        File file = new File(dir + "txt/" + date + ".txt");
        writeGraphToFile(graph.toString(), dir + "txt/" + date + ".txt");
        creatBash();
        try {
            runtime.exec(bash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creatBash(){
        bash += "dot " + dir + "txt/" + date + ".txt ";
        bash += "-T png ";
        bash += "-o ";
        bash += dir + "png/" + date + ".png";
        System.out.println(bash);
    }

    public void writeGraphToFile(String dotcode, String filename) {
        try {
            File file = new File(filename);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dotcode.getBytes());
            fos.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void add(String line) {
        graph.append(line);
    }

    public void addln(String line) {
        graph.append("\t" + line + "\n");
    }

    public void addln() {
        graph.append('\n');
    }

    public void addLabel(String label) {
        graph.append("[label=\"" + label + "\"]");
    }

    public void addSpace() {
        graph.append("\t");
    }

    public void startGraph() {
        graph.append("digraph G {\n") ;
    }

    public void endGraph() {
        graph.append("}") ;
    }

    public void startSubGraph(String subGraphName) {
        graph.append("subgraph " + subGraphName + " {");
    }
}
