
//package DeltaFill;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class SinTab
{
  public static double R = 39;
  public static List<Integer> data;

  static
  {
    data = sinTab(R, 0);
  }

  public static void test()
  {
    double frac = 0.0;
    for (double r = 40; r < 128; r += 0.01)
    {
      List<Integer> tab = sinTab(r, frac);
      boolean ok = verifySinTab(tab);
      if (ok)
        System.out.println(r + ": " + ok);
    }
    System.out.println(verifySinTab(data));
    System.out.println(data);
  }

  static List<Integer> sinTab(double r, double frac)
  {
    ArrayList<Integer> tab = new ArrayList<Integer>();
    for (int i = 0; i < 256; ++i)
    {
      tab.add((int) (0.5 + (1 + Math.sin((i + frac) / 256.0 * 2 * Math.PI)) * r));
    }
    return tab;
  }

  static boolean verifySinTab(List<Integer> tab)
  {
    for (int i = 0; i < 256; ++i)
    {
      Set<Integer> vs = new HashSet<Integer>();
      int min = 1000, max = -1000;
      for (int j = 0; j < 256; ++j)
      {
        int v = (tab.get(j) + tab.get((i + j) % 256)) / 2;
        vs.add(v);
        if (v < min)
          min = v;
        if (v > max)
          max = v;
      }
      if (vs.size() != (max - min + 1))
        return false;
    }
    return true;
  }
}
