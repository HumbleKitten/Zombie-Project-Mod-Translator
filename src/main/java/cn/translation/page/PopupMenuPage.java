package cn.translation.page;

import cn.translation.entity.Config;
import cn.translation.entity.IdKey;
import cn.translation.tool.GetConfig;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

@Data
public class PopupMenuPage extends JPopupMenu {

    private IdKey idKey1;
    //菜单
    private ArrayList<JMenu> jMenuArrayList;
    //菜单项
    public ArrayList<JMenuItem> jMenuItemArrayList;
    private Config config;
    private Integer num;
    private JPopupMenu JPopupMenu = this;

    public PopupMenuPage(Integer num,JButton jButton, MouseEvent e, IdKey idKey1) {
        this.num = num;
        this.idKey1 = idKey1;
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
                        ((idKey1 != null && idKey1.getName() != null && idKey1.getName().equals(idKey.getName())) ? "***" : "") +
                                idKey.getName() +
                                ((idKey1 != null && idKey1.getName() != null && idKey1.getName().equals(idKey.getName())) ? "***" : "")
                );
                jMenuItem.addActionListener(new aActionListener(idKey));
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
    public class aActionListener implements ActionListener {

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
                    System.out.println(idKey);
                    idKey1 = idKey;
                    JPopupMenu.setName(JSON.toJSONString(idKey));
                }
            }
        }
    }

}
