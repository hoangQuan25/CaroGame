package caro.view;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import caro.bean.RoundedBorder;
import caro.bean.Setting;
import caro.bo.CaroAI;
import caro.dao.SettingDao;
import caro.values.Value;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class App extends JFrame implements MouseListener{

    private JPanel contentPane;
    private JPanel TableCells; // Panel chứa ma trận cells
    private Border cellBorder; // tạo đường viền của mỗi cell
    private JLabel[][] cell; // Ma trận cells
    private JLabel userClickedCell; // cell được user click chọn
    private JLabel aiClickedCell; // cell được AI click chọn
    private JLabel lblUserScore; // điểm của User
    private JLabel lblAIScore; // điểm của AI

    // Add these fields to your App class
    private JTextField sizeTextField;
    private JButton applyButton;

    private CaroAI caro;
    private Setting setting;
    private Notification notification;

    public static final int TEXT_CELL_SIZE = Value.TEXT_CELL_SIZE; // cỡ chữ trong mỗi cell

    private String currentPath; // đường dẫn hiện tại của project

    private final ButtonGroup buttonGroup = new ButtonGroup();


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App frame = new App();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Tạo game mới, clear màn chơi cũ
     */
//    public void newGame() {
//        setting = SettingDao.LoadSettingInfo();
//        caro = new CaroAI(setting.getMode());
//        userClickedCell = null;
//        aiClickedCell = null;
//        for (int i = 0; i < Value.SIZE; i++) {
//            for (int j = 0; j < Value.SIZE; j++) {
//                cell[i][j].setBackground(setting.getCellColor());
//                cell[i][j].setForeground(setting.getxColor());
//                cell[i][j].setText("");
//            }
//        }
//        if(setting.getMode() == 1) {
//            // cập nhật nước đi của AI
//            int x = caro.getNextX();
//            int y = caro.getNextY();
//            updateTableCells(x, y, Value.AI_VALUE);
//        }
//    }
    public void newGame() {
        setting = SettingDao.LoadSettingInfo();
        caro = new CaroAI(setting.getMode());
        userClickedCell = null;
        aiClickedCell = null;

        // Resize the cell array according to the new board size
        cell = new JLabel[Value.SIZE][Value.SIZE];
        TableCells.removeAll(); // Clear the TableCells panel before adding new cells
        TableCells.setLayout(new GridLayout(Value.SIZE, Value.SIZE, 0, 0)); // Reset layout

        // Reinitialize the cells
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                cell[i][j] = new JLabel();
                cell[i][j].setSize(Value.CELL_WIDTH, Value.CELL_WIDTH);
                cell[i][j].setOpaque(true);
                cell[i][j].setBorder(cellBorder);
                cell[i][j].setFont(new Font("Comic Sans MS", Font.BOLD, TEXT_CELL_SIZE));
                cell[i][j].setBackground(setting.getCellColor());
                cell[i][j].setForeground(setting.getxColor());
                cell[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                cell[i][j].addMouseListener(this);
                TableCells.add(cell[i][j]);
            }
        }

        // If AI plays first, make the AI's move
        if (setting.getMode() == 1) {
            int x = caro.getNextX();
            int y = caro.getNextY();
            updateTableCells(x, y, Value.AI_VALUE);
        }

        // Repaint the frame to reflect the changes
        revalidate();
        repaint();
    }


    public void updateTableCells(int x, int y, int player) {
        if(player == Value.AI_VALUE) {
            if(aiClickedCell != null) {
                aiClickedCell.setBackground(setting.getCellColor()); // đặt lại màu clickedCell cũ
            }
            aiClickedCell = cell[x][y];
            aiClickedCell.setForeground(setting.getoColor());
            aiClickedCell.setText("O");
            aiClickedCell.setBackground(Value.CLICK_CELL_COLOR); // làm nổi bật cell được AI chọn
        }
        else {
            cell[x][y].setBackground(setting.getCellColor()); // đặt lại màu clickedCell cũ
            cell[x][y].setText("X");
        }
    }

    public Notification getNotificationInstance() {
        if(notification == null) notification = new Notification();
        return notification;
    }

    // Method to load and resize the picture
    private ImageIcon loadAndResizeImage(String imagePath, int maxWidth, int maxHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // Calculate new dimensions while maintaining aspect ratio
//            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
//            int newWidth = (int) (width * scale);
//            int newHeight = (int) (height * scale);

            int newWidth = width / 3;
            int newHeight = height / 3;

            // Resize the image
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create the frame.
     */
    public App() {
        /*--------------Set các giá trị mặc định--------------*/
        setResizable(false);
        currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        setIconImage(Toolkit.getDefaultToolkit().getImage(currentPath+"\\file\\img\\icon3.png"));
        setTitle("SuperCaro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Value.SIZE*Value.CELL_WIDTH+3*Value.MARGIN+280, Value.SIZE*Value.CELL_WIDTH+50);//kích thước của mỗi phần thay đổi tự động khi thay đổi SIZE(số hàng/số cột)
        setLocationRelativeTo(null);


        setting = SettingDao.LoadSettingInfo();
        caro = new CaroAI(setting.getMode()); // khởi tạo CaroAI
        cellBorder = new LineBorder(Color.black, 1); // tạo border cho mỗi cell trong ma trận

        /*------------------Tạo các đối tượng------------------*/
        contentPane = new JPanel();
        contentPane.setBackground(setting.getBackgroundColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        TableCells = new JPanel();
        TableCells.setBackground(new Color(255, 255, 255));
        TableCells.setLayout(new GridLayout(Value.SIZE, Value.SIZE, 0, 0));
        TableCells.setFont(new Font("Tahoma", Font.PLAIN, 14));
        TableCells.setBounds(Value.MARGIN, Value.MARGIN, Value.SIZE*Value.CELL_WIDTH, Value.SIZE*Value.CELL_WIDTH);
        contentPane.add(TableCells);

        // Tạo ma trận và add vào TableCells
        cell = new JLabel[Value.SIZE][Value.SIZE];
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                cell[i][j] = new JLabel();
                cell[i][j].setSize(Value.CELL_WIDTH, Value.CELL_WIDTH); // kích cỡ mỗi cell
                cell[i][j].setOpaque(true);
                cell[i][j].setBorder(cellBorder);
                cell[i][j].setFont(new Font("Comic Sans MS", Font.BOLD, TEXT_CELL_SIZE));
                cell[i][j].setBackground(setting.getCellColor());
                cell[i][j].setForeground(setting.getxColor());
                cell[i][j].setHorizontalAlignment(SwingConstants.CENTER); // căn giữa chữ
                cell[i][j].addMouseListener(this); // add hàm bắt sự kiện click chuột
                TableCells.add(cell[i][j]); // add cell vào TableCells
            }
        }
        // Nếu chế độ AI đánh trước => cập nhật lượt đầu của AI
        if(setting.getMode() == 1) updateTableCells(caro.getNextX(), caro.getNextX(), Value.AI_VALUE);

        JPanel view = new JPanel();
        view.setBackground(new Color(250, 235, 215));
        view.setForeground(Color.BLACK);
        // view.setBounds(Value.SIZE*Value.CELL_WIDTH+2*Value.MARGIN, Value.MARGIN, 274, Value.SIZE*Value.CELL_WIDTH);
        // contentPane.add(view);
        view.setLayout(null);

        JLabel lbltitle = new JLabel("SuperCaro");
        lbltitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbltitle.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        lbltitle.setBounds(10, 5, 254, 40);
        view.add(lbltitle);

        JLabel smallTitle = new JLabel("A product of HUSTers!");
        smallTitle.setHorizontalAlignment(SwingConstants.CENTER);
        smallTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        smallTitle.setBounds(10, 30, 254, 50);
        view.add(smallTitle);

        // Add placeholder picture
        // Load and resize the picture
        ImageIcon imageIcon = loadAndResizeImage(currentPath + "\\file\\img\\hustlogo.png", 200, 200); // Update with your image path
        JLabel pictureLabel = new JLabel(imageIcon);
//        pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pictureLabel.setBounds(100, 80, imageIcon.getIconWidth(), imageIcon.getIconHeight()); // Adjust position as needed
        view.add(pictureLabel);

        JLabel lblMode = new JLabel("Mode");
        lblMode.setHorizontalAlignment(SwingConstants.LEFT);
        lblMode.setForeground(new Color(0, 0, 139));
        lblMode.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        lblMode.setBounds(10, 162, 254, 20);
        view.add(lblMode);

        JButton btnNewGame = new JButton("New game");
        btnNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Do you want to play a new game?", "Yes", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) newGame();
            }
        });
        btnNewGame.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        btnNewGame.setBounds(30, 390, 89, 37);
        btnNewGame.setBackground(new Color(255, 20, 147));
        btnNewGame.setForeground(new Color(85, 107, 47));
        btnNewGame.setOpaque(false);
        btnNewGame.setBorder(new RoundedBorder(10));
        view.add(btnNewGame);

        JButton btnExitGame = new JButton("Exit game");
        btnExitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Do you want to close the game?", "Yes", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) System.exit(0); // thoát game
            }
        });
        btnExitGame.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        btnExitGame.setOpaque(false);
        btnExitGame.setForeground(new Color(85, 107, 47));
        btnExitGame.setBorder(new RoundedBorder(10));
        btnExitGame.setBackground(new Color(255, 20, 147));
        btnExitGame.setBounds(156, 390, 89, 37);
        view.add(btnExitGame);

        JRadioButton rdbtnUserPlaysFirst = new JRadioButton("You play first");
        JRadioButton rdbtnAiPlaysFirst = new JRadioButton("AI plays first");
        if(setting.getMode() == 0) rdbtnUserPlaysFirst.setSelected(true);
        else rdbtnAiPlaysFirst.setSelected(true);

        rdbtnUserPlaysFirst.setForeground(new Color(107, 142, 35));
        rdbtnUserPlaysFirst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(setting.getMode() == 1) {
                    int result = JOptionPane.showConfirmDialog(null, "Do you want to change game mode?", "Yes", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        setting.setMode(0); // cập nhật mode
                        newGame(); // clear màn chơi cũ
                    }
                    else {
                        rdbtnUserPlaysFirst.setSelected(false);
                        rdbtnAiPlaysFirst.setSelected(true);
                    }
                }
            }
        });
        rdbtnUserPlaysFirst.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        buttonGroup.add(rdbtnUserPlaysFirst);
        rdbtnUserPlaysFirst.setOpaque(false);
        rdbtnUserPlaysFirst.setBounds(26, 192, 232, 23);
        view.add(rdbtnUserPlaysFirst);

        rdbtnAiPlaysFirst.setForeground(new Color(107, 142, 35));
        rdbtnAiPlaysFirst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(setting.getMode() == 0) {
                    int result = JOptionPane.showConfirmDialog(null, "Do you want to change game mode?", "Yes", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        setting.setMode(1); // cập nhật mode
                        newGame(); // clear màn chơi cũ
                    }
                    else {
                        rdbtnUserPlaysFirst.setSelected(true);
                        rdbtnAiPlaysFirst.setSelected(false);
                    }
                }
            }
        });
        rdbtnAiPlaysFirst.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        buttonGroup.add(rdbtnAiPlaysFirst);
        rdbtnAiPlaysFirst.setOpaque(false);
        rdbtnAiPlaysFirst.setBounds(26, 218, 232, 23);
        view.add(rdbtnAiPlaysFirst);

        JLabel lblSetting = new JLabel("Color:");
        lblSetting.setHorizontalAlignment(SwingConstants.LEFT);
        lblSetting.setForeground(new Color(0, 0, 139));
        lblSetting.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        lblSetting.setBounds(10, 254, 254, 20);
        view.add(lblSetting);

        JSeparator separator = new JSeparator();
        separator.setBackground(Color.GRAY);
        separator.setForeground(Color.DARK_GRAY);
        separator.setBounds(10, 369, 254, 2);
        view.add(separator);

        JButton btnIntroduce = new JButton("Introduce");
        btnIntroduce.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                notification = getNotificationInstance();
                notification.show("Information", "Information", Value.INTRODUCE_MESSAGE);
            }
        });
        btnIntroduce.setOpaque(false);
        btnIntroduce.setForeground(new Color(85, 107, 47));
        btnIntroduce.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        btnIntroduce.setBorder(new RoundedBorder(10));
        btnIntroduce.setBackground(new Color(255, 20, 147));
        btnIntroduce.setBounds(30, 438, 89, 37);
        view.add(btnIntroduce);

        JButton btnXColor = new JButton("Color X");
        btnXColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Color color = JColorChooser.showDialog(App.this,
                        "Select user's font color", setting.getxColor());
                if(color != null) {
                    setting.setxColor(color);
                    newGame();
                }
            }
        });
        btnXColor.setOpaque(false);
        btnXColor.setForeground(new Color(85, 107, 47));
        btnXColor.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnXColor.setBorder(new RoundedBorder(10));
        btnXColor.setBackground(new Color(255, 20, 147));
        btnXColor.setBounds(30, 289, 89, 28);
        view.add(btnXColor);

        JButton btnBackgroundColor = new JButton("Background color");
        btnBackgroundColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(App.this,
                        "Select background color", setting.getBackgroundColor());
                if(color != null) {
                    setting.setBackgroundColor(color);
                    contentPane.setBackground(color);
                }
            }
        });
        btnBackgroundColor.setOpaque(false);
        btnBackgroundColor.setForeground(new Color(85, 107, 47));
        btnBackgroundColor.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBackgroundColor.setBorder(new RoundedBorder(10));
        btnBackgroundColor.setBackground(new Color(255, 20, 147));
        btnBackgroundColor.setBounds(30, 328, 89, 28);
        view.add(btnBackgroundColor);

        JButton btnOColor = new JButton("Color O");
        btnOColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(App.this,
                        "Select AI's font color", setting.getoColor());
                if(color != null) {
                    setting.setoColor(color);
                    newGame();
                }
            }
        });
        btnOColor.setOpaque(false);
        btnOColor.setForeground(new Color(85, 107, 47));
        btnOColor.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnOColor.setBorder(new RoundedBorder(10));
        btnOColor.setBackground(new Color(255, 20, 147));
        btnOColor.setBounds(156, 289, 89, 28);
        view.add(btnOColor);

        JButton btnCellColor = new JButton("Cell color");
        btnCellColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(App.this,
                        "Select color of one cell", setting.getCellColor());
                if(color != null) {
                    setting.setCellColor(color);
                    newGame();
                }
            }
        });
        btnCellColor.setOpaque(false);
        btnCellColor.setForeground(new Color(85, 107, 47));
        btnCellColor.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCellColor.setBorder(new RoundedBorder(10));
        btnCellColor.setBackground(new Color(255, 20, 147));
        btnCellColor.setBounds(156, 328, 89, 28);
        view.add(btnCellColor);

        JSeparator separator1 = new JSeparator();
        separator1.setBackground(Color.GRAY);
        separator1.setForeground(Color.DARK_GRAY);
        separator1.setBounds(10, 490, 254, 2);
        view.add(separator1);

        JLabel changeSize = new JLabel("Change board's size:");
        changeSize.setHorizontalAlignment(SwingConstants.LEFT);
        changeSize.setForeground(new Color(0, 0, 139));
        changeSize.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        changeSize.setBounds(10, 500, 254, 20);
        view.add(changeSize);

        initSizeInput();
        view.add(sizeTextField);

        applyButton.setOpaque(false);
        applyButton.setForeground(new Color(85, 107, 47));
        applyButton.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        applyButton.setBorder(new RoundedBorder(10));
        applyButton.setBackground(new Color(255, 20, 147));
        applyButton.setBounds(156, 530, 89, 37);

        view.add(applyButton);


        // Calculate the total height needed for the view
        int totalHeight = calculateTotalHeight(view); // Implement this method to calculate the total height based on your component's contents

// Set the preferred size of the view component
        view.setPreferredSize(new Dimension(274, totalHeight));

// Create JScrollPane with the view
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBounds(Value.SIZE * Value.CELL_WIDTH + 2 * Value.MARGIN, Value.MARGIN, 274, Value.SIZE * Value.CELL_WIDTH);

        // Adjust the scroll speed
        scrollPane.setWheelScrollingEnabled(true); // Enable wheel scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Set vertical scroll speed
        scrollPane.getVerticalScrollBar().setBlockIncrement(32); // Set vertical block scroll speed
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16); // Set horizontal scroll speed
        scrollPane.getHorizontalScrollBar().setBlockIncrement(32); // Set horizontal block scrol

        contentPane.add(scrollPane);

    }

    private int calculateTotalHeight(JPanel view) {
        int totalHeight = 0;

        // Calculate the height of each component and sum them up
        for (Component component : view.getComponents()) {
            totalHeight += component.getHeight();
        }

        // Add extra padding if needed
        totalHeight += 20; // Adjust this value as per your requirement for padding

        return totalHeight;
    }

    // Add this method to your App class
    private void initSizeInput() {
        sizeTextField = new JTextField();
        sizeTextField.setBounds(30, 530, 80, 30);

        applyButton = new JButton("Apply");
        applyButton.setBounds(130, 500, 80, 30);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int newSize = Integer.parseInt(sizeTextField.getText());
                    if (newSize >= 6 && newSize <= 20) {
                        Value.SIZE = newSize; // Update SIZE constant
                        newGame(); // Restart the game
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid board size. Please enter a size between 6 and 20.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer size.");
                }
            }
        });
    }




    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent element) {
        int x = -1, y = -1; // lưu tọa độ
        // lấy tọa độ user click
        for (int i = 0; i < Value.SIZE; i++) {
            boolean fl = false;
            for (int j = 0; j < Value.SIZE; j++) {
                if(cell[i][j] == element.getSource()) { // nếu người dùng click vào ô này
                    x = i;
                    y = j;
                    fl = true;
                    break;
                }
            }
            if(fl) break; // dừng kiểm tra
        }

        /*kiểm tra số lần click của user*/
        if(element.getClickCount() == 1) { // người dùng click dạo(click 1 lần)
            if(userClickedCell != null && userClickedCell != aiClickedCell) {
                userClickedCell.setBackground(setting.getCellColor()); // đặt lại màu clickedCell cũ
            }
            userClickedCell = cell[x][y]; // cập nhật clickedCell
            userClickedCell.setBackground(Value.CLICK_CELL_COLOR); // làm nổi bật ô được click
        }

        else if(element.getClickCount() == 2) { // người dùng chọn đánh ô này
            if(caro.isClickable(x, y)) {//nếu ô này chưa được đánh
                caro.update(x, y, Value.USER_VALUE); // update matrix
                System.out.println("\n----------------------------------------------------------------------");
                System.out.println("User's play:" + x + " " + y);

                // Cập nhật bước đi của User
                updateTableCells(x, y, Value.USER_VALUE);
                // Kiểm tra trạng thái của bàn cờ sau khi User đánh
                if(checkResult(Value.USER_VALUE)) return;
                // Nếu user không thắng và bàn cờ chưa full thì đến lượt AI đánh
                caro.nextStep();

                // Cập nhật bước đi của AI
                x = caro.getNextX();
                y = caro.getNextY();
                updateTableCells(x, y, Value.AI_VALUE);

                // Kiểm tra trạng thái của bàn cờ sau khi AI đánh
                if(checkResult(Value.AI_VALUE)) return;
            }
        }
    }

    /**
     *
     */
    private boolean checkResult(int player) {
        if(player == Value.USER_VALUE) {
            boolean result = caro.checkWinner(Value.USER_VALUE);
            if(result == true) {
                System.out.println("User win!");
                JOptionPane.showMessageDialog(null, "You win!");
                return true; // kết thúc màn chơi
            }
        }
        else {
            boolean result = caro.checkWinner(Value.AI_VALUE);
            if(result == true) {
                System.out.println("AI wins!");
                JOptionPane.showMessageDialog(null, "AI wins!");
                return true;
            }
        }
        if(caro.isOver()) {
            System.out.println("Draw!");
            JOptionPane.showMessageDialog(null, "Draw!");
            newGame();
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
