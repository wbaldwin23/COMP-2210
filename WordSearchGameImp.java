import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.Math;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;



public class WordSearchGameImp implements WordSearchGame {
   private TreeSet<String> lexicon; 
   private String[][] board; 
   private static final int MAX_NEIGHBORS = 8;
   private int width;
   private int height;
   private boolean[][] visited; 
   private ArrayList<Integer> path; 
   private String wordSoFar; 
   private SortedSet<String> allWords;
   private ArrayList<Position> path2;
   
   
   public WordSearchGameImp() {
      lexicon = null;
      board = new String[4][4];
      board[0][0] = "E"; 
      board[0][1] = "E"; 
      board[0][2] = "C"; 
      board[0][3] = "A"; 
      board[1][0] = "A"; 
      board[1][1] = "L"; 
      board[1][2] = "E"; 
      board[1][3] = "P"; 
      board[2][0] = "H"; 
      board[2][1] = "N"; 
      board[2][2] = "B"; 
      board[2][3] = "O"; 
      board[3][0] = "Q"; 
      board[3][1] = "T"; 
      board[3][2] = "T"; 
      board[3][3] = "Y";    
      width = board.length;
      height = board[0].length;
      markAllUnvisited(); 
   }
   
   public void loadLexicon(String fileName) {
      lexicon = new TreeSet<String>(); 
   
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      try {
         Scanner scan = new Scanner(new BufferedReader(new FileReader(new File(fileName))));
         while (scan.hasNext()) {
            String str = scan.next();
            str = str.toUpperCase();
            lexicon.add(str); 
            scan.nextLine();
         }
      }
      catch (java.io.FileNotFoundException e) {
         throw new IllegalArgumentException();
      } 
   }
   
   public void setBoard(String[] letterArray) {
    
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
      int n = (int)Math.sqrt(letterArray.length);
    
      if ((n * n) != letterArray.length) {
         throw new IllegalArgumentException();
      }
      
      board = new String[n][n];
      width = n;
      height = n;
      int index = 0;
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            board[i][j] = letterArray[index];
            index++;
         }
      }
      markAllUnvisited(); 
   }
   
  
   public String getBoard() {
      String strBoard = "";
      for (int i = 0; i < height; i ++) {
         if (i > 0) {
            strBoard += "\n";
         }
         for (int j = 0; j < width; j++) {
            strBoard += board[i][j] + " ";
         }
      }
      return strBoard;
   } 
   
   public SortedSet<String> getAllValidWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      path2 = new ArrayList<Position>(); 
      allWords = new TreeSet<String>();
      wordSoFar = "";
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j ++) {
            wordSoFar = board[i][j];
            if (isValidWord(wordSoFar) && wordSoFar.length() >= minimumWordLength) {
               allWords.add(wordSoFar);
            }
            if (isValidPrefix(wordSoFar)) {
               Position temp = new Position(i,j);
               path2.add(temp);
               
               dfs2(i, j, minimumWordLength); 
               
               path2.remove(temp);
            }
         }
      }
      return allWords;
   }
   
   private void dfs2(int x, int y, int min) {
      Position start = new Position(x, y);
      markAllUnvisited(); 
      markPathVisited(); 
      for (Position p : start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            if (isValidPrefix(wordSoFar + board[p.x][p.y])) {
               wordSoFar += board[p.x][p.y];
               path2.add(p);
               if (isValidWord(wordSoFar) && wordSoFar.length() >= min) {
                  allWords.add(wordSoFar);
               }
               dfs2(p.x, p.y, min);
               
               path2.remove(p);
               int endIndex = wordSoFar.length() - board[p.x][p.y].length();
               wordSoFar = wordSoFar.substring(0, endIndex);
            }
         }
      }
      markAllUnvisited(); 
      markPathVisited(); 
   }
   
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      int score = 0;
      Iterator<String> itr = words.iterator();
      while (itr.hasNext()) {
         String word = itr.next();
         
         if (word.length() >= minimumWordLength && isValidWord(word)
             && !isOnBoard(word).isEmpty()) {
            
            score += (word.length() - minimumWordLength) + 1;
         }
      }
      return score;
   }
   
   public boolean isValidWord(String wordToCheck) {
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      
      wordToCheck = wordToCheck.toUpperCase();
      return lexicon.contains(wordToCheck);
   }
   
   public boolean isValidPrefix(String prefixToCheck) {
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      prefixToCheck = prefixToCheck.toUpperCase();
      
      String word = lexicon.ceiling(prefixToCheck);
      if (word != null) {
         return word.startsWith(prefixToCheck);
      }
      return false;
   }
   
   public List<Integer> isOnBoard(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      path2 = new ArrayList<Position>();
      wordToCheck = wordToCheck.toUpperCase();
      wordSoFar = "";
      path = new ArrayList<Integer>();
   
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j ++) {
         
            if (wordToCheck.equals(board[i][j])) {
               path.add(i * width + j); 
               return path;
            }
            if (wordToCheck.startsWith(board[i][j])) {
               Position pos = new Position(i, j);
               path2.add(pos); 
               wordSoFar = board[i][j]; 
               dfs(i, j, wordToCheck); 
               
               if (!wordToCheck.equals(wordSoFar)) {
                  path2.remove(pos);
               }
               else {
               
                  for (Position p: path2) {
                     path.add((p.x * width) + p.y);
                  } 
                  return path;
               }
            }
         }
      }
      return path;
   }
   
   private void dfs(int x, int y, String wordToCheck) {
      Position start = new Position(x, y);
      markAllUnvisited(); 
      markPathVisited(); 
      for (Position p: start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            if (wordToCheck.startsWith(wordSoFar + board[p.x][p.y])) {
               wordSoFar += board[p.x][p.y]; 
               path2.add(p);
               dfs(p.x, p.y, wordToCheck);
               if (wordToCheck.equals(wordSoFar)) {
                  return;
               }
               else {
                  path2.remove(p);
               
                  int endIndex = wordSoFar.length() - board[p.x][p.y].length();
                  wordSoFar = wordSoFar.substring(0, endIndex);
               }
            }
         }
      }
      markAllUnvisited(); 
      markPathVisited(); 
   }
   
   private void markAllUnvisited() {
      visited = new boolean[width][height];
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }
   
   private void markPathVisited() {
      for (int i = 0; i < path2.size(); i ++) {
         visit(path2.get(i));
      }
   }
   
   private class Position {
      int x;
      int y;
      
      public Position(int x, int y) {
         this.x = x;
         this.y = y;
      }
      
      @Override
      public String toString() {
         return "(" + x + ", " + y + ")";
      }
      
      public Position[] neighbors() {
         Position[] nbrs = new Position[MAX_NEIGHBORS];
         int count = 0;
         Position p;
        
         for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
               if (!((i == 0) && (j == 0))) {
                  p = new Position(x + i, y + j);
                  if (isValid(p)) {
                     nbrs[count++] = p;
                  }
               }
            }
         }
         return Arrays.copyOf(nbrs, count);
      }
   }
   
   private boolean isValid(Position p) {
      return (p.x >= 0) && (p.x < width) && (p.y >= 0) && (p.y < height);
   }
   
   private boolean isVisited(Position p) {
      return visited[p.x][p.y];
   } 
   
   private void visit(Position p) {
      visited[p.x][p.y] = true;
   }   
}