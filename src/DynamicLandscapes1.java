/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

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
		double x, y, trans;
		trans=0;

		//parameters to trans or osc
		//must uncomment function/ test below
		//set trans= 0 if user doesn't want translating
		if(Search.trans)
		{
			trans = 0.5;
			if(Search.r.nextDouble() > 0.5)
				trans *= -1;
		}
		else
			trans = 0;
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
			x= (X.getXGeneValue() * 5)+trans;
			y= (X.getYGeneValue() * 5)+trans;
			if(!Search.osc){
				X.rawFitness= fitnessFunction5(x,y);
			}
			else{
				X.rawFitness= fitnessFunction1(x,y);
			}

			//System.out.print("x = " + x + " ");
			//System.out.print("y = " + y + "  ");
			//System.out.print("fit = " + X.rawFitness + " \n");

	}

	public void assessNovelty(Chromo X)
	{
		X.noveltyFitness = 0;
		// Grab the species array list
		ArrayList<Chromo> currentSpecies = KModes.speciesList.get(X.speciesKey);
		// TODO: Loop through this chromo's species and accumulate hammingDistance score
		for(int i = 0; i < currentSpecies.size(); i++)
		{
			if(currentSpecies.get(i).chromo != X.chromo)
			{
				// Accumulate score
				X.noveltyFitness += KModes.hammingDistance(currentSpecies.get(i).chromo, X.chromo);
			}
		}
		// TODO: Average the distance score and save to noveltyFitness score
		if((currentSpecies.size() - 1) != 0)
			X.noveltyFitness /= (currentSpecies.size() - 1);
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
	public static double fitnessFunction1(double x, double y){
		double fitness = 0.0;
		fitness= (1+ (Math.pow(x+y+1,2)*(19-(14*x)+(3*x*x)-(14*y)+(6*x*y)+(3*y*y))))
		*(30+(Math.pow((2*x)-(3*y), 2)*(18-(32*x)+(12*x*x)+(48*y)-(36*x*y)+(27*y*y))));
		return fitness;
		//min @ 3 , (3,0)
	}

	//DeJong's function 2.16
	public static double fitnessFunction2(double x, double y){
		double fitness = 0.0;
		fitness= (4-2.1*Math.pow(x, 2)+(Math.pow(x, 4)/3))*Math.pow(x, 2)+x*y+(-4+4*Math.pow(y, 2))*Math.pow(y, 2);
		return fitness;
		//range -3<=x<=3, -2<=y<=2
		//min @ f(x1,x2)=-1.0316; (x1,x2)=(-0.0898,0.7126), (0.0898,-0.7126).
	}

	//function 2.13
	public static double fitnessFunction3(double x, double y){
		double fitness = 0.0;
		double b= 5.1/(4*Math.pow(Math.PI,2));
		double c = 5/Math.PI;
		double f=1/(8*Math.PI);
		fitness= Math.pow(y-b*Math.pow(x,2)+(c*x)-6, 2)+ 10*(1-f)*Math.cos(x)+10;
		return fitness;
		//range: -5<=x<=10, 0<=y<=15
		//min @ f(x1,x2)=0.397887; (x1,x2)=(-pi,12.275), (pi,2.275), (9.42478,2.475).
	}

	public static double fitnessFunction5(double x, double y)
	{
		double fitness = 0.0;
		for(int i = 1; i <= 2; i++)
		{
				fitness += Math.abs(i*x*y);
		}
		return fitness;
	}

	//shekel's foxholes
	public static double fitnessFunction4(double x, double y){
		double[][] a;
		int i, j;
		a = new double[][] {{-32,32}, {-16, -32}, {0,-32}, {16,-32},  {32,-32}, {-32,-16},{-16,-16},{0,-16}, {16,-16},{32,-16},{-32,0},{-16,0}, {0,0},{16,0},{32,0},
				     {-32,16},{-16,16}, {0,16},{16,16}, {32,16}, {-32,32}, {-16,32}, {0,32}, {16,32}, {32,32} };
		double v = 0;
		double v_1 = 0;
		for (j = 0; j < 25; j++) {
			v_1 = 0;
			for (i = 0; i < 2; i++) {
				if(i==0){
					v_1 = v_1 + Math.pow(x - a[j][i],6);
				}
				if(i==1){
					v_1 = v_1 + Math.pow(y - a[j][i],6);
				}
			}
			v = v + 1.0/v_1;
		}
		v = v + 1.0/500.0;
		v = Math.pow(v, -1);
		return v;
		//max @ 500, (x, y)= −31.97833
	}
	// public static void main(String[] args) throws java.io.IOException{
	// 	//(-0.0898,0.7126)
	// 	//(pi,2.275)
	// 	//-31.97833

	// 	double x= -31;
	// 	double y= -65.97833;
	// 	double fitness1, fitness2, fitness3, fitness4;
	// 	fitness1= fitnessFunction1(x, y);
	// 	fitness2= fitnessFunction2(x, y);
	// 	fitness3= fitnessFunction3(x, y);
	// 	fitness4= fitnessFunction4(x, y);
	// 	System.out.println("fitness1 = " + fitness1);
	// 	System.out.println("fitness2 = " + fitness2);
	// 	System.out.println("fitness3 = " + fitness3);
	// 	System.out.println("fitness4 = " + fitness4);

	//function 2.6
	// public static double fitnessFunction5(double x, double y){
	// 	double fitness = 0.0;


	// 	return fitness;
	// 	//min @ f(x1,x2)=0.397887; (x1,x2)=(-pi,12.275), (pi,2.275), (9.42478,2.475).
	// }
}   // End of DynamicLandscapes.java ******************************************************
