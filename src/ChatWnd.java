import javax.swing.*;
import javax.swing.Box;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


public class ChatWnd extends JFrame {
    private JTextArea jTxtArea;
    private JTextField jTxtField;
    private PrintWriter printWriter;

    public ChatWnd(){
        // установка параметров объекта окна, с последующей инициализацией.
        setTitle("Chat Master");
        setBounds(100,100,400,500);
        setResizable(true);     // запрет на изменение размеров окна, в случае если используется свободная компановка
        setUndecorated(true);   // разрешение на изменение геометрии окна
        RoundRectangle2D shapeOfWindow = new RoundRectangle2D.Float(0,0, getWidth(), getHeight(), 25.0f, 25.0f); // создание формы окна
        setShape(shapeOfWindow);        // задание формы окна
        setLocationRelativeTo(null);    // установка окна по центру экрана
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // создание элементов управления
        setLayout(new BorderLayout()); // установка стиля компановки элементов управления(в том числе и JPanel)

        Font fontBtn = new Font("Verdana", Font.ITALIC|Font.BOLD, 17);
        Font fontTxtArea = new Font("Verdana", Font.ITALIC, 12);
        Font fontTxtField = new Font("Calibri", Font.PLAIN, 12);

        JButton sendBtn = new JButton("SEND");
        sendBtn.setFont(fontBtn);
        sendBtn.setForeground(Color.white);
        sendBtn.setBackground(new Color(25, 193, 130));
        sendBtn.setToolTipText("Send message");

        jTxtArea = new JTextArea();
        jTxtArea.setEditable(false);
        jTxtArea.setLineWrap(true);
        jTxtArea.setFont(fontTxtArea);
        jTxtArea.setForeground(new Color(30, 150, 10));
        JScrollPane jScrollPane = new JScrollPane(jTxtArea);

        jTxtField = new JTextField();
        jTxtField.setFont(fontTxtField);

        // создание панелей на форме для размещения элементов управления
        JPanel jPanel1 = new JPanel(new BorderLayout());
        JPanel jPanel2 = new JPanel(new BorderLayout());
        jPanel1.setBackground(Color.gray);
        jPanel2.setBackground(Color.orange);

        // раземещение панелей на форме
        add(jPanel1, BorderLayout.CENTER);
        add(jPanel2, BorderLayout.SOUTH);

        // размещение элементов управления на панелях
        jPanel1.add(jScrollPane, BorderLayout.CENTER);
        jPanel2.add(jTxtField, BorderLayout.CENTER);
        jPanel2.add(sendBtn, BorderLayout.EAST);

        // создание меню
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        JMenu menuAbout = new JMenu("About");
        JMenuItem mItemFileSettings= new JMenuItem("Settings");
        JMenuItem mItemFileExit= new JMenuItem("Exit");
        JMenuItem mItemEditCleanUpChat = new JMenuItem("Clean up chat");
        menuFile.add(mItemFileSettings);
        menuFile.addSeparator();
        menuFile.add(mItemFileExit);
        menuEdit.add(mItemEditCleanUpChat);
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(Box.createHorizontalGlue()); // распределение элементов меню семетрично против друг друга
        menuBar.add(menuAbout);
        setJMenuBar(menuBar);
        mItemFileSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
        mItemFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
        mItemEditCleanUpChat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));

        // установка Listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                printWriter.flush();
                printWriter.close();
            }
        });

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appendData2TxtArea(jTxtField.getText());
            }
        });

        jTxtField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTxtField.hasFocus())
                    jTxtField.select(0, jTxtField.getText().length());
                appendData2TxtArea(jTxtField.getText());
            }
        });

        mItemFileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mItemEditCleanUpChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTxtArea.setText("");
            }
        });

        menuBar.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseDragged(MouseEvent e) {
                 super.mouseDragged(e);
             }
             @Override
             public void mouseReleased(MouseEvent e) {
                 super.mouseReleased(e);
                 setBounds(e.getLocationOnScreen().x - getWidth()/2, e.getLocationOnScreen().y, getWidth(), getHeight());
             }
         });

        setVisible(true); // отображение окна

        try {
            printWriter = new PrintWriter(new FileWriter("db/ChatMaster.log", true));
            printWriter.write("======================" + (new SimpleDateFormat("yyyy.MM.dd").format(System.currentTimeMillis())) + "======================" + "\n");
        } catch (IOException e) {
            appendData2TxtArea(e.getMessage());
        }
    }

    public void appendData2TxtArea(String data){
        if (data.length() > 0) {
            String timeStamp = "[" + (new SimpleDateFormat("H:m:s")).format(System.currentTimeMillis()) + "] ";
            jTxtArea.append(timeStamp + data + "\n");
            printWriter.write(timeStamp + data + "\n");
            printWriter.flush();
            jTxtField.setText("");
        }
    }
}