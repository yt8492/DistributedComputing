import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ex10_rest {

    private static Gson gson = new Gson();

    static private String call_url(String base_url, String function, Map<String, String> args) throws Exception {
        String[] escaped = args.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .toArray(String[]::new);
        String argslist = String.join("&", escaped);
        URL url = new URL(base_url + "/" + function + "?" + argslist);
        System.out.println("Calling: " + url);
        InputStreamReader is = new InputStreamReader(url.openStream());
        int c;
        String r = "";
        while ((c = is.read()) != -1)
            r += (char) c;
        return r;
    }

    static public void main(String args[]) throws Exception {
        Map<String, String> queries = new HashMap<>();
        queries.put("sp", "sal?on");
        String response = call_url("https://api.datamuse.com", "words", queries);
        DataJson[] json = gson.fromJson(response, DataJson[].class);
        System.out.println(Arrays.toString(json));
    }
}

class DataJson {
    String word;
    int score;

    @Override
    public String toString() {
        return "{" +
                "word=\"" + word + '"' +
                ",score=" + score +
                '}';
    }
}
