package com.iscas.smarthome.graphviz;

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

    /**
     * @return 图片生成后的位置
     */
    public String run() {
        File file = new File(dir + "txt/" + date + ".txt");
        writeGraphToFile(graph.toString(), dir + "txt/" + date + ".txt");
        String res = creatBash();
        try {
            runtime.exec(bash).waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String creatBash(){
        bash += "dot " + dir + "txt/" + date + ".txt ";
        bash += "-T png ";
        bash += "-o ";
        bash += dir + "png/" + date + ".png";
        System.out.println(bash);
        return dir + "png/" + date + ".png";
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
