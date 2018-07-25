package exceptions;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.commons.lang.math.NumberUtils;
import java.util.ArrayList;

public class IncorrectData extends Exception{
    private ArrayList<String> errors;
    private static ObservableList data;
    private static String value;
    public IncorrectData(ArrayList<String> errors){
        this.errors = errors;
    }
    public String getMessageFields(){
        return "Вы ввели некорректные данные в следующие поля:\n "+errors+".";
    }

    public String getMessageTables(){
        return "Вы ввели некорректные данные в следующие таблицы:\n" +
                " "+errors+".";
    }

    public static boolean incorrectTable(TableView tableView){
        for (int i=0;i<tableView.getItems().size();i++){
            data = (ObservableList) tableView.getItems().get(i);
            for(int j=0;j<data.size();j++) {
                if (!NumberUtils.isNumber(value = data.get(j).toString()))
                    return true;
            }
        }
        return false;
    }
}