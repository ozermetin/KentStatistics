package stat.kent.input;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VectorCovarianceOperation extends VectorOperations {

	public VectorCovarianceOperation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void operate(Vector3D vector) {
		count++;
		totalSumX+=Math.pow(vector.getX(), 2);
		totalSumY+=Math.pow(vector.getY(), 2);
		totalSumZ+=Math.pow(vector.getZ(), 2);
		totalXY+=vector.getX()*vector.getY();
		totalYZ+=vector.getY()*vector.getZ();
		totalXZ+=vector.getX()*vector.getZ();
	}

}
