/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.analysis;

import java.util.*;
import minigen.syntax3.node.*;

public class DepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        {
            List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClasses());
            for(PClassDecl e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PInstr> copy = new ArrayList<PInstr>(node.getInstrs());
            for(PInstr e : copy)
            {
                e.apply(this);
            }
        }
        outAProgram(node);
    }

    public void inAClassDecl(AClassDecl node)
    {
        defaultIn(node);
    }

    public void outAClassDecl(AClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAClassDecl(AClassDecl node)
    {
        inAClassDecl(node);
        if(node.getKclass() != null)
        {
            node.getKclass().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getFormalDecls() != null)
        {
            node.getFormalDecls().apply(this);
        }
        if(node.getSuperDecls() != null)
        {
            node.getSuperDecls().apply(this);
        }
        if(node.getKend() != null)
        {
            node.getKend().apply(this);
        }
        outAClassDecl(node);
    }

    public void inAFormalDecls(AFormalDecls node)
    {
        defaultIn(node);
    }

    public void outAFormalDecls(AFormalDecls node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFormalDecls(AFormalDecls node)
    {
        inAFormalDecls(node);
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
        }
        {
            List<PFormalDecl> copy = new ArrayList<PFormalDecl>(node.getFormalDecl());
            for(PFormalDecl e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        outAFormalDecls(node);
    }

    public void inAFormalDecl(AFormalDecl node)
    {
        defaultIn(node);
    }

    public void outAFormalDecl(AFormalDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFormalDecl(AFormalDecl node)
    {
        inAFormalDecl(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        {
            List<PAdditionalFormalTypes> copy = new ArrayList<PAdditionalFormalTypes>(node.getAdditionalFormalTypes());
            for(PAdditionalFormalTypes e : copy)
            {
                e.apply(this);
            }
        }
        outAFormalDecl(node);
    }

    public void inAAdditionalFormalTypes(AAdditionalFormalTypes node)
    {
        defaultIn(node);
    }

    public void outAAdditionalFormalTypes(AAdditionalFormalTypes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAdditionalFormalTypes(AAdditionalFormalTypes node)
    {
        inAAdditionalFormalTypes(node);
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        outAAdditionalFormalTypes(node);
    }

    public void inAType(AType node)
    {
        defaultIn(node);
    }

    public void outAType(AType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAType(AType node)
    {
        inAType(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getGenericPart() != null)
        {
            node.getGenericPart().apply(this);
        }
        outAType(node);
    }

    public void inAGenericPart(AGenericPart node)
    {
        defaultIn(node);
    }

    public void outAGenericPart(AGenericPart node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGenericPart(AGenericPart node)
    {
        inAGenericPart(node);
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
        }
        if(node.getGenericTypes() != null)
        {
            node.getGenericTypes().apply(this);
        }
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        outAGenericPart(node);
    }

    public void inAGenericTypes(AGenericTypes node)
    {
        defaultIn(node);
    }

    public void outAGenericTypes(AGenericTypes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGenericTypes(AGenericTypes node)
    {
        inAGenericTypes(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        {
            List<PAdditionalTypes> copy = new ArrayList<PAdditionalTypes>(node.getAdditionalTypes());
            for(PAdditionalTypes e : copy)
            {
                e.apply(this);
            }
        }
        outAGenericTypes(node);
    }

    public void inAAdditionalTypes(AAdditionalTypes node)
    {
        defaultIn(node);
    }

    public void outAAdditionalTypes(AAdditionalTypes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAdditionalTypes(AAdditionalTypes node)
    {
        inAAdditionalTypes(node);
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        outAAdditionalTypes(node);
    }

    public void inASuperDecls(ASuperDecls node)
    {
        defaultIn(node);
    }

    public void outASuperDecls(ASuperDecls node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperDecls(ASuperDecls node)
    {
        inASuperDecls(node);
        if(node.getKsuper() != null)
        {
            node.getKsuper().apply(this);
        }
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        {
            List<PAdditionalSupers> copy = new ArrayList<PAdditionalSupers>(node.getAdditionalSupers());
            for(PAdditionalSupers e : copy)
            {
                e.apply(this);
            }
        }
        outASuperDecls(node);
    }

    public void inAAdditionalSupers(AAdditionalSupers node)
    {
        defaultIn(node);
    }

    public void outAAdditionalSupers(AAdditionalSupers node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAdditionalSupers(AAdditionalSupers node)
    {
        inAAdditionalSupers(node);
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        outAAdditionalSupers(node);
    }

    public void inASuperType(ASuperType node)
    {
        defaultIn(node);
    }

    public void outASuperType(ASuperType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperType(ASuperType node)
    {
        inASuperType(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getSuperGenericPart() != null)
        {
            node.getSuperGenericPart().apply(this);
        }
        outASuperType(node);
    }

    public void inASuperGenericPart(ASuperGenericPart node)
    {
        defaultIn(node);
    }

    public void outASuperGenericPart(ASuperGenericPart node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperGenericPart(ASuperGenericPart node)
    {
        inASuperGenericPart(node);
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
        }
        if(node.getSuperGenericTypes() != null)
        {
            node.getSuperGenericTypes().apply(this);
        }
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        outASuperGenericPart(node);
    }

    public void inASuperGenericTypes(ASuperGenericTypes node)
    {
        defaultIn(node);
    }

    public void outASuperGenericTypes(ASuperGenericTypes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperGenericTypes(ASuperGenericTypes node)
    {
        inASuperGenericTypes(node);
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        {
            List<PSuperAdditionalTypes> copy = new ArrayList<PSuperAdditionalTypes>(node.getSuperAdditionalTypes());
            for(PSuperAdditionalTypes e : copy)
            {
                e.apply(this);
            }
        }
        outASuperGenericTypes(node);
    }

    public void inASuperAdditionalTypes(ASuperAdditionalTypes node)
    {
        defaultIn(node);
    }

    public void outASuperAdditionalTypes(ASuperAdditionalTypes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperAdditionalTypes(ASuperAdditionalTypes node)
    {
        inASuperAdditionalTypes(node);
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        outASuperAdditionalTypes(node);
    }

    public void inAIsaInstr(AIsaInstr node)
    {
        defaultIn(node);
    }

    public void outAIsaInstr(AIsaInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIsaInstr(AIsaInstr node)
    {
        inAIsaInstr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getKisa() != null)
        {
            node.getKisa().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAIsaInstr(node);
    }
}