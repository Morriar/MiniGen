/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import java.util.*;
import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AFormalDecl extends PFormalDecl
{
    private TName _name_;
    private final LinkedList<PAdditionalFormalTypes> _additionalFormalTypes_ = new LinkedList<PAdditionalFormalTypes>();

    public AFormalDecl()
    {
        // Constructor
    }

    public AFormalDecl(
        @SuppressWarnings("hiding") TName _name_,
        @SuppressWarnings("hiding") List<PAdditionalFormalTypes> _additionalFormalTypes_)
    {
        // Constructor
        setName(_name_);

        setAdditionalFormalTypes(_additionalFormalTypes_);

    }

    @Override
    public Object clone()
    {
        return new AFormalDecl(
            cloneNode(this._name_),
            cloneList(this._additionalFormalTypes_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFormalDecl(this);
    }

    public TName getName()
    {
        return this._name_;
    }

    public void setName(TName node)
    {
        if(this._name_ != null)
        {
            this._name_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._name_ = node;
    }

    public LinkedList<PAdditionalFormalTypes> getAdditionalFormalTypes()
    {
        return this._additionalFormalTypes_;
    }

    public void setAdditionalFormalTypes(List<PAdditionalFormalTypes> list)
    {
        this._additionalFormalTypes_.clear();
        this._additionalFormalTypes_.addAll(list);
        for(PAdditionalFormalTypes e : list)
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
            + toString(this._name_)
            + toString(this._additionalFormalTypes_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._name_ == child)
        {
            this._name_ = null;
            return;
        }

        if(this._additionalFormalTypes_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._name_ == oldChild)
        {
            setName((TName) newChild);
            return;
        }

        for(ListIterator<PAdditionalFormalTypes> i = this._additionalFormalTypes_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PAdditionalFormalTypes) newChild);
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