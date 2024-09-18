package org.tomstools.utils.classsearcher.shell;

import org.tomstools.utils.classsearcher.ClassSearcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class UIClassSearcher {
    public static void main(String[] args) {
        int width = 500;
        int height = 340;
        //获取屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("Java类搜索");
        frame.setSize(width, height);

        //让窗口居中显示 
        frame.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = createPanel();
        frame.add(panel);
        // 设置界面可见
        frame.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("待搜索目录：");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        final JTextField txtDirectory = new JTextField(20);
        txtDirectory.setBounds(100, 20, 300, 25);
        panel.add(txtDirectory);

        JButton btnSelectFile = new JButton("选择");
        btnSelectFile.setBounds(410, 20, 50, 25);
        panel.add(btnSelectFile);
        btnSelectFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (!txtDirectory.getText().trim().isEmpty()){
                    fileChooser.setCurrentDirectory(new File(txtDirectory.getText().trim()));
                }
                fileChooser.showOpenDialog(panel);//显示打开的文件对话框
                File f = fileChooser.getSelectedFile();//使用文件类获取选择器选择的文件
                if (null != f) {
                    String s = f.getAbsolutePath();//返回路径名
                    txtDirectory.setText(s);
                }
            }
        });
        JLabel passwordLabel = new JLabel("带搜素类名：");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);


        final JTextField txtClassName = new JTextField(20);
        txtClassName.setBounds(100, 50, 300, 25);
        panel.add(txtClassName);

        // 创建按钮
        JButton btnSearch = new JButton("开始搜索");
        btnSearch.setBounds(10, 80, 150, 25);
        panel.add(btnSearch);
        JLabel lblResult = new JLabel("");
        lblResult.setBounds(200, 80, 300, 25);
        panel.add(lblResult);

        final JTextArea txtResult = new JTextArea();
        txtResult.setBounds(10, 110, 450, 150);
        txtResult.setAutoscrolls(true);
        txtResult.setEditable(false);
        panel.add(txtResult);

        JScrollPane scroller = new JScrollPane(txtResult);

        scroller.setBounds(10, 110, 450, 150);
        scroller.setVisible(true);
        panel.add(scroller, BorderLayout.EAST);


        JProgressBar progressBar = new JProgressBar(0,5);
        progressBar.setBounds(10,270,450,20);
        //progressBar.setStringPainted(true);
        progressBar.setValue(0);
        panel.add(progressBar);

        AtomicBoolean isProcessing = new AtomicBoolean(false);
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dir = txtDirectory.getText();
                String className = txtClassName.getText();
                if (dir.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "\"待搜素目录\"不能为空！");
                    txtDirectory.setFocusable(true);
                    txtDirectory.grabFocus();
                } else if (className.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "\"待搜素类名\"不能为空！");
                    txtClassName.grabFocus();
                    txtDirectory.setFocusable(true);
                } else {
                    btnSearch.setEnabled(false);
                    btnSearch.setText("搜索中...");
                    lblResult.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isProcessing.set(true);
                            long st = System.currentTimeMillis();
                            List<String> fileNames = ClassSearcher.getInstance().search(dir.trim(), className.trim());
                            long time = System.currentTimeMillis() - st;
                            txtResult.setText("");
                            if (!fileNames.isEmpty()) {
                                fileNames.forEach(fileName -> txtResult.append(fileName + "\n"));
                                txtResult.selectAll();
                                txtResult.copy();
                            }

                            lblResult.setText("结果数： " + fileNames.size() + "  耗时：" + (time > 1000? time/1000 + "秒" : time + "毫秒"));
                            btnSearch.setEnabled(true);
                            btnSearch.setText("开始搜索");
                            isProcessing.set(false);
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isProcessing.get()) {
                                int value = (progressBar.getValue() + 1) % progressBar.getMaximum();
                                progressBar.setValue(value);
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            progressBar.setValue(progressBar.getMaximum());
                        }
                    }).start();
                }
            }
        });
        return panel;
    }
}
