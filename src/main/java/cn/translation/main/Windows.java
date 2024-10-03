package cn.translation.main;


import cn.translation.components.JTextPaneA;
import cn.translation.entity.Config;
import cn.translation.entity.IdKey;
import cn.translation.page.ConfigPage;
import cn.translation.tool.GetConfig;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.translation.service.Scripts.a1;
import static cn.translation.service.Translation.b1;
import static cn.translation.tool.GetAllFile.getAllFile;


public class Windows {

    private static JFrame jFrame;
    private static JTextPaneA jTextPane;
    private static JTextField jTextField;
    private static JButton jButton;
    private static JButton baidu;
    private static JButton tengxun;
    private static JButton nu;
    private static Integer index;
    private static IdKey idKey1;
    private static boolean isTranslationCompleted = true;//是否翻译完成


    public Windows() {
        jFrame = new JFrame();
        String src = "/img/512.png";
        Image img = null;
        try {
            URL url = Windows.class.getResource(src);
            img = ImageIO.read(Objects.requireNonNull(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jFrame.setIconImage(img);
        jFrame.setTitle("僵尸毁灭工程mod翻译器1.0");
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topWindow(topPanel);
        JScrollPane bottomPanel = bottomWindow();
        jFrame.add(topPanel, BorderLayout.NORTH);
        jFrame.add(bottomPanel, BorderLayout.CENTER);
//        jFrame.pack();
        jFrame.setVisible(true);
    }

    /**
     * 顶部窗口
     *
     * @param jPanel
     */
    public static void topWindow(JPanel jPanel) {
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new GridLayout(1, 3));
        baidu = new JButton("百度配置(右键编辑)");
        tengxun = new JButton("腾讯配置(右键编辑)");
        nu = new JButton("无配置");

        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("输入目录:");
        jTextField = new JTextField();
        jButton = new JButton("翻译");
        jButton.setEnabled(false);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (jTextPane) {
                    if (jTextField.getText().isEmpty()) {
                        return;
                    }
                    String filePath = jTextField.getText();
                    final Integer[] num1 = {0, 0};
                    final Boolean[] b1 = {false, false, false};
                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        jTextPane.setText(jTextPane.getText() +
                                "目录不存在" +
                                "\n");
                    }
                    List<File> translateList = new ArrayList<>();
                    List<File> itemList = new ArrayList<>();
                    getAllFile(dir, translateList, itemList, false, 0);
                    int num = translateList.size() + itemList.size();
                    jTextPane.setText(jTextPane.getText() +
                            "总共文件数:" + num +
                            "\n");
                    b1[2] = true;
                    isTranslationCompleted = false;
                    jButton.setEnabled(false);
                    jButton.setText("正在翻译");
                    new Thread(() -> {
                        num1[0] = b1(translateList, String.valueOf(index), jTextPane, idKey1);
                        b1[0] = true;
                    }).start();
                    new Thread(() -> {
                        num1[1] = a1(itemList, String.valueOf(index), jTextPane, idKey1);
                        b1[1] = true;
                    }).start();
                    new Thread(() -> {
                        while (true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                throw new RuntimeException(e1);
                            }
                            if (b1[0] && b1[1]) {
                                jTextPane.setText(jTextPane.getText() +
                                        "************总文件:" + num + ",总共使用字符数:" + (num1[0] + num1[1]) + "************" +
                                        "\n");
                                b1[0] = false;
                                b1[1] = false;
                                b1[2] = false;
                                isTranslationCompleted = true;
                                jButton.setText("翻译");
                                whetherToEnableTheTranslationButton(jTextField.getDocument());
                                break;
                            }
                        }
                    }).start();
                }
            }
        });
        jTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document document = e.getDocument();
                whetherToEnableTheTranslationButton(document);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document document = e.getDocument();
                whetherToEnableTheTranslationButton(document);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        baidu.addMouseListener(new aMouseListener(1));
        tengxun.addMouseListener(new aMouseListener(2));
        nu.addMouseListener(new aMouseListener(3));
        baidu.setBackground(Color.WHITE);
        tengxun.setBackground(Color.WHITE);
        nu.setBackground(Color.WHITE);
        jButton.setBackground(Color.WHITE);
        jPanel1.add(baidu);
        jPanel1.add(tengxun);
        jPanel1.add(nu);
        jPanel.add(jPanel1, BorderLayout.NORTH);
        jPanel.add(jLabel, BorderLayout.WEST);
        jPanel.add(jTextField, BorderLayout.CENTER);
        jPanel.add(jButton, BorderLayout.EAST);
    }

    /**
     * 监听文本判断是否开启翻译按钮
     *
     * @param document 要监听的文本
     */
    public static void whetherToEnableTheTranslationButton(Document document) {
        String text = null;
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        File file = new File(text);
        if (file.isDirectory() && index != null && (index == 1 || index == 2 || index == 3)&&isTranslationCompleted) {
            jButton.setEnabled(true);
        } else {
            jButton.setEnabled(false);
        }
    }

    /**
     * 底部窗口
     *
     * @return
     */
    public static JScrollPane bottomWindow() {
        jTextPane = new JTextPaneA();
        jTextPane.setFont(new Font("黑体", Font.PLAIN, 14));
        JScrollPane jScrollPane = new JScrollPane(jTextPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return jScrollPane;
    }

    public static class aMouseListener implements MouseListener {
        private Integer num;

        public aMouseListener() {
        }

        public aMouseListener(Integer num) {
            this.num = num;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int button = e.getButton();

            if (button == MouseEvent.BUTTON1) {
                if (num == 1) {
                    if (idKey1 != null) {
                        baidu.setBackground(Color.lightGray);
                        tengxun.setBackground(Color.WHITE);
                        nu.setBackground(Color.WHITE);
                        index = 1;
                        whetherToEnableTheTranslationButton(jTextField.getDocument());
                    }
                    PopupMenuPage popupMenu = new PopupMenuPage(num, baidu, e, idKey1);
                } else if (num == 2) {
                    if (idKey1 != null) {
                        baidu.setBackground(Color.WHITE);
                        tengxun.setBackground(Color.lightGray);
                        nu.setBackground(Color.WHITE);
                        index = 2;
                        whetherToEnableTheTranslationButton(jTextField.getDocument());

                    }
                    PopupMenuPage popupMenu = new PopupMenuPage(num, tengxun, e, idKey1);
                } else if (num == 3) {
                    baidu.setBackground(Color.WHITE);
                    tengxun.setBackground(Color.WHITE);
                    nu.setBackground(Color.lightGray);
                    index = 3;
                    idKey1 = null;
                    whetherToEnableTheTranslationButton(jTextField.getDocument());
                }
            }
            if (button == MouseEvent.BUTTON3) {
                if (num == 1) {
//                    弹出配置页面
                    ConfigPage configPage = new ConfigPage(num, jFrame);

                } else if (num == 2) {
                    ConfigPage configPage = new ConfigPage(num, jFrame);

                } else if (num == 3) {
//                    ConfigPage configPage = new ConfigPage(num, jFrame);

                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    @Data
    public static class PopupMenuPage extends JPopupMenu {
        //菜单
        private ArrayList<JMenu> jMenuArrayList;
        //菜单项
        public ArrayList<JMenuItem> jMenuItemArrayList;
        private Config config;
        private Integer num;
        private JPopupMenu JPopupMenu = this;
        private JButton jButton;

        public PopupMenuPage(Integer num, JButton jButton, MouseEvent e, IdKey idKey2) {
            this.num = num;
            idKey1 = idKey2;
            this.jButton = jButton;
            config = new GetConfig().getConfig();
            List<IdKey> idKeys = null;
            if (num == 1) {
                idKeys = config.getBaidu();
            } else if (num == 2) {
                idKeys = config.getTengxun();
            }
//初始化菜单
            String[] menuNames = new String[]{"请选择一项配置"};
            jMenuArrayList = new ArrayList<>();
            jMenuItemArrayList = new ArrayList<>();
            for (int i = 0; i < menuNames.length; i++) {
                JMenu jMenu = new JMenu(menuNames[i]);
                for (int j = 0; j < idKeys.size(); j++) {
                    IdKey idKey = idKeys.get(j);
                    JMenuItem jMenuItem = new JMenuItem(
                            ((idKey1 != null && idKey1.getName() != null && idKey1.getName().equals(idKey.getName()) && idKey1.getId().equals(idKey.getId())) ? "***" : "") +
                                    idKey.getName() +
                                    ((idKey1 != null && idKey1.getName() != null && idKey1.getName().equals(idKey.getName()) && idKey1.getId().equals(idKey.getId())) ? "***" : "")
                    );
                    jMenuItem.addActionListener(new bActionListener(idKey));
                    jMenuItemArrayList.add(jMenuItem);
                    jMenu.add(jMenuItem);
                }
                jMenuArrayList.add(jMenu);
            }
            //注册第一个菜单
            for (JMenu jMenu : jMenuArrayList) {
                this.add(jMenu);
            }
            this.show(jButton, e.getX(), e.getY());
        }

        @Data
        @AllArgsConstructor
        public class bActionListener implements ActionListener {

            private IdKey idKey;

            @Override
            public void actionPerformed(ActionEvent e) {
                List<IdKey> idKeys = null;
                if (num == 1) {
                    idKeys = config.getBaidu();
                } else if (num == 2) {
                    idKeys = config.getTengxun();
                }
                for (IdKey key : idKeys) {
                    if (key.getName().equals(idKey.getName())) {
                        for (int i = 0; i < jMenuItemArrayList.size(); i++) {
                            JMenuItem jMenuItem = jMenuItemArrayList.get(i);
                            if (jMenuItem.getText().equals(idKey.getName())) {
                                jMenuItem.setText("***" + jMenuItem.getText() + "***");
                            }
                        }
                        idKey1 = idKey;
                        JPopupMenu.setName(JSON.toJSONString(idKey));
                        if (num == 1) {
                            baidu.setBackground(Color.lightGray);
                            tengxun.setBackground(Color.WHITE);
                            nu.setBackground(Color.WHITE);
                            index = 1;
                        } else if (num == 2) {
                            baidu.setBackground(Color.WHITE);
                            tengxun.setBackground(Color.lightGray);
                            nu.setBackground(Color.WHITE);
                            index = 2;
                        }
                    }
                }
            }
        }

    }


}
