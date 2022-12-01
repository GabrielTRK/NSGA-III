package com.nsgaiii.nsgaiiidemo.App.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class ReferencePoint {

	public List<Double> position ;
	private int memberSize ;
	private List<Pair<Individuo, Double>> potentialMembers ;

	public ReferencePoint() {
	}

	  /** Constructor */
	public ReferencePoint(int size) {
	  position = new ArrayList<>();
	  for (int i =0; i < size; i++)
	    position.add(0.0);
	  memberSize = 0 ;
	  potentialMembers = new ArrayList<>();
	}

	public ReferencePoint(ReferencePoint point) {
	  position = new ArrayList<>(point.position.size());
	  for (Double d : point.position) {
	    position.add(d);
	  }
	  memberSize = 0 ;
	  potentialMembers = new ArrayList<>();
	}

	public void generateReferencePoints(
	        List<ReferencePoint> referencePoints,
	        int numberOfObjectives,
	        int numberOfDivisions) {

	  ReferencePoint refPoint = new ReferencePoint(numberOfObjectives) ;
	  generateRecursive(referencePoints, refPoint, numberOfObjectives, numberOfDivisions, numberOfDivisions, 0);
	}

	private void generateRecursive(
	        List<ReferencePoint> referencePoints,
	        ReferencePoint refPoint,
	        int numberOfObjectives,
	        int left,
	        int total,
	        int element) {
	  if (element == (numberOfObjectives - 1)) {
	    refPoint.position.set(element, (double) left / total) ;
	    referencePoints.add(new ReferencePoint(refPoint)) ;
	  } else {
	    for (int i = 0 ; i <= left; i +=1) {
	      refPoint.position.set(element, (double)i/total) ;

	      generateRecursive(referencePoints, refPoint, numberOfObjectives, left-i, total, element+1);
	    }
	  }
	}
	  
	public List<Double> pos()  { return this.position; }
	public int  MemberSize(){ return memberSize; }
	public boolean HasPotentialMember() { return potentialMembers.size()>0; }
	public void clear(){ memberSize=0; this.potentialMembers.clear();}
	public void AddMember(){this.memberSize++;}
	public void AddPotentialMember(Individuo member_ind, double distance){
	  this.potentialMembers.add(new ImmutablePair<Individuo,Double>(member_ind,distance) );
	}

	@Override
	public String toString() {
		return "ReferencePoint [position=" + position + ", memberSize=" + memberSize + ", potentialMembers="
				+ potentialMembers + "]";
	}
	
	public List<Pair<Individuo, Double>> getPotentialMembers() {
		return potentialMembers;
	}

	public void setPotentialMembers(List<Pair<Individuo, Double>> potentialMembers) {
		this.potentialMembers = potentialMembers;
	}

	public void sort() {
		this.potentialMembers.sort(new Comparator<Pair<Individuo, Double>>() {
	        @Override
	        public int compare(Pair<Individuo, Double> o1, Pair<Individuo, Double> o2) {
	            if (o1.getRight() > o2.getRight()) {
	                return -1;
	            } else if (o1.getRight().equals(o2.getRight())) {
	                return 0; 
	            } else {
	                return 1;
	            }
	        }
	    });
	}

	public Individuo FindClosestMember() {
	  return this.potentialMembers.remove(this.potentialMembers.size() - 1)
	            .getLeft();
	}
	  
	public Individuo RandomMember() {
	  int index = this.potentialMembers.size()>1 ? Utils.nextInt(0, this.potentialMembers.size()-1):0;
	  return this.potentialMembers.remove(index).getLeft();
	}
	
}
