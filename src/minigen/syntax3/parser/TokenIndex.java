/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.parser;

import minigen.syntax3.node.*;
import minigen.syntax3.analysis.*;

class TokenIndex extends AnalysisAdapter
{
    int index;

    @Override
    public void caseTComma(@SuppressWarnings("unused") TComma node)
    {
        this.index = 0;
    }

    @Override
    public void caseTEquals(@SuppressWarnings("unused") TEquals node)
    {
        this.index = 1;
    }

    @Override
    public void caseTLb(@SuppressWarnings("unused") TLb node)
    {
        this.index = 2;
    }

    @Override
    public void caseTRb(@SuppressWarnings("unused") TRb node)
    {
        this.index = 3;
    }

    @Override
    public void caseTKend(@SuppressWarnings("unused") TKend node)
    {
        this.index = 4;
    }

    @Override
    public void caseTKisa(@SuppressWarnings("unused") TKisa node)
    {
        this.index = 5;
    }

    @Override
    public void caseTKclass(@SuppressWarnings("unused") TKclass node)
    {
        this.index = 6;
    }

    @Override
    public void caseTKtype(@SuppressWarnings("unused") TKtype node)
    {
        this.index = 7;
    }

    @Override
    public void caseTKclassname(@SuppressWarnings("unused") TKclassname node)
    {
        this.index = 8;
    }

    @Override
    public void caseTKsuper(@SuppressWarnings("unused") TKsuper node)
    {
        this.index = 9;
    }

    @Override
    public void caseTKnew(@SuppressWarnings("unused") TKnew node)
    {
        this.index = 10;
    }

    @Override
    public void caseTKvar(@SuppressWarnings("unused") TKvar node)
    {
        this.index = 11;
    }

    @Override
    public void caseTName(@SuppressWarnings("unused") TName node)
    {
        this.index = 12;
    }

    @Override
    public void caseTId(@SuppressWarnings("unused") TId node)
    {
        this.index = 13;
    }

    @Override
    public void caseEOF(@SuppressWarnings("unused") EOF node)
    {
        this.index = 14;
    }
}
