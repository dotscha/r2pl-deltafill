import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;


public class Main
{
  public static void main(String[] args)
  {
    if (args.length == 0)
      return;
    if (args[0].equals("-sintable"))
    {
      int p = -1;
      for (int s : SinTab.data)
      {
        if (s != p)
          Code.code("lda #lo(sin_offset+" + s + ")");
        Code.code("sta sintab,y");
        Code.code("iny");
        p = s;
      }
      Code.code("rts");
    }
    if (args[0].equals("-renderer"))
    {
      Map<Condition, List<Plotter>> code = null;
      if (args.length>1 && args[1].equals("obj2"))
      {
        Map<Integer,Slice> ss = new TreeMap<Integer,Slice>();
        /**/
        double ang = Math.PI*2/3;
        P3D a = new P3D(5,100-40,0);
        P3D b = new P3D(40*Math.cos(0*ang),100+40,40*Math.sin(0*ang));
        P3D c = new P3D(40*Math.cos(1*ang),100+20,40*Math.sin(1*ang));
        P3D d = new P3D(40*Math.cos(2*ang),100+00,40*Math.sin(2*ang));
        /**/
        /**
        int l = 10;
        Matrix m = Matrix.rotx(0.5);
        P3D v = new P3D(0,100,0);
        P3D a = new P3D(-l,-l,-l).rot(m).add(v);
        P3D b = new P3D( l,-l, l).rot(m).add(v);
        P3D c = new P3D(-l, l, l).rot(m).add(v);
        P3D d = new P3D( l, l,-l).rot(m).add(v);
        a.y = 2*a.y-100;
        b.y = 2*b.y-100;
        c.y = 2*c.y-100;
        d.y = 2*d.y-100;
        /**/
        Polygon p;
        p = new Polygon("b");
        p.addContour(a,b,c);
        p.sliceIt(ss);
        p = new Polygon("g");
        p.addContour(a,c,d);
        p.sliceIt(ss);
        p = new Polygon("a");
        p.addContour(a,d,b);
        p.sliceIt(ss);
        p = new Polygon("r");
        p.addContour(b,c,d);
        p.sliceIt(ss);
        for (Integer yc : ss.keySet())
        {
          //if (yc!=118) continue;
          Slice s = ss.get(yc);
          for (DPoint dp : s.dpoints())
          {
            //dp.zoom(1+(yc-100.0)*(yc-100.0)/10000);
            dp.rotate(yc/2);
          }
          if (code == null)
            code = s.plotters();
          else
            s.plotters(code);
        }
      }
      else if (args.length>1 && args[1].equals("obj3"))
      {
        int r = 26;
        double w = 5;
        for (int yc = 101-r; yc < 100+r; yc+=1)
        {
          Slice s = new Slice(yc);
          double rx = Math.sqrt(r*r-(100-yc)*(100-yc))/r*40;
	  Code.comment(""+rx);
          DPoint a = new DPoint(-rx, w);
          DPoint b = new DPoint( rx, w);
          DPoint c = new DPoint( rx,-w);
          DPoint d = new DPoint(-rx,-w);
          a.rotate(yc/1);
          b.rotate(yc/1);
          c.rotate(yc/1);
          d.rotate(yc/1);
          s.add(a,b,"r");
          s.add(b,c,"g");
          s.add(c,d,"r");
          s.add(d,a,"g");
          if (code == null)
            code = s.plotters();
          else
            s.plotters(code);
        }
      }
      else if (args.length>1 && args[1].equals("mx"))
      {
        String left = "b";
        String right= "b";
        String[] obj0= {
          "bbbb              ",
          "b   b  aaaa       ",
          "bbbb  a      bbbb ",
          "b   b  aaa  b    b",
          "b   b     a b     ",
          "      aaaa  b    b",
          "             bbbb "
        };
        String[] obj = readTxt("omega2x.txt");
        Matrix rot = Matrix.I;
        double lx = 1;
        double ly = 2;
        double lz = 6;
        Map<Integer,Slice> ss = new TreeMap<Integer,Slice>();
        double ys = -4+100-ly/2*obj.length;
        for (int j=0; j<obj.length; ++j)
        {
          String s = " "+obj[j]+" ";
          double xs = -lx*s.length()/2;
          for (int i=1; i<s.length(); ++i)
          {
            double z = 0;
            char a = s.charAt(i-1), b = s.charAt(i);
            if (a!=b && (a==' ' || b==' '))
            {
              Polygon p = new Polygon(a==' ' ? left : right);
              p.addContour(
                new P3D(xs+i*lx,ys+ly*j    ,+lz+z).rot(rot),
                new P3D(xs+i*lx,ys+ly*j    ,-lz+z).rot(rot),
                new P3D(xs+i*lx,ys+ly*(j+1),-lz+z).rot(rot),
                new P3D(xs+i*lx,ys+ly*(j+1),+lz+z).rot(rot)
              );
              p.sliceIt(ss);
            }
            if (a!=b && a!=' ')
            {
              int i0 = i;
              while (s.charAt(i0-1)==a)
                i0--;
              Polygon p = new Polygon(""+a);
              p.addContour(
                new P3D(xs+i0*lx,ys+ly*j    ,+lz+z).rot(rot),
                new P3D(xs+ i*lx,ys+ly*j    ,+lz+z).rot(rot),
                new P3D(xs+ i*lx,ys+ly*(j+1),+lz+z).rot(rot),
                new P3D(xs+i0*lx,ys+ly*(j+1),+lz+z).rot(rot)
              );
              p.sliceIt(ss);
              p = new Polygon(""+a);
              p.addContour(
                new P3D(xs+i0*lx,ys+ly*j    ,-lz+z).rot(rot),
                new P3D(xs+ i*lx,ys+ly*j    ,-lz+z).rot(rot),
                new P3D(xs+ i*lx,ys+ly*(j+1),-lz+z).rot(rot),
                new P3D(xs+i0*lx,ys+ly*(j+1),-lz+z).rot(rot)
              );
              p.sliceIt(ss);
            }
          }
        }
        for (Integer yc : ss.keySet())
        {
          //if (yc%2==1) continue;
          Slice s = ss.get(yc);
          for (DPoint dp : s.dpoints())
          {
            //dp.rotate(((int)yc/2)/3);
            //dp.rotate(16);
          }
          if (code == null)
            code = s.plotters();
          else
            s.plotters(code);
        }
      }
      else
      {
        for (int yc = 35; yc < 165; yc+=2)
        {
          Slice s = slice(yc);
          if (code == null)
            code = s.plotters();
          else
            s.plotters(code);
        }
      }
      Code.renderer(code);
    }
    if (args[0].equals("-test"))
    {
      Condition c = new Condition();
      List l = new ArrayList<Boolean>();
      l.add(true);l.add(true);l.add(false);l.add(true);l.add(false);l.add(true);
      c.set(l);
      System.out.println(c);
      System.out.println(c.apply(0));
      System.out.println(c.apply(2));
      System.out.println(c.apply(5));
      l.clear();
      l.add(false);l.add(true);
      c.set(l);
      System.out.println(c);
      System.out.println(c.apply(0));
      System.out.println(c.apply(1));
    }
  }

  private static Slice slice(int yc)
  {
    double m = 1.9+0.5*(1-Math.cos((yc-100)/20.0));//(yc + 200.0) / 100;
    DPoint p = new DPoint(10 * m, 10 * m);
    DPoint q = new DPoint(-10 * m, 10 * m);
    DPoint r = new DPoint(-10 * m, -10 * m);
    DPoint S = new DPoint(10 * m, -10 * m);
    Slice s = new Slice(yc);
    s.add(p, q, "r");
    s.add(q, r, "g");
    s.add(r, S, "b");
    s.add(S, p, "a");
    return s;
  }

  private static String[] readTxt(String f)
  {
        try {
            List<String> lines = Files.readAllLines(Paths.get(f));
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
  }
}