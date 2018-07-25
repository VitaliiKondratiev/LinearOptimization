package math;

import java.math.BigDecimal;

public class TableM {

    private BigFraction alfa[], betta[], c[],CsI[],CsII[],CjI[],CjII[],teta[], A[][];
    private int k,r,Fs[];

    public TableM(BigFraction[] alfa, BigFraction[] betta, BigFraction[] c, BigFraction[] teta, BigFraction[][] a, int k, int r,int[]Fs) {
        this.alfa = alfa;
        this.betta = betta;
        this.c = c;
        this.teta = teta;
        A = a;
        this.k = k;
        this.r = r;
        this.Fs = Fs;
    }

    public BigFraction[] getAlfa() {
        return alfa;
    }

    public BigFraction[] getBetta() {
        return betta;
    }

    public BigFraction[] getC() {
        return c;
    }

    public BigFraction[] getCsI() {
        return CsI;
    }

    public BigFraction[] getCsII() {
        return CsII;
    }

    public void setAlfa(BigFraction[] alfa) {
        this.alfa = alfa;
    }

    public void setBetta(BigFraction[] betta) {
        this.betta = betta;
    }

    public void setC(BigFraction[] c) {
        this.c = c;
    }

    public void setTeta(BigFraction[] teta) {
        this.teta = teta;
    }

    public void setA(BigFraction[][] a) {
        A = a;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setR(int r) {
        this.r = r;
    }

    public BigFraction[] getTeta() {
        return teta;
    }

    public BigFraction[][] getA() {
        return A;
    }

    public int getK() {
        return k;
    }

    public int getR() {
        return r;
    }

    public int[] getFs() {
        return Fs;
    }

    public void setFs(int[] fs) {
        Fs = fs;
    }

    private void fillingTable() {
        CsI = new BigFraction[Fs.length];
        CsII = new BigFraction[Fs.length];
        CjI = new BigFraction[A[0].length + A.length];
        CjII = new BigFraction[A[0].length + A.length];

        for (int i = 0; i < Fs.length; i++) {
            if (Fs[i] < A[i].length - (A.length -2)) {
                CsI[i] = new BigFraction(0);
                CsII[i] = c[Fs[i]-1];
            } else {
                CsI[i] = new BigFraction(-1);
                CsII[i] = new BigFraction(0);
            }
        }
        for (int i = 0; i < A[0].length + A.length; i++) {
            if (i <(c.length)) {
                CjI[i] = new BigFraction(0);
                CjII[i] = c[i];
            }
            else {
                CjI[i] = new BigFraction(-1);
                CjII[i] = new BigFraction(0);
            }
        }

    }

    @Override
    public String toString() {
        fillingTable();
        StringBuilder mas = new StringBuilder();
        StringBuilder format = new StringBuilder();
        Object [][] mainTable = new Object[A.length+5][A[0].length+5];
        for(int i=0;i<A[0].length+5;i++){
            if(i==0){ format.append("%"+(i+1)+"$-3.3s |"); }
            else if(i!=0 && i < 4){
                format.append("%"+(i+1)+"$7.7s |");
            }else{
                format.append("%"+(i+1)+"$"+17+"."+(14)+"s |");
            }
        }
        for (int i = 0; i < mainTable[0].length -1 ; i++) {
            if(  i == 4){
                mainTable[0][i] = "C`j";
                mainTable[1][i] = "C``j";
            }
            else if(i > 4){
                mainTable[0][i]=CjI[i-5].intValue();
                mainTable[1][i]=CjII[i-5].intValue();
            }
        }
        Object [] ogl = new Object[mainTable[0].length ];
        ogl[0] = "№";
        ogl[1] = "Cs`";
        ogl[2] = "Cs``";
        ogl[3] = "Fs";
        ogl[ogl.length -1] = "Θ";
        for (int i = 4; i < ogl.length-1 ; i++) {
            ogl[i]= ("A"+(i-4));
        }
        for (int i = 0; i < mainTable[0].length  ; i++) {
         mainTable[2][i] = ogl[i];
        }
        for (int i = 3; i < mainTable.length-4 ; i++) {
            mainTable[i][0] = (i-2);
            mainTable[i][1] = CsI[i-3].intValue();
            mainTable[i][2] = CsII[i-3].intValue();
            mainTable[i][3] = "A" + Fs[i-3];
            if( teta[i-3] != null)    { mainTable[i][mainTable[0].length-1] = teta[i-3].doubleValue();}
            mainTable[i+1][0] = (i - 1) ;
            mainTable[i+2][0] = i;
            mainTable[i+1][3] = "α"  ;
            mainTable[i+2][3] = "β";
            mainTable[i+3][0] = ((i - 1) +"'") ;
            mainTable[i+4][0] = (i+ "'");
            mainTable[i+3][3] = "α'"  ;
            mainTable[i+4][3] = "β'";

        }
        for (int i = 3; i < mainTable.length; i++) {
            for (int j = 4; j < mainTable[0].length; j++) {
                if(j<mainTable[0].length-1 && i <mainTable.length -2){
                    mainTable[i][j] = A[i-3][j-4].doubleValue();
                }
                else if(j<mainTable[0].length-1 && i == mainTable.length -2){
                    mainTable[i][j] = alfa[j-4].doubleValue();
                }
                else if(j<mainTable[0].length-1 && i == mainTable.length -1){
                    mainTable[i][j] = betta[j-4].doubleValue();
                }
            }
        }
        for (int i = 0; i < mainTable.length; i++) {
            for (int j = 0; j < mainTable[0].length; j++) {
                if(mainTable[i][j] == null){
                    mainTable[i][j] = "";
                }
            }
        }

        for (int i = 0; i < mainTable.length ; i++) {
            mas.append(String.format(format.toString(), mainTable[i]));
            mas.append("\n");
        }
        mas.append("\n");
        String[] delta = new String[alfa.length -1];
        for (int i = 1; i < alfa.length ; i++) {
            if(alfa[i].signum() == -1){
                delta[i-1]=("Δ"+i+ " < 0");
            }
            else if(alfa[i].signum() == 1){
                delta[i-1]=("Δ"+i+ " > 0");
            }
            else if(alfa[i].signum() == 0){
                if(betta[i].signum() == 0){
                    delta[i-1]=("Δ"+i+ " = 0");
                }
                if(betta[i].signum() == -1){
                    delta[i-1]=("Δ"+i+ " < 0");
                }
                else if(betta[i].signum() == 1){
                    delta[i-1]=("Δ"+i+ " > 0");
                }
            }

        }
        StringBuilder format1 = new StringBuilder();

        for(int i=0;i<A[0].length-1;i++){
            if(i==0){ format1.append("%"+(i+1)+"$65.6s "); }
            else{
                format1.append("%"+(i+1)+"$"+18+"."+7+"s ");
            }

        }
        mas.append(String.format(format1.toString(),delta));
        mas.append("\n\n");

        for (int j = 1; j <A[0].length; j++) {
            if ((alfa[j].signum() == -1) || (alfa[j].signum() == 0 && betta[j].signum() == -1)) {
                mas.append("Ω"+j+"={");
                boolean wr = false;
                for (int i = 0; i < A.length-2; i++) {
                    if (A[i][j].signum() == 1) {
                        mas.append(new BigDecimal(A[i][j].doubleValue()).setScale(4,BigDecimal.ROUND_FLOOR)+" ; ");
                        wr = true;
                    }
                }
                if(wr){
                mas.append("}≠Ø \n");
                }
                else{
                    mas.append("}= Ø \n");
                }
            }
        }
        return mas.toString();
    }
}
