/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TKclassname extends Token
{
    public TKclassname()
    {
        super.setText("classname");
    }

    public TKclassname(int line, int pos)
    {
        super.setText("classname");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKclassname(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKclassname(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKclassname text.");
    }
}
