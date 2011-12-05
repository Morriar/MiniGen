/* This file was generated by SableCC (http://www.sablecc.org/). */

package minigen.syntax3.analysis;

import java.util.*;
import minigen.syntax3.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
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
        node.getEOF().apply(this);
        node.getPProgram().apply(this);
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
            List<PInstr> copy = new ArrayList<PInstr>(node.getInstrs());
            Collections.reverse(copy);
            for(PInstr e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClasses());
            Collections.reverse(copy);
            for(PClassDecl e : copy)
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
        if(node.getKend() != null)
        {
            node.getKend().apply(this);
        }
        {
            List<PInstr> copy = new ArrayList<PInstr>(node.getInstrs());
            Collections.reverse(copy);
            for(PInstr e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getSuperDecls() != null)
        {
            node.getSuperDecls().apply(this);
        }
        if(node.getFormalDecls() != null)
        {
            node.getFormalDecls().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getKclass() != null)
        {
            node.getKclass().apply(this);
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
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        {
            List<PFormalDecl> copy = new ArrayList<PFormalDecl>(node.getFormalDecl());
            Collections.reverse(copy);
            for(PFormalDecl e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
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
        {
            List<PAdditionalFormalTypes> copy = new ArrayList<PAdditionalFormalTypes>(node.getAdditionalFormalTypes());
            Collections.reverse(copy);
            for(PAdditionalFormalTypes e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
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
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
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
        if(node.getGenericPart() != null)
        {
            node.getGenericPart().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
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
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        if(node.getGenericTypes() != null)
        {
            node.getGenericTypes().apply(this);
        }
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
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
        {
            List<PAdditionalTypes> copy = new ArrayList<PAdditionalTypes>(node.getAdditionalTypes());
            Collections.reverse(copy);
            for(PAdditionalTypes e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
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
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
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
        {
            List<PAdditionalSupers> copy = new ArrayList<PAdditionalSupers>(node.getAdditionalSupers());
            Collections.reverse(copy);
            for(PAdditionalSupers e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        if(node.getKsuper() != null)
        {
            node.getKsuper().apply(this);
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
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
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
        if(node.getSuperGenericPart() != null)
        {
            node.getSuperGenericPart().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
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
        if(node.getRb() != null)
        {
            node.getRb().apply(this);
        }
        if(node.getSuperGenericTypes() != null)
        {
            node.getSuperGenericTypes().apply(this);
        }
        if(node.getLb() != null)
        {
            node.getLb().apply(this);
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
        {
            List<PSuperAdditionalTypes> copy = new ArrayList<PSuperAdditionalTypes>(node.getSuperAdditionalTypes());
            Collections.reverse(copy);
            for(PSuperAdditionalTypes e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
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
        if(node.getSuperType() != null)
        {
            node.getSuperType().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
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
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getKisa() != null)
        {
            node.getKisa().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAIsaInstr(node);
    }

    public void inADeclInstr(ADeclInstr node)
    {
        defaultIn(node);
    }

    public void outADeclInstr(ADeclInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADeclInstr(ADeclInstr node)
    {
        inADeclInstr(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getEquals() != null)
        {
            node.getEquals().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getKvar() != null)
        {
            node.getKvar().apply(this);
        }
        outADeclInstr(node);
    }

    public void inAAssignInstr(AAssignInstr node)
    {
        defaultIn(node);
    }

    public void outAAssignInstr(AAssignInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAssignInstr(AAssignInstr node)
    {
        inAAssignInstr(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getEquals() != null)
        {
            node.getEquals().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAAssignInstr(node);
    }

    public void inANewInstr(ANewInstr node)
    {
        defaultIn(node);
    }

    public void outANewInstr(ANewInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANewInstr(ANewInstr node)
    {
        inANewInstr(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getKnew() != null)
        {
            node.getKnew().apply(this);
        }
        outANewInstr(node);
    }

    public void inATypeInstr(ATypeInstr node)
    {
        defaultIn(node);
    }

    public void outATypeInstr(ATypeInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATypeInstr(ATypeInstr node)
    {
        inATypeInstr(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getKtype() != null)
        {
            node.getKtype().apply(this);
        }
        outATypeInstr(node);
    }

    public void inAClassnameInstr(AClassnameInstr node)
    {
        defaultIn(node);
    }

    public void outAClassnameInstr(AClassnameInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAClassnameInstr(AClassnameInstr node)
    {
        inAClassnameInstr(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getKclassname() != null)
        {
            node.getKclassname().apply(this);
        }
        outAClassnameInstr(node);
    }

    public void inAExecInstr(AExecInstr node)
    {
        defaultIn(node);
    }

    public void outAExecInstr(AExecInstr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAExecInstr(AExecInstr node)
    {
        inAExecInstr(node);
        if(node.getKexec() != null)
        {
            node.getKexec().apply(this);
        }
        if(node.getDot() != null)
        {
            node.getDot().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAExecInstr(node);
    }

    public void inANewExp(ANewExp node)
    {
        defaultIn(node);
    }

    public void outANewExp(ANewExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANewExp(ANewExp node)
    {
        inANewExp(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getKnew() != null)
        {
            node.getKnew().apply(this);
        }
        outANewExp(node);
    }

    public void inAVarExp(AVarExp node)
    {
        defaultIn(node);
    }

    public void outAVarExp(AVarExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarExp(AVarExp node)
    {
        inAVarExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAVarExp(node);
    }
}
