package exceptions;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import java.util.ArrayList;

public class EmptyException extends  Exception{
    private ArrayList<String> errors;
    private static ObservableList data;
    private static String value;

    public EmptyException(ArrayList<String> errors){
        this.errors = errors;
    }

    public String getMessageFields(){
        return "Пожалуйста заполните все необходимые поля.\nНе заполнены следующие поля: "+errors+".";
    }

    public String getMessageTables(){
        return "Пожалуйста заполните все таблицы.\nНе заполнены полностью следующие таблицы: "+errors+".";
    }

    public String getMessageTablesAndFields(){
        return "Пожалуйста заполните все таблицы и необходимые поля.\nНе заполнены полностью следующие элементы: "+errors+".";
    }

    public static boolean emptyTable(TableView tableView){
        for (int i=0;i<tableView.getItems().size();i++){
            data = (ObservableList) tableView.getItems().get(i);
            for(int j=0;j<data.size();j++) {
                if ((value = data.get(j).toString()).equals(""))
                    return true;
            }
        }
        return false;
    }
}
