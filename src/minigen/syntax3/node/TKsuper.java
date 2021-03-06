/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TKsuper extends Token
{
    public TKsuper()
    {
        super.setText("super");
    }

    public TKsuper(int line, int pos)
    {
        super.setText("super");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKsuper(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKsuper(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKsuper text.");
    }
}
