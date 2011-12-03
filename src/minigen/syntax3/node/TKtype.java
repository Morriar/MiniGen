/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TKtype extends Token
{
    public TKtype()
    {
        super.setText("type");
    }

    public TKtype(int line, int pos)
    {
        super.setText("type");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKtype(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKtype(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKtype text.");
    }
}
