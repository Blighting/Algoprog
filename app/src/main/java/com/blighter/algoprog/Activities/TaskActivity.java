package com.blighter.algoprog.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blighter.algoprog.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class TaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        TextView tv_aboutTask = findViewById(R.id.tv_about_task);
        TextView tv_aboutInput = findViewById(R.id.tv_about_input);
        TextView tv_aboutOutput = findViewById(R.id.tv_about_output);
        TextView tv_title = findViewById(R.id.tv_title_in_task);
        String html = "\"<h1>Числа Фибоначи</h1><div>↵   <div class=\"legend\">↵↵↵<pПоследовательность чисел=\"\" Фибоначчи=\"\" определяется=\"\" следующим=\"\" образом:=\"\" <em=\"\">F<sub>1</sub> = <em>F<sub>2</sub></em> = 1, F<sub>n</sub> = F<sub>n-1</sub> + F<sub>n-2</sub>, при n &gt; 2 <p></p>↵↵   </pПоследовательность></div>↵↵   <div class=\"input-specification\">↵    <div class=\"section-title\">↵     Входные данные↵    </div>↵↵<p>В единственной строке входных данных записано натуральное число <em>n</em> (1<em>≤n</em><span>≤</span>45).</p> ↵↵   </div>↵↵   <div class=\"output-specification\">↵    <div class=\"section-title\">↵     Выходные данные↵    </div>↵↵<p>Вывести одно число F<sub>n</sub></p>↵↵    </div>↵    </div><div><div class=\"sample-tests\"><div class=\"section-title\">Примеры</div><div class=\"sample-test\"><div class=\"input\"><div class=\"title\">Входные данные</div><pre class=\"content\">2</pre></div><div class=\"output\"><div class=\"title\">Выходные данные</div><pre class=\"content\">1</pre></div></div><div class=\"sample-test\"><div class=\"input\"><div class=\"title\">Входные данные</div><pre class=\"content\">5</pre></div><div class=\"output\"><div class=\"title\">Выходные данные</div><pre class=\"content\">5</pre></div></div></div></div>\"";
        html = html.replace("&gt;", ">");
        html = html.replace("&lt;", "<");
        html = html.replaceAll("↵", "");
        html = html.replaceAll("\n", "");
        Document doc1 = Jsoup.parse(html);
        //убираем автоматическое удаление \n
        doc1.outputSettings(new Document.OutputSettings().prettyPrint(false));
        //заменяем все теги, которые значат \n на \\n
        doc1.select("br").after("\\n");
        doc1.select("p").before("\\n");
        Elements legend = doc1.getElementsByClass("legend");
        // все теги \\n заменяем на нормальные \n
        tv_aboutTask.setText(replacement(Jsoup.clean(legend.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false))));
        Elements aboutInput = doc1.getElementsByClass("input-specification");
        String aboutInputStr = aboutInput.html().replace("Входные данные", "");
        tv_aboutInput.setText(Jsoup.clean(replacement(aboutInputStr), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)));
        Elements aboutOutput = doc1.getElementsByClass("output-specification");
        String aboutOutputStr = aboutOutput.html().replace("Выходные данные", "");
        tv_aboutOutput.setText(Jsoup.clean(replacement(aboutOutputStr), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)));
        Elements title = doc1.getElementsByTag("h1");
        tv_title.setText(title.text());
    }

    private String replacement(String string) {
        string = string.replace("&gt;", ">");
        string = string.replace("&lt;", "<");
        string = string.replaceAll("\\\\n", "\n");
        return string;
    }
}
