# Kenken
Kenken is a game (somewhat similar to suduku) involving an n by n grid. Within each grid, there are cages that contain a target number and an arithmetic operation. 
The numbers selected in that cage must result in the target number using the given arithmetic operation. In an n by n Kenken, the only numbers that can be used are 
in the range 1 -> n. No number can be repeated within a row or column. Here is an example of a 4 by 4 kenken   

![alt text](http://www.kenkenpuzzle.com/assets/4x4-72dc4cbbdf6703b668e89907cf51d79f.png)

For my solution, I use Depth First Search and backtracking to find the solution. I try to optimize the backtracking by noting the freebies, and consequently updating the 
candidates of neighboring cells. This approach seems to work well with kenkens that have a grid length of n <= 9. 

There are 2 main ways to input the kenken puzzle:
  1) GUI: Enter the targetNum and operator in the top left input box and click the buttons that make up the corresponding cage. Click "Lock" to confirm that cage. Repeat for all 
     cages. Hit compute. 
     
     ![alt text](https://github.com/seharpanesar/Kenken/blob/master/ReadMePics/Capture.PNG?raw=true) {: height="75px" width="75px" align="left"}
     <br/><br/>
     
     
     ![alt text](https://github.com/seharpanesar/Kenken/blob/master/ReadMePics/Capture1.PNG?raw=true) {: height="75px" width="75px" align="left"}
     <br/><br/>
     
  2) File input (more tedious): Format a txt file in the following manner. Pass the file path in Kenken constructor and run program. Solution will be 
     printed in System.out
     
     ![alt text](https://github.com/seharpanesar/Kenken/blob/master/ReadMePics/Capture2.PNG?raw=true) {: height="75px" width="75px" align="left"}
  
