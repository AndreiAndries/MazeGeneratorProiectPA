package Mazes;

public interface Maze {//interfata care e folosita ca si template pentru fiecare generator de maze
    void display();
    void drawSquare (int row, int column, int colorCode);
    void makeMaze(int inputRow,int inputColumn);
}
