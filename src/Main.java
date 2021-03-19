import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        readTheFile();

    }
    public static boolean check(String str){
        Pattern pattern = Pattern.compile("([1-9]+\\[.*\\])");
        Matcher matcher;
        matcher = pattern.matcher(str);
        if(matcher.find()){
            // System.out.println(matcher.group(0));
            return true;
        }//выводит список совпавших с паттерном строк
        return false;
    }

    public static void readTheFile(){
        try(BufferedReader bufreader = Files.newBufferedReader(Paths.get("src/Stroka.txt"),StandardCharsets.UTF_8)){

            String str;
            while((str = bufreader.readLine()) != null) {
                if(check(str)) { //содержить хотя бы одну пару "число[" и "]"
                    String readyStr = repeat(str);
                    System.out.println(readyStr);
                }
                else{
                    System.out.println("строка не содержит заданный шаблон");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static String repeat(String str){
        System.out.println(str);
        while(str.contains("[")&&(str.contains("]"))){
            str = separate(str).toString();
            //System.out.println(str);

        };
        return(str);
    }


    public static StringBuilder separate(String str){

        StringBuilder mainStr = new StringBuilder(str);
        StringBuilder result = new StringBuilder();

        boolean isOpen = false;
        int open = 0;
        int close = 0;

        while(!mainStr.isEmpty()) {
            StringBuilder bufCounter = new StringBuilder();
            StringBuilder firstPart = new StringBuilder();
            int i = 0;
            char currChar;

            while (mainStr.length() > i) {
                currChar = mainStr.charAt(i);
                if(currChar != '[') {
                    if (Character.isDigit(currChar)) {
                        bufCounter.append(currChar);
                    } else {
                        firstPart.append(currChar);
                        bufCounter.delete(0, bufCounter.length());
                    }
                }
                else{
                    isOpen=true;
                    break;
                }
                i++;
            }

            int counter = 0;
            if(!bufCounter.isEmpty()){
                counter = Integer.parseInt(bufCounter.toString());//сколько раз повторить строку - число[
            }

            i++;//получаем индекс знака стоящего после первой открытой скобки скобки

            if(isOpen) {
                StringBuilder bufText = new StringBuilder();
                StringBuilder lastPart = new StringBuilder();

                for (int j = i; j < mainStr.length(); j++) {

                    currChar = mainStr.charAt(j);

                    if(currChar == '['){
                        bufText = new StringBuilder();

                        for (int l = i; l < mainStr.length(); l++) {
                            currChar = mainStr.charAt(l);
                            bufText.append(currChar);

                            if(currChar == '['){
                                open++;
                            }
                            if (currChar == ']') {
                                close++;
                                if(open >= close){
                                    continue;
                                }

                                bufText.deleteCharAt(bufText.length()-1);
                                bufText = separate(bufText.toString());

                                if(l != mainStr.length()) {
                                    j = l;
                                }
                                currChar = mainStr.charAt(j);
                                break;
                            }
                        }
                    }


                    if (currChar == ']') {
                        isOpen = false;
                        for (int k = j+1; k < mainStr.length(); k++) {
                            currChar = mainStr.charAt(k);
                            lastPart.append(currChar);
                        }
                        break;
                    }

                    bufText.append(currChar);
                }


                result.append(firstPart);
                for (int k = 0; k < counter; k++) {
                    result.append(bufText);
                }
                result.append(lastPart);
            }
            mainStr.delete(0, mainStr.length());
        }
        return (result);

    }

}
