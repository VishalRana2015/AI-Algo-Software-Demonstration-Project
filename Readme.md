<h1> AI Searching Algorithm</h1>

This tool allows you to visualize how AI Searching algorithm work. <br/>
In a grid of cells, given source and destination and a set of obstacles, this tool will visually demonstrate how different AI Path finding algorithm finds path from source to destination without going through obstacles. <br/>
I have implemented following four algorithms <br/>
<ul>
<li> Breadth First Search</li>
<li> Best First Search </li>
<li> A* Start </li>
<li> Depth First Search </li>
</ul>

<h2>Demonstrating A* Star</h2> <br/>
In the following image, it finds path between source and destination using A* Star Algorithm.<br/>
A cell have its neighbour in North, East, South and West direction (Only four direction).<br/>
![AStarFour.gif](/images/AStarFour.gif)

<br/>
We can also choose a cell to have eight neighbour instead of 4, as shown below. <br/>

![AStarEight.gif](/images/AStarEight.gif)

A* Star always find the shortest path to destination.<br/>

<h2>Demonstrating Breadth First Search </h2> <br/>
Breadth First Search always find the shortest path from the Source to the Destination. In the following images, for the given obstacles, it has been shown that the A* has found the shortest path between the source and the destination with path lenght of 43( See Breadth First Serach Image).
<img width="543" alt="BreachFirstSearch4" src="https://github.com/VishalRana2015/AI-Algo-Software-Demonstration-Project/assets/69715143/5c283590-da18-4fce-bd74-dee6cd794765">

<img width="451" alt="A_Star_4" src="https://github.com/VishalRana2015/AI-Algo-Software-Demonstration-Project/assets/69715143/c16ca94a-324b-4b3b-9b94-a9b7212a1ad8">



<h2>Demonstrating Best First Search </h2>
Based on a heuristic function, it picks the best node with lowest heuristic value. Here as the heuristic function, it uses Manhattan distance from the evaluating node to the destination.<br/>

<img width="485" alt="BestFirstSearch" src="https://github.com/VishalRana2015/AI-Algo-Software-Demonstration-Project/assets/69715143/74d52ca4-cfd7-4f2f-bc90-6f3c99efea7c">


<h3> Controls </h3> 

* Import/Export: You can import and export Grid Map. Create a map with obstacles and export it to use later. Import when you want to use it. It uses <b>.aialgo</b> extension to save files. 
* Set number of rows and columns in the grid
* Set cell size
* Set animation Delay : Default 50 milli seconds.
* Neighbours: controls to select whether you want adjacent cells sharing edges as neighbors of the current cell or all cells sharing edges and borders as neighbors of the current cell. A neighbor cell is a cell which can be reached from the current cell.
* Algorithm Selection
* Edit City Control: To create Grid Map 
* Play Button: Start playing the selected algorithm on the grid map.

Importing a map Demo<br/>
<img width="622" alt="Controls" src="https://github.com/VishalRana2015/AI-Algo-Software-Demonstration-Project/assets/69715143/508029af-29b7-475e-be57-64b62491f077">

