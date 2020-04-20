import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.Math;

public class DynamicLandscapes1 extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public DynamicLandscapes1(){
		name = "DynamicLandscapes Problem 1";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X, int G){
		double x, y, transX, transY;
		transX=0;
		transY=0;
		//parameters to trans or osc
		//must uncomment function/ test below
		//set trans= 0 if user doesn't want translating
		if(Search.trans)
		{
			if(Search.r.nextDouble() > 0.5)
				transX -= 0.5;
      else
        transX += 0.5;

			if(Search.r.nextDouble() > 0.5)
				transY -= 0.5;
      else
        transY += 0.5;
		}
			//multiply x and y value by specific function range
			//change these lines to change function

			//Function 1
			//range: -5<=x<=5, -5<=y<=5
			//min @ 3 , (0,-1)

			//Function 2
			//range: -5<=x<=5, -5<=y<=5
			//min @ f(x1,x2)=-1.0316; (x1,x2)=(-0.0898,0.7126), (0.0898,-0.7126).
			// x= (X.getXGeneValue(j)*5)+trans;
			// y= (X.getYGeneValue(j)*5)+trans;
			// X.rawFitness= (4-2.1*Math.pow(x, 2)+(Math.pow(x, 4)/3))*Math.pow(x, 2)+x*y+(-4+4*Math.pow(y, 2))*Math.pow(y, 2);

			//Function 3
			//range:: -5<=x<=10, 0<=y<=15
			//min @ f(x1,x2)=0.397887; (x1,x2)=(-pi,12.275), (pi,2.275), (9.42478,2.475).
			// double b= 5.1/(4*Math.pow(Math.PI,2));
			// double c = 5/Math.PI;
			// double f=1/(8*Math.PI);
			// x= (X.getXGeneValue(j)*10)+trans;
			// y= (X.getYGeneValue(j)*10)+trans;
			// X.rawFitness= Math.pow(y-b*Math.pow(x,2)+(c*x)-6, 2)+ 10*(1-f)*Math.cos(x)+10;

			//Oscillation Test: flip between function 1 & 2 every x gen
			if(!Search.osc){
				x= (X.getXGeneValue());
        //y= (X.getYGeneValue() * 2) + transY;
				if(x < 0)
				{
					x = x * 5 + transX;
				}else x = x * 10 + transX;
				y= (X.getYGeneValue());
				if(y < 0)
				{
					y = y * 5 + transY;
				}else y = y * 15 + transY;
				X.rawFitness= fitnessFunction3(x,y);
			}
			else{
				x= (X.getXGeneValue() * 3);
				y= (X.getYGeneValue() * 2);
				X.rawFitness= fitnessFunction2(x,y);
			}

			//System.out.print("x = " + x + " ");
			//System.out.print("y = " + y + "  ");
			//System.out.print("fit = " + X.rawFitness + " \n");

	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getPosIntGeneValue(i),11,output);
		}
		Hwrite.right( X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}


	//function 2.6
	// public static double fitnessFunction5(double x, double y){
	// 	double fitness = 0.0;


	// 	return fitness;
	// 	//min @ f(x1,x2)=0.397887; (x1,x2)=(-pi,12.275), (pi,2.275), (9.42478,2.475).
	// }


/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/
//sinusoidal landscape
	public static double fitnessFunction1(double x, double y){ // Bound is 2
		double fitness = 0.0;
		fitness= (1+ (Math.pow(x+y+1,2)*(19-(14*x)+(3*x*x)-(14*y)+(6*x*y)+(3*y*y))))
		*(30+(Math.pow((2*x)-(3*y), 2)*(18-(32*x)+(12*x*x)+(48*y)-(36*x*y)+(27*y*y))));
		return fitness;
		//min @ 3 , (3,0)
	}

	//DeJong's function 2.16
	public static double fitnessFunction2(double x, double y){ // Bound is 3 for x, y is 2
		double fitness = 0.0;
		fitness= (4-2.1*Math.pow(x, 2)+(Math.pow(x, 4)/3))*Math.pow(x, 2)+x*y+(-4+4*Math.pow(y, 2))*Math.pow(y, 2);
		return fitness;
		//range -3<=x<=3, -2<=y<=2
		//min @ f(x1,x2)=-1.0316; (x1,x2)=(-0.0898,0.7126), (0.0898,-0.7126).
	}

	//function 2.13
	public static double fitnessFunction3(double x, double y){ // Bound is 5 to 10 for x, 5 to 15 for y
		double fitness = 0.0;
		double b= 5.1/(4*Math.pow(Math.PI,2));
		double c = 5/Math.PI;
		double f=1/(8*Math.PI);
		fitness= Math.pow(y-b*Math.pow(x,2)+(c*x)-6, 2)+ 10*(1-f)*Math.cos(x)+10;
		return fitness;
		//range: -5<=x<=10, 0<=y<=15
		//min @ f(x1,x2)=0.397887; (x1,x2)=(-pi,12.275), (pi,2.275), (9.42478,2.475).
	}
}   // End of DynamicLandscapes.java ******************************************************
