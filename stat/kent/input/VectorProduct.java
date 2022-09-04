package stat.kent.input;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class VectorProduct {

	public VectorProduct() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector3D vector1 = new Vector3D(-0.9972230359171196,-0.024288288322674594,0.07040096367662711);
		Vector3D eig2 = new Vector3D(0.01142287578648903,0.8842519984489067,0.4668703472570092);
		
		Vector3D vector2 = new Vector3D(0, 0, 1);
		//Vector3D vector1 = new Vector3D(1, 0, 0);
		//Vector3D vector2 = new Vector3D(0, 0, 1);
		Vector3D vector3 = vector1.crossProduct(vector2);
		double rotation = Vector3D.angle(vector1, vector2);
		System.out.println("X:" + vector3.getX() + " Y:" + vector3.getY()
				+ " Z:" + vector3.getZ() + " Rotation:" + rotation);

		Vector3D rotationVector = vector3.normalize();

		System.out.println("X:" + rotationVector.getX() + " Y:" + rotationVector.getY()
				+ " Z:" + rotationVector.getZ() + " Rotation:" + rotation);
		
		RealMatrix identity3_3 = MatrixUtils.createRealIdentityMatrix(3);

		RealVector tensor = MatrixUtils.createRealVector(rotationVector
				.toArray());
		RealMatrix crossProduct = MatrixUtils.createRealMatrix(3, 3);
		crossProduct.setEntry(0, 0, 0);
		crossProduct.setEntry(0, 1, -rotationVector.getZ());
		crossProduct.setEntry(0, 2, rotationVector.getY());
		crossProduct.setEntry(1, 0, rotationVector.getZ());
		crossProduct.setEntry(1, 1, 0);
		crossProduct.setEntry(1, 2, -rotationVector.getX());
		crossProduct.setEntry(2, 0, -rotationVector.getY());
		crossProduct.setEntry(2, 1, rotationVector.getX());
		crossProduct.setEntry(2, 2, 0);
		RealMatrix tensorProduct = tensor.outerProduct(tensor);

		RealMatrix rotationMatrix = (identity3_3.scalarMultiply(
				Math.cos(rotation)).add((crossProduct).scalarMultiply(Math
				.sin(rotation)))).add(tensorProduct.scalarMultiply(1 - Math
				.cos(rotation)));
		
		System.out.println(rotationMatrix);
		
		RealMatrix vector1Matrix = MatrixUtils.createColumnRealMatrix(vector1.toArray());
		RealMatrix result = rotationMatrix.multiply(vector1Matrix);
		System.out.println("Result by Rotation:"+result);
		
		RealMatrix vector2Matrix = MatrixUtils.createColumnRealMatrix(eig2.toArray());
		RealMatrix result2 = rotationMatrix.multiply(vector2Matrix);
		System.out.println("Result by Rotation:"+result2);
		/* Rotation to quart */
		/*
		double t=rotationMatrix.getTrace();
		double r=Math.sqrt(t);
		double s=0.5/r;
		double w=0.5*r;
		double x=rotationMatrix.getEntry(2,1)-rotationMatrix.getEntry(1, 2)*s;
		double y=rotationMatrix.getEntry(0,2)-rotationMatrix.getEntry(2, 0)*s;
		double z=rotationMatrix.getEntry(1,0)-rotationMatrix.getEntry(0, 1)*s;
		
		System.out.println("w:"+w+" x:"+x+" y:"+y+" z:"+z);
		*/
		//double q1,q2,q3,q4;
		Quaternion quaRot=new Quaternion();
		quaRot.setX(rotationVector.getX()*Math.sin(rotation/2));
		quaRot.setY(rotationVector.getY()*Math.sin(rotation/2));
		quaRot.setZ(rotationVector.getZ()*Math.sin(rotation/2));
		quaRot.setW(Math.cos(rotation/2));
		System.out.println("w:"+quaRot.getW()+" x:"+quaRot.getX()+" y:"+quaRot.getY()+" z:"+quaRot.getZ());
		
		double angle,x,y,z;
		angle=2*Math.acos(quaRot.getW());
		x=quaRot.getX()/Math.sqrt(1-quaRot.getW()*quaRot.getW());
		y=quaRot.getY()/Math.sqrt(1-quaRot.getW()*quaRot.getW());
		z=quaRot.getZ()/Math.sqrt(1-quaRot.getW()*quaRot.getW());
		System.out.println("angle:"+angle+" x:"+x+" y:"+y+" z:"+z);
		
		Quaternion quatVec=new Quaternion(0,vector1.getX(),vector1.getY(),vector1.getZ());
		Quaternion resultQua=quaRot.multiply(quatVec).multiply(quaRot.inverse());
		
		System.out.println("Result by Qua w:"+resultQua.getW()+" x:"+resultQua.getX()+" y:"+resultQua.getY()+" z:"+resultQua.getZ());
		/*
		RealMatrix quatRot=MatrixUtils.createRowRealMatrix(new double[]{q4, q1,q2,q3});
		RealMatrix quatInv = MatrixUtils.createRowRealMatrix(new double[]{q4, -q1,-q2,-q3,});
		RealMatrix vector2Matrix = MatrixUtils.createColumnRealMatrix(new double[]{vector1.getX(),vector1.getY(),vector1.getZ(),0});
		*/
		//System.out.println("Result by Quart:"+quatRot.multiply(vector2Matrix).multiply(quatInv));
	}

}
