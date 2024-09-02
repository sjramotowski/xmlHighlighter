Stephen Ramotowski
9/1/24
CSCI Programming Assignment #1

To Run:

Compile and run xmlHighlighter with the commands: 

        javac xmlHighlighter.java
        java xmlHighlighter.java path

        where path is the path to the directory containing the xml and png pairs
        ex. C:\Users\sjram\Downloads\Programming-Assignment-Data\Programming-Assignment-Data

This program takes an input of a directory containing xml and png files and 
creates edited versions of the png files where leaf nodes of the xml, 
objects with which a user would typically interact with, are highlighted 
with yellow boxes

The highlighted images are created in the same directory as the program file is stored.
The names of the new file are "oldname" + ".highlighted.png"
ex. com.yelp.android.png becomes com.yelp.android.highlighted.png

I chose to write this in Java because I am most familiar with java and have used xml files with java before

While researching for this project I identified important steps/ processes I would need to implement.
- get the file directory where the xml png pairs are
- separate the xml files
- check if each xml file had a png file pair
- parse the xml file and get the bounds for each leaf node
- copy the png
- draw the rectangles
- create the new file

I implemented the file directory acquisition by requiring a command line argument containing the path to the directory
Using the path, separating the xml files was easy. I had these in the main function because they involved the command line argument
and could fail if the command line argument was not present or incorrect.

I chose to have a seperate function for checking if an xml file had a pair because I was going to process each xml
one at a time in a loop and having this step as its own function made the code more readable and easier to test.

For similar reasons I created a function for parsing the xml files. 
I chose not to convert the bounds to usable coordinates in integer form in this function because it was easier to return
and pass the arrayList as an argument. 

I combined the copy png, draw rectangles, and create new file steps into one function because all three steps involved using the
ImageIO and Graphics2D libraries. I didn't create a separate function for converting the bounds strings into coordinates because 
I would have had to create a new data structure to contain the four coordinates when returning the values.

I chose to have the new files created in the directory of the program file because it was the default behavior.

