import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlAnalyzer {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        URL url = new URL("http://192.168.1.7:8080");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder htmlContent = new StringBuilder();
        String line;
        Stack<String> tagsStack = new Stack<>();
        int depthCounter = 0;
        int deepestTag = 0;
        String targetText = "";
        while ((line = reader.readLine()) != null) {
            Pattern allTags = Pattern.compile("<[^/<>]+(?:\\s+[^<>]+)*>|<\\/[^<>]+>");
            Matcher findOut = allTags.matcher(line);
            while (findOut.find()) {
                String tag = findOut.group();
                if (tag.startsWith("</")) {

                    depthCounter--;
                    tagsStack.pop();
                    System.out.println("removido " + tag + " profundidade " + depthCounter);
                } else {
                    depthCounter++;
                    if (depthCounter > deepestTag) {
                        deepestTag = depthCounter;

                    }
                    tagsStack.push(tag);
                    System.out.println("adicionado " + tag + " profundidade " + depthCounter);
                }
            }

            htmlContent.append(line);
            if (!line.startsWith("<") && depthCounter == deepestTag) {targetText=line;}
        }
        System.out.println(tagsStack);
        reader.close();
        is.close();
        conn.disconnect();
        System.out.println(htmlContent.toString());
        System.out.println("the Deepst level is " + deepestTag);
        System.out.println("o texto mais profundo é " + targetText);
    }
}