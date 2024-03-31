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
        String initialURL = args[0];
        String htmlText = HtmlAnalyzer.getHtml(initialURL);
        String targetText = HtmlAnalyzer.findNestedText(htmlText);
        System.out.println(targetText);
    }

    public static String getHtml(String urlArg) {
        String htmlContent = ""; // String para receber o texto html deste método
        try {
            URL url = new URL(urlArg);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder htmlContentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContentBuilder.append(line);
                htmlContentBuilder.append("\n");
            }
            htmlContent = htmlContentBuilder.toString();
            reader.close();
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("URL connection error");
        }
        return htmlContent;
    }

    public static String findNestedText(String htmlContent) {
        String targetText = "";
        Stack<String> tagsStack = new Stack<>();
        int deepestTag = 0;
        Pattern tagPattern = Pattern.compile("<\\/?(\\w+)>");
        Scanner scanner = new Scanner(htmlContent);
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                Matcher tagMatched = tagPattern.matcher(line);
                if (tagMatched.find()) {
                    String tagName = tagMatched.group(1);
                    if (line.startsWith("</")) {
                        String lastTagInStack = tagsStack.pop();
                        if (!lastTagInStack.equals(tagName)) {
                            throw new Error("malformed HTML");
                        }
                    } else if (line.startsWith("<")) {
                        tagsStack.push(tagName);
                    }
                } else {
                    if (tagsStack.size() > deepestTag) {
                        targetText = line;
                        deepestTag = tagsStack.size();
                    }
                }
            }
        } catch (Error e) {
            targetText = "";
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
        return targetText;
    }

}