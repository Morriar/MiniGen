/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.node;

import minigen.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AExecInstr extends PInstr
{
    private TId _id_;
    private TDot _dot_;
    private TKexec _kexec_;

    public AExecInstr()
    {
        // Constructor
    }

    public AExecInstr(
        @SuppressWarnings("hiding") TId _id_,
        @SuppressWarnings("hiding") TDot _dot_,
        @SuppressWarnings("hiding") TKexec _kexec_)
    {
        // Constructor
        setId(_id_);

        setDot(_dot_);

        setKexec(_kexec_);

    }

    @Override
    public Object clone()
    {
        return new AExecInstr(
            cloneNode(this._id_),
            cloneNode(this._dot_),
            cloneNode(this._kexec_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExecInstr(this);
    }

    public TId getId()
    {
        return this._id_;
    }

    public void setId(TId node)
    {
        if(this._id_ != null)
        {
            this._id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._id_ = node;
    }

    public TDot getDot()
    {
        return this._dot_;
    }

    public void setDot(TDot node)
    {
        if(this._dot_ != null)
        {
            this._dot_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dot_ = node;
    }

    public TKexec getKexec()
    {
        return this._kexec_;
    }

    public void setKexec(TKexec node)
    {
        if(this._kexec_ != null)
        {
            this._kexec_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._kexec_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._id_)
            + toString(this._dot_)
            + toString(this._kexec_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        if(this._dot_ == child)
        {
            this._dot_ = null;
            return;
        }

        if(this._kexec_ == child)
        {
            this._kexec_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        if(this._dot_ == oldChild)
        {
            setDot((TDot) newChild);
            return;
        }

        if(this._kexec_ == oldChild)
        {
            setKexec((TKexec) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}