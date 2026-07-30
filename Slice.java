import java.util.*;

public class Slice
{
  private int idx;
  private List<DPoint> ps;
  private List<Integer> as, bs;
  private List<String> cs;

  public Slice(int index)
  {
    idx = index;
    ps = new ArrayList<DPoint>();
    as = new ArrayList<Integer>();
    bs = new ArrayList<Integer>();
    cs = new ArrayList<String>();
  }

  public void add(DPoint a, DPoint b, String color)
  {
    as.add(index(a));
    bs.add(index(b));
    cs.add(color);
  }
  
  public List<DPoint> dpoints()
  {
    return ps;
  }
  
  private int index(DPoint x)
  {
    int i = ps.indexOf(x);
    if (i == -1)
    {
      i = ps.size();
      ps.add(x);
    }
    return i;
  }

  public Map<Condition, List<Plotter>> plotters()
  {
    Map<Condition, List<Plotter>> out = new HashMap<Condition, List<Plotter>>();
    plotters(out);
    return out;
  }

  public void plotters(Map<Condition, List<Plotter>> code)
  {
    ZBuff zbp = render(255);
    Map<Plotter, List<Boolean>> plm = new HashMap<Plotter, List<Boolean>>();
    for (int i = 0; i < 256; ++i)
    {
      ZBuff zb = render(i);
//System.out.println(zb); , 
      for (Plotter pl : plotters(zbp, zb))
      {
        List<Boolean> visible = plm.get(pl);
        if (visible == null)
        {
          visible = new ArrayList<Boolean>(256);
          for (int x = 0; x < 256; ++x)
            visible.add(false);
          plm.put(pl, visible);
        }
        visible.set(i, true);
      }
      zbp = zb;
    }
    for (Map.Entry<Plotter, List<Boolean>> e : plm.entrySet())
    {
      Condition c = new Condition();
      c.set(fixNoise(e.getValue()));
      List<Plotter> pls = code.get(c);
      if (pls == null)
      {
        pls = new ArrayList<Plotter>();
        code.put(c, pls);
      }
      pls.add(e.getKey());
    }
  }
  
  private List<Boolean> fixNoise(List<Boolean> m)
  {
    /**/
    for (int i = 0; i<m.size(); i++)
    {
      if (m.get(i) && !m.get((i+1)%m.size()) && m.get((i+2)%m.size()))
      {
        m.set((i+1)%m.size(), true);
      }
    }
    /**/
    return m;
  }
  

  public List<Plotter> plotters(ZBuff zbp, ZBuff zb)
  {
    List<Plotter> pls = new ArrayList<Plotter>();
    for (Map.Entry<Integer, Integer> e : zb.ps.entrySet())
    {
      int x = e.getKey();
      DPoint p = ps.get(e.getValue());
      boolean incr = (zb.lri.get(x) & ZBuff.INCR) > 0;
      String color = zb.cs.get(x);
      boolean fill = true;
      Integer xb = x + (incr ? -1 : 1);
      Integer xa = x + (incr ? 1 : -1);
      String cb = zb.cs.get(xb);
      String ca = zb.cs.get(xa);
      if (color.equals(ca) && color.equals(cb))
      {
        if (color.equals(zbp.cs.get(x)))
          continue;
      } else
      {
        if (!color.equals(cb))
        {
          if ((zb.lri.get(x) & (ZBuff.LEFT | ZBuff.RIGHT)) == (ZBuff.LEFT | ZBuff.RIGHT))
          {
            pls.add(new Plotter(idx, p, fill, incr, color));
          }
          if (!zb.ps.containsKey(xb))
          {
            fill = false;
            color = cb;
          }
        }
      }
      pls.add(new Plotter(idx, p, fill, incr, color));
    }
    return pls;
  }

  public ZBuff render(int ang)
  {
    ZBuff zb = new ZBuff();
    for (int i = 0; i < cs.size(); ++i)
    {
      String c = cs.get(i);
      DPoint a = ps.get(as.get(i));
      DPoint b = ps.get(bs.get(i));
      int ax = a.ix(ang);
      int bx = b.ix(ang);
      if (ax > bx)
      {
        int t = ax;
        ax = bx;
        bx = t;
        DPoint tp = a;
        a = b;
        b = tp;
      }
      // System.out.println(c+": "+ax+"-"+bx);
      double az = a.zi(ang);
      double bz = b.zi(ang);
      if (bx > ax)
      {
        double tan = (bz - az) / (bx - ax);
        for (int x = ax; x <= bx; ++x)
        {
          Integer p = x == ax ? (Integer) index(a) : (x == bx ? (Integer) index(b) : null);
          if (x == bx)
            az = bz;
          zb.set(x, az, c, p, tan, x == ax, x == bx, p != null && ps.get(p).increasing(ang));
          az += tan;
        }
      } else
      {
        double z = Math.max(az, bz);
        Integer p = az == z ? (Integer) index(a) : (Integer) index(b);
        zb.set(ax, z, c, p, Math.abs(az - bz), true, true, ps.get(p).increasing(ang));
      }
    }
    return zb;
  }

  public class ZBuff
  {
    static final int LEFT = 1, RIGHT = 2, INCR = 4, COVER = 8;
    Map<Integer, String> cs;
    Map<Integer, Double> zs;
    Map<Integer, Double> ts;
    Map<Integer, Integer> ps;
    Map<Integer, Integer> lri;

    public ZBuff()
    {
      cs = new HashMap<Integer, String>();
      zs = new HashMap<Integer, Double>();
      ts = new HashMap<Integer, Double>();
      ps = new HashMap<Integer, Integer>();
      lri = new HashMap<Integer, Integer>();
    }

    public String toString()
    {
      String out = "";
      int count = 0, max = 0;
      for (int i = 0; count < cs.size(); ++i)
      {
        if (cs.containsKey(i))
        {
          out += cs.get(i).substring(0, 1);
          count++;
          max = i;
        } else
        {
          out += " ";
        }
      }
      out += "\n";
      String pstr = "";
      for (int i = 0; i <= max; ++i)
      {
        out += ps.containsKey(i) ? ((lri.get(i) & INCR) > 0 ? ">" : "<") : " ";
        pstr += ps.containsKey(i) ? "" + ps.get(i) : " ";
      }
      return out + "\n" + pstr;
    }

    public void set(int x, double z, String color, Integer p, double tan, boolean left, boolean right, boolean incr)
    {
      //
      //if (Math.abs(Math.sin(Slice.this.idx/30.0)*10+45-x)<3)
      //  return;
      //
      boolean update = false;
      Double oz = zs.get(x);
      if (oz == null || z > oz)
      {
        update = true;
      } 
      else if (z == oz)
      {
        Integer op = ps.get(x);
        if (p != null && p.equals(op))
        {
          int olri = lri.get(x);
          if (incr && right && (olri & LEFT) > 0)
          {
            update = true;
          } else if (!incr && left && (olri & RIGHT) > 0)
          {
            update = true;
          } else if (left && (olri & LEFT) > 0 && tan > ts.get(x))
          {
            update = true;
          } else if (right && (olri & RIGHT) > 0 && tan < ts.get(x))
          {
            update = true;
          }
        } 
        else
        {
          // System.out.println("p: "+p);
          // System.out.println("op: "+op);
          // System.out.println("x: "+x);
          // System.out.println("z: "+z);
          // System.out.println("oz: "+oz);
          //throw new RuntimeException("Not implemented yet.");
        }
      }
      if (update)
      {
        zs.put(x, z);
        ts.put(x, tan);
        cs.put(x, color);
        if (p != null)
        {
          ps.put(x, p);
          lri.put(x, (left ? LEFT : 0) + (right ? RIGHT : 0) + (incr ? INCR : 0));
        } else
        {
          ps.remove(x);
          lri.remove(x);
        }
      }
    }
  }

  public String toString()
  {
    return "<" + ps + ";" + as + ";" + bs + ";" + cs + ">";
  }
}
