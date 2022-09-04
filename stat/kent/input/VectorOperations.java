package stat.kent.input;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class VectorOperations {

	double totalSumX=0;
	double totalSumY=0;
	double totalSumZ=0;
	double totalXY=0;
	double totalYZ=0;
	double totalXZ=0;
	int count=0;
	public VectorOperations() {
		// TODO Auto-generated constructor stub
	}
	
	public void compute(List<Vector3D> vectorList)
	{
		for(Vector3D vector : vectorList)
		{
			operate(vector);
		}
	}
	
	public abstract void operate(Vector3D vector);
	
	public void printStats()
	{
		System.out.println("Total X:"+totalSumX+" Total Y:"+totalSumY+" Total Z:"+totalSumZ);
		System.out.println("Total XY:"+totalXY+" Total YZ:"+totalYZ+" Total XZ:"+totalXZ);
		System.out.println("Avg X:"+totalSumX/count+" Avg Y:"+totalSumY/count+" Avg Z:"+totalSumZ/count);
		System.out.println("Avg XY:"+totalXY/count+" Avg YZ:"+totalYZ/count+" Avg XZ:"+totalXZ/count);
	}
}
