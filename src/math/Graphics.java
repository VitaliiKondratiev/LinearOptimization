package math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Graphics {
    private int firstdbetta;
    private int seconddbetta;
    private BigFraction[] startB;
    private BigFraction[][] obrAFs;
    private BigFraction [][] Ddb;
    private ArrayList<Point> listPoint = new ArrayList();
    private BigFraction maxX = new BigFraction(Double.MIN_VALUE);
    private BigFraction maxY = new BigFraction(Double.MIN_VALUE);
    StringBuilder ust = new StringBuilder();

    public StringBuilder getUst() {
        return ust;
    }

    public BigFraction[][] getDdb() {
        return Ddb;
    }

    public ArrayList<Point> getListPoint() {
        return listPoint;
    }

    public BigFraction getMaxX() {
        return maxX;
    }

    public BigFraction getMaxY() {
        return maxY;
    }

    public Graphics(int firstBetta, int secondBetta){
        this.firstdbetta = firstBetta;
        this.seconddbetta = secondBetta;
        this.startB = MMethod.getStartB();
        this.obrAFs = Validation.getObrAFs();
    }

    public void DoIt (){
        OblastUstoichevosti();
        findMinAndMaxValue();
        sortPoints();
    }

    private void sortPoints(){
        Comparator<Point> comparator = new Comparator<Point>() {
            public int compare(Point lhs, Point rhs) {
                double lhsAngle = Math.atan2(lhs.getY().doubleValue(), lhs.getX().doubleValue());
                double rhsAngle = Math.atan2(rhs.getY().doubleValue(), rhs.getX().doubleValue());

                if (lhsAngle < rhsAngle) return -1;
                if (lhsAngle > rhsAngle) return 1;

                return 0;
            }
        };
        Collections.sort(listPoint, comparator);
    }

    public void OblastUstoichevosti(){
        ust.append("DΔb ={");
        Ddb = new BigFraction[obrAFs.length+2][3];
        for (int i = 0; i < Ddb.length; i++) {
            for (int j = 0; j < 3; j++) {
                if(i <Ddb.length-2){
                    if(j == 2){
                    Ddb[i][j] = MV(obrAFs,startB)[i].multiply(new BigFraction(-1));
                    ust.append((new BigDecimal(Ddb[i][j].doubleValue()).setScale(7,BigDecimal.ROUND_FLOOR)+";\n"));
                }
                else if(j == 0) {
                    Ddb[i][j] = obrAFs[i][firstdbetta-1];
                        ust.append((new BigDecimal(Ddb[i][j].doubleValue()).setScale(7,BigDecimal.ROUND_FLOOR) + "Δb" + firstdbetta + " + "));
                }
            else if( j == 1){
                    Ddb[i][j] = obrAFs[i][seconddbetta-1];
                        ust.append((new BigDecimal(Ddb[i][j].doubleValue()).setScale(7,BigDecimal.ROUND_FLOOR) + "Δb"+seconddbetta+" ≧ "));
                }
            }
                else if(i == Ddb.length-2){
                    if(j == 2){
                        Ddb[i][j] = startB[firstdbetta-1].multiply(new BigFraction(-1));
                    }
                    else if(j == 0) {
                        Ddb[i][j] =  new BigFraction(1);
                    }
                    else if( j == 1){
                        Ddb[i][j] =  new BigFraction(0);
                    }
                }
                else if(i == Ddb.length-1){
                    if(j == 2){
                        Ddb[i][j] = startB[seconddbetta-1].multiply(new BigFraction(-1));
                    }
                    else if(j == 0) {
                        Ddb[i][j] = new BigFraction(0);
                    }
                    else if( j == 1){
                        Ddb[i][j] =  new BigFraction(1);
                    }
                }
            }
        }
        ust.append("}\n\nDb = {" + "Δb"+firstdbetta+" ≧ " + (startB[firstdbetta-1].doubleValue()*-1)+";  Δb"+seconddbetta+" ≧ " + (startB[seconddbetta-1].doubleValue()*-1)+"}\n" );

        for (int i = 0; i < Ddb.length-1; i++) {
            for (int j = i+1; j < Ddb.length; j++) {
                if(checktochek(Gaus(Ddb[i].clone() , Ddb[j].clone()),Ddb)){
                    listPoint.add(Gaus(Ddb[i].clone() , Ddb[j].clone()));
                }
            }
        }
        ust.append("Область устойчивости:\n Db∩DΔb = {");

        for (Point x : listPoint){
            ust.append("("+x.getX().doubleValue() +";  "+ x.getY().doubleValue()+")\n");
        }
        ust.append("}\n В данной области найденный план сохраняет свою оптимальность.");
    }
    public boolean checktochek(Point tochka ,  BigFraction[][] uslovia){
        boolean cheking = true;
        for (int i = 0; i < uslovia.length ; i++) {
            if(MMethod.compareTwoFraction(tochka.getX().multiply(uslovia[i][0]).add(tochka.getY().multiply(uslovia[i][1])),uslovia[i][2])== -1){
                cheking =false;
                break;
            }

        }
        return cheking;
    }

    private void findMinAndMaxValue(){
        for (Point x : listPoint){
            if(MMethod.compareTwoFraction(x.getX().abs(), maxX) == 1){
                maxX = (x.getX().abs());
            }
            if(MMethod.compareTwoFraction(x.getY().abs(), maxY) == 1){
                maxY = (x.getY().abs());
            }
        }
        maxX = maxX.multiply(new BigFraction(5));
        maxY = maxY.multiply(new BigFraction(5));   }

    public static Point Gaus(BigFraction[] f, BigFraction[] s) { /// не ставить в Ф уравнение типа (0 + б9 > -1910)
        if(s[1].doubleValue() == 0){
            BigFraction [] change = f;
            f =s;
            s= change;
        }

        BigFraction x;
        BigFraction y;
        BigFraction[] arr = new BigFraction[f.length];
        for (int i = 0; i < arr.length; i++){
            arr[i] = f[i].divide(f[0]);
            arr[i] = arr[i].multiply(s[0]);
         }
        for (int i = 0; i <arr.length ; i++) {
            s[i] = s[i].subtract(arr[i]);
        }
        y = s[2].divide(s[1]);
        x = f[1].multiply(y);
        x = f[2].subtract(x);
        x = x.divide(f[0]);
        return new Point(x,y);
    }

    public BigFraction []MV( BigFraction [][] Matrix, BigFraction[]Vector ){
        BigFraction[] result = new BigFraction[Matrix.length];

        for (int i = 0; i < Matrix.length ; i++) {
            BigFraction sum = new BigFraction(0);
            for (int j = 0; j < Matrix[0].length; j++) {
                sum = sum.add(Matrix[i][j].multiply(Vector[j]));
            }
            result[i] = sum;
        }
        return result;
    }
}
