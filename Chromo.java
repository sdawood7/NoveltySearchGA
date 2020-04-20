/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public String chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Set gene values to a randum sequence of 1's and 0's
		char geneBit;
		chromo = "";
		for (int i=0; i<Parameters.numGenes; i++){
			for (int j=0; j<Parameters.geneSize; j++){
				randnum = Search.r.nextDouble();
				if (randnum > 0.5) geneBit = '0';
				else geneBit = '1';
				this.chromo = chromo + geneBit;
			}
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		int start = geneID * Parameters.geneSize;
		int end = (geneID+1) * Parameters.geneSize;
		String geneAlpha = this.chromo.substring(start, end);
		return (geneAlpha);
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneSign;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=1; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		geneSign = geneAlpha.charAt(0);
		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
		return (geneValue);
	}

	public double getXGeneValue(){
		String geneAlpha = this.chromo;
		double geneValue = 0;
		char geneSign;
		char geneBit;
		//System.out.print(" GeneAlpha= " + geneAlpha + " ");
		for (int i=(Parameters.geneSize/2)-1; i>=1; i--){
			geneBit = geneAlpha.charAt(i);
			if(i == 0)
			{
				// Skip
			}
			else if (geneBit == '1') geneValue = geneValue + (1/Math.pow(2.0, ((Parameters.geneSize/2)-i)));
		}
		geneSign = geneAlpha.charAt(0);
		if (geneSign == '1') geneValue = geneValue*-1;
		//System.out.print(" geneVal= " + geneValue + "\n");
		return (geneValue);
	}

	public double getYGeneValue(){
		String geneAlpha = this.chromo;
		double geneValue = 0;
		char geneSign;
		char geneBit;
		//System.out.print(" GeneAlpha= " + geneAlpha + " ");
		for (int i=Parameters.geneSize-1; i>=(Parameters.geneSize/2)+1; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (1/Math.pow(2.0, (Parameters.geneSize-i)));
		}
		geneSign = geneAlpha.charAt((Parameters.geneSize/2));
		if (geneSign == '1') geneValue = geneValue*-1;
		//System.out.print(" y= " + geneValue + "\n");
		return (geneValue);
	}

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=0; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		return (geneValue);
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
        char x;
        double mutationRate;

        // Use hypermutation if enabled
        if (Parameters.usingHypermutation) {
            mutationRate = Parameters.hypermutationRate;
        }
        else {
            mutationRate = Parameters.mutationRate;
        }

		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number

			for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
				x = this.chromo.charAt(j);
				randnum = Search.r.nextDouble();
				if (randnum < mutationRate){
					if (x == '1') x = '0';
					else x = '1';
				}
				mutChromo = mutChromo + x;
			}
			this.chromo = mutChromo;
			break;

		case 2:     //  Random Immigrants attempt

			//changes certain genes
			for (int i=0; i<Parameters.numGenes; i++){
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate2){
					char[] testChromo = this.chromo.toCharArray();
					char geneBit;
					for (int j=0; j<Parameters.geneSize; j++){
						randnum = Search.r.nextDouble();
						if (randnum > 0.5) geneBit = '0';
						else geneBit = '1';
						testChromo[j+(i*Parameters.geneSize)]= geneBit;
					}
					this.chromo = String.valueOf(testChromo);
				}
			}

			//still does regular mutation
			for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
				x = this.chromo.charAt(j);
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate){
					if (x == '1') x = '0';
					else x = '1';
				}
				mutChromo = mutChromo + x;
			}
			this.chromo = mutChromo;


			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;
		int l = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 2:     //  Tournament Selection
			randnum = Search.r.nextDouble();
            j = (int) (randnum * Parameters.popSize);
            randnum = Search.r.nextDouble();
            k = (int) (randnum * Parameters.popSize);
            while (j == k) {
                randnum = Search.r.nextDouble();
                k = (int) (randnum * Parameters.popSize);
            }
            Chromo candidate1 = Search.member[j];
            Chromo candidate2 = Search.member[k];
            
			randnum = Search.r.nextDouble();
            if (candidate1.rawFitness < candidate2.rawFitness) { // Candidate1 is "more fit"
                if (randnum < Parameters.tournamentRate) { 
                    return j;
                }
                else {
                    return k;
                }
            }
            else if (candidate2.rawFitness < candidate1.rawFitness) { // Candidate2 is "more fit"
                if (randnum < Parameters.tournamentRate) {
                    return k;
                }
                else {
                    return j;
                }
            }
            else { // Candidates are equally fit
                if (randnum < 0.5) {
                    return j;
                }
                else {
                    return k;
                }
            }



		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return (j);

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			//  Create child chromosome from parental material
			child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
