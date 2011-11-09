/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.analysis;

import java.util.*;
import minigen.syntax3.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable<Node,Object> in;
    private Hashtable<Node,Object> out;

    public Object getIn(Node node)
    {
        if(this.in == null)
        {
            return null;
        }

        return this.in.get(node);
    }

    public void setIn(Node node, Object o)
    {
        if(this.in == null)
        {
            this.in = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.in.put(node, o);
        }
        else
        {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node)
    {
        if(this.out == null)
        {
            return null;
        }

        return this.out.get(node);
    }

    public void setOut(Node node, Object o)
    {
        if(this.out == null)
        {
            this.out = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.out.put(node, o);
        }
        else
        {
            this.out.remove(node);
        }
    }

    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    public void caseAProgram(AProgram node)
    {
        defaultCase(node);
    }

    public void caseAClassDecl(AClassDecl node)
    {
        defaultCase(node);
    }

    public void caseAFormalDecls(AFormalDecls node)
    {
        defaultCase(node);
    }

    public void caseAFormalDecl(AFormalDecl node)
    {
        defaultCase(node);
    }

    public void caseAAdditionalFormalTypes(AAdditionalFormalTypes node)
    {
        defaultCase(node);
    }

    public void caseAType(AType node)
    {
        defaultCase(node);
    }

    public void caseAGenericPart(AGenericPart node)
    {
        defaultCase(node);
    }

    public void caseAGenericTypes(AGenericTypes node)
    {
        defaultCase(node);
    }

    public void caseAAdditionalTypes(AAdditionalTypes node)
    {
        defaultCase(node);
    }

    public void caseASuperDecls(ASuperDecls node)
    {
        defaultCase(node);
    }

    public void caseAAdditionalSupers(AAdditionalSupers node)
    {
        defaultCase(node);
    }

    public void caseASuperType(ASuperType node)
    {
        defaultCase(node);
    }

    public void caseASuperGenericPart(ASuperGenericPart node)
    {
        defaultCase(node);
    }

    public void caseASuperGenericTypes(ASuperGenericTypes node)
    {
        defaultCase(node);
    }

    public void caseASuperAdditionalTypes(ASuperAdditionalTypes node)
    {
        defaultCase(node);
    }

    public void caseAIsaInstr(AIsaInstr node)
    {
        defaultCase(node);
    }

    public void caseTComma(TComma node)
    {
        defaultCase(node);
    }

    public void caseTLb(TLb node)
    {
        defaultCase(node);
    }

    public void caseTRb(TRb node)
    {
        defaultCase(node);
    }

    public void caseTKend(TKend node)
    {
        defaultCase(node);
    }

    public void caseTKisa(TKisa node)
    {
        defaultCase(node);
    }

    public void caseTKclass(TKclass node)
    {
        defaultCase(node);
    }

    public void caseTKsuper(TKsuper node)
    {
        defaultCase(node);
    }

    public void caseTKnew(TKnew node)
    {
        defaultCase(node);
    }

    public void caseTName(TName node)
    {
        defaultCase(node);
    }

    public void caseTBlanks(TBlanks node)
    {
        defaultCase(node);
    }

    public void caseTComment(TComment node)
    {
        defaultCase(node);
    }

    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    public void defaultCase(@SuppressWarnings("unused") Node node)
    {
        // do nothing
    }
}
