public class Plotter
{
  public Integer s;
  public DPoint p;
  public boolean fill, increase;
  public String color;

  public Plotter(Integer s, DPoint p, boolean fillOrClear, boolean increase, String color)
  {
    this.s = s;
    this.p = p;
    this.fill = fillOrClear;
    this.increase = increase;
    this.color = color == null ? "" : color;
    ;
  }

  public String toString()
  {
    return "<yc:" + s + "; p:" + p + "; fill:" + fill + "; incr:" + increase + "; color:" + color + ">";
  }

  public int hashCode()
  {
    int hc = s.hashCode() + 3 * color.hashCode() + 5 * p.hashCode();
    if (!fill)
      hc += increase ? 0 : 1;
    return hc;
  }

  public boolean equals(Object o)
  {
    if (!(o instanceof Plotter))
      return false;
    Plotter op = (Plotter) o;
    if (!s.equals(op.s) || !p.equals(op.p) || !eq(color, op.color) || fill != op.fill)
      return false;
    if (!fill && increase != op.increase)
      return false;
    return true;
  }

  private static boolean eq(String a, String b)
  {
    return (a == null && b == null) || (a != null && a.equals(b));
  }
}
