public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, long price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    @Override
    public String toString() {
        return "The epicSword's id is " + super.getId() + ", name is " + super.getName() + ", " +
                "sharpness is " + super.getSharpness() + ", evolveRatio is " + evolveRatio + ".";
    }

    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0);
        user.setMoney(user.getMoney() + getSharpness());
        System.out.println(user.getName() + " used " + getName() + " and earned " + getSharpness()
                + ".");
        setSharpness(getSharpness() * getEvolveRatio());
        System.out.println(getName() + "'s sharpness became " + getSharpness() + ".");
    }
}
