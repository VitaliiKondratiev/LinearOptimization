package math;

import Jama.LUDecomposition;
import Jama.Matrix;
import java.math.BigDecimal;

public class Validation {
    private BigFraction[][] AFs;
    private static BigFraction[][] obrAFs;
    private int finalFs[];
    private BigFraction det;
    private BigFraction[][] startA;
    private BigFraction[][] finalA;
    private BigFraction[] startB, startL, XI;
    private boolean check;
    private StringBuilder listCheck = new StringBuilder();
    private StringBuilder validStr = new StringBuilder();

    public Validation() {
        this.finalFs = MMethod.getFs();
        this.finalA = MMethod.getNewA();
        this.startA = MMethod.getStartA();
        this.startB = MMethod.getStartB();
        this.startL = MMethod.getStartL();
        this.XI = new BigFraction[startA[0].length];
        for(int i = 0; i < startA[0].length; i++) {
            XI[i] = new BigFraction(0);
        }
        for(int i = 0; i < finalFs.length; i++){
            XI[finalFs[i]-1]= finalA[i][0];
        }
    }

    public StringBuilder getListCheck() {
        return listCheck;
    }

    public StringBuilder getValidStr() {
        return validStr;
    }

    public static BigFraction[][] getObrAFs() {
        return obrAFs;
    }

    public BigFraction[][] getAFs() {
        BigFraction[][] Buf = new BigFraction[finalFs.length][finalFs.length];
        for (int i = 0; i < Buf.length; i++) {
            for (int j = 0; j < Buf[0].length; j++) {
                Buf[i][j] = startA[i][finalFs[j] - 1];
            }
        }
        return Buf;
    }

    public BigFraction [][] findAfsObr(BigFraction [][] aMatrix , BigFraction det){
        BigFraction[][] result = new BigFraction [aMatrix.length][aMatrix[0].length];
        if (aMatrix.length == 1){
           result[0][0] = aMatrix[0][0].pow(-1);
       }
        else{
        BigFraction[][] sss = new BigFraction [aMatrix.length-1][aMatrix[0].length-1];
        for (int i = 0; i < aMatrix.length ; i++) {/////Строки
            for (int j = 0; j < aMatrix[0].length; j++) {//////Столбцы
                for (int k = 0; k < aMatrix.length ; k++) {/////Строки
                    for (int l = 0; l < aMatrix[0].length; l++) {//////Столбцы
                        if( k < i && l < j){
                            sss[k][l] = aMatrix[k][l];
                        }
                        else if( k > i && l > j){
                            sss[k-1][l-1] = aMatrix[k][l];
                        }
                        else if( k < i && l > j){
                            sss[k][l-1] = aMatrix[k][l];
                        }
                        else if( k > i && l < j){
                            sss[k-1][l] = aMatrix[k][l];}
                        }
                }
                if ((i+j)%2 == 0) {
                    result[j][i] = findDeterminant(sss).divide(det);
                }
                else  {
                    result[j][i] = findDeterminant(sss).divide(det).multiply(new BigFraction(-1));
                }
            }
        }
        }
        return result;
    }

    public static BigFraction[] vectorMatrix(BigFraction[][] matrix, BigFraction[] vector){
        BigFraction[] result = new BigFraction[vector.length];
        BigFraction ssum;
        for (int i = 0; i < vector.length; i++) {
            ssum = new BigFraction(0);
            for (int j = 0; j < vector.length; j++) {
                 ssum = ssum.add(matrix[j][i].multiply(vector[j]));
            }
            result[i] = ssum;
        }
        return result;
    }

    public BigFraction findDeterminant(BigFraction[][] m) {
        double[][] A = new double[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                A[i][j] = m[i][j].doubleValue();
            }
        }
        if(m.length == 1){
            det = new BigFraction(A[0][0]);
        }else{
            Matrix matrix = new Matrix(A);
            LUDecomposition dec = new LUDecomposition(matrix);
            det =  new BigFraction(dec.det());
        }
        return det;
    }

    public void DopustimostCheck(){
        if(check){
            return;
        }
        BigFraction[][] AfsObr = findAfsObr(AFs, det);
        BigFraction [] Cs = new BigFraction[finalFs.length];
        for (int i = 0; i < finalFs.length ; i++) {
               Cs[i] = startL[finalFs[i]-1];
        }
        BigDecimal deltaI[] = new BigDecimal[startA.length];
        BigFraction deltaJ[] = new BigFraction[startA[0].length];
        BigFraction YI [] = vectorMatrix(AfsObr,Cs);
        listCheck.append("\n\nПроверка допустимости решения\n");
        validStr.append("\n\nПроверка допустимости решения\n");
        listCheck.append("X* ={");
        validStr.append("\nX* ={");
        for (BigFraction x : XI) {
            if (MMethod.compareTwoFraction(x,new BigFraction(0)) == -1){
                validStr.append("\nПроверка на неотрицательность не пройдена!\n");
                listCheck.append("Проверка на неотрицательность не пройдена!\n");
                check = false;
                return;
            }
        }

        for (BigFraction x: XI) {
            listCheck.append(new BigDecimal(x.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR) + " ; ");
            validStr.append(new BigDecimal(x.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR) + " ; ");
        }

        listCheck.append("}\nПроверка на неотрицательность пройдена.\nОпределение δi\n");
        validStr.append("}\nПроверка на неотрицательность пройдена.\nОпределение дельта i\n");
        for (int i = 0; i < startA.length; i++) {
            BigDecimal sum = new BigDecimal(0);
            StringBuilder sbi = new StringBuilder();
            sbi.append("δ"+(i+1)+" = " + startB[i].intValue()+" - (");
            for (int j = 0; j <startA[0].length; j++) {
                sum = sum.add(startA[i][j].toBigDecimal().multiply(XI[j].toBigDecimal().setScale(8, BigDecimal.ROUND_HALF_UP)));
                if(!( startA[i][j].doubleValue() == 0 || XI[j].doubleValue()== 0))
               sbi.append( startA[i][j].intValue() + " * "+ new BigDecimal(XI[j].doubleValue()).setScale(8, BigDecimal.ROUND_FLOOR)+" + ");
            }
             deltaI[i] = startB[i].toBigDecimal().subtract(sum);
            sbi.append(" )= " +deltaI[i].doubleValue() );
            listCheck.append(sbi +"\n");
            validStr.append(sbi +"\n");
        }
        BigDecimal maxI = new BigDecimal(-1);
        for (BigDecimal x: deltaI) {
            //if(MMethod.compareTwoFraction(x,maxI) == 1 )
            if(x.doubleValue() > maxI.doubleValue()) {
                maxI = x;
            }
        }
        validStr.append("Максимальное дельта i = " + maxI.doubleValue());
        listCheck.append("\nmaxδi = " + maxI.doubleValue());
        validStr.append("\nОпределение дельта j\nДля определения оптимального плана двойственной задачи умножим вектор Cs на обратную к Afs* матрицу\n");
        listCheck.append("\nОпределение дельта j\n"/*+"Для определения оптимального плана двойственной задачи умножим вектор Cs на обратную к Afs* матрицу\n"*/);
        StringBuilder format = new StringBuilder();
        BigDecimal doubleAfsObr [][] = new BigDecimal[AfsObr.length][AfsObr[0].length];
        for (int i = 0; i < AfsObr.length; i++) {
            for (int j = 0; j < AfsObr[0].length; j++) {
                doubleAfsObr[i][j] = new BigDecimal(AfsObr[i][j].doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR);
            }
        }
        for(int i=0;i<finalFs.length;i++){
            format.append("%"+(i+1)+"$"+14+"."+(14)+"s |");
        }
//        for (int i = 0; i < AfsObr.length ; i++) {
//            listCheck.append(String.format(format.toString(), doubleAfsObr[i]));
//            //validStr.append(String.format(format.toString(), doubleAfsObr[i]));
//            listCheck.append("\n");
//            //validStr.append("\n");
//        }

        listCheck.append("\nY* =(");
        validStr.append("\nY* =(");
        for (BigFraction x: YI) {
            listCheck.append(new BigDecimal(x.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR)+ " ; ");
            validStr.append(new BigDecimal(x.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR)+ " ; ");
        }
        listCheck.append(")\n");
        validStr.append(")\n");
        if(MMethod.compareTwoFraction(new BigFraction(0.000001), new BigFraction(maxI.abs())) == -1){
            validStr.append("\nПроверка допустимости не пройдена");
            listCheck.append("\nПроверка допустимости не пройдена");
            check = false;
            return;
        }

        for (int i = 0; i < startA[0].length; i++) {
            BigFraction sum = new BigFraction(0);
            StringBuilder sbj = new StringBuilder();
            sbj.append("δ"+(i+1)+" = " + startL[i].doubleValue() + " - (");
            for (int j = 0; j <startA.length; j++) {
                sum = sum.add(startA[j][i].multiply(YI[j]));
                if(!( startA[j][i].doubleValue() == 0 || YI[j].doubleValue()== 0))
                sbj.append((startA[j][i].intValue() + " * "+ new BigDecimal( YI[j].doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR)+" + "));
            }
            deltaJ[i] = startL[i].subtract(sum);
            sbj.append(") = " + new BigDecimal( deltaJ[i].doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR) );
            listCheck.append(sbj + "\n");
            validStr.append(sbj + "\n");
        }
        BigFraction maxJ = new BigFraction(-1);
        for (BigFraction x: deltaJ) {
            if(MMethod.compareTwoFraction(x,maxJ) == 1 )
                maxJ = x;
        }
        listCheck.append("\nmaxδj = " + new BigDecimal( maxJ.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        validStr.append("Максимальное дельта j = " + new BigDecimal( maxJ.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        if(MMethod.compareTwoFraction(new BigFraction(0.0000001),maxJ.abs()) == -1){
            listCheck.append("\nПроверка допустимости не пройдена");
            validStr.append("\nПроверка допустимости не пройдена");
            check = false;
            return;
        }
    }

    public void OpornoCheck(){
        //validStr.append("\n\n                                                               Проверка достоверности\n");
        listCheck.append("Проверка опорности решения\n");
        validStr.append("\nПроверка опорности решения\n\n");

        AFs = getAFs();
        det = findDeterminant(AFs);
        listCheck.append("AFs*\n");
        //validStr.append("AFs*\n");

        StringBuilder format = new StringBuilder();
        Object [][] AfS = new Object[AFs.length+1][finalFs.length];
        for (int i = 0; i < AFs.length+1; i++) {
            for (int j = 0; j < finalFs.length; j++) {
                if(i ==0){
                    AfS[i][j] = "A" + finalFs[j];
                }
                else{
                    AfS[i][j] = AFs[i-1][j].intValue();
                }
            }
        }
        for(int i=0;i<finalFs.length;i++){
                format.append("%"+(i+1)+"$"+6+"."+(4)+"s |");
        }
        for (int i = 0; i < AfS.length ; i++) {
            listCheck.append(String.format(format.toString(), AfS[i]));
            //validStr.append(String.format(format.toString(), AfS[i]));
            listCheck.append("\n");
            //validStr.append("\n");
        }

        if(MMethod.compareTwoFraction(det,new BigFraction(0)) != 0) {
            listCheck.append("\nТак как определитель матрицы AFs* равен " + det.doubleValue()+ " и отличен от 0, то вектора условий соответствующие базисным компонентам линейно независимы.");
            validStr.append("Так как определитель матрицы AFs* равен " + det.doubleValue()+ " и отличен от 0, то вектора условий соответствующие базисным компонентам линейно независимы.");
        }
        else{
            listCheck.append("\nТак как определитель матрицы AFs* равен " + det.doubleValue()+ ", то вектора условий соответствующие базисным компонентам линейно зависимы.");
            validStr.append("\nТак как определитель матрицы AFs* равен " + det.doubleValue()+ ", то вектора условий соответствующие базисным компонентам линейно зависимы.");
            listCheck.append("\nПроверка решения не пройдена!");
            validStr.append("\nПроверка решения не пройдена!");
            check = true;
        }
    }

    public void OptimalityCheck(){
        if(check){
            return;
        }
        listCheck.append("\n\nПроверка оптимальности решения");
        validStr.append("\n\nПроверка оптимальности решения\n");
        det = findDeterminant(AFs);
        obrAFs = findAfsObr(AFs, det);

        BigFraction [] Cs = new BigFraction[finalFs.length];
        for (int i = 0; i < finalFs.length ; i++) {
            Cs[i] = startL[finalFs[i]-1];
        }
        BigFraction YI [] = vectorMatrix(obrAFs,Cs);

        listCheck.append("\nОптимальное значение прямой задачи:\nL1* = ");
        validStr.append("\nОптимальное значение прямой задачи:\nL1* = ");
        BigFraction l1 = new BigFraction(0);
        String l11 ="";
        String l22 ="";
        for (int i = 0; i < startA[0].length ; i++) {
            l1= l1.add(XI[i].multiply(startL[i]));
            l11 += (XI[i].doubleValue() +" * "+ startL[i].doubleValue()+" + ");
        }
        listCheck.append( l11 + " = ");
        validStr.append( l11 + " = ");
        listCheck.append(new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        validStr.append(new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        listCheck.append("\nОптимальное значение двойственной задачи:\nL2* = ");
        validStr.append("\nОптимальное значение двойственной задачи:\nL2* = ");
        BigFraction l2 = new BigFraction(0);
        for (int i = 0; i < startA.length ; i++) {
            l2 = l2.add(YI[i].multiply(startB[i]));
            l22 += (YI[i].doubleValue() +" * "+ startB[i].doubleValue()+" + ");
        }
        listCheck.append( l22 + " = ");
        validStr.append( l22 + " = ");
        listCheck.append(new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        validStr.append(new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        validStr.append("\nОптимальное значение через Бетта 0:\nL3* = ");
        listCheck.append("\nОптимальное значение через Бетта 0:\nL3* = ");
        BigFraction l3 = finalA[finalA.length-1][0];
        listCheck.append(new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        validStr.append(new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_FLOOR));
        if(finalA[finalA.length-1][0].signum() < 0){
            listCheck.append("\nТак как функция минимизируется, имеем:");
            validStr.append("\nТак как функция минимизируется, имеем:");
            l1 = l1.multiply(new BigFraction(-1));
            listCheck.append("\nL1* = " + new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
            validStr.append("\nL1* = " + new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
            l2 = l2.multiply(new BigFraction(-1));
            listCheck.append("\nL2* = "+new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
            validStr.append("\nL2* = "+new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
            l3 = l3.multiply(new BigFraction(-1));
            listCheck.append("\nL3* = "+new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
            validStr.append("\nL3* = "+new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
        }


        listCheck.append("\nСреднее оптимальное значение:\nLср* = (" + new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " + "+ new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " + "+ new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + ")\\3 = ");
        validStr.append("\nСреднее оптимальное значение:\nLср* = (" + new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " + "+ new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " + "+ new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + ")\\3 = ");
        BigFraction avgl = l1.add(l2.add(l3)).divide(new BigFraction(3));
        listCheck.append(new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
        validStr.append(new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP));
        listCheck.append(l1.subtract(avgl).doubleValue() < 0.0000001 ? "\nL1* - Lср* = " + new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " +new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L1* - Lср* = " + l1.doubleValue() + " - " + avgl.doubleValue() + " = " + l1.subtract(avgl).doubleValue() + " >= 0.0000001");
        validStr.append(l1.subtract(avgl).doubleValue() < 0.0000001 ? "\nL1* - Lср* = " + new BigDecimal( l1.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " +new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L1* - Lср* = " + l1.doubleValue() + " - " + avgl.doubleValue() + " = " + l1.subtract(avgl).doubleValue() + " >= 0.0000001");
        listCheck.append(l2.subtract(avgl).doubleValue() < 0.0000001 ? "\nL2* - Lср* = " + new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " + new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L2* - Lср* = " + l2.doubleValue() + " - " + avgl.doubleValue() + " = " + l2.subtract(avgl).doubleValue() + " >= 0.0000001");
        validStr.append(l2.subtract(avgl).doubleValue() < 0.0000001 ? "\nL2* - Lср* = " + new BigDecimal( l2.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " + new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L2* - Lср* = " + l2.doubleValue() + " - " + avgl.doubleValue() + " = " + l2.subtract(avgl).doubleValue() + " >= 0.0000001");
        listCheck.append(l3.subtract(avgl).doubleValue() < 0.0000001 ? "\nL3* - Lср* = " +new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " + new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L3* - Lср* = " + l3.doubleValue() + " - " + avgl.doubleValue() + " = " + l3.subtract(avgl).doubleValue() + " >= 0.0000001");
        validStr.append(l3.subtract(avgl).doubleValue() < 0.0000001 ? "\nL3* - Lср* = " +new BigDecimal( l3.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " - " + new BigDecimal( avgl.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP) + " = 0.0" + " < 0.0000001" : "L3* - Lср* = " + l3.doubleValue() + " - " + avgl.doubleValue() + " = " + l3.subtract(avgl).doubleValue() + " >= 0.0000001");
   }
}
