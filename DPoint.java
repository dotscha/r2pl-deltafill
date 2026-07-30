//package DeltaFill;

public class DPoint
{
  private double x, z;
  private double ang1, ang2;
  public int ang1i, ang2i;

  public DPoint(double x, double z)
  {

    this.x = x;
    this.z = z;
    // System.out.println("x: "+x+";z: "+z);
    double beta = Math.acos(Math.sqrt(x * x + z * z) / SinTab.R);
    double alfa = Math.atan2(x, z);
    // System.out.println("alfa/pi: "+alfa/Math.PI);
    double a1 = ang1 = alfa - beta;
    double a2 = ang2 = alfa + beta;
    while (a1 < 0)
      a1 += 2 * Math.PI;
    while (a2 < 0)
      a2 += 2 * Math.PI;
    while (a1 > 2 * Math.PI)
      a1 -= 2 * Math.PI;
    while (a2 > 2 * Math.PI)
      a2 -= 2 * Math.PI;
    ang1i = ((int) (a1 / Math.PI * 128 + 0.5)) & 255;
    ang2i = ((int) (a2 / Math.PI * 128 + 0.5)) & 255;
  }

  public void rotate(int a)
  {
    ang1+= a*Math.PI/128;
    ang2+= a*Math.PI/128;
    ang1i=(ang1i+a)&255;
    ang2i=(ang2i+a)&255;
  }

  public void zoom(double f)
  {
    DPoint t = new DPoint(x*f,z*f);
    this.x = t.x;
    this.z = t.z;
    this.ang1 = t.ang1;
    this.ang2 = t.ang2;
    this.ang1i = t.ang1i;
    this.ang2i = t.ang2i;
  }

  public double zi(int ang)
  {
    return z(Math.PI * ang / 128.0);
  }

  public double z(double ang)
  {
    return SinTab.R * (Math.cos(ang1 + ang) + Math.cos(ang2 + ang)) / 2;
  }

  public double x(double ang)
  {
    return SinTab.R * (Math.sin(ang1 + ang) + Math.sin(ang2 + ang)) / 2;
  }

  public int ix(int ang)
  {
    return (SinTab.data.get((ang1i + ang) & 255) + SinTab.data.get((ang2i + ang) & 255)) / 2;
  }

  public boolean increasing(int ang)
  {
    return Math.cos((ang1 + ang2) / 2 + Math.PI * ang / 128) >= 0;
  }

  public int hashCode()
  {
    return ang1i + 256 * ang2i;
  }

  public boolean equals(Object o)
  {
    return o instanceof DPoint && hashCode() == o.hashCode();
  }

  public String toString()
  {
    return "(" + ang1i + ";" + ang2i + ")";
  }
}
