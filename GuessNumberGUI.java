package guessnum;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GuessNumberGUI extends JFrame {
    private int secretNumber;
    private int attempts;
    private JTextField guessField;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private int minRange = 1;
    private int maxRange = 100;
    private JTextField minRangeField;
    private JTextField maxRangeField;
    private JRadioButton randomModeRadio;
    private DefaultListModel<String> historyListModel;
    private JLabel hintLabel;

    public GuessNumberGUI() {
        // 初始化游戏数据
        Random random = new Random();
        secretNumber = random.nextInt(maxRange - minRange + 1) + minRange;
        attempts = 0;
        historyListModel = new DefaultListModel<>();

        // 设置窗口属性
        setTitle("猜数字游戏");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // 窗口居中
        pack();

        // 设置窗口图标
        try {
            // 使用PNG格式图标并提供完整的路径诊断
            String iconFileName = "/game_icon.png";
            java.net.URL iconUrl = getClass().getResource(iconFileName);
            
            if (iconUrl == null) {
                // 打印类路径和当前目录帮助诊断
                System.out.println("图标资源未找到: " + iconFileName);
                System.out.println("当前类路径: " + System.getProperty("java.class.path"));
                System.out.println("当前工作目录: " + System.getProperty("user.dir"));
    
            }
            
            ImageIcon icon = new ImageIcon(iconUrl);
            MediaTracker tracker = new MediaTracker(new JPanel());
            tracker.addImage(icon.getImage(), 0);
            tracker.waitForID(0);
            
            if (tracker.statusID(0, true) == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
                System.out.println("图标加载成功: " + iconUrl);
            } else {
                System.out.println("图标文件加载失败，可能格式不支持: " + iconUrl);
            }
        } catch (Exception e) {
            System.out.println("无法加载图标文件: " + e.getMessage());
        }

        // 创建顶部控制面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 创建模式选择面板
        JPanel modePanel = new JPanel();
        modePanel.setBorder(BorderFactory.createTitledBorder("游戏模式选择"));
        modePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, modePanel.getPreferredSize().height));
        randomModeRadio = new JRadioButton("随机出题", true);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(randomModeRadio);
        modePanel.add(randomModeRadio);

        // 创建设置面板
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createTitledBorder("游戏设置"));
        settingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // 创建范围设置面板
        settingsPanel.add(new JLabel("最小值:"));
        minRangeField = new JTextField(String.valueOf(minRange), 5);
        settingsPanel.add(minRangeField);
        settingsPanel.add(new JLabel("最大值:"));
        maxRangeField = new JTextField(String.valueOf(maxRange), 5);
        settingsPanel.add(maxRangeField);
        JButton applyRangeButton = new JButton("应用范围");
        settingsPanel.add(applyRangeButton);

        topPanel.add(modePanel);
        topPanel.add(settingsPanel);
        add(topPanel, BorderLayout.NORTH);

        // 创建中央内容面板（左右布局）
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建游戏面板
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBorder(BorderFactory.createTitledBorder("游戏区域"));
        gamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gamePanel.setMinimumSize(new Dimension(0, 350));
        gamePanel.setPreferredSize(new Dimension(400, 350));

        JLabel titleLabel = new JLabel("猜数字游戏 (" + minRange + "-" + maxRange + ")");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 16));

        JLabel promptLabel = new JLabel("请输入你猜的数字:");
        guessField = new JTextField(10);
        guessField.setAlignmentX(Component.LEFT_ALIGNMENT);
        guessField.setMaximumSize(new Dimension(200, 30));
        JButton guessButton = new JButton("猜");
        guessButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageLabel = new JLabel("欢迎来到猜数字游戏！");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attemptsLabel = new JLabel("猜测次数: 0");
        attemptsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 添加提示按钮
        JButton hintButton = new JButton("获取提示");
        hintButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintLabel = new JLabel("提示: 点击按钮获取提示");
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintLabel.setForeground(Color.GRAY);

        gamePanel.add(titleLabel);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(promptLabel);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(guessField);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(guessButton);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(hintButton);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(hintLabel);
        gamePanel.add(Box.createVerticalStrut(15));
        gamePanel.add(messageLabel);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(attemptsLabel);

        // 创建右侧信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("游戏信息"));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // 猜测历史列表
        JLabel historyLabel = new JLabel("猜测历史:");
        historyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JList<String> historyList = new JList<>(historyListModel);
        historyList.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        historyScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyScrollPane.setPreferredSize(new Dimension(300, 200));
        historyScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        infoPanel.add(historyLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(historyScrollPane);

        centerPanel.add(gamePanel);
        centerPanel.add(infoPanel);
        add(centerPanel, BorderLayout.CENTER);

        // 添加作者信息面板
        JPanel authorPanel = new JPanel();
        authorPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel authorLabel = new JLabel("By 洛谷scratch_szc  uid:1260767");
        authorLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        authorLabel.setForeground(Color.GRAY);
        authorPanel.add(authorLabel);
        add(authorPanel, BorderLayout.SOUTH);

        // 应用范围按钮事件
        applyRangeButton.addActionListener(e -> {
            try {
                int newMin = Integer.parseInt(minRangeField.getText());
                int newMax = Integer.parseInt(maxRangeField.getText());
                if (newMin >= newMax) {
                    JOptionPane.showMessageDialog(this, "最小值必须小于最大值！", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                minRange = newMin;
                maxRange = newMax;
                titleLabel.setText("猜数字游戏 (" + minRange + "-" + maxRange + ")");
                JOptionPane.showMessageDialog(this, "范围已更新为 " + minRange + "-" + maxRange);
                resetGame();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 提示按钮事件
        hintButton.addActionListener(e -> {
            int range = maxRange - minRange;
            String hint;
            if (range <= 10) {
                hint = String.format("提示: 秘密数字在 %d-%d 之间", minRange, maxRange);
            } else {
                int mid = (minRange + maxRange) / 2;
                if (secretNumber <= mid) {
                    hint = String.format("提示: 秘密数字在 %d-%d 之间", minRange, mid);
                } else {
                    hint = String.format("提示: 秘密数字在 %d-%d 之间", mid+1, maxRange);
                }
            }
            hintLabel.setText(hint);
            hintLabel.setForeground(Color.BLUE);
        });

        // 添加按钮点击事件
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });

        // 添加回车键事件
        guessField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            if (guess < minRange || guess > maxRange) {
                JOptionPane.showMessageDialog(this, "请输入 " + minRange + "-" + maxRange + " 之间的数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                guessField.setText("");
                return;
            }
            attempts++;
            attemptsLabel.setText("猜测次数: " + attempts);

            // 记录猜测历史
            String result = guess < secretNumber ? "猜小了" : guess > secretNumber ? "猜大了" : "猜对了！";
            historyListModel.addElement(String.format("%d. 猜了 %d → %s", attempts, guess, result));

            if (guess < secretNumber) {
                messageLabel.setText("猜小了！再试试。");
                messageLabel.setForeground(Color.BLUE);
                minRange = guess + 1; // 更新最小值范围
            } else if (guess > secretNumber) {
                messageLabel.setText("猜大了！再试试。");
                messageLabel.setForeground(Color.RED);
                maxRange = guess - 1; // 更新最大值范围
            } else {
                messageLabel.setText("恭喜你猜对了！");
                messageLabel.setForeground(Color.GREEN);
                JOptionPane.showMessageDialog(this, "恭喜你猜对了！\n你一共猜了" + attempts + "次。");
                resetGame();
            }
            guessField.setText("");
            return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
            guessField.setText("");
            return;
        }
    }

    private void resetGame() {
        Random random = new Random();
        secretNumber = random.nextInt(maxRange - minRange + 1) + minRange;
        attempts = 0;
        attemptsLabel.setText("猜测次数: 0");
        messageLabel.setText("游戏已重置，开始新游戏吧！");
        messageLabel.setForeground(Color.BLACK);
        historyListModel.clear(); // 清空历史记录
        hintLabel.setText("提示: 点击按钮获取提示");
        hintLabel.setForeground(Color.GRAY);
        // 重置范围为初始值
        minRange = Integer.parseInt(minRangeField.getText());
        maxRange = Integer.parseInt(maxRangeField.getText());
    }

    public static void main(String[] args) {
        // 在事件调度线程中创建并显示GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuessNumberGUI().setVisible(true);
            }
        });
    }
}
