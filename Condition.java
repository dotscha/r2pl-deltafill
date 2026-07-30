import java.util.*;

public class Condition
{
  public List<Integer> in, out;

  public Condition()
  {
    in = new ArrayList<Integer>();
    out = new ArrayList<Integer>();
  }

  boolean apply(int v)
  {
    int siz = in.size();
    if (siz==0)
      return false;
    int i = -1;
    while (i+1<siz && v>=in.get(i+1))
      i++;
    if (i<0)
    	return out.get(0)<in.get(0) && v<out.get(0);
    if (in.get(i)>out.get(i))
      i++;
    return i==siz || v<out.get(i);
  }

  public void set(List<Boolean> p)
  {
    in.clear();
    out.clear();
    for (int i = 0; i < p.size(); ++i)
    {
      int j = (i + p.size() - 1) % p.size();
      if (p.get(i) && !p.get(j))
        in.add(i);
      if (p.get(j) && !p.get(i))
        out.add(i);
    }
  }

  public String toString()
  {
    return "<in:" + in + ";out:" + out + ">";
  }

  public int hashCode()
  {
    return in.hashCode() + 2 * out.hashCode();
  }

  public boolean equals(Object o)
  {
    if (!(o instanceof Condition))
      return false;
    Condition c = (Condition) o;
    return in.equals(c.in) && out.equals(c.out);
  }
}
