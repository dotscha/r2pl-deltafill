
import java.util.*;

public class Polygon
{
  private String color;
  private List<P3D> contour = new ArrayList<P3D>();
  
  public Polygon(String c)
  {
    color = c;
  }
  
  public void addContour(P3D... p)
  {
    int s = contour.size();
    for (P3D pi : p)
      contour.add(pi);
    if (s>2)
    {
      contour.add(contour.get(s));
      contour.add(contour.get(s-1));
    }
  }
  
  public void sliceIt(Map<Integer,Slice> ss)
  {
    double ymin = Double.POSITIVE_INFINITY;
    double ymax = Double.NEGATIVE_INFINITY;
    for (P3D p : contour)
    {
      ymin = Math.min(ymin, p.y);
      ymax = Math.max(ymax, p.y);
    }
    if (ymin > Math.floor(ymin))
      ymin = Math.floor(ymin)+1;
    ymax = Math.floor(ymax);
    if (ymax>=ymin)
    {
      for (int y = (int) Math.round(ymin); y<=Math.round(ymax); ++y)
      {
        Set<List<P3D>> pairs = new HashSet<List<P3D>>();
        for (int i = 0; i<contour.size(); ++i)
        {
          int j = (i+1)%contour.size();
          P3D p1 = contour.get(i);
          P3D p2 = contour.get(j);
          if ((p1.y<=y && y<p2.y) || (p2.y<=y && y<p1.y))
          {
            ArrayList<P3D> pair = new ArrayList<P3D>(2);
            if (p1.compareTo(p2)>0)
            {
              pair.add(p2);pair.add(p1);
            }
            else
            {
              pair.add(p1);pair.add(p2);
            }
            if (pairs.contains(pair))
              pairs.remove(pair);
            else
              pairs.add(pair);
          }
        }
        List<P3D> iss = new ArrayList<P3D>(pairs.size());
        for (List<P3D> pair : pairs)
        {
          P3D p1 = pair.get(0);
          P3D p2 = pair.get(1);
          double w1 = (p2.y-y)/(p2.y-p1.y);
          double w2 = (y-p1.y)/(p2.y-p1.y);
          P3D py = new P3D(p1.x*w1+p2.x*w2,y,p1.z*w1+p2.z*w2);
          if (iss.contains(py))
            iss.remove(py);
          else
            iss.add(py);
        }
        if (iss.size()%2 == 1)
        {
          throw new RuntimeException("How come?");
        }
        if (iss.isEmpty())
          continue;
        Collections.sort(iss);
        Slice s = ss.get(y);
        s = s!=null ? s : new Slice(y);
        for (int i = 0; i<iss.size(); i+=2)
        {
          P3D p1 = iss.get(i);
          P3D p2 = iss.get(i+1);
          s.add(new DPoint(p1.x,p1.z), new DPoint(p2.x,p2.z), color);
        }
        ss.put(y, s);
      }
    }
  }

  @Override
  public String toString()
  {
    return "Polygon [color=" + color + ", contour=" + contour + "]";
  }
}
