import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    public static final int WIDTH = 192;
    public static final int HEIGHT = 416;

    // ボード
    private Board board;

    // 現在のブロック
    private Block block;
    // 次のブロック
    private Block nextBlock;

    // ブロックのイメージ
    private Image blockImage;

    // ゲームループ用スレッド
    private Thread gameLoop;

    private Random rand;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // ブロックのイメージをロード
        loadImage("image/block.gif");

        rand = new Random();

        board = new Board();
        block = createBlock(board);

        addKeyListener(this);

        try {
            // サウンドをロード
            WaveEngine.load("se/kachi42.wav");
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
        
        try {
            // BGMをロード
            MidiEngine.load("bgm/tetrisb.mid");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // BGMスタート！
        MidiEngine.play(0);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
        long lastTime = 0;
        
        while (true) {
            // ブロックを移動する
            // ブロックが固定されたらtrueが返される
            boolean isFixed = block.move(Block.DOWN);
            if (isFixed) { // ブロックが固定されたら
                // かちゃって鳴らす
                WaveEngine.play(0);
                // 次のブロックを作成（ランダムに）
                nextBlock = createBlock(board);
                block = nextBlock;
            }

            // ブロックがそろった行を消す
            board.deleteLine();

            // ゲームオーバーか
            if (board.isStacked()) {
                board = new Board();
                block = createBlock(board);
            }

            // WAVEファイルのレンダリング
            WaveEngine.render();

            // 再描画
            repaint();

            // 休止
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ボード（固定ブロックを含む）を描画
        board.draw(g, blockImage);

        // 落ちてくるブロックを描画
        block.draw(g, blockImage);
    }

    /**
     * ランダムに次のブロックを作成
     * 
     * @param board ボードへの参照
     * @return ブロック
     */
    public Block createBlock(Board board) {
        int blockNo = rand.nextInt(7);
        switch (blockNo) {
            case Block.BAR :
                return new BarBlock(board);
            case Block.Z_SHAPE :
                return new ZShapeBlock(board);
            case Block.SQUARE :
                return new SquareBlock(board);
            case Block.L_SHAPE :
                return new LShapeBlock(board);
            case Block.REVERSE_Z_SHAPE :
                return new ReverseZShapeBlock(board);
            case Block.T_SHAPE :
                return new TShapeBlock(board);
            case Block.REVERSE_L_SHAPE :
                return new ReverseLShapeBlock(board);
        }

        return null;
    }

    /**
     * ブロックのイメージをロード
     * 
     * @param string
     */
    private void loadImage(String filename) {
        // ブロックのイメージを読み込む
        // ImageIconを使うとMediaTrackerを使わなくてすむ
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) { // ブロックを左へ移動
            block.move(Block.LEFT);
        } else if (key == KeyEvent.VK_RIGHT) { // ブロックを右へ移動
            block.move(Block.RIGHT);
        } else if (key == KeyEvent.VK_DOWN) { // ブロックを下へ移動
            block.move(Block.DOWN);
        } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) { // ブロックを回転
            block.turn();
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}