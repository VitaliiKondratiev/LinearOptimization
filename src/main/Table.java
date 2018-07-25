package main;
import math.BigFraction;
import сontroller.Controller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public static void createTable(TableView tableView, int cols, int rows, String name){
        TestDataGenerator dataGenerator = new TestDataGenerator();
        List<String> columnNames = dataGenerator.getNext(cols);
        TableColumn<ObservableList<String>, String> column;
        for (int i = 0; i < columnNames.size(); i++) {
            final int finalIdx = i;
            if (name =="A") {
                column = new TableColumn<>(
                        "X" + (i+1)
                );
                column.setStyle("-fx-alignment: CENTER;");
                column.setSortable(false);
                if (name =="A") {
                    Controller.setArrayTableAColumn(column);
                }
            }
            else{
                column = new TableColumn<>(
                        "B"
                );
                column.setStyle("-fx-alignment: CENTER;");
                column.setSortable(false);
                Controller.setArrayTableBColumn(column);
            }
            if(name == "C") {
                column = new TableColumn<>(
                        "C" + (i+1)
                );
                column.setStyle("-fx-alignment: CENTER;");
                column.setSortable(false);
                Controller.setArrayTableCColumn(column);
            }
            column.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<String>(param.getValue().get(finalIdx))
            );
            tableView.getColumns().add(column);
        }
        // add data
        for (int i = 0; i <rows; i++) {
            tableView.getItems().add(
                    FXCollections.observableArrayList(
                            dataGenerator.getNext(cols)
                    )
            );
        }
    }

    public static BigFraction[][] getTableA(TableView tableA, int cols, int rows){
        BigFraction[][] A = new BigFraction[rows][cols];
        for (int i=0;i<rows;i++){
            ObservableList table = (ObservableList) tableA.getItems().get(i);
            for (int j=0;j<cols;j++){
                A[i][j] = new BigFraction(table.get(j).toString());
                //A[i][j]= Double.parseDouble(table.get(j).toString());
            }
        }
        return A;
    }

    public static BigFraction[] getTableB(TableView tableB, int rows){
        BigFraction[] B = new BigFraction[rows];
        for (int j=0;j<rows;j++){
            ObservableList table = (ObservableList) tableB.getItems().get(j);
            B[j] = new BigFraction(table.get(0).toString());
            //B[j]= Double.parseDouble(table.get(0).toString());
        }
        return B;
    }

    public static BigFraction[] getTableC(TableView tableC, int cols){
        BigFraction[] C = new BigFraction[cols];
        ObservableList table = (ObservableList) tableC.getItems().get(0);
        for (int j=0;j<cols;j++){
            C[j] = new BigFraction(table.get(j).toString());
        }
        return C;
    }

    public static void setTable(TableView tableView, BigFraction[] arr) {
        int row = tableView.getItems().size();
        TestDataGenerator dataGenerator = new TestDataGenerator();
        tableView.getItems().clear();
        for (int i = 0; i < row; i++) {
            tableView.getItems().add(
                    FXCollections.observableArrayList(
                            dataGenerator.getNext(tableView.getColumns().size(), arr)
                    )
            );
        }
    }

    public static void setTable(TableView tableView, BigFraction[][] arr) {
        int row = tableView.getItems().size();
        TestDataGenerator dataGenerator = new TestDataGenerator();
        tableView.getItems().clear();
        for (int i = 0; i < row; i++) {
            tableView.getItems().add(
                    FXCollections.observableArrayList(
                            dataGenerator.getNext(tableView.getColumns().size(), arr)
                    )
            );
        }
    }

    private static class TestDataGenerator {
        private static final String[] LOREM = ("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 " +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0" +
                " 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.").split(" ");
        private int curWord = 0, curWordA = 0, curWordB = 0;

        List<String> getNext(int nWords) {
            List<String> words = new ArrayList<>();
            for (int i = 0; i < nWords; i++) {
                if (curWord == Integer.MAX_VALUE) {
                    curWord = 0;
                }
                words.add(LOREM[curWord % LOREM.length]);
                curWord++;
            }
            return words;
        }

        List<String> getNext(int nWords, BigFraction[] arr) {
            List<String> words = new ArrayList<>();
            for (int i = 0; i < nWords; i++) {
                if (nWords != 1)
                    words.add(String.valueOf(arr[i].doubleValue()));
                else
                    words.add(String.valueOf(arr[curWordB].doubleValue()));
            }
            curWordB++;
            return words;
        }

        List<String> getNext(int nWords, BigFraction[][] arr) {
            List<String> words = new ArrayList<>();
            if (curWordA == Integer.MAX_VALUE) {
                curWordA = 0;
            }
            for (int j = 0; j < nWords; j++)
                words.add(String.valueOf(arr[curWordA % arr.length][j].doubleValue()));
            curWordA++;
            return words;
        }
    }
}