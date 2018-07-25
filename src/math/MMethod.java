package math;

import javafx.scene.control.Alert;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

@XmlRootElement(name = "mmethod")
@XmlType(propOrder = {"c","a","b","extr"})
public class MMethod
{
    private BigFraction b[], c[], A[][];
    private static BigFraction newA[][], startA[][], startB[], startL[];
    private int m, n;
    private int r = -1,k;
    private boolean min;
    private static int Fs[];
    private BigFraction CjI[];
    private BigFraction CjII[];
    private BigFraction CsI[];
    private BigFraction CsII[];
    private BigFraction alfa[];
    private BigFraction betta[];
    private boolean  check = true; // false значит не прошел проверку сит 2
    private LinkedList<StringBuilder> listAnswer = new LinkedList<>();
    private StringBuilder zvit1 = new StringBuilder();
    private static ArrayList<Double> iteratAlfa, iteratBetta;

    public MMethod(){}

    public MMethod(BigFraction[] c, BigFraction A[][], BigFraction b[], boolean extr){
        this.c=c;
        this.b=b;
        this.A=A;
        this.startA = A;
        this.startB = b;
        this.startL = c;
        this.min=extr;
        this.m = A.length;

        this.n = A[0].length;
    }

    public static BigFraction[] getStartL() {
        return startL;
    }

    public static BigFraction[][] getStartA() {
        return startA;
    }

    public static BigFraction[] getStartB() {
        return startB;
    }

    public boolean getExtr() {
        return min;
    }

    public BigFraction[][] getA() {
        return A;
    }

    public static ArrayList<Double> getIteratAlfa() {
        return iteratAlfa;
    }

    public static ArrayList<Double> getIteratBetta() {
        return iteratBetta;
    }

    public StringBuilder getZvit1() {
        return zvit1;
    }

    @XmlElement
    public void setA(BigFraction[][] a) {
        A = a;
    }

    @XmlElement
    public void setExtr(boolean extr) {
        this.min = extr;
    }

    public BigFraction[] getC() {
        return c;
    }

    @XmlElement
    public void setC(BigFraction[] c) {
        this.c = c;
    }

    public BigFraction[] getB() {
        return b;
    }

    @XmlElement
    public void setB(BigFraction[] b) {
        this.b = b;
    }

    public LinkedList<StringBuilder> getAnswer() {
        return listAnswer;
    }

    public static BigFraction[][] getNewA() {
        return newA;
    }

    public static int[] getFs() {
        return Fs;
    }

    public void run() throws Exception {
        iteratAlfa = new ArrayList<>();
        iteratBetta = new ArrayList<>();
        Fs = new int[m];
        CjI = new BigFraction[n + m + 1];
        CjII = new BigFraction[n + m + 1];
        CsI = new BigFraction[m];
        CsII = new BigFraction[m];
        alfa = new BigFraction[n + m + 1];
        betta = new BigFraction[n + m + 1];
        StringBuilder usl = new StringBuilder();

        if (min) {
            for (int i = 0; i < c.length; i++) {
                c[i] = c[i].multiply(new BigFraction(-1));
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (b[i].signum() == -1) {
                b[i] = b[i].multiply(new BigFraction(-1));
                for (int j = 0; j < A[i].length; j++)
                    A[i][j] = A[i][j].multiply(new BigFraction(-1));
            }
        }
        newA = MainTable(A, b);

        for (int i = 0; i < Fs.length; i++) {
            Fs[i] = i + 2 + newA[0].length - newA.length;
        }
        for (int i = 0; i < (n + m + 1); i++) {
            if (i <= n) {
                CjI[i] = new BigFraction(0);
            } else {
                CjI[i] = new BigFraction(-1);
            }
        }
        BigFraction L[] = new BigFraction[n + 1];
        for (int i = 0; i < n + 1; i++) {
            if (i == 0)
                L[i] = new BigFraction(0);
            else {
                L[i] = c[i - 1];
            }
        }
        for (int i = 0; i < (n + m + 1); i++) {
            if (i >= n + 1 || i == 0) {
                CjII[i] = new BigFraction(0);
            } else {
                CjII[i] = L[i];
            }
        }
        for (int i = 0; i < m; i++) {
            CsI[i] = new BigFraction(-1);
        }
        for (int i = 0; i < m; i++) {
            CsII[i] = new BigFraction(0);
        }

        for (int i = 0; i < (n + m + 1); i++) {
            alfa[i] = alfabetta(CsI, newA, CjI[i], i);
            newA[m][i] = alfa[i];
        }

        for (int i = 0; i < n + m + 1; i++) {
            betta[i] = alfabetta(CsII, newA, CjII[i], i);
            newA[m + 1][i] = betta[i];
        }


        usl.append("Условия исходной задачи:\n");
        usl.append("Целевая функция:\nL = ");
        StringBuilder usl1 = new StringBuilder();
        for (int i = 1; i < n + 1; i++) {
            if (i != n) {
                usl1.append(L[i].intValue() + "*X" + (i) + " + ");
            } else {
                usl1.append(L[i].intValue() + "*X" + (i));
            }
        }
        usl.append(usl1);
        usl.append("->max\n");


        zvit1.append("\nЦелевая функция:\nL = ");
        if (min) {
            for (int i = 0; i < c.length; i++) {
                if (i != n) {
                    zvit1.append(c[i].intValue() * -1 + "*X" + (i + 1) + " + ");
                } else {
                    zvit1.append(c[i].intValue() * -1 + "*X" + (i + 1));
                }
            }
            zvit1.append("=>min\n");
        } else {
            for (int i = 0; i < c.length; i++) {
                if (i != n) {
                    zvit1.append(c[i].intValue() + "*X" + (i + 1) + " + ");
                } else {
                    zvit1.append(c[i].intValue() + "*X" + (i + 1));
                }
            }
            zvit1.append("=>max\n");
        }
        ;

        zvit1.append("Вектора ограничений:\n");
        usl.append("Вектора ограничений:\n");
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (j != n) {
                    if (!(compareTwoFraction(newA[i][j], new BigFraction(0)) == 0))
                        zvit1.append(newA[i][j].intValue() + "*X" + j + " + ");
                    usl.append(newA[i][j].intValue() + "*X" + j + " + ");
                } else {
                    if (!(compareTwoFraction(newA[i][j], new BigFraction(0)) == 0))
                        zvit1.append(newA[i][j].intValue() + "*X" + j);
                    usl.append(newA[i][j].intValue() + "*X" + j);
                }
            }

            usl.append(" = " + newA[i][0].intValue() + "\n");
            zvit1.append(" = " + newA[i][0].intValue() + "\n");
        }

        usl.append("\nМ-задача" +
                "\n\n Целевая функция М-задачи:\n" +
                usl1 + " - M*(");
        zvit1.append("\r\t\t\t\t\tМ-задача:" +
                "\nЦелевая функция М-задачи:\n" +
                usl1 + " - M*(");
        for (int i = n + 1; i < n + m + 1; i++) {
            if (i != (n + m)) {
                usl.append("X" + i + " + ");
                zvit1.append("X" + i + " + ");
            } else {
                usl.append("X" + i);
                zvit1.append("X" + i);

            }
        }
        usl.append(")");
        zvit1.append(")");
        usl.append("->max\n");
        zvit1.append("=>max\nВектора ограничений М-задачи:\n");

        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n + m + 1; j++) {
                if (j < (n + i + 1)) {
                    if (!(compareTwoFraction(newA[i][j], new BigFraction(0)) == 0)) {
                        usl.append(newA[i][j].intValue() + "*X" + j + " + ");
                        zvit1.append(newA[i][j].intValue() + "*X" + j + " + ");
                    }
                } else {
                    if (!(compareTwoFraction(new BigFraction(0), newA[i][j]) == 0)) {
                        usl.append(newA[i][j].intValue() + "*X" + j);
                        zvit1.append(newA[i][j].intValue() + "*X" + j);
                    }
                }
            }

            usl.append(" = " + newA[i][0].intValue() + "\n");
            zvit1.append(" = " + newA[i][0].intValue() + "\n");
        }
        zvit1.append("\n\nНачальный опорный план и базис:\nFs0 = (");
        for (int i = 0; i < Fs.length; i++) {
            if (i != Fs.length - 1) {
                zvit1.append("A" + Fs[i] + " ; ");
            } else {
                zvit1.append("A" + Fs[i] + " }\n X0 =(");
            }
        }
        for (int i = 0; i < n + m + 1; i++) {
            if (i < n) {
                zvit1.append(" 0.0 ;");
            } else if (i < (n + m - 1)) {
                zvit1.append(new BigDecimal(startB[(i - (n))].doubleValue()).setScale(1, BigDecimal.ROUND_FLOOR) + " ; ");
            } else if (i == (n + m)) {
                zvit1.append(new BigDecimal(startB[startB.length - 1].doubleValue()).setScale(1, BigDecimal.ROUND_FLOOR) + ")\r");
            }
        }


        int count = 0;
        k = minimumK(alfa, betta);

        iteratAlfa.add(alfa[0].doubleValue());
        iteratBetta.add(betta[0].doubleValue());
        while (((alfa[k].multiply(1000000000).add(betta[k])).compareTo(new BigFraction(0))) < 0) {
            check = sit2(alfa, betta, newA);
            if (!check) {
                break;
            } else {
                if (count == 0) {
                    usl.append("\nНАЧАЛО ИТЕРАЦИОННОГО ПРОЦЕССА\n");
                    listAnswer.add(usl);
                }
                StringBuilder iterat = new StringBuilder();
                iterat.append(count + "-я итерация\n");
                BigFraction tetaMas[] = new BigFraction[m];


                BigFraction teta = new BigFraction(10000000);
                for (int i = 0; i < m; i++) {
                    if (compareTwoFraction(new BigFraction(0), newA[i][k]) == -1) {
                        tetaMas[i] = newA[i][0].divide(newA[i][k]);
                    }
                    if ((compareTwoFraction(newA[i][k], new BigFraction(0)) == 1) && (compareTwoFraction(teta, newA[i][0].divide(newA[i][k])) == 1)) {
                        teta = newA[i][0].divide(newA[i][k]);
                        r = i;
                    }
                }
                TableM raschet = new TableM(alfa, betta, c, tetaMas, newA, k, r, Fs);
                iterat.append(raschet.toString() + "\n");
                if (count != 0) {
                    iterat.append("\nПроверка погрешности");

                    for (int i = 0; i < alfa.length; i++) {
                        iterat.append("\nαtable - α' = " + newA[newA.length - 2][i].doubleValue() + " - " + alfa[i].doubleValue() + " = " + newA[newA.length - 2][i].subtract(alfa[i]).doubleValue());
                    }
                    for (int i = 0; i < betta.length; i++) {
                        iterat.append("\nβtable - β' = " + newA[newA.length - 1][i].doubleValue() + " - " + betta[i].doubleValue() + " = " + newA[newA.length - 1][i].subtract(betta[i]).doubleValue());

                    }
                }
                iterat.append("\nТак как, существуют Δj <0 и все Ωj≠∅, имеет место ситуация 3 \nНаправляющий столбец: " + k + "-ый\nНаправляющая строка: " + (r + 1) + "-ая\n\n");
                iterat.append("Fs ->A" + Fs[r] + "\nA" + k + "->Fs\n\n");
                Fs[r] = k;
                CsI[r] = CjI[k];
                CsII[r] = CjII[k];

                newA = findMatrix(newA, k, r, m, n);
                for (int i = 0; i < n + m + 1; i++) {
                    alfa[i] = alfabetta(CsI, newA, CjI[i], i);

                }
                for (int i = 0; i < n + m + 1; i++) {
                    betta[i] = alfabetta(CsII, newA, CjII[i], i);
                }
                k = minimumK(alfa, betta);
                count++;
                iteratAlfa.add(alfa[0].doubleValue());
                iteratBetta.add(betta[0].doubleValue());


                listAnswer.add(iterat);
            }
        }
        StringBuilder ab = new StringBuilder();
        ab.append("Конечная таблица М-метода:\n");
        BigFraction[] tetaMas = new BigFraction[m];
        TableM raschet = new TableM(alfa, betta, c, tetaMas, newA, k, r, Fs);
        ab.append(raschet.toString());


        if (!check) {
            zvit1.append("\nЗадача не имеет решения из-за неограниченности целевой функции сверху!");
            ab.append("\nЗадача не имеет решения из-за неограниченности целевой функции сверху!");
            listAnswer.add(ab);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация о решении");
            alert.setHeaderText("При решении исходной задачи был получен следующий результат:");
            alert.setContentText("Задача не имеет решения из-за неограниченности целевой функции сверху!");
            alert.showAndWait();
            return;
        } else {
            for (int i = 0; i < Fs.length; i++) {
                if (Fs[i] > n && newA[i][0].doubleValue() != 0) {
                    zvit1.append("\n Задача не имеет решения, так как содержит несовместимые условия!");
                    ab.append("Задача не имеет решения, так как содержит несовместимые условия!\n");
                    listAnswer.add(ab);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация о решении");
                    alert.setHeaderText("При решении исходной задачи был получен следующий результат:");
                    alert.setContentText("Задача не имеет решения, так как содержит несовместимые условия!");
                    alert.showAndWait();
                    return;
                }
            }

            for (int i = 0; i < Fs.length; i++) {
                if (Fs[i] > n) {

                    int a = i + 1;
                    for (int j = 0; j < Fs.length; j++) {
                        if (a == Fs[j]) {
                            j = 0;
                            a++;
                        }
                    }
                    Fs[i] = a;
                }
            }

            StringBuilder ale = new StringBuilder();
            ab.append("Так как,все Δj >0 , имеет место ситуация 1\nФормируем решение:\n");
            ale.append("Fs* = (");
            ab.append("Fs* = (");
            zvit1.append("Fs* = (");
            for (int x : Fs) {
                ale.append("A" + x + " ; ");
                zvit1.append("A" + x + " ; ");
                ab.append("A" + x + " ; ");
            }
            zvit1.append(")\n");
            ale.append(")\n");
            ab.append(")\n");
            ab.append("X* = (");
            ale.append("X* = (");
            zvit1.append("\n\nРешение\nX* = (");
            for (int x = 0; x < xOptimalniy(Fs, newA).length; x++) {

                if (x != xOptimalniy(Fs, newA).length - 1) {
                    ale.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR) + " ; ");
                    zvit1.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR) + " ; ");
                    ab.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR) + " ; ");
                } else {
                    ale.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR));
                    zvit1.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR));
                    ab.append(new BigDecimal(xOptimalniy(Fs, newA)[x].doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR));
                }
            }
            ale.append(")");
            ab.append(")\nL*: " + new BigDecimal(finaloo(c, xOptimalniy(Fs, newA), min).doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR));
            zvit1.append((")\nL*= " + new BigDecimal(finaloo(c, xOptimalniy(Fs, newA), min).doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR)));
            listAnswer.add(ab);
            ale.append((")\nL*= " + new BigDecimal(finaloo(c, xOptimalniy(Fs, newA), min).doubleValue()).setScale(6, BigDecimal.ROUND_FLOOR)));
            Alert result = new Alert(Alert.AlertType.INFORMATION);
            result.setTitle("Информация о решении");
            result.setHeaderText("Задача решена!");
            result.setContentText(ale.toString());
            result.showAndWait();

        }
    }

    public BigFraction[] xOptimalniy(int[] fs,BigFraction[][] a){
        BigFraction[] xToReturn = new BigFraction[n+m+1];
        for(int i = 0; i < xToReturn.length; i++) {
            xToReturn[i] = new BigFraction("0");
        }
        for(int i = 0; i < fs.length; i++){
            xToReturn[fs[i]-1] = a[i][0];
        }
        return xToReturn;
    }


    public static BigFraction finaloo (BigFraction c[],BigFraction x[], boolean min){
        BigFraction otvet = new BigFraction(0);
        for (int i = 0; i < c.length; i++)
        {
            otvet = otvet.add(c[i].multiply(x[i]));
        }
        if (min) {
            return otvet.multiply(new BigFraction(-1));
        }
        else {return otvet;}
    }

    public static int compareTwoFraction(BigFraction fr1, BigFraction fr2){
        if(fr1.signum()>fr2.signum()){
            return 1;
        }else if(fr1.signum()<fr2.signum()){
            return -1;
        }else{
            if(fr1.getDenominator()==fr2.getDenominator()){
                return fr1.getNumerator().compareTo(fr2.getNumerator());
            }else{
                return (fr1.getNumerator().multiply(fr2.getDenominator())).compareTo((fr2.getNumerator().multiply(fr1.getDenominator())));
            }
        }

    }

    public static BigFraction[][] findMatrix(BigFraction A[][],int stolb,int strok,int m, int n){
        BigFraction X[][] = new BigFraction [m+2][n+m+1];

        for (int i = 0; i < m+2 ; i++)
        {
            for (int j = 0; j < n+m+1 ; j++)                                //////////////
            {
                if(i != strok){
                    X[i][j] = A[i][j].subtract(A[i][stolb].multiply(A[strok][j]).divide((A[strok][stolb])));
                }
                else{           X[i][j] =   A[strok][j].divide(A[strok][stolb]);
                }
            }
        }
        return  X;
    }

    public static BigFraction alfabetta(BigFraction[] Cs, BigFraction A[][], BigFraction Cj, int iter){
        BigFraction res = new BigFraction (0);
        for (int i = 0; i < Cs.length; i++)
        {
            res = res.add(Cs[i].multiply(A[i][iter]));                                    /////функция нахождения альфа подходит для нахождения бетты
        }
        return  res.subtract(Cj);
    }

    public static int minimumK (BigFraction[]a,BigFraction []b){
        BigFraction ma = a[1];
        for (int i = 1; i < a.length ; i++)
        {
            if(compareTwoFraction(ma,a[i])==1 && compareTwoFraction(new BigFraction(0),a[i]) == 1)
                ma = a[i];
        }
        ArrayList<Integer> mas = new ArrayList<Integer>();
        for (int i = 1; i < a.length; i++)
        {
            if (compareTwoFraction(ma,a[i])==0)
            {
                mas.add(i);
            }
        }
        if((mas.size() > 1) || mas.size() == 0 ) {
            int numb = mas.get(0);
            BigFraction mb = b[mas.get(0)];
            for (int i = 0; i < mas.size() ; i++)
            {
                if (compareTwoFraction(mb , b[mas.get(i)]) == 1){
                    mb = b[mas.get(i)];
                    numb = mas.get(i);
                }
            }
            return numb;
        }
        else  {
            return mas.get(0);
        }
    }

    public BigFraction[][] MainTable(BigFraction[][] a, BigFraction[] b){
        BigFraction l[][] = new BigFraction[m+2][m+n+1];
        for (int i = 0; i < m+2 ; i++) {
            for (int j = 0; j < m+n+1 ; j++) {
                if( j == (i + n+1))
                    l[i][j] = new BigFraction(1);
                else
                    l[i][j] = new BigFraction(0);
            }
        }

        for (int i = 0; i < m ; i++) {
            l[i][0] = b[i];
        }

        for (int i = 0; i < m ; i++) {
            for (int j = 1; j < n+1 ; j++) {
                l[i][j] = a[i][j-1];
            }
        }
        return l;
    }
    public boolean sit2(  BigFraction [] a , BigFraction[] b , BigFraction[][]checking) {
        boolean c = false;
        for (int j = 1; j < a.length; j++) {
            c = false;
            if (a[j].signum() == -1 || a[j].signum() == 0 && b[j].signum() == -1) {
                for (int i = 0; i < m; i++) {
                    if (compareTwoFraction(checking[i][j], new BigFraction(0)) == 1) {
                        c = true;
                    }
                    if (c) break;
                }
            }
            else{
                c = true;
            }
            if(c == false) break;
        }
        return c;
    }
}