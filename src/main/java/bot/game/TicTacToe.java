package bot.game;

import bot.DiscordBot;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe extends Game{

    private int size;
    private int[][] board;

    public TicTacToe(GameBot bot, int size){
        super(bot);
        this.size = size;
    }

    public TicTacToe(GameBot bot){
        super(bot);
        this.size = 3;
    }

    public void start(){
        String input = "";
        do{
            playGame();
            bot.say("Want to play again?");
            input = this.bot.nextLine();
        }
        while(input.toLowerCase().startsWith("y"));
    }

    public void playGame(){
        this.board = new int[this.size][this.size];

        int x = 0;
        int y = 0;

        Random random = new Random();

        bot.say("Do you want to go first? ");
        int turn;
        if(this.bot.nextLine().toLowerCase().startsWith("y")){
            turn = 1;
        }
        else turn = -1;

        boolean won = false;
        while(!won){
            if(turn == 1){
                this.bot.say("Your move? ");
                String line = this.bot.nextLine();
                Scanner lineReader = new Scanner(line);
                x = lineReader.nextInt();
                y = lineReader.nextInt();
                if(board[x - 1][y - 1] == 0){
                    board[x - 1][y - 1] = 1;
                    turn = -1;
                }
                else{
                    this.bot.say("That spot has already been played!");
                }
                lineReader.close();
            }
            else{
                boolean didTurn = false;

                do{
                    x = random.nextInt(3) + 1;
                    y = random.nextInt(3) + 1;
                    if(board[x - 1][y - 1] == 0){
                        try{
                            Thread.sleep(500L);
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        this.bot.say("I move to " + x + ", " + y);
                        board[x - 1][y - 1] = -1;
                        didTurn = true;
                    }
                }
                while(!didTurn);

                turn = 1;
            }

            printBoard();

            int condition = checkWin( x - 1, y - 1);
            if(condition == 1){
                this.bot.say("You win!");
            }
            else if(condition == -1){
                this.bot.say("I win.");
            }
            else if(condition == 2){
                this.bot.say("It's a draw!");
            }

            if(condition != 0){
                won = true;
                printBoard();
            }
        }
    }

    public void printBoard(){
        StringBuilder builder = new StringBuilder();

        for(int i = 0;i < board.length;i++){
            builder.append("\n-------------\n");
            for(int j = 0;j < board[i].length;j++){
                builder.append("|" + getRepresentation(board[i][j]));
            }
            builder.append("|\n");
        }
        builder.append("-------------\n\n");

        say(builder.toString());
    }

    public String getRepresentation(int number){
        if(number == 1) return "X";
        else if(number == -1) return "O";
        else return "#";
    }

    public int checkWin(int x, int y){
        int col = 0;
        int row = 0;
        int diag = 0;
        int idiag = 0;

        int player = board[x][y];

        for(int i = 0;i < 3;i++){
            if(board[i][y] == player) row++;
            if(board[x][i] == player) col++;
            if(board[i][i] == player) diag++;
            if(board[i][3 - (i + 1)] == player) idiag++;
        }
        if(col == 3 || row == 3 || diag == 3 || idiag == 3) return player;

        int count = 0;
        for(int i = 0;i < board.length;i++){
            for(int j = 0;j < board[i].length;j++){
                if(board[i][j] != 0) count++;
            }
        }
        if(count == 9) return 2;

        return 0;
    }

    private void say(String message){
        DiscordBot.instance.say(message);
    }
}
