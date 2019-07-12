import javax.swing.*;
import javax.swing.text.Document;

public class TextPaneLogger {
    private JTextPane textPane;

    public TextPaneLogger(JTextPane textPane){
        setTextPane(textPane);
    }

    public void setTextPane(JTextPane textPane){
        this.textPane = textPane;
    }

    public void log(String msg){
        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), msg + "\n", null);
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
