import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * Created by Dell on 15.05.2016.
 */
public class DirectoryScanner {

    static Scanner in = new Scanner(System.in);
    static String maska;
    static String inputDir;
    static String outputDir;
    static boolean includeSubFolder;
    static boolean autoDelete;
    static long waitInterval;
    static boolean checker = true; //Проверяет, есть ли ошибки во входных данных
    static String error = "";  //Сообщение об ошибке
    static boolean alreadyStarted = true; //Проверяет работает ли сканер в данный момент( если да, то новый скан создать нельзя)


    public static void main(String[] args) throws Exception {


        String a1 ;
        String a ;
        Thread myThread = new Thread();
        while (true) {
            a1 = in.next();
            a = in.nextLine();


            if (a1.equals("exit")) {
                myThread.stop();
                System.out.println("Поток закончен: ");
                alreadyStarted = true;

            }
            if (a1.equals("scan")) {
                if (alreadyStarted) {
                    myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                System.out.println("Сканирование началось:");
                                try {
                                    directoryScanner(inputDir, outputDir);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Сканирование закончилось: ");
                                try {
                                    sleep(waitInterval);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    inputData(a);



                    if (checker) {
                        myThread.start();
                        alreadyStarted = false;
                    }


                    if (!checker) {


                        System.out.println(error);
                        error = "";
                        checker = true;
                    }



                }
                else {
                    System.out.println("Сканирование уже идёт.");
                }


            }


        }
    }

    public static String kavichkiRemover(String a){
        char[] aa = a.toCharArray();
        a = "";
        for(int i = 0; i<aa.length; i++){
            if(aa[i] !='\"'){
                a = a+ aa[i];
            }
        }
        return a;
    }
    //Уберает кавычки( используется в названиях дирректорий и в маске)


    public static void inputData(String inputData) {
        String[] mass = inputData.split(" ");

        for (int i = 0; i < mass.length; i++)
            switch (mass[i]) {
                case "-input":
                    inputDir = kavichkiRemover(mass[++i]);
                    File myFile = new File(inputDir);
                    if (!myFile.isDirectory()) {
                        checker = false;
                        error = error + "Illegal symbols in input path: " + inputDir + "\n";
                    }
                    break;
                case "-output":
                    outputDir = kavichkiRemover(mass[++i]);
                    File myFile1 = new File(outputDir);
                    if (!myFile1.isDirectory()) {
                        checker = false;
                        error = error + "Illegal symbols in output path: " + outputDir + "\n";
                    }
                    break;
                case "-mask":
                    maska = kavichkiRemover(mass[++i]);
                    char[] a = maska.toCharArray();
                    char[] none = new char[]{'!', '@', '#', '$', '%', '^', '&', '(', ')', '_', '+', '№', ';', '%', ':'};
                    int check = 0;
                    for (int j = 0; j < a.length; j++) {
                        for (int k = 0; k < none.length; k++) {
                            if (a[j] == none[k]) {
                                check++;
                            }
                        }
                    }
                    if (check != 0) {
                        checker = false;
                        error = error + "Illegal symbols in mask: " + maska + "\n";
                    }

                    break;
                case "-waitInterval":
                    waitInterval = Integer.parseInt(mass[++i]);

                    if (waitInterval < 0) {
                        checker = false;
                        error = error + "Incorrect value for waitInterval: " + waitInterval + "(Must be possitive int value)" + "\n";

                    }
                    break;
                case "-includeSubfolder":
                    ++i;
                    if (mass[i].equals("true") || mass[i].equals("false")) {
                        includeSubFolder = Boolean.valueOf(mass[i]);
                    } else {
                        checker = false;
                        error = error + "Invalid value for IncludeSubfolder: " + mass[i] + "(Must be true or false)" + "\n";
                    }

                    break;
                case "-autoDelete":
                    ++i;
                    if (mass[i].equals("true") || mass[i].equals("false")) {
                        autoDelete = Boolean.valueOf(mass[i]);
                    } else {
                        checker = false;
                        error = error + "Invalid value for autoDelete: " + mass[i] + "(Must be true or false)" + "\n";
                    }
                    break;
            }
    }
    // Ввод данных

    public static String maskRemaker(String mask){

        String[] aa = mask.split("");
        mask="";
        for(int i=0; i<aa.length;i++ ){
            if(aa[i].equals(".")){
                aa[i] = "\\.";
            }
            if(aa[i].equals("*")){
                aa[i] = ".*";
            }
            mask = mask + aa[i];
        }
        return mask;

    }
    //Изменяет маску в регулярные выражения

    public static boolean mask(String mask){
        Pattern p = Pattern.compile(maskRemaker(maska));
        Matcher m = p.matcher(mask);
        return m.matches();
    }
    //Созадёт маску


    public static void directoryScanner(String inputDir, String outputDir) throws Exception {

            File dir1 = new File(inputDir);

            for (File a : dir1.listFiles()) {
                if (a.isDirectory()) {
                    if (includeSubFolder) {
                        directoryScanner(a.getAbsolutePath(), outputDir);
                    } else {
                        continue;
                    }
                }


                if (mask(a.getName())) {
                    File newFile = new File(outputDir + "\\", a.getName());
                    newFile.createNewFile();
                    if (autoDelete) {
                        a.delete();
                    }
                }
            }

    }
    //Наш сканнер
}
