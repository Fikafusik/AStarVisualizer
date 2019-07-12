import javax.swing.*;
import javax.swing.text.Document;

/**
 * Класс, расширяющий возможности стандартной текстовой панели:
 * добавляет возможность логировать действия внутрь компонента
 */
public class TextPaneLogger {
    /**
     * Композиция лучше наследования)0))
     * Графический компонент для вывода логов программы
     */
    private JTextPane textPane;

    public TextPaneLogger(JTextPane textPane){
        setTextPane(textPane);
    }

    /**
     * Сеттер для текстовой панели
     * @param textPane экземпляр текстовой панели
     */
    public void setTextPane(JTextPane textPane){
        this.textPane = textPane;
    }

    /**
     * Вывод сообщения на текстовую панель
     * @param msg сообщение для вывода
     */
    public void log(String msg){
        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), msg + "\n", null);
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
