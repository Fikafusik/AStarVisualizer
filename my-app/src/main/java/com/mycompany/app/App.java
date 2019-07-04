package com.mycompany.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        AStarVisualizer aStarVisualizer = new AStarVisualizer(1000, 600);
        aStarVisualizer.setVisible(true);
    }
}
