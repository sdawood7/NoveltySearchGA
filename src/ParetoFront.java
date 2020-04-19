/*************SKELETON*************

// Compare chromo1 and chromo2

// First compare 
int rawFitnessComparison;
int noveltyFitnessComparison;

// CONSIDER DOING FLOATING POINT ROUNDING

if (chromo1.rawFitness > chromo2.rawFitness) {
    rawFitnessComparison = 1;  // chromo1 has higher rawFitness
}
else if (chromo1.rawFitness < chromo2.rawFitness) {
    rawFitnessComparison = -1; // chromo2 has higher rawFitness
}
else {
    rawFitnessComparison = 0;  // rawFitnesses are equal
}

if (chromo1.noveltyFitness > chromo2.noveltyFitness) {
    noveltyFitnessComparison = 1;  // chromo1 has higher noveltyFitness
}
else if (chromo1.noveltyFitness < chromo2.noveltyFitness) {
    noveltyFitnessComparison = -1; // chromo2 has higher noveltyFitness
}
else {
    noveltyFitnessComparison = 0;  // noveltyFitnesses are equal
}


if ((rawFitnessComparison == 1) && (noveltyFitnessComparison == 1) ||
    (rawFitnessComparison == 1) && (noveltyFitnessComparison == 0) ||
    (rawFitnessComparison == 0) && (noveltyFitnessComparison == 1)) {
    // chromo1 dominates chromo2
}
else if ((rawFitnessComparison == -1) && (noveltyFitnessComparison == -1) ||
         (rawFitnessComparison == -1) && (noveltyFitnessComparison ==  0) ||
         (rawFitnessComparison ==  0) && (noveltyFitnessComparison == -1)) {
    // chromo2 dominates chromo1
}
else if ((rawFitnessComparison == 0) && (noveltyFitnessComparison == 0)) {
    // chromo1 and chromo2 have the same fitnesses
}
else {
    // Neither chromo1 nor chromo2 dominate each other
}

***********END SKELETON***********/