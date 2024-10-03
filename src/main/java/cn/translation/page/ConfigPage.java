package cn.translation.page;

import cn.translation.entity.Config;
import cn.translation.entity.IdKey;
import cn.translation.tool.GetConfig;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ConfigPage extends JDialog {

    private String filePath;
    private JPanel listjPanel;
    private JScrollPane jScrollPane;
    private JTextField namejTextField;
    private JTextField aidjTextField;
    private JTextField keyjTextField;
    private Config config1;
    private Integer num;
    private JDialog jd = this;
    private String oldName;

    public ConfigPage(Integer num, JFrame jFrame) throws HeadlessException {
        this.num = num;
        this.setLayout(new GridLayout(0, 1));
        GetConfig getConfig = new GetConfig();
        filePath = getConfig.getFilePath();
        config1 = getConfig.getConfig();
        List<IdKey> idKeys = null;
        if (num == 1) {
            idKeys = config1.getBaidu();
        } else if (num == 2) {
            idKeys = config1.getTengxun();
        }
        listjPanel = new JPanel();
        listjPanel.setLayout(new GridLayout(0, 1));
        for (int i = 0; i < idKeys.size(); i++) {
            IdKey idKey = idKeys.get(i);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(1, 3));
            JButton namejButton = new JButton("name:" + idKey.getName());
            namejButton.setEnabled(false);
            namejButton.setBackground(Color.white);
            JButton editjButton = new JButton("编辑");
            editjButton.setBackground(Color.white);
            editjButton.addActionListener(new editActionListener(idKey.getName()));
            JButton deletejButton = new JButton("删除");
            deletejButton.setBackground(Color.white);
            deletejButton.setForeground(Color.red);
            deletejButton.addActionListener(new deleteActionListener(idKey.getName()));
            jPanel.add(namejButton);
            jPanel.add(editjButton);
            jPanel.add(deletejButton);
            listjPanel.add(jPanel);
        }
        jScrollPane = new JScrollPane(listjPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBackground(Color.MAGENTA);
        JLabel namejLabel = new JLabel("name:");
        namejTextField = new JTextField(15);
        JLabel aidjLabel = new JLabel("aid     :");
        aidjTextField = new JTextField(15);
        JLabel keyjLabel = new JLabel("key    :");
        keyjTextField = new JTextField(15);
        JButton jButton = new JButton("保存配置");
        jButton.setBackground(Color.WHITE);
        jButton.addActionListener(new aActionListener());
        JPanel Panel = new JPanel();
        Panel.setLayout(new GridLayout(4, 1));
        JPanel namejPanel = new JPanel();
        JPanel aidjPanel = new JPanel();
        JPanel keyjPanel = new JPanel();
        JPanel ButtonjPanel = new JPanel();
        namejPanel.add(namejLabel);
        namejPanel.add(namejTextField);
        aidjPanel.add(aidjLabel);
        aidjPanel.add(aidjTextField);
        keyjPanel.add(keyjLabel);
        keyjPanel.add(keyjTextField);
        ButtonjPanel.add(jButton);
        Panel.add(namejPanel);
        Panel.add(aidjPanel);
        Panel.add(keyjPanel);
        Panel.add(ButtonjPanel);
        this.add(jScrollPane);
        this.add(Panel);
        this.setTitle("" +
                (num == 1 ? "百度" : num == 2 ? "腾讯" : "") +
                "配置页");
        this.setBounds(// 让新窗口与SwingTest窗口示例错开50像素。
                new Rectangle(
                        (int) jFrame.getBounds().getX() + 50,
                        (int) jFrame.getBounds().getY() + 50,
                        350,
                        300
                )
        );
        // 参数 APPLICATION_MODAL：阻塞同一 Java 应用程序中的所有顶层窗口（它自己的子层次
        this.setModalityType(ModalityType.APPLICATION_MODAL);    // 设置模式类型。
        this.setVisible(true);
    }

    public void updateListjPanel() {
        String s = JSON.toJSONString(config1);
        List<IdKey> idKeys = null;
        if (num == 1) {
            idKeys = config1.getBaidu();
        } else if (num == 2) {
            idKeys = config1.getTengxun();
        }
        listjPanel.removeAll();
        for (int j = 0; j < idKeys.size(); j++) {
            IdKey idKey = idKeys.get(j);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(1, 3));
            JButton namejButton = new JButton("name:" + idKey.getName());
            namejButton.setEnabled(false);
            namejButton.setBackground(Color.white);
            JButton editjButton = new JButton("编辑");
            editjButton.setBackground(Color.white);
            editjButton.addActionListener(new editActionListener(idKey.getName()));
            JButton deletejButton = new JButton("删除");
            deletejButton.setBackground(Color.white);
            deletejButton.setForeground(Color.red);
            deletejButton.addActionListener(new deleteActionListener(idKey.getName()));
            jPanel.add(namejButton);
            jPanel.add(editjButton);
            jPanel.add(deletejButton);
            listjPanel.add(jPanel);
        }
        jScrollPane.updateUI();
        jScrollPane.invalidate();
        jScrollPane.validate();
        jScrollPane.repaint();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class editActionListener implements ActionListener {

        public String name;

        @Override
        public void actionPerformed(ActionEvent e) {
            List<IdKey> idKeys = null;
            if (num == 1) {
                idKeys = config1.getBaidu();
            } else if (num == 2) {
                idKeys = config1.getTengxun();
            }
            for (IdKey idKey : idKeys) {
                if (idKey.getName() != null && idKey.getName().equals(name)) {
                    oldName = idKey.getName();
                    namejTextField.setText(idKey.getName());
                    aidjTextField.setText(idKey.getId());
                    keyjTextField.setText(idKey.getKey());
                    break;
                }
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class deleteActionListener implements ActionListener {

        public String name;

        @Override
        public void actionPerformed(ActionEvent e) {
            List<IdKey> idKeys = null;
            if (num == 1) {
                idKeys = config1.getBaidu();
            } else if (num == 2) {
                idKeys = config1.getTengxun();
            }
            for (IdKey idKey : idKeys) {
                if (idKey.getName() != null && idKey.getName().equals(name)) {
                    idKeys.remove(idKey);
                    break;
                }
            }
            if (num == 1) {
                config1.setBaidu(idKeys);
            } else if (num == 2) {
                config1.setTengxun(idKeys);
            }
            String s = JSON.toJSONString(config1);
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new FileWriter(filePath));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            printWriter.print(s);
            printWriter.close();
            updateListjPanel();
        }
    }

    public class aActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (
                    (namejTextField.getText() == null && aidjTextField.getText() == null && keyjTextField.getText() == null) ||
                            (namejTextField.getText().isEmpty() && aidjTextField.getText().isEmpty() && keyjTextField.getText().isEmpty())
            ) {
                JOptionPane.showMessageDialog(null, "(name,aid,key)都要填");
                return;
            }
            List<IdKey> idKeys = null;
            if (num == 1) {
                idKeys = config1.getBaidu();
            } else if (num == 2) {
                idKeys = config1.getTengxun();
            }
            int i = 0;
            Boolean exists = false;
            for (; i < idKeys.size(); i++) {
                IdKey idKey = idKeys.get(i);
                if ((oldName != null && oldName.equals(idKey.getName())) || idKey.getName().equals(namejTextField.getText())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                IdKey idKey = idKeys.get(i);
                idKey.setName(namejTextField.getText());
                idKey.setId(aidjTextField.getText());
                idKey.setKey(keyjTextField.getText());
                idKeys.set(i, idKey);
            } else {
                IdKey idKey = new IdKey();
                idKey.setName(namejTextField.getText());
                idKey.setId(aidjTextField.getText());
                idKey.setKey(keyjTextField.getText());
                idKeys.add(idKey);
            }
            if (num == 1) {
                config1.setBaidu(idKeys);
            } else if (num == 2) {
                config1.setTengxun(idKeys);
            }
            String s = JSON.toJSONString(config1);
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new FileWriter(filePath));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            printWriter.print(s);
            printWriter.close();
            updateListjPanel();
            oldName = null;
            namejTextField.setText(null);
            aidjTextField.setText(null);
            keyjTextField.setText(null);
        }
    }

}
