package stat.kent.input;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VectorColumnSumOperation extends VectorOperations{

	public VectorColumnSumOperation() {
		// TODO Auto-generated constructor stub
	}
	
	public void operate(Vector3D vector)
	{
		count++;
		totalSumX+=vector.getX();
		totalSumY+=vector.getY();
		totalSumZ+=vector.getZ();
	}

}
