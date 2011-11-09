/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AAdditionalFormalTypes extends PAdditionalFormalTypes
{
    private TComma _comma_;
    private TName _name_;

    public AAdditionalFormalTypes()
    {
        // Constructor
    }

    public AAdditionalFormalTypes(
        @SuppressWarnings("hiding") TComma _comma_,
        @SuppressWarnings("hiding") TName _name_)
    {
        // Constructor
        setComma(_comma_);

        setName(_name_);

    }

    @Override
    public Object clone()
    {
        return new AAdditionalFormalTypes(
            cloneNode(this._comma_),
            cloneNode(this._name_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAdditionalFormalTypes(this);
    }

    public TComma getComma()
    {
        return this._comma_;
    }

    public void setComma(TComma node)
    {
        if(this._comma_ != null)
        {
            this._comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._comma_ = node;
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._comma_)
            + toString(this._name_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._comma_ == child)
        {
            this._comma_ = null;
            return;
        }

        if(this._name_ == child)
        {
            this._name_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(this._name_ == oldChild)
        {
            setName((TName) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
