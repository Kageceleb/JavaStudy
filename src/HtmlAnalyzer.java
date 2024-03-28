import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlAnalyzer {
    String tagRegex = "<\\/?(\\w+)>";

    public static void main(String[] args) throws Exception {
        String initialURL = args[0];
        HtmlAnalyzer htmlAnalyzer = new HtmlAnalyzer();
        String htmlText = htmlAnalyzer.getHtml(initialURL);
        String targetText = htmlAnalyzer.findNestedText(htmlText);
        System.out.println(targetText);
    }

    public String getHtml(String urlArg) throws Exception {
        String htmlContent = ""; // String para receber o texto html deste método
        try {
            URL url = new URL(urlArg);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // abre uma conexão com a url (line 21)
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder htmlContentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContentBuilder.append(line.trim());
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

    public String findNestedText(String htmlContent) throws Exception {
        String targetText = "";
        Stack<String> tagsStack = new Stack<>();
        int depthCounter = 0;
        int deepestTag = 0;
        Pattern tagPattern = Pattern.compile(tagRegex);
        Matcher findOut = tagPattern.matcher(htmlContent);
        int beginIndex=0; //seta o index onde começa o texto mais profundo (line 69 (nice!))
        int endIndex=0; //seta o index onde termina o texto mais profundo (line 57)
        while (findOut.find()) {
            String tag = findOut.group();
            String tagClear = findOut.group(1);
            if (tag.startsWith("</")) {
                // if(tagClear == tagsStack.peek()){System.out.println("tags "+tagClear+ " " + tagsStack.peek());}
                if(deepestTag == depthCounter){
                    endIndex=findOut.start();
                    targetText = htmlContent.substring(beginIndex,endIndex) ;           
                }
                depthCounter--;
                tagsStack.pop();
            } else {
                depthCounter++;
                tagsStack.push(tagClear.trim());
                if (depthCounter > deepestTag) {
                    deepestTag = depthCounter;
                    beginIndex=findOut.end();
                }
            
            }

        }

        return targetText;
    }

}