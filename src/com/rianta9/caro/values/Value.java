package caro.values;

import java.awt.Color;

public class Value {
    public static final String INTRODUCE_MESSAGE =
            "Meet Our Development Team: " + "\r\n"
                    + "Lê Quốc Việt 20215165"
                    + "\r\n"
                    + "Chu Việt Anh 2021xxxx"
                    + "\r\n"
                    + "Võ Sơn Long 2021xxxx"
                    + "\r\n"
                    + "Nguyễn Hoàng Quân 20215127"
                    + "\r\n"
            + "\r\n"
            + "\r\n"
            + "We are passionate developers who have come together to bring you the best Caro game experience."
            + " Caro, also known as Gomoku, is a classic game of strategy and skill."
            + " It's a favorite pastime for many, especially among students, and involves a simple yet deep gameplay mechanic."
            + "\r\n"
            + "\r\n"
            + "History and Rules of Caro"
            + "\r\n"
            + "\r\n"
            + "Caro – a very popular game in Vietnam, beloved by many, especially students."
            + " Just a piece of graph paper and two pens are all you need to start a real competition."
            + "\r\n"
            + "\r\n"
            + "1. History of Caro"
            + "\r\n"
            + "The exact origins of Caro remain unclear. It is rumored to have appeared over 2000 years BC in China."
            + " However, evidence suggests it also existed in ancient Greece and pre-Columbian America."
            + "\r\n"
            + "After its inception, Caro spread widely to various countries, resulting in numerous variations and significant milestones in its history."
            + "\r\n"
            + "Initially called Wutzu in China, it became known as Gomoku in Japan, where it also had different regional names such as Kakugo, gomoku-narabe, Itsutsu-ishi, and more."
            + "\r\n"
            + "\r\n"
            + "2. Game Rules"
            + "\r\n"
            + "Originally played with Go pieces (black and white stones) on a Go board (19×19), black goes first, and players alternately place their stones on empty intersections."
            + " The winner is the first to achieve an unbroken row of five stones horizontally, vertically, or diagonally."
            + " Since stones are not moved or removed once placed, the game can also be played with pen and paper."
            + "\r\n"
            + "In Vietnam, it is commonly played on student notebook paper with pre-drawn grids, using circles (O) and crosses (X) to represent the two sides. The first to align five marks in a row, column, or diagonal wins."
            + "\r\n"
            + "In Caro, the first player has a significant advantage. To balance this, additional rules limit the first player's (black's) advantage and enhance the second player's (white's) defensive capabilities."
            + "\r\n"
            + "\r\n"
            + "3. Versions of Caro"
            + "\r\n"
            + "Although it may seem simple at first glance, the Caro game we commonly play is an older version. The game has evolved into various formats with more complex rules, demanding quick, logical thinking and keen observation skills for success."
            + "\r\n"
            + "Caro, or Gomoku in different languages, is a captivating board game of abstract strategy."
            + " Played on a 15x15 grid, players alternate placing stones at the intersections. The first move is made by black in the center. The goal is to align five stones in a row, but black must avoid certain patterns like overlines and double-threes to prevent an unfair advantage."
            + "\r\n"
            + "\r\n"
            + "Despite its apparent simplicity, Caro is a complex and intriguing game, especially suitable for students with a knack for logical thinking."
            + "\r\n"
            + "Source: cothu.vn";


    /**
     *  số hàng/số cột mặc định
     */
    public static int SIZE = 20;
    /**
     *  chế độ chơi mặc định: User đi trước
     */
    public static final int DEFAULT_MODE = 0;
    /**
     *  độ rộng của mỗi cell
     */
    public static final int CELL_WIDTH = 30;
    /**
     *  khoảng cách giữa các panel
     */
    public static final int MARGIN = 10;
    /**
     *  cỡ chữ trong mỗi cell
     */
    public static final int TEXT_CELL_SIZE = 20;
    /**
     *  màu chữ mặc định của X
     */
    public static final Color USER_TEXT_COLOR = Color.magenta;
    /**
     *  màu chữ mặc định của O
     */
    public static final Color AI_TEXT_COLOR = Color.GREEN;
    /**
     *  màu mặc định của mỗi ô vuông
     */
    public static final Color CELL_COLOR = Color.white;

    /**
     * màu mặc định khi user click vào một ô trong bàn cờ
     */
    public static final Color CLICK_CELL_COLOR = new Color(0, 139, 139);
    /**
     * màu nền mặc định
     */
    public static final Color BACKGROUND_COLOR = new Color(31, 31, 51);
    /**
     * Giá trị mặc định của user
     */
    public static final int USER_VALUE = 1;
    /**
     * Giá trị mặc định của AI
     */
    public static final int AI_VALUE = 2;
    /**
     * Độ sâu tìm kiếm tối đa
     */
    public static final int MAX_DEPTH = 3;
    /**
     * số lượng lấy ra tối đa của danh sách các ô được lượng giá cao nhất
     */
    public static final int MAX_NUM_OF_HIGHEST_CELL_LIST = 8;
}
