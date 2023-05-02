public class RareSword extends Sword {
    private double extraExpBonus;

    public RareSword(int id, String name, long price, double sharpness, double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    @Override
    public String toString() {
        return "The rareSword's id is " + super.getId() + ", name is " + super.getName() + ", " +
                "sharpness is " + super.getSharpness() + ", extraExpBonus is " + extraExpBonus +
                ".";
    }

    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0 + getExtraExpBonus());
        user.setMoney(user.getMoney() + getSharpness());
        System.out.println(user.getName() + " used " + getName() + " and earned " + getSharpness()
                + ".");
        System.out.println(user.getName() + " got extra exp " + getExtraExpBonus() + ".");
    }
}
