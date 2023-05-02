package poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Polynomial {
    private ArrayList<PolyItem> polyItems;

    public Polynomial() {
        this.polyItems = new ArrayList<>();
    }

    public ArrayList<PolyItem> getPolyItems() {
        return polyItems;
    }

    public void setPolyItems(ArrayList<PolyItem> polyItems) {
        this.polyItems = polyItems;
    }

    public void addPolyItem(PolyItem polyItem) {
        this.polyItems.add(polyItem);
    }

    public void addAllPolyItem(Polynomial polynomial) {
        this.polyItems.addAll(polynomial.getPolyItems());
    }

    public static Polynomial multiplyPoly(Polynomial polynomial1, Polynomial polynomial2) {
        Polynomial ans = new Polynomial();
        ArrayList<PolyItem> polyItems1 = polynomial1.getPolyItems();
        ArrayList<PolyItem> polyItems2 = polynomial2.getPolyItems();
        for (PolyItem polyItem1 : polyItems1) {
            for (PolyItem polyItem2 : polyItems2) {
                BigInteger newCoe = polyItem1.getCoe().multiply(polyItem2.getCoe());
                int newIndex;
                String name;
                if (polyItem1.getName().isEmpty() && polyItem2.getName().isEmpty()) {
                    name = "";
                    newIndex = 0;
                } else if (polyItem1.getName().isEmpty()) {
                    name = polyItem2.getName();
                    newIndex = polyItem2.getIndex();
                } else if (polyItem2.getName().isEmpty()) {
                    name = polyItem1.getName();
                    newIndex = polyItem1.getIndex();
                } else {
                    name = polyItem1.getName();
                    newIndex = polyItem1.getIndex() + polyItem2.getIndex();
                }
                PolyItem polyItem = new PolyItem(name, newCoe, newIndex);
                ans.addPolyItem(polyItem);
            }
        }
        return ans;
    }

    public static Polynomial addPoly(Polynomial polynomial1, Polynomial polynomial2) {
        Polynomial ans = new Polynomial();
        ans.addAllPolyItem(polynomial1);
        ans.addAllPolyItem(polynomial2);
        return ans;
    }

    public void mergePoly() {
        HashMap<Integer, PolyItem> ans = new HashMap<>();
        for (PolyItem polyItem : polyItems) {
            //System.out.println("polyitem:   " + polyItem);
            if (ans.containsKey(polyItem.getIndex())) {
                PolyItem newPolyItem = ans.get(polyItem.getIndex());
                newPolyItem.setCoe(newPolyItem.getCoe().add(polyItem.getCoe()));
                ans.replace(polyItem.getIndex(), newPolyItem);
            } else {
                ans.put(polyItem.getIndex(), polyItem);
            }
        }
        ArrayList<Integer> key = new ArrayList<>(ans.keySet());
        for (Integer i: key) {
            if (ans.get(i).getCoe().compareTo(BigInteger.valueOf(0)) == 0) {
                ans.remove(i);
            }
        }
        if (ans.isEmpty()) {
            ans.put(0,new PolyItem("",BigInteger.valueOf(0), 0));
        }
        this.setPolyItems(new ArrayList<>(ans.values()));
    }

    @Override
    public String toString() {
        Iterator<PolyItem> ite = polyItems.iterator();
        StringBuilder sb = new StringBuilder();
        if (ite.hasNext()) {
            sb.append(ite.next().toString());
            while (ite.hasNext()) {
                sb.append("+");
                sb.append(ite.next().toString());
            }
        }
        return sb.toString();
    }
}
