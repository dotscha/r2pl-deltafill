
public class P3D implements Comparable<P3D>
{
  public double x,y,z;

  public P3D(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public P3D rot(Matrix m)
  {
    return new P3D(
      x*m.get(0,0)+y*m.get(1,0)+z*m.get(2,0),
      x*m.get(0,1)+y*m.get(1,1)+z*m.get(2,1),
      x*m.get(0,2)+y*m.get(1,2)+z*m.get(2,2)
    );
  }

  public P3D add(P3D v)
  {
    return new P3D(x+v.x,y+v.y,z+v.z);
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(z);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    P3D other = (P3D) obj;
    if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
      return false;
    if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
      return false;
    if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "P3D [x=" + x + ", y=" + y + ", z=" + z + "]";
  }

  @Override
  public int compareTo(P3D o)
  {
    if (o.x>x) return  1;
    if (o.x<x) return -1;
    if (o.y>y) return  1;
    if (o.y<y) return -1;
    if (o.z>z) return  1;
    if (o.z<z) return -1;
    return 0;
  }
}
