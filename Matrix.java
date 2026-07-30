public class Matrix
{
  private double[] data;
  
  public static Matrix I = new Matrix(new double[]{1,0,0,0,1,0,0,0,1});
  
  public Matrix()
  {
    this(new double[]{0,0,0,0,0,0,0,0,0});
  }
  
  public Matrix(double[] d)
  {
    data = d;
  }
  
  public double get(int row, int col)
  {
    return data[3*row+col];
  }
  
  public static Matrix rotz(double a)
  {
    return new Matrix(new double[]
    {
      Math.cos(a),-Math.sin(a), 0,
      Math.sin(a), Math.cos(a), 0,
      0          , 0          , 1
    });
  }
  
  public static Matrix roty(double a)
  {
    return new Matrix(new double[]
    {
      Math.cos(a), 0,-Math.sin(a),
      0          , 1, 0          ,             
      Math.sin(a), 0, Math.cos(a)
    });
  }
  
  public static Matrix rotx(double a)
  {
    return new Matrix(new double[]
    {
      1, 0          , 0          ,
      0, Math.cos(a),-Math.sin(a),            
      0, Math.sin(a), Math.cos(a)
    });
  }
  
  public Matrix mul(Matrix m)
  {
    double[] d = new double[9];
    for (int i=0; i<3; ++i)
      for (int j=0; j<3; ++j)
        d[i+3*j] = get(j,0)*m.get(0,i)+get(j,1)*m.get(1,i)+get(j,2)*m.get(2,i);
    return new Matrix(d);
  }
}