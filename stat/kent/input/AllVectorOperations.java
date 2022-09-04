package stat.kent.input;


import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.RealMatrix;

public class AllVectorOperations extends VectorOperations {

	VectorColumnSumOperation sumOperation;
	VectorCovarianceOperation covOperation;
	double sumTheta=0, sumPhi=0;
	Vector3D r1Vector;
	
	public AllVectorOperations() {
		sumOperation= new VectorColumnSumOperation();
		covOperation = new VectorCovarianceOperation();
	}
	

	@Override
	public void operate(Vector3D vector) {
		SphericalCoordinates coor = new SphericalCoordinates(vector);
		sumTheta+=coor.getPhi();
		double phi = coor.getTheta();
		if(phi<0)
			phi+=Math.PI*2;
		sumPhi+=phi;
		sumOperation.operate(vector);
		covOperation.operate(vector);
	}
	
	public Vector3D calculateAll(RealMatrix covMatrix)
	{
		r1Vector = new Vector3D(sumOperation.totalSumX,sumOperation.totalSumY,sumOperation.totalSumZ);
		covMatrix.setEntry(0, 0, covOperation.totalSumX/covOperation.count);
		covMatrix.setEntry(0, 1, covOperation.totalXY/covOperation.count);
		covMatrix.setEntry(0, 2, covOperation.totalXZ/covOperation.count);
		covMatrix.setEntry(1, 0, covOperation.totalXY/covOperation.count);
		covMatrix.setEntry(1, 1, covOperation.totalSumY/covOperation.count);
		covMatrix.setEntry(1, 2, covOperation.totalYZ/covOperation.count);
		covMatrix.setEntry(2, 0, covOperation.totalXZ/covOperation.count);
		covMatrix.setEntry(2, 1, covOperation.totalYZ/covOperation.count);
		covMatrix.setEntry(2, 2, covOperation.totalSumZ/covOperation.count);
		return r1Vector;
	}

}
