// import java.awt.*;
// import java.awt.event.*;
// import java.net.URL;
// import java.util.ArrayList;
// import java.util.Random;
// import javax.swing.*;

// public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
//     // Board
//     int tileSize = 32;
//     int rows = 16;
//     int columns = 16;

//     int boardWidth = tileSize * columns; // 32 * 16
//     int boardHeight = tileSize * rows; // 32 * 16

//     Image shipImg;
//     Image alienImg;
//     Image alienCyanImg;
//     Image alienMagentaImg;
//     Image alienYellowImg;
//     ArrayList<Image> alienImgArray;

//     class Block {
//         int x;
//         int y;
//         int width;
//         int height;
//         Image img;
//         boolean alive = true; // Used for aliens
//         boolean used = false; // Used for bullets
        
//         Block(int x, int y, int width, int height, Image img) {
//             this.x = x;
//             this.y = y;
//             this.width = width;
//             this.height = height;
//             this.img = img;
//         }
//     }

//     // Ship
//     int shipWidth = tileSize * 2;
//     int shipHeight = tileSize;
//     int shipX = tileSize * columns / 2 - tileSize;
//     int shipY = tileSize * rows - tileSize * 2;
//     int shipVelocityX = tileSize; // Ship moving speed
//     Block ship;

//     // Aliens
//     ArrayList<Block> alienArray;
//     int alienWidth = tileSize * 2;
//     int alienHeight = tileSize;
//     int alienX = tileSize;
//     int alienY = tileSize;

//     int alienRows = 2;
//     int alienColumns = 3;
//     int alienCount = 0; // Number of aliens to defeat
//     int alienVelocityX = 1; // Alien moving speed

//     // Bullets
//     ArrayList<Block> bulletArray;
//     int bulletWidth = tileSize / 8;
//     int bulletHeight = tileSize / 2;
//     int bulletVelocityY = -10; // Bullet moving speed

//     Timer gameLoop;
//     boolean gameOver = false;
//     int score = 0;

//     SpaceInvaders() {
//         setPreferredSize(new Dimension(boardWidth, boardHeight));
//         setBackground(Color.black);
//         setFocusable(true);
//         addKeyListener(this);

//         // Load images with the proper path
//         shipImg = loadImage("resources/ship.png");
//         alienImg = loadImage("resources/alien.png");
//         alienCyanImg = loadImage("resources/alien-cyan.png");
//         alienMagentaImg = loadImage("resources/alien-magenta.png");
//         alienYellowImg = loadImage("resources/alien-yellow.png");

//         shipImg = loadImage("resources/ship.png");
//         alienImg = loadImage("resources/alien.png");
//         alienCyanImg = loadImage("resources/alien-cyan.png");
//         alienMagentaImg = loadImage("resources/alien-magenta.png");
//         alienYellowImg = loadImage("resources/alien-yellow.png");


//         alienImgArray = new ArrayList<Image>();
//         alienImgArray.add(alienImg);
//         alienImgArray.add(alienCyanImg);
//         alienImgArray.add(alienMagentaImg);
//         alienImgArray.add(alienYellowImg);

//         ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
//         alienArray = new ArrayList<Block>();
//         bulletArray = new ArrayList<Block>();

//         // Game timer
//         gameLoop = new Timer(1000 / 60, this); // 1000/60 = 16.6
//         createAliens();
//         gameLoop.start();
//     }

//     // Load an image from resources folder
//     private Image loadImage(String path) {
//         URL imgUrl = getClass().getResource(path);
//         if (imgUrl != null) {
//             return new ImageIcon(imgUrl).getImage();
//         } else {
//             System.out.println("Error: Image not found at " + path);
//             return null; // Or return a default image
//         }
//     }

//     public void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         draw(g);
//     }

//     public void draw(Graphics g) {
//         // Ship
//         g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

//         // Aliens
//         for (int i = 0; i < alienArray.size(); i++) {
//             Block alien = alienArray.get(i);
//             if (alien.alive) {
//                 g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
//             }
//         }

//         // Bullets
//         g.setColor(Color.white);
//         for (int i = 0; i < bulletArray.size(); i++) {
//             Block bullet = bulletArray.get(i);
//             if (!bullet.used) {
//                 g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
//             }
//         }

//         // Score
//         g.setColor(Color.white);
//         g.setFont(new Font("Arial", Font.PLAIN, 32));
//         if (gameOver) {
//             g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
//         } else {
//             g.drawString(String.valueOf((int) score), 10, 35);
//         }
//     }

//     public void move() {
//         // Alien movement
//         for (int i = 0; i < alienArray.size(); i++) {
//             Block alien = alienArray.get(i);
//             if (alien.alive) {
//                 alien.x += alienVelocityX;

//                 // If alien touches the borders
//                 if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
//                     alienVelocityX *= -1;
//                     alien.x += alienVelocityX * 2;

//                     // Move all aliens up by one row
//                     for (int j = 0; j < alienArray.size(); j++) {
//                         alienArray.get(j).y += alienHeight;
//                     }
//                 }

//                 if (alien.y >= ship.y) {
//                     gameOver = true;
//                 }
//             }
//         }

//         // Bullet movement and collision detection
//         for (int i = 0; i < bulletArray.size(); i++) {
//             Block bullet = bulletArray.get(i);
//             bullet.y += bulletVelocityY;

//             // Bullet collision with aliens
//             for (int j = 0; j < alienArray.size(); j++) {
//                 Block alien = alienArray.get(j);
//                 if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
//                     bullet.used = true;
//                     alien.alive = false;
//                     alienCount--;
//                     score += 100;
//                 }
//             }
//         }

//         // Clear bullets that are used or out of bounds
//         while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
//             bulletArray.remove(0);
//         }

//         // Next level when all aliens are defeated
//         if (alienCount == 0) {
//             score += alienColumns * alienRows * 100; // Bonus points :)
//             alienColumns = Math.min(alienColumns + 1, columns / 2 - 2); // Cap at 16/2 -2 = 6
//             alienRows = Math.min(alienRows + 1, rows - 6); // Cap at 16-6 = 10
//             alienArray.clear();
//             bulletArray.clear();
//             createAliens();
//         }
//     }

//     public void createAliens() {
//         Random random = new Random();
//         for (int c = 0; c < alienColumns; c++) {
//             for (int r = 0; r < alienRows; r++) {
//                 int randomImgIndex = random.nextInt(alienImgArray.size());
//                 Block alien = new Block(
//                         alienX + c * alienWidth,
//                         alienY + r * alienHeight,
//                         alienWidth,
//                         alienHeight,
//                         alienImgArray.get(randomImgIndex)
//                 );
//                 alienArray.add(alien);
//             }
//         }
//         alienCount = alienArray.size();
//     }

//     public boolean detectCollision(Block a, Block b) {
//         return a.x < b.x + b.width &&
//                 a.x + a.width > b.x &&
//                 a.y < b.y + b.height &&
//                 a.y + a.height > b.y;
//     }

//     @Override
//     public void actionPerformed(ActionEvent e) {
//         move();
//         repaint();
//         if (gameOver) {
//             gameLoop.stop();
//         }
//     }

//     @Override
//     public void keyPressed(KeyEvent e) {}

//     @Override
//     public void keyTyped(KeyEvent e) {}

//     @Override
//     public void keyReleased(KeyEvent e) {
//         if (gameOver) { // Any key to restart
//             ship.x = shipX;
//             bulletArray.clear();
//             alienArray.clear();
//             gameOver = false;
//             score = 0;
//             alienColumns = 3;
//             alienRows = 2;
//             alienVelocityX = 1;
//             createAliens();
//             gameLoop.start();
//         } else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
//             ship.x -= shipVelocityX; // Move left one tile
//         } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipVelocityX + ship.width <= boardWidth) {
//             ship.x += shipVelocityX; // Move right one tile
//         } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//             // Shoot bullet
//             Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
//             bulletArray.add(bullet);
//         }
//     }
// }

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    // Board dimensions
    int tileSize = 32;
    int rows = 16;
    int columns = 16;

    int boardWidth = tileSize * columns; // 32 * 16
    int boardHeight = tileSize * rows;  // 32 * 16

    // Images
    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;

    // Block class
    class Block {
        int x, y, width, height;
        Image img;
        boolean alive = true; // Used for aliens
        boolean used = false; // Used for bullets

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // Ship properties
    int shipWidth = tileSize * 2;
    int shipHeight = tileSize;
    int shipX = tileSize * columns / 2 - tileSize;
    int shipY = tileSize * rows - tileSize * 2;
    int shipVelocityX = tileSize; // Ship moving speed
    Block ship;

    // Aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; // Number of aliens to defeat
    int alienVelocityX = 1; // Alien moving speed

    // Bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    int bulletVelocityY = -10; // Bullet moving speed

    // Game state
    Timer gameLoop;
    boolean gameOver = false;
    int score = 0;

    // Constructor
    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // Load images
        shipImg = loadImage("resources/ship.png");
        alienImg = loadImage("resources/alien.png");
        alienCyanImg = loadImage("resources/alien-cyan.png");
        alienMagentaImg = loadImage("resources/alien-magenta.png");
        alienYellowImg = loadImage("resources/alien-yellow.png");

        alienImgArray = new ArrayList<>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        // Initialize game objects
        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        // Start game loop
        gameLoop = new Timer(1000 / 60, this); // 60 FPS
        createAliens();
        gameLoop.start();
    }

    // Load an image from the resources folder
    private Image loadImage(String path) {
        URL imgUrl = getClass().getClassLoader().getResource(path);
        if (imgUrl != null) {
            return new ImageIcon(imgUrl).getImage();
        } else {
            System.out.println("Error: Image not found at " + path);
            return null; // Placeholder image or null
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Draw ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        // Draw aliens
        for (Block alien : alienArray) {
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // Draw bullets
        g.setColor(Color.white);
        for (Block bullet : bulletArray) {
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + score, 10, 35);
        } else {
            g.drawString("Score: " + score, 10, 35);
        }
    }

    private void move() {
        // Move aliens
        for (Block alien : alienArray) {
            if (alien.alive) {
                alien.x += alienVelocityX;

                // Reverse direction if touching edge
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    for (Block a : alienArray) {
                        a.y += alienHeight; // Move down
                    }
                    break;
                }
                if (alien.y >= ship.y) {
                    gameOver = true;
                }
            }
        }

        // Move bullets
        for (Block bullet : bulletArray) {
            bullet.y += bulletVelocityY;
            for (Block alien : alienArray) {
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        // Remove off-screen or used bullets
        bulletArray.removeIf(b -> b.used || b.y < 0);

        // Next level
        if (alienCount == 0) {
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            createAliens();
        }
    }

    private void createAliens() {
        Random random = new Random();
        for (int c = 0; c < alienColumns; c++) {
            for (int r = 0; r < alienRows; r++) {
                int index = random.nextInt(alienImgArray.size());
                alienArray.add(new Block(alienX + c * alienWidth, alienY + r * alienHeight, alienWidth, alienHeight, alienImgArray.get(index)));
            }
        }
        alienCount = alienArray.size();
    }

    private boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            gameOver = false;
            score = 0;
            alienColumns = 3;
            alienRows = 2;
            createAliens();
            gameLoop.start();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth < boardWidth) {
            ship.x += shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bulletArray.add(new Block(ship.x + shipWidth / 2 - bulletWidth / 2, ship.y, bulletWidth, bulletHeight, null));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
