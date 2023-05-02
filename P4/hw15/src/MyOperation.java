import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class MyOperation {
    private final UmlOperation operation;
    private final ArrayList<UmlParameter> inParas = new ArrayList<>();
    private final ArrayList<UmlParameter> returnParas = new ArrayList<>();

    public MyOperation(UmlOperation operation) {
        this.operation = operation;
    }

    public void addInParas(UmlParameter parameter) {
        inParas.add(parameter);
    }

    public void addReturnParas(UmlParameter parameter) {
        returnParas.add(parameter);
    }

    public ArrayList<UmlParameter> getInParas() {
        return inParas;
    }

    public ArrayList<UmlParameter> getAllParas() {
        ArrayList<UmlParameter> allParameter = new ArrayList<>(inParas);
        allParameter.addAll(returnParas);
        return allParameter;
    }

    public Visibility getVisibility() {
        return this.operation.getVisibility();
    }

    public String getName() {
        return this.operation.getName();
    }

    public boolean hasWrongType(Set<String> ids) {
        ArrayList<String> allowedType = new ArrayList<>(
                Arrays.asList("byte/short/int/long/float/double/char/boolean/String".split("/")));
        for (UmlParameter inPara : inParas) {
            if (inPara.getType() instanceof NamedType) {
                if (!allowedType.contains(((NamedType) inPara.getType()).getName())) {
                    return true;
                }
            } else if (inPara.getType() instanceof ReferenceType) {
                if (!ids.contains(((ReferenceType) inPara.getType()).getReferenceId())) {
                    return true;
                }
            }
        }

        allowedType.add("void");
        for (UmlParameter returnPara : returnParas) {
            if (returnPara.getType() instanceof NamedType) {
                if (!allowedType.contains(((NamedType) returnPara.getType()).getName())) {
                    return true;
                }
            } else if (returnPara.getType() instanceof ReferenceType) {
                if (!ids.contains(((ReferenceType) returnPara.getType()).getReferenceId())) {
                    return true;
                }
            }
        }

        return false;
    }
}
