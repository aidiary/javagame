import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2005/05/04
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel
        implements
            MouseListener,
            MouseMotionListener {
    // グリッドサイズ
    private static final int GS = 64;
    // 行数、列数
    public static final int ROW = 5;
    public static final int COL = 5;
    // パネルサイズ
    private static final int WIDTH = GS * COL;
    private static final int HEIGHT = GS * ROW;

    // 最大パターン数
    private static final int MAX_PATTERN = 50;

    // パターン
    private int[] pattern;

    // 訓練データセット
    private double[][] trainingSet;
    // 教師信号
    private double[][] teacherSet;

    // ニューラルネット（3層パーセプトロン）
    private NeuralNetwork nn;
    // 入力したパターン数
    private int numPattern;

    // 情報パネルへの参照
    private InfoPanel infoPanel;

    public MainPanel(InfoPanel infoPanel) {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.infoPanel = infoPanel;

        addMouseListener(this);
        addMouseMotionListener(this);

        // パターンは2次元だけど1次元配列で表す
        pattern = new int[ROW * COL];

        trainingSet = new double[MAX_PATTERN][ROW * COL];
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(204, 255, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // パターンを描画
        g.setColor(Color.RED);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (pattern[i * COL + j] == 1) {
                    g.fillRect(j * GS, i * GS, GS, GS);
                }
            }
        }

        // 格子を描く
        g.setColor(Color.BLACK);
        for (int i = 0; i <= ROW; i++) {
            g.drawLine(0, i * GS, WIDTH, i * GS);
        }
        for (int i = 0; i <= COL + 1; i++) {
            g.drawLine(i * GS, 0, i * GS, HEIGHT);
        }
    }

    /**
     * パターンを追加する
     */
    public void addPattern() {
        // 訓練データ配列に描かれたパターンを追加
        for (int i = 0; i < ROW * COL; i++) {
            trainingSet[numPattern][i] = pattern[i];
        }
        infoPanel.print("パターン" + numPattern + "を追加しました");

        numPattern++;
        clearPattern();
    }

    /**
     * パターンを消去する
     */
    public void clearPattern() {
        // パターンを消去
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                pattern[i * COL + j] = 0;
            }
        }

        repaint();
    }

    /**
     * 訓練データをもとにパターンを学習する
     */
    public void learnPattern() {
        // 入力されたパターン数に応じて教師信号を生成
        teacherSet = new double[numPattern][numPattern];
        // 単位行列にする
        for (int i = 0; i < numPattern; i++) {
            for (int j = 0; j < numPattern; j++) {
                if (i == j) {
                    teacherSet[i][j] = 1.0;
                } else {
                    teacherSet[i][j] = 0.0;
                }
            }
        }

        nn = new NeuralNetwork();
        // 入力層（パターンの5×5）
        // 隠れ層（適当に決めた）
        // 出力層（追加パターン数）
        nn.init(25, 50, numPattern);

        // 訓練データを学習
        double error = 1.0;
        int count = 0;
        while ((error > 0.0001) && (count < 50000)) {
            error = 0;
            count++;
            // 各訓練データを誤差が小さくなるまで繰り返し学習
            for (int i = 0; i < numPattern; i++) {
                // 入力をセット
                for (int j = 0; j < trainingSet[i].length; j++) {
                    nn.setInput(j, trainingSet[i][j]);
                }
                // 教師信号をセット
                for (int j = 0; j < numPattern; j++) {
                    nn.setTeacherValue(j, teacherSet[i][j]);
                }
                // 学習開始
                nn.feedForward();
                error += nn.calculateError();
                nn.backPropagate();
            }
            error /= trainingSet.length;

            // 情報パネルへ誤差を出力
            // infoPanel.print(error + "");
        }

        infoPanel.print("学習が完了しました");
        infoPanel.print("パターンを入力し認識できるか試してください");
    }

    /**
     * パターン認識
     */
    public void recognizePattern() {
        // 入力パターンをセット
        for (int j = 0; j < ROW * COL; j++) {
            nn.setInput(j, pattern[j]);
        }
        // 出力を計算
        nn.feedForward();
        // 最大出力を出しているノード番号を得る
        int id = nn.getMaxOutputID();

        infoPanel.print("パターン" + id + "です");
        clearPattern();
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / GS;
        int y = e.getY() / GS;

        if (x < 0 || x > COL - 1)
            return;
        if (y < 0 || y > ROW - 1)
            return;

        pattern[y * COL + x] = 1;

        repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / GS;
        int y = e.getY() / GS;

        if (x < 0 || x > COL - 1)
            return;
        if (y < 0 || y > ROW - 1)
            return;

        pattern[y * COL + x] = 1;

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}