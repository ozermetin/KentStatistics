package stat.kent.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class KentStatistic {
	
	public List<Vector3D> vectorList;
	private RealMatrix covarianceMatrix;
	private Vector3D r1Vector;
	private RealMatrix hMatrix, bMatrix, kMatrix, vMatrix;
	private double avgTheta;
	private double avgPhi;
	private double kappa;
	private double betta;

	public KentStatistic() {
		vectorList = new ArrayList<Vector3D>();
		covarianceMatrix = MatrixUtils.createRealMatrix(3, 3);
		hMatrix = MatrixUtils.createRealMatrix(3, 3);
		kMatrix = MatrixUtils.createRealMatrix(3, 3);
		avgTheta = 0;
		avgPhi = 0;
	}
	
	public void addVectorAsInput(double x, double y, double z)
	{
		vectorList.add(new Vector3D(x,y,z));
	}
	
	public void addAllVectorAsList(List<Vector3D> vectors)
	{
		vectorList.addAll(vectors);
	}
	
	public void addSphericalAsInput(double theta, double phi)
	{
		addSphericalAsInput(1, theta, phi); 
	}
	
	public void addSphericalAsInput(double r, double theta, double phi)
	{
		SphericalCoordinates coor = new SphericalCoordinates(r, theta, phi);
		vectorList.add(coor.getCartesian());
	}
	
	public void loadVectorDataFromFile(String fileName) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(fileName) );
		while(sc.hasNext())
		{
			addVectorAsInput(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
		}
		sc.close();
	}
	
	public void loadSphericalDataFromFile(String fileName) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(fileName) );
		while(sc.hasNext())
		{
			addSphericalAsInput(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
		}
		sc.close();
	}
	
	public void printVectorData(OutputStream outStream)
	{
		PrintWriter writer = new PrintWriter(outStream);
		for(Vector3D vector : vectorList)
		{
			writer.println(vector);
		}
		writer.flush();
	}
	
	public void printSphericalData(OutputStream outStream)
	{
		PrintWriter writer = new PrintWriter(outStream);
		for(Vector3D vector : vectorList)
		{
			SphericalCoordinates coor = new SphericalCoordinates(vector);
			double phi = coor.getTheta();
			if(phi<0)
				phi+=Math.PI*2;
			writer.println("r="+coor.getR()+" Theta="+coor.getPhi()+" Phi="+phi);
		}
		writer.flush();
	}
	
	public void calculateMeanR()
	{
		VectorColumnSumOperation operation = new VectorColumnSumOperation();
		operation.compute(vectorList);
		r1Vector = new Vector3D(operation.totalSumX,operation.totalSumY,operation.totalSumZ);
	}
	
	public void calculateSumThetaandPhi()
	{
		for(Vector3D vector: vectorList)
		{
			SphericalCoordinates coor = new SphericalCoordinates(vector);
			avgTheta+=coor.getPhi();
			double phi = coor.getTheta();
			if(phi<0)
				phi+=Math.PI*2;
			avgPhi+=phi;
		}
		avgTheta = avgTheta / vectorList.size();
		avgPhi = avgPhi / vectorList.size();
		
	}
	
	public void calculateCovarianceMatrix()
	{
		VectorCovarianceOperation operationCov= new VectorCovarianceOperation();
		operationCov.compute(vectorList);
		
		covarianceMatrix.setEntry(0, 0, operationCov.totalSumX/operationCov.count);
		covarianceMatrix.setEntry(0, 1, operationCov.totalXY/operationCov.count);
		covarianceMatrix.setEntry(0, 2, operationCov.totalXZ/operationCov.count);
		covarianceMatrix.setEntry(1, 0, operationCov.totalXY/operationCov.count);
		covarianceMatrix.setEntry(1, 1, operationCov.totalSumY/operationCov.count);
		covarianceMatrix.setEntry(1, 2, operationCov.totalYZ/operationCov.count);
		covarianceMatrix.setEntry(2, 0, operationCov.totalXZ/operationCov.count);
		covarianceMatrix.setEntry(2, 1, operationCov.totalYZ/operationCov.count);
		covarianceMatrix.setEntry(2, 2, operationCov.totalSumZ/operationCov.count);
	}
	
	public void calculateAllVectorOperations()
	{
		AllVectorOperations allOperations = new AllVectorOperations();
		allOperations.compute(vectorList);
		r1Vector = allOperations.calculateAll(covarianceMatrix);
		avgTheta=allOperations.sumTheta/vectorList.size();
		avgPhi = allOperations.sumPhi/vectorList.size();
	}
	
	public void calculateHMatrix()
	{
		hMatrix.setEntry(0, 0, Math.cos(avgTheta)*Math.cos(avgPhi));
		hMatrix.setEntry(0, 1, -Math.sin(avgPhi));
		hMatrix.setEntry(0, 2, -Math.sin(avgTheta));
		hMatrix.setEntry(1, 0, Math.cos(avgTheta)*Math.sin(avgPhi));
		hMatrix.setEntry(1, 1, Math.cos(avgPhi));
		hMatrix.setEntry(1, 2, Math.sin(avgTheta)*Math.sin(avgPhi));
		hMatrix.setEntry(2, 0, Math.sin(avgTheta)*Math.cos(avgPhi));
		hMatrix.setEntry(2, 1, 0);
		hMatrix.setEntry(2, 2, Math.cos(avgTheta));
	}
	
	public void calculateBMatrix()
	{
		bMatrix=hMatrix.transpose().multiply(covarianceMatrix).multiply(hMatrix);
	}
	
	public void calculateKMatrix()
	{
		double alpha = Math.atan(2*bMatrix.getEntry(0, 1)/(bMatrix.getEntry(0, 0)-bMatrix.getEntry(1, 1)))/2;
		kMatrix.setEntry(0, 0, Math.cos(alpha));
		kMatrix.setEntry(0, 1, -Math.sin(alpha));
		kMatrix.setEntry(0, 2, 0);
		kMatrix.setEntry(1, 0, Math.sin(alpha));
		kMatrix.setEntry(1, 1, Math.cos(alpha));
		kMatrix.setEntry(1, 2, 0);
		kMatrix.setEntry(2, 0, 0);
		kMatrix.setEntry(2, 1, 0);
		kMatrix.setEntry(2, 2, 1);
	}
	
	public void calculateVMatrix()
	{
		RealMatrix gMatrix= hMatrix.multiply(kMatrix);
		vMatrix = gMatrix.transpose().multiply(covarianceMatrix).multiply(gMatrix);
	}
	
	public void calculateKappaBeta()
	{
		double r2=vMatrix.getEntry(0, 0) - vMatrix.getEntry(1, 1);
		kappa = 1/(2 - 2*r1Vector.getNorm()/vectorList.size() - r2) + 1/(2 - 2*r1Vector.getNorm()/vectorList.size() + r2);
		betta = (1/(2 - 2*r1Vector.getNorm()/vectorList.size() - r2) - 1/(2 - 2*r1Vector.getNorm()/vectorList.size() + r2))/2;
		
		if(kappa/betta > 2)
			System.out.println("Unimodal");
		else
			System.out.println("Bimodal");
	}
	
	public static void main(String[] args)
	{
		KentStatistic kent = new KentStatistic();
		try {
			kent.loadVectorDataFromFile("VectorData.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kent.printVectorData(System.out);
		System.out.println("***************************");
		kent.printSphericalData(System.out);
		/*kent.calculateMeanR();
		kent.calculateCovarianceMatrix();
		kent.calculateSumThetaandPhi();
		*/
		kent.calculateAllVectorOperations();
		System.out.println("***************************");
		System.out.println("RVectorNormLength"+kent.r1Vector.getNorm()/kent.vectorList.size());
		System.out.println("Avg Theta="+kent.avgTheta+" Avg Phi="+kent.avgPhi);
		System.out.println("***************************");
	
		System.out.println("S="+kent.covarianceMatrix);
		
		kent.calculateHMatrix();
		System.out.println("H="+kent.hMatrix);
		kent.calculateBMatrix();
		System.out.println("B="+kent.bMatrix);
		kent.calculateKMatrix();
		System.out.println("K="+kent.kMatrix);
		kent.calculateVMatrix();
		System.out.println("V="+kent.vMatrix);
		kent.calculateKappaBeta();
		System.out.println("Kappa="+kent.kappa+" Betta="+kent.betta);
		System.out.println("***************************");
	}

	public Vector3D getR1Vector() {
		return r1Vector;
	}

	public void setR1Vector(Vector3D r1Vector) {
		this.r1Vector = r1Vector;
	}

	public double getAvgTheta() {
		return avgTheta;
	}

	public void setAvgTheta(double avgTheta) {
		this.avgTheta = avgTheta;
	}

	public double getAvgPhi() {
		return avgPhi;
	}

	public void setAvgPhi(double avgPhi) {
		this.avgPhi = avgPhi;
	}

	public double getKappa() {
		return kappa;
	}

	public void setKappa(double kappa) {
		this.kappa = kappa;
	}

	public double getBetta() {
		return betta;
	}

	public void setBetta(double betta) {
		this.betta = betta;
	}

}
