package stat.kent.input;

public class Quaternion {
	private double w,x,y,z;
	
	public Quaternion() {
		// TODO Auto-generated constructor stub
	}

	public Quaternion(double w, double x, double y,double z) {
		this.w=w;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Quaternion inverse()
	{
		return new Quaternion(w,-x,-y,-z);
	}
	
	public Quaternion multiply(Quaternion by)
	{
		double a1=this.w,b1=this.x,c1=this.y,d1=this.z,a2=by.w,b2=by.x,c2=by.y,d2=by.z;
		
		Quaternion result = new Quaternion();
		result.w=a1*a2-b1*b2-c1*c2-d1*d2;
		result.x=a1*b2+b1*a2+c1*d2-d1*c2;
		result.y=a1*c2-b1*d2+c1*a2+d1*b2;
		result.z=a1*d2+b1*c2-c1*b2+d1*a2;
		return result;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

}
