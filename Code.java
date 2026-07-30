import java.util.*;

public class Code
{
  public static void renderer(Map<Condition, List<Plotter>> code)
  {
    int group = 0;
    Map<Integer, List<Integer>> a2gsIn = new HashMap<Integer, List<Integer>>();
    Map<Integer, List<Integer>> a2gsOut = new HashMap<Integer, List<Integer>>();
    for (Condition c : code.keySet())
    {
      comment(c.toString());
      if(c.apply(255))
        code("plotterGroupBegin1 " + group);
      else
        code("plotterGroupBegin0 " + group);
      for (Plotter pl : code.get(c))
        code(pl);
      code("plotterGroupEnd " + group);
      for (Integer a : c.in)
      {
        List<Integer> in = a2gsIn.get(a);
        if (in == null)
          a2gsIn.put(a, in = new ArrayList<Integer>());
        in.add(group);
      }
      for (Integer a : c.out)
      {
        List<Integer> out = a2gsOut.get(a);
        if (out == null)
          a2gsOut.put(a, out = new ArrayList<Integer>());
        out.add(group);
      }
      group++;
    }
    code("plottersEnd");
    for (int a = 0; a < 256; ++a)
    {
      List<Integer> gsIn = a2gsIn.get(a);
      List<Integer> gsOut = a2gsOut.get(a);
      int gr1 = 0;
      /**/
      for (Condition c : code.keySet())
      {
        if (c.apply(a))
          break;
        gr1++;
      }
      /**/
      if (gsIn == null && gsOut == null)
      {
        code("renderer4AngDefaults " + a + ", "+gr1);
        continue;
      }
      code("renderer4Ang " + a);
      if (gsIn != null)
      {
        code("enableGroup");
        for (Integer gr : gsIn)
          code("updateGroup " + gr);
      }
      if (gsOut != null)
      {
        code("disableGroup");
        for (Integer gr : gsOut)
          code("updateGroup " + gr);
      }
      code("callRender "+gr1);
    }
  }

  static void code(Plotter pl)
  {
    code("plotter color_" + pl.color + "," + pl.s + "," + pl.p.ang1i + "," + pl.p.ang2i + ","
        + (pl.fill ? 0 : pl.increase ? -1 : 1));
  }

  static void label(String l)
  {
    System.out.println(l);
  }

  static void code(String c)
  {
    System.out.println("\t" + c);
  }

  static void comment(String c)
  {
    System.out.println(";" + c);
  }
}
