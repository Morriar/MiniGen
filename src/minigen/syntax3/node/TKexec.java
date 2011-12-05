/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TKexec extends Token
{
    public TKexec()
    {
        super.setText("exec()");
    }

    public TKexec(int line, int pos)
    {
        super.setText("exec()");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKexec(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKexec(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKexec text.");
    }
}
