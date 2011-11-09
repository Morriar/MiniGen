/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import java.util.*;
import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AProgram extends PProgram
{
    private final LinkedList<PClassDecl> _classes_ = new LinkedList<PClassDecl>();
    private final LinkedList<PInstr> _instrs_ = new LinkedList<PInstr>();

    public AProgram()
    {
        // Constructor
    }

    public AProgram(
        @SuppressWarnings("hiding") List<PClassDecl> _classes_,
        @SuppressWarnings("hiding") List<PInstr> _instrs_)
    {
        // Constructor
        setClasses(_classes_);

        setInstrs(_instrs_);

    }

    @Override
    public Object clone()
    {
        return new AProgram(
            cloneList(this._classes_),
            cloneList(this._instrs_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAProgram(this);
    }

    public LinkedList<PClassDecl> getClasses()
    {
        return this._classes_;
    }

    public void setClasses(List<PClassDecl> list)
    {
        this._classes_.clear();
        this._classes_.addAll(list);
        for(PClassDecl e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public LinkedList<PInstr> getInstrs()
    {
        return this._instrs_;
    }

    public void setInstrs(List<PInstr> list)
    {
        this._instrs_.clear();
        this._instrs_.addAll(list);
        for(PInstr e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._classes_)
            + toString(this._instrs_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._classes_.remove(child))
        {
            return;
        }

        if(this._instrs_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        for(ListIterator<PClassDecl> i = this._classes_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PClassDecl) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<PInstr> i = this._instrs_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PInstr) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
